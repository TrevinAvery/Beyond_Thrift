package com.trevinavery.beyondthrift.service;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.logging.Level;

import com.trevinavery.beyondthrift.dao.Database;
import com.trevinavery.beyondthrift.dao.DatabaseException;
import com.trevinavery.beyondthrift.dao.EventDao;
import com.trevinavery.beyondthrift.dao.InvalidValuesException;
import com.trevinavery.beyondthrift.dao.PersonDao;
import com.trevinavery.beyondthrift.dao.UserDao;
import com.trevinavery.beyondthrift.model.Event;
import com.trevinavery.beyondthrift.model.Location;
import com.trevinavery.beyondthrift.model.LocationData;
import com.trevinavery.beyondthrift.model.NameData;
import com.trevinavery.beyondthrift.model.Person;
import com.trevinavery.beyondthrift.model.User;
import com.trevinavery.beyondthrift.request.FillRequest;
import com.trevinavery.beyondthrift.result.FillResult;

/**
 * The FillService class uses a Database object to process a FillRequest and return the
 * FillResult.
 */
public class FillService extends AbstractService {

    // arrays to load from JSON
    private static String[] firstNames;
    //    private static String[] middleNames;
    private static String[] lastNames;
    private static Location[] locations;

    static {

        try {
            loadJSONData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.severe("Could not find JSON files to load person data.");
        }
    }

    private static void loadJSONData() throws FileNotFoundException {
        Gson gson = new Gson();

        firstNames  = gson.fromJson(
                getInputStreamReader("fnames.json"), NameData.class).getData();
//        middleNames = gson.fromJson(
//                getInputStreamReader("mnames.json"), NameData.class).getData();
        lastNames   = gson.fromJson(
                getInputStreamReader("snames.json"), NameData.class).getData();
        locations   = gson.fromJson(
                getInputStreamReader("locations.json"), LocationData.class).getData();
    }

    private static InputStreamReader getInputStreamReader(String fileName)
            throws FileNotFoundException {
        return new InputStreamReader(new FileInputStream(new File("json/" + fileName)));
    }

    /**
     * Creates a service using the given database.
     * If the database us null, it instantiates a new one.
     *
     * @param database the Database object to use when performing the service
     */
    public FillService(Database database) {
        super(database);
    }


    private String userName;

    private int personsCreated;
    private int eventsCreated;


