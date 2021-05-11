package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.config.BotConstants;
import nl.djj.swgoh_bot_v2.config.GalacticLegends;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.db.Abbreviation;
import nl.djj.swgoh_bot_v2.entities.db.Ability;
import nl.djj.swgoh_bot_v2.entities.db.GLRequirement;
import nl.djj.swgoh_bot_v2.entities.db.Unit;
import nl.djj.swgoh_bot_v2.exceptions.DeletionError;
import nl.djj.swgoh_bot_v2.exceptions.HttpRetrieveError;
import nl.djj.swgoh_bot_v2.exceptions.InsertionError;
import nl.djj.swgoh_bot_v2.exceptions.RetrieveError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * @author DJJ
 */
public class UpdateImpl {
    private final transient String className = this.getClass().getSimpleName();
    private final transient Logger logger;
    private final transient DAO dao;
    private final transient HttpHelper httpHelper;

    /**
     * @param logger the logger.
     * @param dao    the DB handler.
     */
    public UpdateImpl(final Logger logger, final DAO dao) {
        super();
        this.dao = dao;
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
            for (int i = 0; i < characterData.length(); i++) {
                final JSONObject charJson = characterData.getJSONObject(i);
                dao.unitDao().save(new Unit(charJson.getString("base_id"), charJson.getString("name").replace("'", "`"), charJson.getString("alignment"), true));
            }
            for (int i = 0; i < shipData.length(); i++) {
                final JSONObject shipJson = shipData.getJSONObject(i);
                dao.unitDao().save(new Unit(shipJson.getString("base_id"), shipJson.getString("name").replace("'", "`"), shipJson.getString("alignment"), false));
            }
            logger.info(className, "Done updating the units");
            message.done("Units updated!");
        } catch (final HttpRetrieveError | InsertionError error) {
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
            dao.glRequirementDao().clear();
            requirementData = httpHelper.getJsonObject(SwgohGgEndpoint.GL_CHECKLIST_ENDPOINT.getUrl());
            final JSONArray unitReqData = requirementData.getJSONArray("units");
            for (int i = 0; i < unitReqData.length(); i++) {
                final JSONObject glData = unitReqData.getJSONObject(i);
                final String glName = glData.getString("unitName");
                final JSONArray units = glData.getJSONArray("requiredUnits");
                for (int j = 0; j < units.length(); j++) {
                    final JSONObject unitData = units.getJSONObject(j);
                    final String unitId = unitData.getString("baseId");
                    final int gearLevel = unitData.getInt("gearLevel");
                    final int relicLevel = unitData.getInt("relicTier");
                    dao.glRequirementDao().save(new GLRequirement(GalacticLegends.getByName(glName), unitId, gearLevel, relicLevel));
                }
            }
            logger.info(className, "Done updating the GL Requirements");
            message.done("Units updated!");
        } catch (final InsertionError | HttpRetrieveError | DeletionError error) {
            logger.error(className, "updateGLRequirements", error.getMessage());
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
        try {
            dao.abbreviationDao().clear();
            final List<String> abbreviationData = httpHelper.getCsv(BotConstants.ABBREVIATIONS_LINK);
            for (final String abbrString : abbreviationData) {
                if (abbrString.contains("toonId")) {
                    continue;
                }
                final String[] splitted = abbrString.split(";");
                if (splitted[0].isEmpty() || splitted[1].isEmpty()) {
                    logger.error(className, "updateAbbreviations", "Skipping because empty: 0: " + splitted[0] + " | 1: " + splitted[1]);
                    continue;
                }
                dao.abbreviationDao().save(new Abbreviation(dao.unitDao().getById(splitted[0]), splitted[1]));
            }
            logger.info(className, "Done updating the abbreviations");
            message.done("Abbreviations updated!");
        } catch (final HttpRetrieveError | InsertionError | RetrieveError | DeletionError error) {
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
                dao.abilityDao().save(new Ability(baseId, name, tierMax, isZeta, isOmega, dao.unitDao().getById(unitBaseId)));
            }
            message.done("Abilities updated");
        } catch (final RetrieveError | InsertionError | HttpRetrieveError error) {
            message.error(error.getMessage());
        }
    }

    //TODO: save farming locations
}
