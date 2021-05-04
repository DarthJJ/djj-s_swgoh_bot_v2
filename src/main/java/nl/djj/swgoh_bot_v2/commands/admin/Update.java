package nl.djj.swgoh_bot_v2.commands.admin;

import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.CommandCategory;
import nl.djj.swgoh_bot_v2.config.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public class Update extends BaseCommand {
    private static final transient String FLAG_UNITS = "units";
    private static final transient String FLAG_ABBREVIATIONS = "abbreviations";
    private static final transient String FLAG_GL_REQUIREMENTS = "glRequirements";
    private static final transient String FLAG_ABILITIES = "abilities";

    /**
     * The constructor.
     *
     * @param logger     the logger to use.
     * @param implHelper the DB connection.
     */
    public Update(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "update";
        requiredLevel = Permission.ADMINISTRATOR;
        description = "All the update commands of the bot, aka the danger-zone";
        aliases = new String[]{
                "up"
        };
        category = CommandCategory.ADMIN;
        flagRequired = true;
    }


    @Override
    public void createFlags() {
        flags.put(FLAG_UNITS, new Flag(FLAG_UNITS, "Updates the units", true, name, FLAG_UNITS));
        flags.put(FLAG_ABBREVIATIONS, new Flag(FLAG_ABBREVIATIONS, "Updates the unit abbreviations", true, name, FLAG_ABBREVIATIONS));
        flags.put(FLAG_GL_REQUIREMENTS, new Flag(FLAG_GL_REQUIREMENTS, "Updates the GL Requirements", true, name, FLAG_GL_REQUIREMENTS));
        flags.put(FLAG_ABILITIES, new Flag(FLAG_ABILITIES, "Updates the unit abilities", true, name, FLAG_ABILITIES));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_UNITS -> this.implHelper.getUpdateImpl().updateUnits(message);
            case FLAG_ABBREVIATIONS -> this.implHelper.getUpdateImpl().updateAbbreviations(message);
            case FLAG_GL_REQUIREMENTS -> this.implHelper.getUpdateImpl().updateGlRequirements(message);
            case FLAG_ABILITIES -> this.implHelper.getUpdateImpl().updateAbilities(message);
            default -> message.error("This is not a valid flag, use '" + message.getGuildPrefix() + "help " + name + "'");
        }
    }
}
