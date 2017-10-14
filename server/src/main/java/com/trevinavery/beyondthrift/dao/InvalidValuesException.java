package com.trevinavery.beyondthrift.dao;

/**
 * This is thrown when adding an entry to the database,
 * but the values are invalid or missing.
 */
public class InvalidValuesException extends Exception {
    public InvalidValuesException() {
        super();
    }

    public InvalidValuesException(String s) {
        super(s);
    }

    public InvalidValuesException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidValuesException(Throwable throwable) {
        super(throwable);
    }

    protected InvalidValuesException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
