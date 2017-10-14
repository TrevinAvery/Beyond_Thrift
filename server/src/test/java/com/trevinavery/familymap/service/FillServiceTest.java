package com.trevinavery.familymap.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.trevinavery.familymap.dao.AuthTokenDao;
import com.trevinavery.familymap.dao.Database;
import com.trevinavery.familymap.dao.PersonDao;
import com.trevinavery.familymap.dao.UserDao;
import fms.model.AuthToken;
import fms.model.Person;
import fms.model.User;
import fms.request.FillRequest;
import fms.result.FillResult;

import static org.junit.Assert.*;

/**
 * Created by trevinpa on 5/31/17.
 */
public class FillServiceTest {

    private Database database;

    User user;
    User someoneelse;
    User noancestors;

    AuthToken authToken;
    AuthToken noAncestorsAuthToken;
    AuthToken invalidAuthToken;

    Person person;
    Person father;
    Person mother;
    Person notRelated;

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
        someoneelse = new User(
                "someoneelse",
                "password",
                "email2@site.com",
                "some",
                "one",
                "f",
                "notrelated"
        );
        noancestors = new User(
                "noancestors",
                "password",
                "email3@site.com",
                "no",
                "ancestors",
                "f",
                null
        );
        UserDao userDao = database.getUserDao();
        userDao.addUser(user);
        userDao.addUser(someoneelse);
        userDao.addUser(noancestors);


        authToken = new AuthToken(
                "token",
                user.getUserName(),
                System.currentTimeMillis()
        );
        noAncestorsAuthToken = new AuthToken(
                "token2",
                noancestors.getUserName(),
                System.currentTimeMillis()
        );
        invalidAuthToken = new AuthToken(
                "invalidtoken",
                someoneelse.getUserName(),
                0
        );
        AuthTokenDao authTokenDao = database.getAuthTokenDao();
        authTokenDao.addAuthToken(authToken);
        authTokenDao.addAuthToken(noAncestorsAuthToken);
        authTokenDao.addAuthToken(invalidAuthToken);


        person = new Person(
                "personid",
                "username",
                "firstname",
                "lastname",
                "m",
                "poppy",
                "mummy",
                null
        );
        father = new Person(
                "poppy",
                "username",
                "ffirstname",
                "flastname",
                "m",
                null,
                null,
                "mummy"
        );
        mother = new Person(
                "mummy",
                "username",
                "mfirstname",
                "mlastname",
                "f",
                null,
                null,
                "poppy"
        );
        notRelated = new Person(
                "notrelated",
                "someoneelse",
                "hi",
                "bye",
                "f",
                null,
                null,
                null
        );

        PersonDao personDao = database.getPersonDao();
        personDao.addPerson(person);
        personDao.addPerson(mother);
        personDao.addPerson(father);
        personDao.addPerson(notRelated);
    }

    @After
    public void tearDown() throws Exception {
        database.setLockedOpen(false);
        database.close(false);
    }

    @Test
    public void fill() throws Exception {

        FillRequest request = new FillRequest(
                "username",
                4
        );

        FillService service = new FillService(database);
        FillResult result = service.perform(request);

        assertEquals(FillService.STATUS_SUCCESS, service.getStatus());
        Assert.assertTrue(result.getMessage()
                .startsWith("Successfully added 31 persons"));
    }

    @Test
    public void fillBadUsername() throws Exception {

        FillRequest request = new FillRequest(
                "asdf",
                4
        );

        FillService service = new FillService(database);
        FillResult result = service.perform(request);

        FillResult expectedResult = new FillResult(
                "Invalid username or generations parameter"
        );

        assertEquals(FillService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void fillBadNumGenerations() throws Exception {

        FillRequest request = new FillRequest(
                "username",
                -1
        );

        FillService service = new FillService(database);
        FillResult result = service.perform(request);

        FillResult expectedResult = new FillResult(
                "Invalid username or generations parameter"
        );

        assertEquals(FillService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }
}