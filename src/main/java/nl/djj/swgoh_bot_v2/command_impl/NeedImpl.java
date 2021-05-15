package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.entities.Message;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 **/
public class NeedImpl extends BaseImpl {

    /**
     * Constructor.
     **/
    public NeedImpl(final Logger logger, final DAO dao) {
        super(logger, dao, NeedImpl.class.getName());
    }

    public void lightSide(final Message message) {
    }

    public void darkSide(final Message message) {
    }

    public void cantina(final Message message) {
    }

    public void all(final Message message) {
    }
}
