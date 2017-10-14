package com.trevinavery.beyondthrift.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import com.trevinavery.beyondthrift.dao.AuthTokenDao;
import com.trevinavery.beyondthrift.dao.Database;
import com.trevinavery.beyondthrift.dao.EventDao;
import com.trevinavery.beyondthrift.dao.PersonDao;
import com.trevinavery.beyondthrift.dao.UserDao;
import fms.model.AuthToken;
import fms.model.Event;
import fms.model.Person;
import fms.model.User;
import fms.request.EventRequest;
import fms.result.EventResult;

import static org.junit.Assert.*;

/**
 * Created by trevinpa on 5/31/17.
 */
public class EventServiceTest {

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

    Event event1;
    Event event2;
    Event eventNotRelated;

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


        event1 = new Event(
                "eventid1",
                "username",
                "personID",
                34.43,
                98.432,
                "country",
                "city",
                Event.TYPE_BIRTH,
                1993
        );
        event2 = new Event(
                "eventid2",
                "username",
                "poppy",
                34.43,
                98.432,
                "country",
                "city",
                Event.TYPE_BIRTH,
                1968
        );
        eventNotRelated = new Event(
                "eventid3",
                "someoneelse",
                "notrelated",
                36.12,
                67.42,
                "country2",
                "city2",
                Event.TYPE_BIRTH,
                1982
        );

        EventDao eventDao = database.getEventDao();
        eventDao.addEvent(event1);
        eventDao.addEvent(event2);
        eventDao.addEvent(eventNotRelated);
    }

    @After
    public void tearDown() throws Exception {
        database.setLockedOpen(false);
        database.close(false);
    }

    @Test
    public void getEvent() throws Exception {

        EventRequest request = new EventRequest(
                authToken.getAuthToken(),
                event1.getEventID()
        );
        EventService service = new EventService(database);
        EventResult result = service.perform(request);

        assertEquals(EventService.STATUS_SUCCESS, service.getStatus());
        assertNull(result.getMessage());
        assertEquals(event1, result.getData()[0]);
    }

    @Test
    public void getEventInvalidAuthToken() throws Exception {

        EventRequest request = new EventRequest(
                invalidAuthToken.getAuthToken(),
                event1.getEventID()
        );
        EventService service = new EventService(database);
        EventResult result = service.perform(request);

        EventResult expectedResult = new EventResult(
                "Invalid auth token"
        );

        assertEquals(EventService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void getEventAuthTokenDoesntExist() throws Exception {

        EventRequest request = new EventRequest(
                "thisIsAFakeToken",
                event1.getEventID()
        );
        EventService service = new EventService(database);
        EventResult result = service.perform(request);

        EventResult expectedResult = new EventResult(
                "Invalid auth token"
        );

        assertEquals(EventService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void getEventThatDoesntExist() throws Exception {

        EventRequest request = new EventRequest(
                authToken.getAuthToken(),
                "badPersonId"
        );
        EventService service = new EventService(database);
        EventResult result = service.perform(request);

        EventResult expectedResult = new EventResult(
                "Invalid eventID parameter"
        );

        assertEquals(EventService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void getEventThatDoesntBelongToUser() throws Exception {

        EventRequest request = new EventRequest(
                authToken.getAuthToken(),
                eventNotRelated.getEventID()
        );
        EventService service = new EventService(database);
        EventResult result = service.perform(request);

        EventResult expectedResult = new EventResult(
                "Requested event does not belong to this user"
        );

        assertEquals(EventService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void getEvents() throws Exception {

        EventRequest request = new EventRequest(
                authToken.getAuthToken()
        );
        EventService service = new EventService(database);
        EventResult result = service.perform(request);

        assertEquals(EventService.STATUS_SUCCESS, service.getStatus());

        Event[] expectedAncestors = {event1, event2};
        ArrayList<Event> expectedList = new ArrayList<>(Arrays.asList(expectedAncestors));
        ArrayList<Event> dbList = new ArrayList<>(Arrays.asList(result.getData()));
        Assert.assertTrue(expectedList.containsAll(dbList));
        Assert.assertTrue(dbList.containsAll(expectedList));
    }

    @Test
    public void getEventsInvalidAuthToken() throws Exception {

        EventRequest request = new EventRequest(
                invalidAuthToken.getAuthToken()
        );
        EventService service = new EventService(database);
        EventResult result = service.perform(request);

        EventResult expectedResult = new EventResult(
                "Invalid auth token"
        );

        assertEquals(EventService.STATUS_BAD_REQUEST, service.getStatus());
        assertEquals(expectedResult, result);
    }

    @Test
    public void getEventsThatDontExist() throws Exception {

        EventRequest request = new EventRequest(
                noAncestorsAuthToken.getAuthToken()
        );
        EventService service = new EventService(database);
        EventResult result = service.perform(request);

        EventResult expectedResult = new EventResult(
                new Event[] {}
        );

        assertEquals(EventService.STATUS_SUCCESS, service.getStatus());
        assertEquals(expectedResult, result);
    }

}