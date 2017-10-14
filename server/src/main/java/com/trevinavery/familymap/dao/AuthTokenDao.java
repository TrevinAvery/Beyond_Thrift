package com.trevinavery.familymap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import com.trevinavery.familymap.model.AuthToken;
import com.trevinavery.familymap.server.Server;

/**
 * The AuthTokenDao class creates a Data Access Object that is used to
 * manipulate all authentication authToken related data in the provided database.
 */
public class AuthTokenDao extends AbstractDao {

    /**
     * Creates a Data Access Object associated with the
     * database that created the given connection.
     *
     * @param connection the Database Connection object to
     *                   use when accessing the database
     */
    public AuthTokenDao(Connection connection) {
        super(connection);
    }

    /**
     * Creates a new authentication token and adds it to the database.
     *
     * @param userName the UserName to associate with the AuthToken
     * @return the AuthToken created
     * @throws DatabaseException if editing the database fails
     */
    public AuthToken createAuthToken(String userName)
            throws DatabaseException {

        String authTokenStr = UUID.randomUUID().toString();

        long dateCreated = System.currentTimeMillis();

        AuthToken authToken = new AuthToken(
                authTokenStr,
                userName,
                dateCreated
        );

        addAuthToken(authToken);

        return authToken;
    }

    /**
     * Adds an authentication token to the database.
     *
     * @param authToken the AuthToken to add to the database
     * @throws DatabaseException if editing the database fails
     */
    public void addAuthToken(AuthToken authToken)
            throws DatabaseException {

        PreparedStatement stmt = null;
        try {
            String sql =
                    "insert into AuthTokens (" +
                            "AuthToken, UserName, DateCreated" +
                    ") " +
                    "values (?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, authToken.getAuthToken());
            stmt.setString(2, authToken.getUserName());
            stmt.setLong(3, authToken.getDateCreated());

            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException();
            }
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

    /**
     * Gets an AuthToken if it exists in the database and has not expired.
     * Returns null otherwise.
     *
     * @param authToken the String representing the AuthToken object to authenticate
     * @return the AuthToken if it exists, null otherwise
     * @throws DatabaseException if editing the database fails
     */
    public AuthToken getAuthToken(String authToken)
            throws DatabaseException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            // check if the authToken is in the database and is not expired

            long expirationDate = System.currentTimeMillis() - Server.getAuthTokenTimeout();

            String sql =
                    "select * " +
                    "from AuthTokens " +
                    "where AuthToken = ? and DateCreated >= ?";
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, authToken);
            stmt.setLong(2, expirationDate);

            rs = stmt.executeQuery();

            // if authToken was found
            if (rs.next()) {
                return new AuthToken(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getLong(3)
                );
            }
            // authToken not found or expired
            return null;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }

    /**
     * Removes all expired authentication authTokens from the database.
     *
     * @throws DatabaseException if editing the database fails
     */
    public void clearExpiredAuthTokens()
            throws DatabaseException {

        PreparedStatement stmt = null;
        try {

            // find all authTokens that have expired and delete them

            long expirationDate = System.currentTimeMillis() - Server.getAuthTokenTimeout();

            String sql =
                    "delete from AuthTokens " +
                    "where DateCreated < ?";
            stmt = connection.prepareStatement(sql);
            stmt.setLong(1, expirationDate);

            stmt.executeUpdate();
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
