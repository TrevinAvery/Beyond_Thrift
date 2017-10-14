package com.trevinavery.beyondthrift.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Database class creates and manages a connection to the database and provides instances of
 * every Data Access Object that can be used with the database. After initialization, the database
 * must be opened with <code>open()</code> before its Data Access Objects can be returned. When all
 * changes have been made to the database, it must be closed with <code>close(true)</code> to save
 * the changes, or <code>close(false)</code> to rollback. If the database is not closed, a memory
 * leak may occur.
 */
public class Database {

    protected static Logger logger;

    static {
        logger = Logger.getLogger("familymapserver");

        // load driver
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch(ClassNotFoundException e) {
            // ERROR! Could not load database driver
            logger.log(Level.SEVERE, "Could not load database driver: " + e.getMessage(), e);
        }
    }

    private Connection connection = null;
    private boolean open = false;
    private boolean lockedOpen = false;

    /**
     * Constructs a Database object
     */
    public Database() {
        // default constructor
    }

    /**
     * Gets an AuthTokenDao for this instance of Database.
     *
     * @return an AuthTokenDao object that shares this Database's connection,
     *          or null if the connection has not been opened
     */
    public AuthTokenDao getAuthTokenDao() {
        if (connection != null) {
            return new AuthTokenDao(connection);
        }
        return null;
    }

    /**
     * Gets an EventDao for this instance of Database.
     *
     * @return an EventDao object that shares this Database's connection,
     *          or null if the connection has not been opened
     */
    public EventDao getEventDao() {
        if (connection != null) {
            return new EventDao(connection);
        }
        return null;
    }

    /**
     * Gets a PersonDao for this instance of Database.
     *
     * @return a PersonDao object that shares this Database's connection,
     *          or null if the connection has not been opened
     */
    public PersonDao getPersonDao() {
        if (connection != null) {
            return new PersonDao(connection);
        }
        return null;
    }

    /**
     * Gets a UserDao for this instance of Database.
     *
     * @return a UserDao object that shares this Database's connection,
     *          or null if the connection has not been opened
     */
    public UserDao getUserDao() {
        if (connection != null) {
            return new UserDao(connection);
        }
        return null;
    }

    /**
     * If lockedOpen is true, then the database will no close.
     * This allows testing access and manipulation of the database
     * without risking the loss of any data by a service committing.
     *
     * @param lockedOpen whether or not the database is locked open
     */
    public void setLockedOpen(boolean lockedOpen) {
        this.lockedOpen = lockedOpen;
    }

    /**
     * Opens the connection to the database. This must be called before any Data Access Objects
     * can be returned. This connection uses the transaction model and requires that it be closed
     * before any changes are saved. If the database is already open when this is called, the
     * result is a no-op.
     *
     * @throws DatabaseException if editing the database fails
     * @see #close(boolean)
     */
    public void open() throws DatabaseException {
        if (open) {
            return;
        }

        String connectionURL = "jdbc:sqlite:db" + File.separator + "database.sqlite";

        try {
            // Open a database connection
            connection = DriverManager.getConnection(connectionURL);

            // Start a transaction
            connection.setAutoCommit(false);

            open = true;
        } catch (SQLException e) {
            throw new DatabaseException("There was an exception in the database while opening.", e);
        }
    }

    /**
     * Closes the database connection. If database is already closed, then this is a no-op.
     *
     * @param commit true if the changes should be committed, false if they should rollback
     * @throws DatabaseException if editing the database fails
     */
    public void close(boolean commit) throws DatabaseException {

        if (connection == null || lockedOpen) return;

        try {
            if (commit) {
                connection.commit();
            } else {
                connection.rollback();
            }

            open = false;
            connection.close();
            connection = null;
        } catch (SQLException e) {
            throw new DatabaseException("There was an exception in the database while closing.", e);
        }
    }

    /**
     * Clears all data in the database.
     *
     * @throws DatabaseException if editing the database fails
     */
    public void clear() throws DatabaseException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from Users";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();

            sql = "delete from Persons";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();

            sql = "delete from Events";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();

            sql = "delete from AuthTokens";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }
}
