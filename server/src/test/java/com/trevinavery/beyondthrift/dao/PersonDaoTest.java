package com.trevinavery.beyondthrift.dao;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import fms.model.Person;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by trevinpa on 5/30/17.
 */
public class PersonDaoTest {

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

    @Test
    public void createPerson() throws Exception {
        Person person = new Person(
                null,
                "username",
                "firstname",
                "lastname",
                "m",
                null,
                null,
                null
        );

        PersonDao personDao = database.getPersonDao();

        Person dbPerson = personDao.createPerson(
                "username",
                "firstname",
                "lastname",
                "m"
        );

        person.setPersonID(dbPerson.getPersonID());

        assertNotNull(dbPerson);
        assertEquals(person, dbPerson);
    }

    @Test
    public void createPerson1() throws Exception {
        Person person = new Person(
                null,
                "username",
                "firstname",
                "lastname",
                "m",
                "poppy",
                "mummy",
                "wifey"
        );

        PersonDao personDao = database.getPersonDao();

        Person dbPerson = personDao.createPerson(
                "username",
                "firstname",
                "lastname",
                "m",
                "poppy",
                "mummy",
                "wifey"
        );

        person.setPersonID(dbPerson.getPersonID());

        assertNotNull(dbPerson);
        assertEquals(person, dbPerson);
    }

    @Test
    public void addPerson() throws Exception {
        Person person = new Person(
                "mypersonID",
                "username",
                "firstname",
                "lastname",
                "m",
                "poppy",
                "mummy",
                "wifey"
        );

        PersonDao personDao = database.getPersonDao();

        personDao.addPerson(person);

        Person dbPerson = personDao.getPerson("mypersonID");

        assertNotNull(dbPerson);
        assertEquals(person, dbPerson);
    }

    @Test
    public void updatePerson() throws Exception {
        Person person = new Person(
                "mypersonID",
                "username",
                "firstname",
                "lastname",
                "m",
                "poppy",
                "mummy",
                "wifey"
        );

        PersonDao personDao = database.getPersonDao();

        personDao.addPerson(person);

        Person dbPerson = personDao.getPerson("mypersonID");
        assertEquals(dbPerson.getFirstName(), "firstname");

        person.setFirstName("a new first name");
        personDao.updatePerson(person);

        dbPerson = personDao.getPerson("mypersonID");
        assertEquals(dbPerson.getFirstName(), "a new first name");
    }

    @Test
    public void getPerson() throws Exception {
        Person person = new Person(
                "mypersonID",
                "username",
                "firstname",
                "lastname",
                "m",
                "poppy",
                "mummy",
                "wifey"
        );

        PersonDao personDao = database.getPersonDao();

        personDao.addPerson(person);

        Person dbPerson = personDao.getPerson("mypersonID");

        assertNotNull(dbPerson);
        assertEquals(person, dbPerson);
    }

    @Test
    public void getPersonThatDoesntExist() throws Exception {
        PersonDao personDao = database.getPersonDao();

        Person dbPerson = personDao.getPerson("mypersonID");

        assertNull(dbPerson);
    }

    @Test
    public void getAncestors() throws Exception {

        Person person = new Person(
                "mypersonID",
                "username",
                "firstname",
                "lastname",
                "m",
                "poppy",
                "mummy",
                "wifey"
        );

        Person father = new Person(
                "poppy",
                "username",
                "ffirstname",
                "flastname",
                "m",
                null,
                null,
                "mummy"
        );

        Person mother = new Person(
                "mummy",
                "username",
                "mfirstname",
                "mlastname",
                "f",
                null,
                null,
                "poppy"
        );

        Person notRelated = new Person(
                "asdf",
                "someoneelse",
                "hi",
                "bye",
                "f",
                null,
                null,
                null
        );

        PersonDao personDao = database.getPersonDao();

        personDao.addPerson(person);
        personDao.addPerson(mother);
        personDao.addPerson(father);
        personDao.addPerson(notRelated);

        Person[] dbAncestors = personDao.getAncestors("username");

        assertNotNull(dbAncestors);
        assertEquals(3, dbAncestors.length);

        Person[] expectedAncestors = {person, father, mother};
        ArrayList<Person> expectedList = new ArrayList<>(Arrays.asList(expectedAncestors));
        ArrayList<Person> dbList = new ArrayList<>(Arrays.asList(dbAncestors));
        Assert.assertTrue(expectedList.containsAll(dbList));
        Assert.assertTrue(dbList.containsAll(expectedList));
    }

    @Test
    public void deleteAncestors() throws Exception {

        Person person = new Person(
                "mypersonID",
                "username",
                "firstname",
                "lastname",
                "m",
                "poppy",
                "mummy",
                "wifey"
        );

        Person father = new Person(
                "poppy",
                "username",
                "ffirstname",
                "flastname",
                "m",
                null,
                null,
                "mummy"
        );

        Person mother = new Person(
                "mummy",
                "username",
                "mfirstname",
                "mlastname",
                "f",
                null,
                null,
                "poppy"
        );

        Person notRelated = new Person(
                "asdf",
                "someoneelse",
                "hi",
                "bye",
                "f",
                null,
                null,
                null
        );

        PersonDao personDao = database.getPersonDao();

        personDao.addPerson(person);
        personDao.addPerson(mother);
        personDao.addPerson(father);
        personDao.addPerson(notRelated);

        personDao.deleteAncestors("username");

        Person[] dbAncestors = personDao.getAncestors("username");
        assertEquals(0, dbAncestors.length);

        Person[] otherAncestors = personDao.getAncestors("someoneelse");
        assertNotNull(otherAncestors);
        assertEquals(1, otherAncestors.length);
        Person[] expectedAncestors = {notRelated};
        assertArrayEquals(expectedAncestors, otherAncestors);
    }

    @Test
    public void deleteAncestorsThatDontExist() throws Exception {

        PersonDao personDao = database.getPersonDao();
        personDao.deleteAncestors("username");
    }

}