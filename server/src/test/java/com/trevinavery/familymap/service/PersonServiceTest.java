package com.trevinavery.familymap.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import com.trevinavery.familymap.dao.AuthTokenDao;
import com.trevinavery.familymap.dao.Database;
import com.trevinavery.familymap.dao.PersonDao;
import com.trevinavery.familymap.dao.UserDao;
import fms.model.AuthToken;
import fms.model.Person;
import fms.model.User;
import fms.request.PersonRequest;
import fms.result.PersonResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by trevinpa on 5/30/17.
 */
public class PersonServiceTest {

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
    public void getPerson() throws Exception {

        PersonRequest request = new PersonRequest(
                authToken.getAuthToken(),
                person.getPersonID()
        );
        PersonService service = new PersonService(database);
        PersonResult result = service.perform(request);

        assertEquals(PersonService.STATUS_SUCCESS, service.getStatus());
        assertNull(result.getMessage());
        assertEquals(person, result.getData()[0]);
    }

    @Test
    public void getPersonInvalidAuthToken() throws Exception {

        PersonRequest request = new PersonRequest(
                invalidAuthToken.getAuthToken(),
                person.getPersonID()
        );
        PersonService service = new PersonService(database);
        PersonResult result = service.perform(request);

        PersonResult expectedResult = new PersonResult(
                "Invalid auth token"
        );

        assertEquals(PersonService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void getPersonAuthTokenDoesntExist() throws Exception {

        PersonRequest request = new PersonRequest(
                "thisIsAFakeToken",
                person.getPersonID()
        );
        PersonService service = new PersonService(database);
        PersonResult result = service.perform(request);

        PersonResult expectedResult = new PersonResult(
                "Invalid auth token"
        );

        assertEquals(PersonService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void getPersonThatDoesntExist() throws Exception {

        PersonRequest request = new PersonRequest(
                authToken.getAuthToken(),
                "badPersonId"
        );
        PersonService service = new PersonService(database);
        PersonResult result = service.perform(request);

        PersonResult expectedResult = new PersonResult(
                "Invalid personID parameter"
        );

        assertEquals(PersonService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void getPersonThatDoesntBelongToUser() throws Exception {

        PersonRequest request = new PersonRequest(
                authToken.getAuthToken(),
                notRelated.getPersonID()
        );
        PersonService service = new PersonService(database);
        PersonResult result = service.perform(request);

        PersonResult expectedResult = new PersonResult(
                "Requested person does not belong to this user"
        );

        assertEquals(PersonService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void getAncestors() throws Exception {

        PersonRequest request = new PersonRequest(
                authToken.getAuthToken()
        );
        PersonService service = new PersonService(database);
        PersonResult result = service.perform(request);

        assertEquals(PersonService.STATUS_SUCCESS, service.getStatus());

        Person[] expectedAncestors = {person, father, mother};
        ArrayList<Person> expectedList = new ArrayList<>(Arrays.asList(expectedAncestors));
        ArrayList<Person> dbList = new ArrayList<>(Arrays.asList(result.getData()));
        Assert.assertTrue(expectedList.containsAll(dbList));
        Assert.assertTrue(dbList.containsAll(expectedList));
    }

    @Test
    public void getAncestorsInvalidAuthToken() throws Exception {

        PersonRequest request = new PersonRequest(
                invalidAuthToken.getAuthToken()
        );
        PersonService service = new PersonService(database);
        PersonResult result = service.perform(request);

        PersonResult expectedResult = new PersonResult(
                "Invalid auth token"
        );

        assertEquals(PersonService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void getAncestorsThatDontExist() throws Exception {

        PersonRequest request = new PersonRequest(
                noAncestorsAuthToken.getAuthToken()
        );
        PersonService service = new PersonService(database);
        PersonResult result = service.perform(request);

        PersonResult expectedResult = new PersonResult(
                new Person[] {}
        );

        assertEquals(PersonService.STATUS_SUCCESS, service.getStatus());
        assertEquals(expectedResult, result);
    }
}