package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.db.*;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        final JSONArray unitReqData = requirementData.getJSONArray("units");
        final List<GlRequirement> requirements = new ArrayList<>();
        for (int i = 0; i < unitReqData.length(); i++) {
            final JSONObject glData = unitReqData.getJSONObject(i);
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
     * Updates the farming locations.
     *
     * @param message message.
     */
    public void updateLocations(final Message message) {
        logger.info(className, "Updating the locations");
        final List<String> locationData;
        try {
            locationData = httpHelper.getCsv(BotConstants.FARMING_LOCATIONS_LINK);
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        final List<FarmingLocation> locations = new ArrayList<>();
        for (final String locDataString : locationData) {
            if (locDataString.contains("Unit")) {
                continue;
            }
            final String[] splitted = locDataString.split(";");
            final boolean isPreferred = splitted[3].toLowerCase(Locale.ROOT).equals("yes");
            locations.add(new FarmingLocation(splitted[0].replace("'", "''"),
                    splitted[1].replace("'", "''"),
                    splitted[2].replace("'", "''"),
                    splitted[3].replace("'", "''"),
                    isPreferred));
        }
        try {
            dbHandler.updateFarmingLocations(locations);
            message.done("Farming locations updated");
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
            abbreviationData = httpHelper.getCsv(BotConstants.ABBREVIATIONS_LINK);
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        final List<Abbreviation> abbreviations = new ArrayList<>();
        for (final String abbrString : abbreviationData) {
            if (abbrString.contains("toonId")) {
                continue;
            }
            final String[] splitted = abbrString.split(";");
            abbreviations.add(new Abbreviation(splitted[0], splitted[1]));
        }
        try {
            dbHandler.updateAbbreviations(abbreviations);
            message.done("Abbreviations updated!");
        } catch (final SQLInsertionError error) {
            message.error(error.getMessage());
        }
    }

    /**
     * Updates all the abilities of characters.
     *
     * @param message the message.
     */
    public void updateAbilities(final Message message) {
        logger.info(className, "Updating the abilities");
        final JSONArray abilityData;
        try {
            abilityData = httpHelper.getJsonArray(SwgohGgEndpoint.ABILITY_ENDPOINT.getUrl());
        } catch (final HttpRetrieveError error) {
            message.error(error.getMessage());
            return;
        }
        final List<Ability> abilities = new ArrayList<>();
        for (int i = 0; i < abilityData.length(); i++) {
            final JSONObject ability = abilityData.getJSONObject(i);
            final String baseId;
            final String name = ability.getString("name");
            final int tierMax = ability.getInt("tier_max");
            final boolean isZeta = ability.getBoolean("is_zeta");
            final boolean isOmega = ability.getBoolean("is_omega");
            final String unitBaseId;
            if (ability.isNull("ship_base_id")) {
                unitBaseId = ability.getString("character_base_id");
            } else {
                unitBaseId = ability.getString("ship_base_id");
            }
            if ("uniqueskill_GALACTICLEGEND01".equals(ability.getString("base_id"))) { //Hack for GL duplicate unique name. Thank you CG
                baseId = ability.getString("base_id") + "_" + unitBaseId;
            } else {
                baseId = ability.getString("base_id");
            }
            abilities.add(new Ability(baseId, name, tierMax, isZeta, isOmega, unitBaseId));
        }
        try {
            dbHandler.updateAbilities(abilities);
            message.done("Abilities updated");
        } catch (final SQLInsertionError error) {
            message.error(error.getMessage());
        }
    }
}
