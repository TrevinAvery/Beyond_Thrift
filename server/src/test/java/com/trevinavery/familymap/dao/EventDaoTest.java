package com.trevinavery.familymap.dao;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import fms.model.Event;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by trevinpa on 5/30/17.
 */
public class EventDaoTest {

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
    public void createEvent() throws Exception {
        Event event = new Event(
                null,
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_BIRTH,
                1993
        );

        EventDao eventDao = database.getEventDao();

        Event dbEvent = eventDao.createEvent(
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_BIRTH,
                1993
        );

        event.setEventID(dbEvent.getEventID());

        assertNotNull(dbEvent);
        assertEquals(event, dbEvent);
    }

    @Test
    public void addEvent() throws Exception {
        Event event = new Event(
                "eventID",
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_BIRTH,
                1993
        );

        EventDao eventDao = database.getEventDao();

        eventDao.addEvent(event);

        Event dbEvent = eventDao.getEvent("eventID");

        assertNotNull(dbEvent);
        assertEquals(event, dbEvent);
    }

    @Test
    public void getEventWithID() throws Exception {
        Event event = new Event(
                "eventID",
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_BIRTH,
                1993
        );

        EventDao eventDao = database.getEventDao();

        eventDao.addEvent(event);

        Event dbEvent = eventDao.getEvent("eventID");

        assertNotNull(dbEvent);
        assertEquals(event, dbEvent);
    }

    @Test
    public void getEventWithPersonAndType() throws Exception {
        Event event = new Event(
                "eventID",
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_BIRTH,
                1993
        );

        EventDao eventDao = database.getEventDao();

        eventDao.addEvent(event);

        Event dbEvent = eventDao.getEvent("personID", Event.TYPE_BIRTH);

        assertNotNull(dbEvent);
        assertEquals(event, dbEvent);
    }

    @Test
    public void getEventThatDoesntExist() throws Exception {
        EventDao eventDao = database.getEventDao();

        Event dbEvent = eventDao.getEvent("eventID");
        assertNull(dbEvent);

        dbEvent = eventDao.getEvent("personID", Event.TYPE_BIRTH);
        assertNull(dbEvent);
    }

    @Test
    public void getEvents() throws Exception {
        Event event1 = new Event(
                "eventID1",
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_BIRTH,
                1993
        );

        Event event2 = new Event(
                "eventID2",
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_CHRISTENING,
                1994
        );

        Event event3 = new Event(
                "eventID3",
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_BAPTISM,
                2001
        );

        Event excludeEvent = new Event(
                "eventID4",
                "someoneelse",
                "personID",
                23.84,
                64.23,
                "usa",
                "city",
                Event.TYPE_BIRTH,
                2001
        );

        EventDao eventDao = database.getEventDao();

        eventDao.addEvent(event1);
        eventDao.addEvent(event2);
        eventDao.addEvent(event3);
        eventDao.addEvent(excludeEvent);

        Event[] dbEvents = eventDao.getEvents("username");

        assertNotNull(dbEvents);
        assertEquals(3, dbEvents.length);

        Event[] expectedEvents = {event1, event2, event3};
        ArrayList<Event> expectedList = new ArrayList<>(Arrays.asList(expectedEvents));
        ArrayList<Event> dbList = new ArrayList<>(Arrays.asList(dbEvents));
        Assert.assertTrue(expectedList.containsAll(dbList));
        Assert.assertTrue(dbList.containsAll(expectedList));
    }

    @Test
    public void deleteEvents() throws Exception {
        Event event1 = new Event(
                "eventID1",
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_BIRTH,
                1993
        );

        Event event2 = new Event(
                "eventID2",
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_CHRISTENING,
                1994
        );

        Event event3 = new Event(
                "eventID3",
                "username",
                "personID",
                23.23,
                52.12,
                "usa",
                "city",
                Event.TYPE_BAPTISM,
                2001
        );

        Event excludeEvent = new Event(
                "eventID4",
                "someoneelse",
                "personID",
                23.84,
                64.23,
                "usa",
                "city",
                Event.TYPE_BIRTH,
                2001
        );

        EventDao eventDao = database.getEventDao();

        eventDao.addEvent(event1);
        eventDao.addEvent(event2);
        eventDao.addEvent(event3);
        eventDao.addEvent(excludeEvent);

        eventDao.deleteEvents("username");

        Event[] dbEvents = eventDao.getEvents("username");
        assertEquals(0, dbEvents.length);

        Event[] otherEvents = eventDao.getEvents("someoneelse");
        assertNotNull(otherEvents);
        assertEquals(1, otherEvents.length);
        Event[] expectedEvents = {excludeEvent};
        assertArrayEquals(expectedEvents, otherEvents);
    }

    @Test
    public void deleteEventsThatDontExist() throws Exception {

        EventDao eventDao = database.getEventDao();
        eventDao.deleteEvents("username");
    }

}