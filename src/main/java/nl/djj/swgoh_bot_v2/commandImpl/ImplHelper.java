package nl.djj.swgoh_bot_v2.commandImpl;

import nl.djj.swgoh_bot_v2.database.DatabaseHandler;
import nl.djj.swgoh_bot_v2.helpers.Logger;

public class ImplHelper {

    private final transient ProfileImpl profileImpl;
    private final transient CommandImpl commandImpl;
    private final transient UpdateImpl updateImpl;
    private final transient ControlImpl controlImpl;

    public ImplHelper(final Logger logger, final DatabaseHandler dbHandler){
        super();
        this.profileImpl = new ProfileImpl(logger, dbHandler);
        this.commandImpl = new CommandImpl(logger, dbHandler);
        this.updateImpl = new UpdateImpl(logger, dbHandler);
        this.controlImpl = new ControlImpl(logger, dbHandler);
    }

    public ProfileImpl getProfileImpl(){
        return this.profileImpl;
    }

    public CommandImpl getCommandImpl(){
        return this.commandImpl;
    }

    public UpdateImpl getUpdateImpl(){
        return this.updateImpl;
    }

    public ControlImpl getControlImpl(){
        return this.controlImpl;
    }
}
