package com.trevinavery.beyondthrift.dao;

import java.sql.Connection;
import java.util.logging.Logger;

/**
 * An abstract class to share like code for all data access objects.
 */
public abstract class AbstractDao {

    protected static Logger logger;

    static {
        logger = Logger.getLogger("familymapserver");
    }

    protected Connection connection;

    /**
     * Creates a Data Access Object associated with the
     * database that created the given connection.
     *
     * @param connection the Database Connection object to
     *                   use when accessing the database
     */
    public AbstractDao(Connection connection) {
        this.connection = connection;
    }
}