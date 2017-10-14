package com.trevinavery.beyondthrift.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

import com.trevinavery.beyondthrift.model.Person;

/**
 * The PersonDao class creates a Data Access Object that is used to
 * manipulate all person related data in the provided database.
 */
public class PersonDao extends AbstractDao {

    /**
     * Creates a Data Access Object associated with the
     * database that created the given connection.
     *
     * @param connection the Database Connection object to
     *                   use when accessing the database
     */
    public PersonDao(Connection connection) {
        super(connection);
    }

    /**
     * Creates a new person and adds it to the database.
     *
     * This method leaves the person's Father, Mother, and
     * Spouse values null. This is the same as calling
     * <code>createPerson(descendant, firstName, lastName,
     *                      gender, null, null, null)</code>
     *
     * @return the Person created
     * @throws DatabaseException if editing the database fails
     * @throws InvalidValuesException if the values entered are invalid
     */
    public Person createPerson(String descendant, String firstName,
                               String lastName, String gender)
            throws DatabaseException, InvalidValuesException {

        return createPerson(
                descendant,
                firstName,
                lastName,
                gender,
                null,
                null,
                null
        );
    }

    /**
     * Creates a new person and adds it to the database.
     *
     * @param descendant Descendant of the new person
     * @param firstName FirstName of the new person
     * @param lastName LastName of the new person
     * @param gender Gender of the new person
     * @param father Father of the new person
     * @param mother Mother of the new person
     * @param spouse Spouse of the new person
     * @return the Person created
     * @throws DatabaseException if editing the database fails
     * @throws InvalidValuesException if the values entered are invalid
     */
    public Person createPerson(String descendant, String firstName,
                               String lastName, String gender,
                               String father, String mother, String spouse)
            throws DatabaseException, InvalidValuesException {

        String personID = UUID.randomUUID().toString();

        Person person = new Person(
                personID,
                descendant,
                firstName,
                lastName,
                gender,
                father,
                mother,
                spouse
        );

        addPerson(person);

        return person;
    }

    /**
     * Adds a person to the database.
     *
     * @param person the person to add to the database
     * @throws DatabaseException if editing the database fails
     * @throws InvalidValuesException if the values entered are invalid
     */
    public void addPerson(Person person)
            throws DatabaseException, InvalidValuesException {

        // check if all values are valid
        if (person.getPersonID() == null
                || person.getDescendant() == null
                || person.getFirstName() == null
                || person.getLastName() == null
                || person.getGender() == null
                || person.getGender().length() != 1) {
            throw new InvalidValuesException();
        }

        PreparedStatement stmt = null;
        try {
            String sql =
                    "insert into Persons (" +
                            "PersonID, Descendant, FirstName, LastName, " +
                            "Gender, Father, Mother, Spouse" +
                    ") " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getDescendant());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFather());
            stmt.setString(7, person.getMother());
            stmt.setString(8, person.getSpouse());

            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException();
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }

    /**
     *
     * Finds the person in the database with the matching PersonID and
     * updates all other values to match those in the provided Person object.
     *
     * @param person the person to update
     * @throws DatabaseException if editing the database fails
     */
    public void updatePerson(Person person)
            throws DatabaseException {

        PreparedStatement stmt = null;
        try {

            String sql =
                    "update Persons " +
                    "set Descendant = ?, FirstName = ?, LastName = ?, " +
                            "Gender = ?, Father = ?, Mother = ?, Spouse = ?" +
                    "where PersonID = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, person.getDescendant());
            stmt.setString(2, person.getFirstName());
            stmt.setString(3, person.getLastName());
            stmt.setString(4, person.getGender());
            stmt.setString(5, person.getFather());
            stmt.setString(6, person.getMother());
            stmt.setString(7, person.getSpouse());
            stmt.setString(8, person.getPersonID());

            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException();
            }

        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }

    /**
     * Gets a specific person from the database.
     *
     * @param personID the ID of the person desired
     * @return the Person object with the specified ID,
     *          or null if no such person exists
     * @throws DatabaseException if editing the database fails
     */
    public Person getPerson(String personID)
            throws DatabaseException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "select * " +
                    "from Persons " +
                    "where PersonID = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, personID);

            rs = stmt.executeQuery();

            // if person was found, return model object
            if (rs.next()) {
                return new Person(
                        rs.getString(1), // PersonID
                        rs.getString(2), // Descendant
                        rs.getString(3), // FirstName
                        rs.getString(4), // LastName
                        rs.getString(5), // Gender
                        rs.getString(6), // Father
                        rs.getString(7), // Mother
                        rs.getString(8)  // Spouse
                );
            }
            // no person found
            return null;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }

    }

    /**
     * Gets all of the ancestors from the database associated with a specific user.
     *
     * @param userName the UserName of the descendant to find ancestors for
     * @return an array of Person objects with all ancestors of the descendant specified.
     *          If no ancestors are found, the array will be size 0.
     * @throws DatabaseException if editing the database fails
     */
    public Person[] getAncestors(String userName)
            throws DatabaseException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "select * " +
                    "from Persons " +
                    "where Descendant = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, userName);

            rs = stmt.executeQuery();

            ArrayList<Person> ancestors = new ArrayList<>();

            // if ancestors were found, build array
            while (rs.next()) {
                ancestors.add(new Person(
                        rs.getString(1), // PersonID
                        rs.getString(2), // Descendant
                        rs.getString(3), // FirstName
                        rs.getString(4), // LastName
                        rs.getString(5), // Gender
                        rs.getString(6), // Father
                        rs.getString(7), // Mother
                        rs.getString(8)  // Spouse
                ));
            }

            Object[] array = ancestors.toArray();
            return Arrays.copyOf(array, array.length, Person[].class);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }

    /**
     * Deletes all of the ancestors of a specific descendant from the database.
     *
     * @param userName the UserName of the descendant
     * @throws DatabaseException if editing the database fails
     */
    public void deleteAncestors(String userName)
            throws DatabaseException {

        PreparedStatement stmt = null;
        try {
            String sql =
                    "delete from Persons " +
                    "where Descendant = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, userName);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.log(Level.FINEST, e.getMessage(), e);
                }
            }

        }
    }
}
