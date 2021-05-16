package nl.djj.swgoh_bot_v2.commands.swgoh;

import nl.djj.swgoh_bot_v2.commands.BaseCommand;
import nl.djj.swgoh_bot_v2.config.enums.CommandCategory;
import nl.djj.swgoh_bot_v2.config.enums.Permission;
import nl.djj.swgoh_bot_v2.entities.Flag;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.ImplHelper;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 **/
public class Need extends BaseCommand {
    private static final String FLAG_LS = "ls";
    private static final String FLAG_DS = "ds";
    private static final String FLAG_CANTINA = "cantina";
    private static final String FLAG_FLEET = "fleet";
    private static final String FLAG_ALL = "all";


    /**
     * Constructor.
     *
     * @param logger     the logger to use.
     * @param implHelper the implHelper.
     **/
    public Need(final Logger logger, final ImplHelper implHelper) {
        super(logger, implHelper);
        name = "need";
        requiredLevel = Permission.USER;
        description = "All information about the farming locations of units";
        aliases = new String[0];
        category = CommandCategory.SWGOH;
        flagRequired = true;
    }

    @Override
    public void createFlags() {
        this.flags.put(FLAG_LS, new Flag(FLAG_LS, "Check which units on the LS battle nodes aren't yet 7*", true, name, FLAG_LS));
        this.flags.put(FLAG_DS, new Flag(FLAG_DS, "Check which units on the DS battle nodes aren't yet 7*", true, name, FLAG_LS));
        this.flags.put(FLAG_CANTINA, new Flag(FLAG_CANTINA, "Check which units on the Cantina battle nodes aren't yet 7*", true, name, FLAG_LS));
        this.flags.put(FLAG_FLEET, new Flag(FLAG_FLEET, "Checks which units on the Fleet battles aren't yet 7*", true, name, FLAG_FLEET));
        this.flags.put(FLAG_ALL, new Flag(FLAG_ALL, "Check which units aren't yet 7*", true, name, FLAG_LS));
    }

    @Override
    public void handleMessage(final Message message) {
        switch (message.getFlag()) {
            case FLAG_LS -> this.implHelper.getNeedImpl().lightSide(message);
            case FLAG_DS -> this.implHelper.getNeedImpl().darkSide(message);
            case FLAG_CANTINA -> this.implHelper.getNeedImpl().cantina(message);
            case FLAG_FLEET -> this.implHelper.getNeedImpl().fleet(message);
            case FLAG_ALL -> this.implHelper.getNeedImpl().all(message);
            default -> unknownFlag(message);
        }
    }
}
