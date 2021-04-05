package nl.djj.swgoh_bot_v2.commands.admin;

import nl.djj.swgoh_bot_v2.command_impl.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

import java.util.HashMap;

/**
 * @author DJJ
 */
public class Update extends BaseCommand {
    private static final transient String FLAG_UNITS = "units";
    private static final transient String FLAG_ABBREVIATIONS = "abbreviations";
    private static final transient String FLAG_GL_REQUIREMENTS = "glRequirements";

    /**
     * The constructor.
     *
     * @param logger     the logger to use.
     * @param implHelper the DB connection.
     */
    public Update(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        NAME = "update";
        REQUIRED_LEVEL = Permission.ADMINISTRATOR;
        DESCRIPTION = "All the update commands of the bot, aka the danger-zone";
        ALIASES = new String[]{
                "up"
        };
        CATEGORY = CommandCategory.ADMIN;
        FLAGS = new HashMap<>();
        FLAG_REQUIRED = true;
        createFlags();
    }


    @Override
    public void createFlags() {
        FLAGS.put(FLAG_UNITS, new Flag(FLAG_UNITS, "Updates the units", NAME, FLAG_UNITS));
        FLAGS.put(FLAG_ABBREVIATIONS, new Flag(FLAG_ABBREVIATIONS, "Updates the unit abbreviations", NAME, FLAG_ABBREVIATIONS));
        FLAGS.put(FLAG_GL_REQUIREMENTS, new Flag(FLAG_GL_REQUIREMENTS, "Updates the GL Requirements", NAME, FLAG_GL_REQUIREMENTS));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_UNITS -> this.implHelper.getUpdateImpl().updateUnits(message);
            case FLAG_ABBREVIATIONS -> this.implHelper.getUpdateImpl().updateAbbreviations(message);
            case FLAG_GL_REQUIREMENTS -> this.implHelper.getUpdateImpl().updateGlRequirements(message);
            default -> message.error("This is not a valid flag, use '" + message.getGuildPrefix() + " help " + NAME + "'");
        }
    }
}
