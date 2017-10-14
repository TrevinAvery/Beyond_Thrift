package com.trevinavery.familymap.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trevinavery.familymap.dao.AuthTokenDao;
import com.trevinavery.familymap.dao.Database;
import com.trevinavery.familymap.dao.UserDao;
import fms.model.AuthToken;
import fms.model.User;
import fms.request.LoginRequest;
import fms.result.LoginResult;

import static org.junit.Assert.*;

/**
 * Created by trevinpa on 5/31/17.
 */
public class LoginServiceTest {

    private Database database;

    User user;

    @Before
    public void setUp() throws Exception {
        database = new Database();
        database.setLockedOpen(true);
        database.open();
        database.clear();


        user = new User(
                "username",
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m",
                "personid"
        );
        UserDao userDao = database.getUserDao();
        userDao.addUser(user);
    }

    @After
    public void tearDown() throws Exception {
        database.setLockedOpen(false);
        database.close(false);
    }

    @Test
    public void login() throws Exception {

        LoginRequest request = new LoginRequest(
                user.getUserName(),
                user.getPassword()
        );
        LoginService service = new LoginService(database);
        LoginResult result = service.perform(request);

        assertEquals(LoginService.STATUS_SUCCESS, service.getStatus());
        assertNull(result.getMessage());
        assertNotNull(result.getAuthToken());
        assertEquals(user.getUserName(), result.getUserName());
        assertEquals(user.getPersonID(), result.getPersonID());

        AuthTokenDao authTokenDao = database.getAuthTokenDao();
        AuthToken authToken = authTokenDao.getAuthToken(result.getAuthToken());

        assertNotNull(authToken);
    }

    @Test
    public void loginBadUserName() throws Exception {

        LoginRequest request = new LoginRequest(
                "not a real username",
                user.getPassword()
        );
        LoginService service = new LoginService(database);
        LoginResult result = service.perform(request);

        LoginResult expectedResult = new LoginResult(
                "Request property missing or has invalid value"
        );

        assertEquals(LoginService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void loginBadPassword() throws Exception {

        LoginRequest request = new LoginRequest(
                user.getUserName(),
                "not the real password"
        );
        LoginService service = new LoginService(database);
        LoginResult result = service.perform(request);

        LoginResult expectedResult = new LoginResult(
                "Request property missing or has invalid value"
        );

        assertEquals(LoginService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

}