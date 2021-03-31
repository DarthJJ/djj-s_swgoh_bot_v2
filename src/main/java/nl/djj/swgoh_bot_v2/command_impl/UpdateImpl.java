package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.Config;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.db.Abbreviation;
import nl.djj.swgoh_bot_v2.entities.db.GlRequirement;
import nl.djj.swgoh_bot_v2.entities.db.UnitInfo;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DJJ
 */
public class UpdateImpl {
    private final transient String className = this.getClass().getSimpleName();
    private final transient Logger logger;
    private final transient DatabaseHandler dbHandler;
    private final transient HttpHelper httpHelper;

    /**
     * @param logger    the logger.
     * @param dbHandler the DB handler.
     */
    public UpdateImpl(final Logger logger, final DatabaseHandler dbHandler) {
        super();
        this.dbHandler = dbHandler;
        this.logger = logger;
        this.httpHelper = new HttpHelper(logger);
    }

    /**
     * Updates the unit data.
     *
     * @param message message.
     */
    public void updateUnits(final Message message) {
        logger.info(className, "Updating the units.");
        final JSONArray characterData;
        final JSONArray shipData;
        try {
            characterData = httpHelper.getJsonArray(SwgohGgEndpoint.CHARACTER_ENDPOINT.getUrl());
            shipData = httpHelper.getJsonArray(SwgohGgEndpoint.SHIP_ENDPOINT.getUrl());
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
            return;
        }

        final List<UnitInfo> characterList = new ArrayList<>();
        for (int i = 0; i < characterData.length(); i++) {
            final JSONObject charJson = characterData.getJSONObject(i);
            final UnitInfo character = new UnitInfo(charJson.getString("base_id"), charJson.getString("name").replace("'", "`"), charJson.getString("alignment"), true);
            characterList.add(character);
        }
        for (int i = 0; i < shipData.length(); i++) {
            final JSONObject shipJson = shipData.getJSONObject(i);
            final UnitInfo character = new UnitInfo(shipJson.getString("base_id"), shipJson.getString("name").replace("'", "`"), shipJson.getString("alignment"), false);
            characterList.add(character);
        }
        try {
            dbHandler.updateUnits(characterList);
            message.done("Units updated!");
        } catch (final SQLInsertionError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Updates the GL Requirements.
     *
     * @param message message.
     */
    public void updateGlRequirements(final Message message) {
        logger.info(className, "Updating the GL Requirements");
        final JSONObject requirementData;
        try {
            requirementData = httpHelper.getJsonObject(SwgohGgEndpoint.GL_CHECKLIST_ENDPOINT.getUrl());
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        final JSONArray unitRequirementsData = requirementData.getJSONArray("units");
        final List<GlRequirement> requirements = new ArrayList<>();
        for (int i = 0; i < unitRequirementsData.length(); i++) {
            final JSONObject glData = unitRequirementsData.getJSONObject(i);
            final String glName = glData.getString("unitName");
            final JSONArray units = glData.getJSONArray("requiredUnits");
            for (int j = 0; j < units.length(); j++) {
                requirements.add(GlRequirement.initFromJson(units.getJSONObject(j), glName));
            }
        }
        try {
            dbHandler.updateGlRequirements(requirements);
            message.done("GL Requirements updated");
        } catch (final SQLInsertionError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Updates the abbreviations data.
     *
     * @param message message.
     */
    public void updateAbbreviations(final Message message) {
        logger.info(className, "Updating the abbreviations");
        final List<String> abbreviationData;
        try {
            abbreviationData = httpHelper.getCsv(Config.ABBREVIATIONS_LINK);
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        final List<Abbreviation> abbreviations = new ArrayList<>();
        for (final String abbreviationString : abbreviationData) {
            if (abbreviationString.contains("toonId")) {
                continue;
            }
            final String[] splitted = abbreviationString.split(";");
            abbreviations.add(new Abbreviation(splitted[0], splitted[1]));
        }
        try {
            dbHandler.updateAbbreviations(abbreviations);
            message.done("Abbreviations updated!");
        } catch (final SQLInsertionError error) {
            message.error(error.getMessage());
        }
    }
}
