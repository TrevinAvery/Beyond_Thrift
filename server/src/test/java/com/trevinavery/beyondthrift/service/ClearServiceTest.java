package com.trevinavery.beyondthrift.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.trevinavery.beyondthrift.dao.AuthTokenDao;
import com.trevinavery.beyondthrift.dao.Database;
import com.trevinavery.beyondthrift.dao.EventDao;
import com.trevinavery.beyondthrift.dao.PersonDao;
import com.trevinavery.beyondthrift.dao.UserDao;
import fms.model.AuthToken;
import fms.model.Event;
import fms.model.Person;
import fms.model.User;
import fms.request.ClearRequest;
import fms.result.ClearResult;

import static org.junit.Assert.*;

/**
 * Created by trevinpa on 5/31/17.
 */
public class ClearServiceTest {

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

    Event event;

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

        event = new Event(
                "eventid",
                "username",
                "personID",
                34.43,
                98.432,
                "country",
                "city",
                Event.TYPE_BIRTH,
                1993
        );

        EventDao eventDao = database.getEventDao();
        eventDao.addEvent(event);
    }

    @After
    public void tearDown() throws Exception {
        database.setLockedOpen(false);
        database.close(false);
    }

    @Test
    public void perform() throws Exception {

        ClearService service = new ClearService(database);

        ClearResult result = service.perform(new ClearRequest());

        ClearResult expectedResult = new ClearResult(
                "Clear succeeded."
        );

        assertEquals(ClearService.STATUS_SUCCESS, service.getStatus());
        assertEquals(expectedResult, result);

//        assertNull(database.getAuthTokenDao().getAuthToken(authToken.getAuthToken()));
//        assertNull(database.getUserDao().getUser(user.getUserName()));
//        assertNull(database.getPersonDao().getPerson(person.getPersonID()));
//        assertNull(database.getEventDao().getEvent(event.getEventID()));
    }

}