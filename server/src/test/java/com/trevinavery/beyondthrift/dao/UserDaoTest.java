package com.trevinavery.beyondthrift.dao;

import fms.model.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by trevinpa on 5/30/17.
 */
public class UserDaoTest {

    private Database database = new Database();

    @org.junit.Before
    public void setUp() throws Exception {
        database.open();
        database.clear();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        database.close(false);
    }

    @org.junit.Test
    public void createUser() throws Exception {
        User user = new User(
                "thisisausername",
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m",
                null
        );

        UserDao userDao = database.getUserDao();

        User dbUser = userDao.createUser(
                "thisisausername",
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m"
        );

        user.setPersonID(dbUser.getPersonID());

        assertNotNull(dbUser);
        assertEquals(user, dbUser);
    }

    @org.junit.Test
    public void addUser() throws Exception {
        User user = new User(
                "thisisausername",
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m",
                "auniqueid"
        );

        UserDao userDao = database.getUserDao();

        userDao.addUser(user);

        User dbUser = userDao.getUser("thisisausername");

        assertNotNull(dbUser);
        assertEquals(user, dbUser);
    }

    @org.junit.Test (expected = UserAlreadyExistsException.class)
    public void addUserThatAlreadyExists() throws Exception {
        User user = new User(
                "thisisausername",
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m",
                "auniqueid"
        );

        UserDao userDao = database.getUserDao();

        userDao.addUser(user);
        userDao.addUser(user);
    }

    @org.junit.Test
    public void updateUser() throws Exception {

        User user = new User(
                "thisisausername",
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m",
                "auniqueid"
        );

        UserDao userDao = database.getUserDao();

        userDao.addUser(user);

        User dbUser = userDao.getUser("thisisausername");
        assertEquals(dbUser.getPassword(), "password");

        user.setPassword("thisisanewpassword");
        userDao.updateUser(user);

        dbUser = userDao.getUser("thisisausername");
        assertEquals(dbUser.getPassword(), "thisisanewpassword");
    }

    @org.junit.Test
    public void getUser() throws Exception {
        User user = new User(
                "thisisausername",
                "password",
                "email@site.com",
                "firstname",
                "lastname",
                "m",
                "auniqueid"
        );

        UserDao userDao = database.getUserDao();

        userDao.addUser(user);

        User dbUser = userDao.getUser("thisisausername");

        assertNotNull(dbUser);
        assertEquals(user, dbUser);
    }

    @org.junit.Test
    public void getUserThatDoesntExist() throws Exception {
        UserDao userDao = database.getUserDao();

        User dbUser = userDao.getUser("thisisausername");

        assertNull(dbUser);
    }

}