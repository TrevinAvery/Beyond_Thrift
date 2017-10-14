package com.trevinavery.beyondthrift.request;

/**
 * The FillRequest class is a Java representation of a JSON request.
 * This is a convenience class to help process the data received by the server.
 */
public class FillRequest implements IRequest {

    private String userName;
    private int numOfGenerations;

    /**
     * Constructs a FillRequest object with no data.
     */
    public FillRequest() {
        // default constructor
    }

    /**
     * Constructs a RegisterRequest object with pre-entered data.
     *
     * @param userName data from handler
     * @param numOfGenerations data from handler
     */
    public FillRequest(String userName, int numOfGenerations) {
        setUserName(userName);
        setNumOfGenerations(numOfGenerations);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getNumOfGenerations() {
        return numOfGenerations;
    }

    public void setNumOfGenerations(int numOfGenerations) {
        this.numOfGenerations = numOfGenerations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FillRequest that = (FillRequest) o;

        if (numOfGenerations != that.numOfGenerations) return false;
        return userName != null ? userName.equals(that.userName) : that.userName == null;

    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + numOfGenerations;
        return result;
    }
}
