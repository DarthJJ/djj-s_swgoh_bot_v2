package nl.djj.swgoh_bot_v2.commands.admin;

import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.HandlerInterface;
import nl.djj.swgoh_bot_v2.database.UpdateHandler;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.db.UnitInfo;
import nl.djj.swgoh_bot_v2.helpers.HttpHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * @author DJJ
 */
public class Update extends BaseCommand {
    private static final String NAME = "Update";
    private static final Permission REQUIRED_LEVEL = Permission.ADMINISTRATOR;
    private static final String DESCRIPTION = "All the update commands of the bot, aka the danger-zone";
    private static final String[] ALIASES = {
            "up"
    };
    private static final CommandCategory CATEGORY = CommandCategory.ADMIN;
    private static final Map<String, Flag> FLAGS = new HashMap<>();
    private boolean enabled;
    private static final boolean FLAG_REQUIRED = true;
    private static final transient String FLAG_ALL = "all";
    private static final transient String FLAG_UNITS = "units";
    private final transient HttpHelper httpHelper;

    /**
     * The constructor.
     * @param logger the logger to use.
     * @param updateHandler the DB connection.
     */
    public Update(final Logger logger, final HandlerInterface updateHandler) {
        super(logger, updateHandler);
        httpHelper = new HttpHelper(logger);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getAliases() {
        return Arrays.copyOf(ALIASES, ALIASES.length);
    }

    @Override
    public Permission getRequiredLevel() {
        return REQUIRED_LEVEL;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public CommandCategory getCategory() {
        return CATEGORY;
    }

    @Override
    public Map<String, Flag> getFlags() {
        return FLAGS;
    }

    @Override
    public boolean isFlagRequired() {
        return FLAG_REQUIRED;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void createFlags() {
        final Flag all = new Flag(FLAG_ALL, "Updates all", "use this command to update all");
        FLAGS.put(all.getName(), all);
        final Flag characters = new Flag(FLAG_UNITS, "Updates the units", "Use this command to only update the units");
        FLAGS.put(characters.getName(), characters);
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_ALL:
                updateAll();
                break;
            case FLAG_UNITS:
                if (updateUnits()) {
                    message.getChannel().sendMessage("success").queue();
                    return;
                }
                break;
            default:
                message.getChannel().sendMessage("This flag doesn't exist").queue();
                return;
        }
        message.getChannel().sendMessage("something went wrong").queue();
    }

    private boolean updateUnits() {
        final JSONArray characterData = httpHelper.getJsonArray(SwgohGgEndpoint.CHARACTER_ENDPOINT.getUrl());
        final JSONArray shipData = httpHelper.getJsonArray(SwgohGgEndpoint.SHIP_ENDPOINT.getUrl());
        if (characterData == null || shipData == null) {
            return false;
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
        return ((UpdateHandler) this.dbHandler).updateUnits(characterList);
    }

    private void updateAll() {
        updateUnits();
    }
}
