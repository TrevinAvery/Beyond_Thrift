package com.trevinavery.beyondthrift.model;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.trevinavery.beyondthrift.R;
import com.trevinavery.beyondthrift.proxy.Proxy;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This is a singleton class to manage shared data across the FamilyMap application.
 * This contains all user data, settings, and filters.
 */
public class Model {

    private Model() {
        // default constructor
        // make private to prevent instantiation
    }

    // general shared variables

    private static Proxy proxy;

    private static String authToken;
    private static String userName;
    private static String personID;


    public static Proxy getProxy() {
        return proxy;
    }

    public static void setProxy(Proxy proxy) {
        Model.proxy = proxy;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        Model.authToken = authToken;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Model.userName = userName;
    }

    public static String getPersonID() {
        return personID;
    }

    public static void setPersonID(String personID) {
        Model.personID = personID;
    }


    // raw data

    private static Person[] persons;
    private static Event[] events;


    // id maps

    private static Map<String, Person> personIdMap;
    private static Map<String, Event> eventIdMap;

    private static Map<String, Set<Event>> eventsByPersonIdMap;

    private static Map<String, Family> familiesByPersonId;


    // filters

    private static Set<String> eventTypes;
    private static Map<String, Boolean> eventTypesEnabled;
    private static Map<String, Set<Event>> eventsByType;
    private static Map<String, Float> eventTypeColors;
    private static int eventTypeColorIndex;

    private static float[] availableEventTypeColors = new float[] {
            BitmapDescriptorFactory.HUE_RED,
            BitmapDescriptorFactory.HUE_BLUE,
            BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_YELLOW,
            BitmapDescriptorFactory.HUE_ORANGE,
            BitmapDescriptorFactory.HUE_MAGENTA,
            BitmapDescriptorFactory.HUE_CYAN,
            BitmapDescriptorFactory.HUE_ROSE,
            BitmapDescriptorFactory.HUE_VIOLET,
            BitmapDescriptorFactory.HUE_AZURE
    };


    private static boolean fathersSideEventsEnabled;
    private static Set<Person> fathersSidePersons;
    private static Set<Event> fathersSideEvents;

    private static boolean mothersSideEventsEnabled;
    private static Set<Person> mothersSidePersons;
    private static Set<Event> mothersSideEvents;


    private static Set<Event> lifeStoryEvents;


    private static boolean maleEventsEnabled;
    private static Set<Person> malePersons;
    private static Set<Event> maleEvents;

    private static boolean femaleEventsEnabled;
    private static Set<Person> femalePersons;
    private static Set<Event> femaleEvents;


    public static Set<String> getEventTypes() {
        return eventTypes;
    }

    public static boolean isEventTypeEnabled(String eventType) {
        return eventTypesEnabled.get(eventType);
    }

    public static void setEventTypeEnabled(String eventType, boolean isEnabled) {
        eventTypesEnabled.put(eventType, isEnabled);
    }

    public static float getEventTypeColor(String eventType) {
        return eventTypeColors.get(eventType);
    }

    public static boolean isFathersSideEventsEnabled() {
        return fathersSideEventsEnabled;
    }

    public static void setFathersSideEventsEnabled(boolean fathersSideEventsEnabled) {
        Model.fathersSideEventsEnabled = fathersSideEventsEnabled;
    }

    public static boolean isMothersSideEventsEnabled() {
        return mothersSideEventsEnabled;
    }

    public static void setMothersSideEventsEnabled(boolean mothersSideEventsEnabled) {
        Model.mothersSideEventsEnabled = mothersSideEventsEnabled;
    }

    public static boolean isMaleEventsEnabled() {
        return maleEventsEnabled;
    }

    public static void setMaleEventsEnabled(boolean maleEventsEnabled) {
        Model.maleEventsEnabled = maleEventsEnabled;
    }

    public static boolean isFemaleEventsEnabled() {
        return femaleEventsEnabled;
    }

    public static void setFemaleEventsEnabled(boolean femaleEventsEnabled) {
        Model.femaleEventsEnabled = femaleEventsEnabled;
    }


    // settings

    public static final int LINE_COLOR_LIFE_STORY  = 0;
    public static final int LINE_COLOR_FAMILY_TREE = 1;
    public static final int LINE_COLOR_SPOUSE      = 2;

    private static int mapType = 0;

    private static int[] lineColors = new int[] {0, 1, 2};
    private static boolean[] lineSwitches = new boolean[] {true, true, true};

    private static int[] colorResources = new int[] {
            R.color.red,
            R.color.green,
            R.color.blue
    };

    public static int getMapType() {
        return mapType;
    }

    public static void setMapType(int mapType) {
        Model.mapType = mapType;
    }

