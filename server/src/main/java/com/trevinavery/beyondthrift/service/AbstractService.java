package com.trevinavery.beyondthrift.service;

import com.trevinavery.beyondthrift.dao.Database;
import com.trevinavery.beyondthrift.dao.DatabaseException;

import java.net.HttpURLConnection;
import java.util.logging.Logger;

import com.trevinavery.beyondthrift.model.AuthToken;

/**
 * An abstract class to share like code for all services.
 */
public abstract class AbstractService {

    protected static Logger logger;

    static {
        logger = Logger.getLogger("familymapserver");
    }

    public static final int STATUS_BAD_REQUEST    = HttpURLConnection.HTTP_BAD_REQUEST;
    public static final int STATUS_SUCCESS        = HttpURLConnection.HTTP_OK;
    public static final int STATUS_INTERNAL_ERROR = HttpURLConnection.HTTP_INTERNAL_ERROR;

    protected Database database;
    protected int status;

    /**
     * Creates a service using the given database.
     * If the database us null, it instantiates a new one.
     *
     * @param database the Database object to use when performing the service
     */
    public AbstractService(Database database) {
        this.database = database;

        if (this.database == null) {
            this.database = new Database();
        }
    }

    /**
     * Checks if the AuthToken string is in the database and not expired.
     *
     * @param authTokenStr the string to check for
     * @return the userName associated with the AuthToken,
     *          or null if the AuthToken was not found
     * @throws DatabaseException if editing the database fails
     */
    protected String authenticate(String authTokenStr)
            throws DatabaseException {

        if (authTokenStr != null) {

            AuthToken authToken = database.getAuthTokenDao().getAuthToken(authTokenStr);

            if (authToken != null) {
                return authToken.getUserName();
            }
        }

        return null;
    }

    public int getStatus() {
        return status;
    }

    protected void setStatus(int status) {
        this.status = status;
    }
}
