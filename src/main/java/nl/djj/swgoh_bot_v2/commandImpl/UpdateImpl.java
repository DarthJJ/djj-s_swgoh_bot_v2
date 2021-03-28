package nl.djj.swgoh_bot_v2.commandImpl;

import nl.djj.swgoh_bot_v2.config.Config;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.db.Abbreviation;
import nl.djj.swgoh_bot_v2.entities.db.UnitInfo;
import nl.djj.swgoh_bot_v2.exceptions.SQLInsertionError;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateImpl {
    private final transient Logger logger;
    private final transient DatabaseHandler dbHandler;
    private final transient HttpHelper httpHelper;

    public UpdateImpl(final Logger logger, final DatabaseHandler dbHandler){
        super();
        this.dbHandler = dbHandler;
        this.logger = logger;
        this.httpHelper = new HttpHelper(logger);
    }

    public void updateUnits(final Message message, final boolean isAll) {
        final JSONArray characterData = httpHelper.getJsonArray(SwgohGgEndpoint.CHARACTER_ENDPOINT.getUrl());
        final JSONArray shipData = httpHelper.getJsonArray(SwgohGgEndpoint.SHIP_ENDPOINT.getUrl());
        if (characterData == null || shipData == null) {
            message.getChannel().sendMessage("Something went wrong retrieving the unit data").queue();
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
            message.getChannel().sendMessage("Success").queue();
        } catch (final SQLInsertionError error) {
            message.getChannel().sendMessage(error.getMessage()).queue();
        }
    }


    public void updateAbbreviations(final Message message, final boolean isAll){
        final List<String> abbreviationData = httpHelper.getCsv(Config.ABBREVIATIONS_LINK);
        final List<Abbreviation> abbreviations = new ArrayList<>();
        for (String abbreviationString : abbreviationData) {
            if (abbreviationString.contains("toonId")) {
                continue;
            }
            final String[] splitted = abbreviationString.split(";");
            abbreviations.add(new Abbreviation(splitted[0], splitted[1]));
        }
        try {
            dbHandler.updateAbbreviations(abbreviations);
            message.getChannel().sendMessage("Success").queue();
        } catch (final SQLInsertionError error){
            message.getChannel().sendMessage(error.getMessage()).queue();
        }
    }
}
