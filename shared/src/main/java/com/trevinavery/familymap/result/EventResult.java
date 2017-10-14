package com.trevinavery.familymap.result;

import java.util.Arrays;

import com.trevinavery.familymap.model.Event;

/**
 * The EventResult class is a Java representation of a JSON response. This is a convenience class
 * to help process the data to be sent by the server.
 */
public class EventResult implements IResult {

    private Event[] data;
    private String message;

    /**
     * Constructs a EventResult object with no data.
     */
    public EventResult() {
        // default constructor
    }

    /**
     * Constructs a EventResult object with pre-entered data.
     *
     * @param event data from service
     */
    public EventResult(Event event) {
        setData(new Event[] {event});
    }

    /**
     * Constructs a EventResult object with pre-entered data.
     *
     * @param data data from service
     */
    public EventResult(Event[] data) {
        setData(data);
    }

    /**
     * Constructs a EventResult object with pre-entered data.
     *
     * @param message data from service
     */
    public EventResult(String message) {
        setMessage(message);
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventResult that = (EventResult) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(data, that.data)) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(data);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
