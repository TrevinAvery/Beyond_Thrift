package com.trevinavery.familymap.dao;

import org.junit.Test;

import fms.model.AuthToken;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by trevinpa on 5/30/17.
 */
public class AuthTokenDaoTest {

    private Database database = new Database();

    @org.junit.Before
    public void setUp() throws Exception {
        database.open();
        database.clear();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        database.close(false);
    }

    @Test
    public void createAuthToken() throws Exception {
        AuthToken authToken = new AuthToken(
                null,
                "username",
                0
        );

        AuthTokenDao authTokenDao = database.getAuthTokenDao();

        AuthToken dbAuthToken = authTokenDao.createAuthToken(
                "username"
        );

        authToken.setAuthToken(dbAuthToken.getAuthToken());
        authToken.setDateCreated(dbAuthToken.getDateCreated());

        assertNotNull(dbAuthToken.getAuthToken());
        assertNotEquals(0, dbAuthToken.getDateCreated());

        assertNotNull(dbAuthToken);
        assertEquals(authToken, dbAuthToken);
    }

    @Test
    public void addAuthToken() throws Exception {
        AuthToken authToken = new AuthToken(
                "somestringforanauthtoken",
                "username",
                System.currentTimeMillis()
        );

        AuthTokenDao authTokenDao = database.getAuthTokenDao();

        authTokenDao.addAuthToken(authToken);

        AuthToken dbAuthToken = authTokenDao.getAuthToken("somestringforanauthtoken");

        assertNotNull(dbAuthToken);
        assertEquals(authToken, dbAuthToken);
    }

    @Test
    public void getAuthToken() throws Exception {
        AuthToken authToken = new AuthToken(
                "somestringforanauthtoken",
                "username",
                System.currentTimeMillis()
        );

        AuthTokenDao authTokenDao = database.getAuthTokenDao();

        authTokenDao.addAuthToken(authToken);

        AuthToken dbAuthToken = authTokenDao.getAuthToken("somestringforanauthtoken");

        assertNotNull(dbAuthToken);
        assertEquals(authToken, dbAuthToken);
    }

    @Test
    public void clearExpiredAuthTokens() throws Exception {

        AuthToken authToken1 = new AuthToken(
                "token1",
                "username1",
                0
        );

        AuthToken authToken2 = new AuthToken(
                "token2",
                "username2",
                0
        );

        AuthToken authToken3 = new AuthToken(
                "token3",
                "username3",
                0
        );

        AuthToken authToken4 = new AuthToken(
                "token4",
                "username4",
                System.currentTimeMillis()
        );

        AuthTokenDao authTokenDao = database.getAuthTokenDao();

        authTokenDao.addAuthToken(authToken1);
        authTokenDao.addAuthToken(authToken2);
        authTokenDao.addAuthToken(authToken3);
        authTokenDao.addAuthToken(authToken4);

        authTokenDao.clearExpiredAuthTokens();

        AuthToken dbAuthToken1 = authTokenDao.getAuthToken("token1");
        AuthToken dbAuthToken2 = authTokenDao.getAuthToken("token2");
        AuthToken dbAuthToken3 = authTokenDao.getAuthToken("token3");
        AuthToken dbAuthToken4 = authTokenDao.getAuthToken("token4");

        // expired
        assertNull(dbAuthToken1);
        assertNull(dbAuthToken2);
        assertNull(dbAuthToken3);

        // not expired
        assertNotNull(dbAuthToken4);
    }

}