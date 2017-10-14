package com.trevinavery.beyondthrift.dao;

/**
 * This is thrown when an attempt to edit the database fails.
 */
public class DatabaseException extends Exception {
    public DatabaseException() {
        super();
    }

    public DatabaseException(String s) {
        super(s);
    }

    public DatabaseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DatabaseException(Throwable throwable) {
        super(throwable);
    }
}