    public static int getLineColor(int position) {
        return lineColors[position];
    }

    public static void setLineColor(int position, int settingsLineColor) {
        Model.lineColors[position] = settingsLineColor;
    }

    public static int getLineColorResource(int line) {
        return colorResources[lineColors[line]];
    }

    public static boolean getLineSwitch(int position) {
        return lineSwitches[position];
    }

    public static void setLineSwitch(int position, boolean settingsLineSwitch) {
        Model.lineSwitches[position] = settingsLineSwitch;
    }


    // control functions

    /**
     * Adds a user's login data.
     */
    public static void login(String authToken, String userName, String personID) {
        Model.setAuthToken(authToken);
        Model.setUserName(userName);
        Model.setPersonID(personID);
    }

    /**
     * Clears all user data.
     */
    public static void logout() {
        // general shared variables
        proxy = null;

        authToken = null;
        userName = null;
        personID = null;

        // raw data
        persons = null;
        events = null;

        // id maps
        personIdMap = null;
        eventIdMap = null;

        eventsByPersonIdMap = null;

        familiesByPersonId = null;

        // filters
        eventTypes = null;
        eventTypesEnabled = null;
        eventsByType = null;
        eventTypeColors = null;

        fathersSidePersons = null;
        fathersSideEvents = null;

        mothersSidePersons = null;
        mothersSideEvents = null;

        lifeStoryEvents = null;

        malePersons = null;
        maleEvents = null;

        femalePersons = null;
        femaleEvents = null;
    }

    /**
     * Loads the model with family history information. This takes the given
     * arrays and creates all required sets and filters.
     *
     * @param persons the array of persons associated with the user
     * @param events the array of events associated with the user and his ancestors
     */
    public static void load(Person[] persons, Event[] events) {

        Model.persons = persons;
        Model.events = events;

        // create all the lists

        initLists();

        loadIdMaps();

        Person person = getPerson(personID);
        loadFamilies(getPerson(person.getFather()), person, fathersSidePersons);
        loadFamilies(getPerson(person.getMother()), person, mothersSidePersons);

        // add the user
        familiesByPersonId.put(person.getPersonID(),
                new Family(
                        getPerson(person.getSpouse()),
                        getPerson(person.getFather()),
                        getPerson(person.getMother()),
                        null
                )
        );

        loadEventFilters();
    }

    private static void initLists() {

        personIdMap = new TreeMap<>();
        eventIdMap = new TreeMap<>();

        eventsByPersonIdMap = new TreeMap<>();

        familiesByPersonId = new TreeMap<>();


        eventTypes = new TreeSet<>();
        eventTypesEnabled = new TreeMap<>();
        eventsByType = new TreeMap<>();
        eventTypeColors = new TreeMap<>();
        eventTypeColorIndex = 0;


        fathersSideEventsEnabled = true;
        fathersSidePersons = new TreeSet<>();
        fathersSideEvents = new TreeSet<>();

        mothersSideEventsEnabled = true;
        mothersSidePersons = new TreeSet<>();
        mothersSideEvents = new TreeSet<>();


        lifeStoryEvents = new TreeSet<>();


        maleEventsEnabled = true;
        malePersons = new TreeSet<>();
        maleEvents = new TreeSet<>();

        femaleEventsEnabled = true;
        femalePersons = new TreeSet<>();
        femaleEvents = new TreeSet<>();
    }

    private static void loadIdMaps() {
        for (Person person : persons) {
            personIdMap.put(person.getPersonID(), person);
        }
        for (Event event : events) {
            eventIdMap.put(event.getEventID(), event);

            Set<Event> events = eventsByPersonIdMap.get(event.getPersonID());
            if (events == null) {
                events = new TreeSet<>();
                eventsByPersonIdMap.put(event.getPersonID(), events);
            }
            events.add(event);
        }
    }

    private static void loadFamilies(Person person, Person child, Set<Person> familySide) {

        if (person == null) {
            return;
        }

        Person father = getPerson(person.getFather());
        Person mother = getPerson(person.getMother());

        familiesByPersonId.put(person.getPersonID(),
                new Family(
                        getPerson(person.getSpouse()),
                        father,
                        mother,
                        child
                )
        );

        familySide.add(person);

        loadFamilies(father, person, familySide);
        loadFamilies(mother, person, familySide);
    }

