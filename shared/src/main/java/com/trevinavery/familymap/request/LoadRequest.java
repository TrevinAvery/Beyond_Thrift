package com.trevinavery.familymap.request;

import java.util.Arrays;

import com.trevinavery.familymap.model.Event;
import com.trevinavery.familymap.model.Person;
import com.trevinavery.familymap.model.User;

/**
 * The LoadRequest class is a Java representation of a JSON request.
 * This is a convenience class to help process the data received by the server.
 */
public class LoadRequest implements IRequest {

    private User[] users;
    private Person[] persons;
    private Event[] events;

    /**
     * Constructs a LoadRequest object with no data.
     */
    public LoadRequest() {
        // default constructor
    }

    /**
     * Constructs a LoadRequest object with pre-entered data.
     *
     * @param users data from handler
     * @param persons data from handler
     * @param events data from handler
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        setUsers(users);
        setPersons(persons);
        setEvents(events);
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoadRequest that = (LoadRequest) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(users, that.users)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(persons, that.persons)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(events, that.events);

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(users);
        result = 31 * result + Arrays.hashCode(persons);
        result = 31 * result + Arrays.hashCode(events);
        return result;
    }
}
