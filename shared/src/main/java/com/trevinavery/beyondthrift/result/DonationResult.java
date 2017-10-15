package com.trevinavery.beyondthrift.result;

/**
 * The FillResult class is a Java representation of a JSON response. This is a convenience class
 * to help process the data to be sent by the server.
 */
public class DonationResult implements IResult {

    private String message;

    /**
     * Constructs a FillResult object with no data.
     */
    public DonationResult() {
        // default constructor
    }

    /**
     * Constructs a FillResult object with pre-entered data.
     *
     * @param message data from service
     */
    public DonationResult(String message) {
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

        DonationResult that = (DonationResult) o;

        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }
}
