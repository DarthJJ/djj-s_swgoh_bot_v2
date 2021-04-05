package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 */
public abstract class ImplHelper {

    private final transient ProfileImpl profileImpl;
    private final transient CommandImpl commandImpl;
    private final transient UpdateImpl updateImpl;
    private final transient ControlImpl controlImpl;
    private final transient UnitImpl unitImpl;
    private final transient ConfigImpl configImpl;
    private final transient GuildImpl guildImpl;

    /**
     * @param logger    the logger.
     * @param dbHandler the DB handler.
     */
    public ImplHelper(final Logger logger, final DatabaseHandler dbHandler) {
        super();
        this.profileImpl = new ProfileImpl(logger, dbHandler, this);
        this.commandImpl = new CommandImpl(logger, dbHandler);
        this.updateImpl = new UpdateImpl(logger, dbHandler);
        this.controlImpl = new ControlImpl(logger, dbHandler) {
            @Override
            public void closeBot() {
                ImplHelper.this.closeBot();
            }
        };
        this.unitImpl = new UnitImpl(dbHandler);
        this.configImpl = new ConfigImpl(dbHandler, logger);
        this.guildImpl = new GuildImpl(logger, dbHandler, this);
    }

    /**
     * Overridden to close the bot.
     */
    public abstract void closeBot();

    public ProfileImpl getProfileImpl() {
        return this.profileImpl;
    }

    public CommandImpl getCommandImpl() {
        return this.commandImpl;
    }

    public UpdateImpl getUpdateImpl() {
        return this.updateImpl;
    }

    public ControlImpl getControlImpl() {
        return this.controlImpl;
    }

    public UnitImpl getUnitImpl() {
        return this.unitImpl;
    }

    public ConfigImpl getConfigImpl() {
        return this.configImpl;
    }

    public GuildImpl getGuildImpl() {
        return this.guildImpl;
    }
}
