package com.trevinavery.familymap.request;

/**
 * The EventRequest class is a Java representation of a JSON request.
 * This is a convenience class to help process the data received by the server.
 */
public class EventRequest implements IRequest {

    private String authToken;
    private String eventID;

    /**
     * Constructs a EventRequest object with no data.
     */
    public EventRequest() {
        // default constructor
    }

    /**
     * Constructs a EventRequest object with pre-entered data.
     *
     * @param authToken data from handler
     */
    public EventRequest(String authToken) {
        this(authToken, null);
    }

    /**
     * Constructs a EventRequest object with pre-entered data.
     *
     * @param authToken data from handler
     * @param eventID data from handler
     */
    public EventRequest(String authToken, String eventID) {
        setAuthToken(authToken);
        setEventID(eventID);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventRequest that = (EventRequest) o;

        if (authToken != null ? !authToken.equals(that.authToken) : that.authToken != null)
            return false;
        return eventID != null ? eventID.equals(that.eventID) : that.eventID == null;

    }

    @Override
    public int hashCode() {
        int result = authToken != null ? authToken.hashCode() : 0;
        result = 31 * result + (eventID != null ? eventID.hashCode() : 0);
        return result;
    }
}
