package com.trevinavery.familymap.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trevinavery.familymap.dao.AuthTokenDao;
import com.trevinavery.familymap.dao.Database;
import com.trevinavery.familymap.dao.PersonDao;
import fms.model.Person;
import fms.request.RegisterRequest;
import fms.result.RegisterResult;

import static org.junit.Assert.*;

/**
 * Created by trevinpa on 5/30/17.
 */
public class RegisterServiceTest {

    private Database database;

    @Before
    public void setUp() throws Exception {
        database = new Database();
        database.setLockedOpen(true);
        database.open();
        database.clear();
    }

    @After
    public void tearDown() throws Exception {
        database.setLockedOpen(false);
        database.close(false);
    }

    @Test
    public void register() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "username",
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m"
        );

        RegisterService service = new RegisterService(database);

        RegisterResult result = service.perform(request);

        assertEquals(RegisterService.STATUS_SUCCESS, service.getStatus());

        assertNotNull(result.getAuthToken());
        assertEquals(result.getUserName(), "username");
        assertNotNull(result.getPersonID());
        assertNull(result.getMessage());

        // confirm it logs the user in
        AuthTokenDao authTokenDao = database.getAuthTokenDao();
        assertNotNull(authTokenDao.getAuthToken(result.getAuthToken()));

        // confirm there was a fill
        PersonDao personDao = database.getPersonDao();
        Person[] ancestors = personDao.getAncestors(result.getUserName());
        assertEquals(31, ancestors.length);
    }

    @Test
    public void registerUserNameTaken() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "username",
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m"
        );

        // register once to make sure username is taken
        RegisterService service1 = new RegisterService(database);
        RegisterResult result1 = service1.perform(request);
        assertEquals(RegisterService.STATUS_SUCCESS, service1.getStatus());

        RegisterService service2 = new RegisterService(database);
        RegisterResult result2 = service2.perform(request);
        assertEquals(RegisterService.STATUS_BAD_REQUEST, service2.getStatus());

        RegisterResult expectedResult = new RegisterResult(
                "Username already taken by another user"
        );

        assertEquals(expectedResult, result2);
    }

    @Test
    public void registerMissingValue() throws Exception {
        RegisterRequest request = new RegisterRequest(
                null,
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m"
        );

        RegisterService service = new RegisterService(database);
        RegisterResult result = service.perform(request);

        RegisterResult expectedResult = new RegisterResult(
                "Request property missing or has invalid value"
        );

        assertEquals(RegisterService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

}