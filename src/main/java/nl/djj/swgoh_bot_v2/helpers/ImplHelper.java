package nl.djj.swgoh_bot_v2.helpers;

import nl.djj.swgoh_bot_v2.command_impl.*;
import nl.djj.swgoh_bot_v2.database.DAO;

/**
 * @author DJJ
 */
public class ImplHelper {

    private final transient ProfileImpl profileImpl;
    private final transient CommandImpl commandImpl;
    private final transient UpdateImpl updateImpl;
    //    private final transient ControlImpl controlImpl;
    private final transient UnitImpl unitImpl;
    private final transient ConfigImpl configImpl;
    private final transient GuildImpl guildImpl;
//    private final transient ReportImpl reportImpl;

    /**
     * @param logger the logger.
     * @param dao    the DB connection.
     */
    public ImplHelper(final Logger logger, final DAO dao) {
        super();
        this.profileImpl = new ProfileImpl(logger, dao, this);
        this.commandImpl = new CommandImpl(logger, dao);
        this.updateImpl = new UpdateImpl(logger, dao);
//        this.controlImpl = new ControlImpl(logger, dao);
        this.unitImpl = new UnitImpl(dao, logger);
        this.configImpl = new ConfigImpl(dao, logger);
        this.guildImpl = new GuildImpl(logger, dao, this);
//        this.reportImpl = new ReportImpl(logger, dao);
    }


    public ProfileImpl getProfileImpl() {
        return this.profileImpl;
    }

    public CommandImpl getCommandImpl() {
        return this.commandImpl;
    }

    public UpdateImpl getUpdateImpl() {
        return this.updateImpl;
    }

//    public ControlImpl getControlImpl() {
//        return this.controlImpl;
//    }

    public UnitImpl getUnitImpl() {
        return this.unitImpl;
    }

    public ConfigImpl getConfigImpl() {
        return this.configImpl;
    }

    public GuildImpl getGuildImpl() {
        return this.guildImpl;
    }

//    public ReportImpl getReportImpl() {
//        return reportImpl;
//    }
}
