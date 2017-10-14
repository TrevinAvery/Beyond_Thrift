package com.trevinavery.familymap.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * Created by trevinpa on 6/12/17.
 */
public class ModelTest {

    private Person[] persons;
    private Event[] events;

    private String person = "person";
    private String father = "father";
    private String mother = "mother";
    private String fathersFather = "fathersFather";
    private String fathersMother = "fathersMother";
    private String mothersFather = "mothersFather";
    private String mothersMother = "mothersMother";

    @Before
    public void setUp() throws Exception {

        persons = new Person[] {
                new Person(
                        person,
                        person,
                        person,
                        person,
                        "m",
                        father,
                        mother,
                        null
                ),
                new Person(
                        father,
                        person,
                        father,
                        father,
                        "m",
                        fathersFather,
                        fathersMother,
                        mother
                ),
                new Person(
                        mother,
                        person,
                        mother,
                        mother,
                        "f",
                        mothersFather,
                        mothersMother,
                        father
                ),
                new Person(
                        fathersFather,
                        person,
                        fathersFather,
                        fathersFather,
                        "m",
                        null,
                        null,
                        fathersMother
                ),
                new Person(
                        fathersMother,
                        person,
                        fathersMother,
                        fathersMother,
                        "f",
                        null,
                        null,
                        fathersFather
                ),
                new Person(
                        mothersFather,
                        person,
                        mothersFather,
                        mothersFather,
                        "m",
                        null,
                        null,
                        mothersMother
                ),
                new Person(
                        mothersMother,
                        person,
                        mothersMother,
                        mothersMother,
                        "f",
                        null,
                        null,
                        mothersFather
                ),
        };

        Set<Event> eventSet = new TreeSet<>();

        int eventId = 0;
        for (Person per : persons) {
            eventSet.add(new Event(
                    Integer.toString(eventId++),
                    person,
                    per.getPersonID(),
                    0,
                    0,
                    "country",
                    "city",
                    "birth",
                    1993
            ));

            eventSet.add(new Event(
                    Integer.toString(eventId++),
                    person,
                    per.getPersonID(),
                    0,
                    0,
                    "country",
                    "city",
                    "death",
                    1993
            ));
        }

        events = new Event[eventSet.size()];
        int i = 0;
        for (Event event : eventSet) {
            events[i++] = event;
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void login() throws Exception {

        // log in
        String authToken = "authToken";
        String username = "username";
        String personID = "personID";

        Model.login(authToken, username, personID);

        // check if the user information is correct
        assertNotNull(Model.getAuthToken());
        assertNotNull(Model.getUserName());
        assertNotNull(Model.getPersonID());
    }

    @Test
    public void logout() throws Exception {

        // enter user information
        String authToken = "authToken";
        String username = "username";
        String personID = "personID";

        Model.login(authToken, username, personID);

        // make sure there is user information
        assertNotNull(Model.getAuthToken());
        assertNotNull(Model.getUserName());
        assertNotNull(Model.getPersonID());

        // log out
        Model.logout();

        // check if the user information is gone
        assertNull(Model.getAuthToken());
        assertNull(Model.getUserName());
        assertNull(Model.getPersonID());

        assertEquals(0, Model.getFilteredEvents().size());
    }

    @Test
    public void getFilteredEvents() throws Exception {

        load();

        Set<Event> filteredEvents = Model.getFilteredEvents();
        assertEquals(14, filteredEvents.size());

        Model.setFathersSideEventsEnabled(false);

        // father's side should have 6 events
        // turn them off and we should have 8
        filteredEvents = Model.getFilteredEvents();
        assertEquals(8, filteredEvents.size());
    }

    @Test
    public void getFilteredEventsWithId() throws Exception {

        load();

        String personID = mother;

        Set<Event> filteredEvents = Model.getFilteredEvents(personID);
        assertEquals(2, filteredEvents.size());

        Model.setMothersSideEventsEnabled(false);

        filteredEvents = Model.getFilteredEvents(personID);
        assertEquals(0, filteredEvents.size());
    }

    @Test
    public void searchPersons() throws Exception {

        load();

        String searchStr = "mo";

        Person[] foundPersons = Model.searchPersons(searchStr);
        assertEquals(4, foundPersons.length);

        searchStr = "asdfasdfasdf";

        foundPersons = Model.searchPersons(searchStr);
        assertEquals(0, foundPersons.length);
    }

    @Test
    public void searchEvents() throws Exception {

        load();

        String searchStr = "cou";

        Event[] foundEvents = Model.searchEvents(searchStr);
        assertEquals(14, foundEvents.length);

        searchStr = "birth";

        foundEvents = Model.searchEvents(searchStr);
        assertEquals(7, foundEvents.length);

        searchStr = "asdfasdfasdf";

        foundEvents = Model.searchEvents(searchStr);
        assertEquals(0, foundEvents.length);
    }

    @Test
    public void familyRelationships() throws Exception {

        load();

        Family family = Model.getFamily(mother);

        assertEquals(family.getSpouse().getPersonID(), father);
        assertEquals(family.getFather().getPersonID(), mothersFather);
        assertEquals(family.getMother().getPersonID(), mothersMother);
        assertEquals(family.getChild().getPersonID(), person);

        family = Model.getFamily(fathersFather);

        assertEquals(family.getSpouse().getPersonID(), fathersMother);
        assertEquals(family.getFather(), null);
        assertEquals(family.getMother(), null);
        assertEquals(family.getChild().getPersonID(), father);
    }

    private void load() {

        String authToken = "authToken";
        String username = person;
        String personID = person;

        Model.login(authToken, username, personID);

        Model.load(persons, events);
    }
}