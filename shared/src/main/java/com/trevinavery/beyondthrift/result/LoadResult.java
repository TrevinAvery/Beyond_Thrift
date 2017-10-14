package com.trevinavery.beyondthrift.result;

/**
 * The LoadResult class is a Java representation of a JSON response. This is a convenience class
 * to help process the data to be sent by the server.
 */
public class LoadResult implements IResult {

    private String message;

    /**
     * Constructs a LoadResult object with no data.
     */
    public LoadResult() {
        // default constructor
    }

    /**
     * Constructs a LoadResult object with pre-entered data.
     *
     * @param message data from service
     */
    public LoadResult(String message) {
        setMessage(message);
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

        LoadResult that = (LoadResult) o;

        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }
}
