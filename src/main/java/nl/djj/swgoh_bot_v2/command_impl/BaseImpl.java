package nl.djj.swgoh_bot_v2.command_impl;

import nl.djj.swgoh_bot_v2.database.DAO;
import nl.djj.swgoh_bot_v2.helpers.Logger;

/**
 * @author DJJ
 **/
public class BaseImpl {
    protected final transient Logger logger;
    protected final transient DAO dao;
    protected transient String className;

    /**
     * Constructor.
     **/
    public BaseImpl(final Logger logger, final DAO dao, final String className) {
        super();
        this.logger = logger;
        this.dao = dao;
        this.className = className;
    }
}
