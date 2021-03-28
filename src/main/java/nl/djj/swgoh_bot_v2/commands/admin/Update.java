package nl.djj.swgoh_bot_v2.commands.admin;

import nl.djj.swgoh_bot_v2.commandImpl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Config;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.config.SwgohGgEndpoint;
import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.entities.db.Abbreviation;
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
    private static final transient String FLAG_UNITS = "units";
    private static final transient String FLAG_ABBREVIATIONS = "abbreviations";

    /**
     * The constructor.
     *
     * @param logger        the logger to use.
     * @param implHelper the DB connection.
     */
    public Update(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
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
        final Flag characters = new Flag(FLAG_UNITS, "Updates the units", "Use this command to only update the units");
        FLAGS.put(characters.getName(), characters);
        final Flag abbreviations = new Flag(FLAG_ABBREVIATIONS, "Updates the unit abbreviations", "Use this command to only update the abbreviations");
        FLAGS.put(abbreviations.getName(), abbreviations);
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_UNITS -> this.implHelper.getUpdateImpl().updateUnits(message, false);
            case FLAG_ABBREVIATIONS -> this.implHelper.getUpdateImpl().updateAbbreviations(message, false);
            default -> message.getChannel().sendMessage("This flag doesn't exist").queue();
        }
    }
}