    private static void loadEventFilters() {

        for (Event event : events) {
            // type lists
            String eventType = event.getEventType().toLowerCase();
            eventTypes.add(eventType);
            eventTypesEnabled.put(eventType, true);

            Set<Event> events = eventsByType.get(eventType);
            if (events == null) {
                events = new TreeSet<>();
                eventsByType.put(eventType, events);

                eventTypeColors.put(eventType, availableEventTypeColors[eventTypeColorIndex++]);
            }
            events.add(event);

            // parental side lists
            Person eventPerson = getPerson(event.getPersonID());
            if (fathersSidePersons.contains(eventPerson)) {
                fathersSideEvents.add(event);
            } else if (mothersSidePersons.contains(eventPerson)){
                mothersSideEvents.add(event);
            } else {
                lifeStoryEvents.add(event);
            }

            // gender lists
            if (eventPerson.getGender().equalsIgnoreCase("m")) {
                maleEvents.add(event);
            } else {
                femaleEvents.add(event);
            }
        }

    }

    /**
     * Gets the person associated with the personID.
     *
     * @param personID the id of the person to look for
     * @return the person object associated with personID,
     *          or null if not found
     */
    public static Person getPerson(String personID) {
        if (personID == null) {
            return null;
        }
        return personIdMap.get(personID);
    }

    /**
     * Gets the family associated with the personID.
     *
     * @param personID the id of the person to find the family of
     * @return the family object associated with personID,
     *          or null if not found
     */
    public static Family getFamily(String personID) {
        if (personID == null) {
            return null;
        }
        return familiesByPersonId.get(personID);
    }

    /**
     * Creates a sorted set of all events that are visible with the
     * current filter settings and are associated with the person
     * referred to by the personID.
     *
     * @param personID the person who is associated with the desired events
     * @return a set of events that match the filter settings
     */
    public static Set<Event> getFilteredEvents(String personID) {
        if (personID == null) {
            return null;
        }

        Set<Event> filteredEvents = getFilteredEvents();

        Set<Event> personEvents = new TreeSet<>();

        for (Event event : filteredEvents) {
            if (event.getPersonID().equals(personID)) {
                personEvents.add(event);
            }
        }

        return personEvents;
    }

    /**
     * Creates a sorted set of all events that are visible with the
     * current filter settings.
     *
     * @return a set of events that match the filter settings
     */
    public static Set<Event> getFilteredEvents() {


        // return an array of events that are available with the current filters

        Set<Event> filteredEvents = new TreeSet<>();

        // quit if there are no events
        if (events == null) {
            return filteredEvents;
        }

        // add all of the events that are enabled by type
        for (String eventType : eventTypes) {
            if (eventTypesEnabled.get(eventType)) {
                filteredEvents.addAll(eventsByType.get(eventType));
            }
        }

        // if father's side disabled, remove them
        if (!fathersSideEventsEnabled) {
            filteredEvents.removeAll(fathersSideEvents);
        }

        // if mother's side disabled, remove them
        if (!mothersSideEventsEnabled) {
            filteredEvents.removeAll(mothersSideEvents);
        }

        // if male events disabled, remove them
        if (!maleEventsEnabled) {
            filteredEvents.removeAll(maleEvents);
        }

        // if female events disabled, remove them
        if (!femaleEventsEnabled) {
            filteredEvents.removeAll(femaleEvents);
        }

        return filteredEvents;
    }

    /**
     * Searches all persons and returns an array of persons who's
     * names contain the search string.
     *
     * @param search what to search for
     * @return an array of all matches
     */
    public static Person[] searchPersons(String search) {
        // ignore null and empty strings
        if (search == null || search.length() == 0) {
            return new Person[]{};
        }

        // normalize search to lowercase
        search = search.toLowerCase();

        // find all persons who's first or last name contain
        // the search string
        Set<Person> foundPersons = new TreeSet<>();
        for (Person person : persons) {
            if (person.getName().toLowerCase().contains(search)) {
                foundPersons.add(person);
            }
        }

        // convert the set to an array
        Person[] result = new Person[foundPersons.size()];
        int i = 0;
        for (Person person : foundPersons) {
            result[i++] = person;
        }

        return result;
    }

    /**
     * Searches all events and returns an array of events who's
     * descriptions contain the search string.
     *
     * @param search what to search for
     * @return an array of all matches
     */
    public static Event[] searchEvents(String search) {
        // ignore null and empty strings
        if (search == null || search.length() == 0) {
            return new Event[]{};
        }

        // normalize search to lowercase
        search = search.toLowerCase();

        // find all events who's description (type, city, country, year)
        // contains the search string
        Set<Event> foundEvents = new TreeSet<>();
        for (Event event : getFilteredEvents()) {
            if (event.getDescription().toLowerCase().contains(search)) {
                foundEvents.add(event);
            }
        }

        // convert the set to an array
        Event[] result = new Event[foundEvents.size()];
        int i = 0;
        for (Event event : foundEvents) {
            result[i++] = event;
        }

        return result;
    }
}