    /**
     * Generates fake ancestors going back a specified number of generations for the user.
     *
     //     * @param userName the userName of the user to add ancestors for
     //     * @param numOfGenerations the number of generations to generate
     * @return the result of the request
     */
    public FillResult perform(FillRequest request) {
        logger.info("Starting service: Fill");

        setStatus(STATUS_BAD_REQUEST);

        boolean commit = false;

        try {
            database.open();

            FillResult result = fill(request.getUserName(), request.getNumOfGenerations());

            if (result.getMessage().startsWith("Successfully")) {
                commit = true;
                setStatus(STATUS_SUCCESS);
            }
            return result;

        } catch (InvalidValuesException e) {
            logger.log(Level.SEVERE, "Error in generating data: " + e.getMessage(), e);
            setStatus(STATUS_INTERNAL_ERROR);
            return new FillResult("Internal server error");
        } catch (DatabaseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            setStatus(STATUS_INTERNAL_ERROR);
            return new FillResult("Internal server error");
        } finally {
            try {
                database.close(commit);
            } catch (DatabaseException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }

    /**
     * Generates fake ancestors going back a specified number of generations for the user.
     *
     * @param userName the userName of the user to add ancestors for
     * @param numOfGenerations the number of generations to generate
     * @return the result of the request
     */
    public FillResult fill(String userName, int numOfGenerations)
            throws DatabaseException, InvalidValuesException {

        personsCreated = 0;
        eventsCreated = 0;

        this.userName = userName;

        UserDao userDao = database.getUserDao();
        User user = userDao.getUser(userName);

        if (user == null || numOfGenerations < 0) {
            // user not found
            logger.info("User not found in database.");
            return new FillResult("Invalid username or generations parameter");
        }

        // clear current data
        PersonDao personDao = database.getPersonDao();
        personDao.deleteAncestors(userName);

        database.getEventDao().deleteEvents(userName);

        // create person for the user
        Person person = personDao.createPerson(
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender()
        );

        user.setPersonID(person.getPersonID());
        userDao.updateUser(user);

        ++personsCreated;

        createGenerations(person, numOfGenerations);

        String resultStr = "Successfully added " + personsCreated +
                " persons and " + eventsCreated + " events to the database.";
        logger.info(resultStr);
        return new FillResult(resultStr);
    }

    private void createGenerations(Person person, int numOfGenerations)
            throws DatabaseException, InvalidValuesException {

        Person[] persons = {person};

        for (int i = 0; i < numOfGenerations; ++i) {
            persons = createGeneration(persons);
        }
    }

    private Person[] createGeneration(Person[] oldGeneration)
            throws DatabaseException, InvalidValuesException {
        Person[] newGeneration = new Person[oldGeneration.length * 2];

        int i = 0;
        for (Person person : oldGeneration) {
            Person[] parents = createParents(person);
            newGeneration[i++] = parents[0];
            newGeneration[i++] = parents[1];
        }
        return newGeneration;
    }

    private Person[] createParents(Person person)
            throws DatabaseException, InvalidValuesException {

        // get person's birthDate and location
        // if not found, create one
        int birthDate;
        Location birthLocation;

        EventDao eventDao = database.getEventDao();
        Event personBirth = eventDao.getEvent(person.getPersonID(), Event.TYPE_BIRTH);

        if (personBirth != null) {
            birthDate = personBirth.getYear();
            birthLocation = new Location(
                    personBirth.getCountry(),
                    personBirth.getCity(),
                    personBirth.getLatitude(),
                    personBirth.getLongitude()
            );
        } else {
            // pick a year between 1981 and 2000, making the person 17-36
            birthDate = 2000 - (int)(Math.random() * 20);
            birthLocation = locations[(int)(Math.random() * locations.length)];

            createEvent(eventDao, Event.TYPE_BIRTH, person, birthDate, birthLocation);
        }


        // two people fall in love
        Person father = createPerson("m");
        Person mother = createPerson("f");

        // they get married
        father.setSpouse(mother.getPersonID());
        mother.setSpouse(father.getPersonID());

        // and they have a child
        person.setFather(father.getPersonID());
        person.setMother(mother.getPersonID());

        createEvents(father, mother, birthDate, birthLocation);

        // push changes to database
        PersonDao personDao = database.getPersonDao();

        personDao.updatePerson(person);
        personDao.updatePerson(father);
        personDao.updatePerson(mother);

        return new Person[] {father, mother};
    }

    private Person createPerson(String gender)
            throws DatabaseException, InvalidValuesException {

        ++personsCreated;

        return database.getPersonDao().createPerson(
                userName,
                randName(firstNames),
                randName(lastNames),
                gender
        );
    }

    private String randName(String[] nameData) {
        int randIndex = (int)(Math.random() * nameData.length);
        return nameData[randIndex];
    }

    private void createEvents(Person father, Person mother,
                              int childBirthDate, Location childBirthLocation)
            throws DatabaseException, InvalidValuesException {

        EventDao eventDao = database.getEventDao();

        EventDates dates = new EventDates(childBirthDate);
        EventLocations locations = new EventLocations(childBirthLocation);

        // births
        createEvent(eventDao, Event.TYPE_BIRTH, father,
                dates.fathersBirth, locations.fathersBirth);
        createEvent(eventDao, Event.TYPE_BIRTH, mother,
                dates.mothersBirth, locations.mothersBirth);

        // christenings
        if (dates.fathersChristening != -1) {
            createEvent(eventDao, Event.TYPE_CHRISTENING, father,
                    dates.fathersChristening, locations.fathersChristening);
        }
        if (dates.mothersChristening != -1) {
            createEvent(eventDao, Event.TYPE_CHRISTENING, mother,
                    dates.mothersChristening, locations.mothersChristening);
        }

        // baptisms
        if (dates.fathersBaptism != -1) {
            createEvent(eventDao, Event.TYPE_BAPTISM, father,
                    dates.fathersBaptism, locations.fathersBaptism);
        }
        if (dates.mothersBaptism != -1) {
            createEvent(eventDao, Event.TYPE_BAPTISM, mother,
                    dates.mothersBaptism, locations.mothersBaptism);
        }

        // marriage
        if (dates.marriage != -1) {
            createEvent(eventDao, Event.TYPE_MARRIAGE, father,
                    dates.marriage, locations.marriage);
            createEvent(eventDao, Event.TYPE_MARRIAGE, mother,
                    dates.marriage, locations.marriage);
        }

        // deaths
        if (dates.fathersDeath != -1) {
            createEvent(eventDao, Event.TYPE_DEATH, father,
                    dates.fathersDeath, locations.fathersDeath);
        }
        if (dates.mothersDeath != -1) {
            createEvent(eventDao, Event.TYPE_DEATH, mother,
                    dates.mothersDeath, locations.mothersDeath);
        }
    }

    private void createEvent(EventDao eventDao, String type, Person person,
                             int date, Location location)
            throws DatabaseException, InvalidValuesException {

        ++eventsCreated;

        eventDao.createEvent(
                userName,
                person.getPersonID(),
                Double.parseDouble(location.getLatitude()),
                Double.parseDouble(location.getLongitude()),
                location.getCountry(),
                location.getCity(),
                type,
                date
        );
    }

    private class EventDates {

        private int fathersBirth;
        private int mothersBirth;

        private int fathersBaptism;
        private int mothersBaptism;

        private int fathersChristening;
        private int mothersChristening;

        private int marriage;

        private int fathersDeath;
        private int mothersDeath;

        private EventDates(int childBirth) {

            // control variables
            int currentYear = 2017;

            int minParentAge = 16;
            int maxParentAge = 45;

            int minMarriageAge = 16;

            int maxLifetimeAge = 95;

            double christeningProbability = 0.73;
            double baptismProbability = 0.82;


            // choose ages of parents when child was born
            int fathersAge = (int)(Math.random() * (maxParentAge - minParentAge)) + minParentAge;
            fathersBirth = childBirth - fathersAge;


            int mothersAgeDifference = (int)(Math.random() * 10) -5; // +/- 5 years
            int mothersAge = Math.max(minParentAge,
                    Math.min(maxParentAge, fathersAge + mothersAgeDifference));

            mothersBirth = childBirth - mothersAge;


            // choose marriage date

            int minMarriage = Math.max(fathersBirth, mothersBirth) + minMarriageAge;

            marriage = (int)(Math.random() * (childBirth - minMarriage)) + minMarriage;


            // choose death dates

            int maxFathersDeath = fathersBirth + maxLifetimeAge;
            fathersDeath = (int)(Math.random() * (maxFathersDeath - childBirth))
                    + childBirth;

            int maxMothersDeath = mothersBirth + maxLifetimeAge;
            mothersDeath = (int)(Math.random() * (maxMothersDeath - childBirth))
                    + childBirth;


            // choose christening

            fathersChristening = christening(fathersBirth, christeningProbability);
            mothersChristening = christening(mothersBirth, christeningProbability);


            // choose baptism

            fathersBaptism = baptism(fathersBirth, fathersDeath, baptismProbability);
            mothersBaptism = baptism(mothersBirth, mothersDeath, baptismProbability);


            // remove dates that haven't occurred yet

            if (marriage > currentYear) {
                marriage = -1;
            }
            if (fathersDeath > currentYear) {
                fathersDeath = -1;
            }
            if (mothersDeath > currentYear) {
                mothersDeath = -1;
            }
            if (fathersBaptism > currentYear) {
                fathersBaptism = -1;
            }
            if (mothersBaptism > currentYear) {
                mothersBaptism = -1;
            }

        }

        private int christening(int birth, double probability) {
            // Here we assume that these are LDS christenings, which are baby blessings and occur
            // withing a few months of being born. They may or may not have occurred at all.
            if (Math.random() < probability) {
                // if the child is born in the first 10 months of the year,
                // then most likely the christening happened in the same year,
                // otherwise it could be the following year. That gives is a
                // 5/6 (0.833) probability of it happening the same year as birth.
                return birth + (Math.random() < 0.833 ? 0 : 1);
            } else {
                return -1;
            }
        }

        private int baptism(int birth, int death, double probability) {
            // this could happen at any point in someone's life after they are 8
            if (Math.random() < probability) {
                int minBaptism = birth + 8;
                return (int)(Math.random() * (death - minBaptism)) + minBaptism;
            } else {
                return -1;
            }
        }
    }

    private class EventLocations {

        private Location fathersBirth;
        private Location mothersBirth;

        private Location fathersBaptism;
        private Location mothersBaptism;

        private Location fathersChristening;
        private Location mothersChristening;

        private Location marriage;

        private Location fathersDeath;
        private Location mothersDeath;

        private EventLocations(Location childBirth) {

            // control variables

            // probability of father and mother being born in the same location
            double birthProb = 0.6;

            // probability of being christened in the same location as birth
            double christenProb = 0.96;

            // probability of being baptized in the same location as birth
            double baptismProb = 0.82;

            // probability of getting married in same location as either birth
            double marriageProb = 0.86;

            // probability of getting married in same as father's birth location
            // (vs mother's birth location)
            double marriageFatherProb = 0.34;

            // probability of dying in same place as marriage
            double deathProb = 0.87;


            // assume child was born at the marriage location

            marriage = childBirth;

            if (Math.random() < marriageProb) {
                // married in same location as birth
                if (Math.random() < birthProb) {
                    // both parents born in same location as marriage
                    fathersBirth = marriage;
                    mothersBirth = marriage;
                } else {
                    // only one parent born in same location as marraige
                    if (Math.random() < marriageFatherProb) {
                        // married in same location as father's birth
                        fathersBirth = marriage;
                        mothersBirth = randLocation();
                    } else {
                        // married in same location as mother's birth
                        fathersBirth = randLocation();
                        mothersBirth = marriage;
                    }
                }
            } else {
                // married in a different location then either birth
                fathersBirth = randLocation();
                mothersBirth = randLocation();
            }

            // christening
            fathersChristening = (Math.random() < christenProb) ? fathersBirth : randLocation();
            mothersChristening = (Math.random() < christenProb) ? mothersBirth : randLocation();

            // baptism
            fathersBaptism = (Math.random() < baptismProb) ? fathersBirth : randLocation();
            mothersBaptism = (Math.random() < baptismProb) ? mothersBirth : randLocation();

            // death
            fathersDeath = (Math.random() < deathProb) ? marriage : randLocation();
            mothersDeath = (Math.random() < deathProb) ? marriage : randLocation();
        }

        private Location randLocation() {
            int randIndex = (int)(Math.random() * locations.length);
            return locations[randIndex];
        }
    }
}
