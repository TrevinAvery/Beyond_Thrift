package com.trevinavery.familymap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import com.trevinavery.familymap.model.User;

/**
 * The UserDao class creates a Data Access Object that is used to
 * manipulate all user related data in the provided database.
 */
public class UserDao extends AbstractDao {

    /**
     * Creates a Data Access Object associated with the
     * database that created the given connection.
     *
     * @param connection the Database Connection object to
     *                   use when accessing the database
     */
    public UserDao(Connection connection) {
        super(connection);
    }

    /**
     * Creates a new user and adds it to the database.
     *
     * @param userName UserName of the new user
     * @param password Password of the new user
     * @param email Email of the new user
     * @param firstName FirstName of the new user
     * @param lastName LastName of the new user
     * @param gender Gender of the new user
     * @return the User created
     * @throws UserAlreadyExistsException if the user already exists
     * @throws DatabaseException if editing the database fails
     * @throws InvalidValuesException if the values entered are invalid
     */
    public User createUser(String userName, String password, String email,
                           String firstName, String lastName, String gender)
            throws UserAlreadyExistsException, DatabaseException, InvalidValuesException {

        User user = new User(
                userName,
                password,
                email,
                firstName,
                lastName,
                gender,
                null // fill in later
        );

        addUser(user);

        return user;
    }

    /**
     * Adds a user to the database.
     *
     * @param user the user to add to the database
     * @throws UserAlreadyExistsException if the user already exists
     * @throws DatabaseException if editing the database fails
     * @throws InvalidValuesException if the values entered are invalid
     */
    public void addUser(User user)
            throws UserAlreadyExistsException, DatabaseException, InvalidValuesException {

        // check if all values are valid
        if (user.getUserName() == null
                || user.getPassword() == null
                || user.getEmail() == null
                || user.getFirstName() == null
                || user.getLastName() == null
                || user.getGender() == null
                || user.getGender().length() != 1) {
            throw new InvalidValuesException();
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // check if userName already exists
            String sql =
                    "select UserName " +
                    "from Users " +
                    "where UserName = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getUserName());

            rs = stmt.executeQuery();

            // if user was found, throw exception
            if (rs.next()) {
                throw new UserAlreadyExistsException();
            }

            // no user found, userName is available, proceed with creating

            stmt.close(); // close so stmt can be reassigned

            sql =
                    "insert into Users (" +
                            "UserName, Password, Email, FirstName, " +
                            "LastName, Gender, PersonID" +
                    ") " +
                    "values (?, ?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            if (stmt.executeUpdate() != 1) {
                throw new DatabaseException();
            }
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
     * Finds the user in the database with the matching UserName and
     * updates all other values to match those in the provided User object.
     *
     * @param user the user to update
     * @throws DatabaseException if editing the database fails
     */
    public void updateUser(User user)
            throws DatabaseException {

        PreparedStatement stmt = null;
        try {

            String sql =
                    "update Users " +
                    "set Password = ?, Email = ?, FirstName = ?, " +
                    "LastName = ?, Gender = ?, PersonID = ?" +
                    "where UserName = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getPersonID());
            stmt.setString(7, user.getUserName());

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
     * Gets a specific user from the database.
     *
     * @param userName the UserName of the user desired
     * @return the User object with the specified userName,
     *          or null if no such user exists
     * @throws DatabaseException if editing the database fails
     */
    public User getUser(String userName)
            throws DatabaseException {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql =
                    "select * " +
                    "from Users " +
                    "where UserName = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, userName);

            rs = stmt.executeQuery();

            // if user was found, return model object
            if (rs.next()) {
                return new User(
                        rs.getString(1), // UserName
                        rs.getString(2), // Password
                        rs.getString(3), // Email
                        rs.getString(4), // FirstName
                        rs.getString(5), // LastName
                        rs.getString(6), // Gender
                        rs.getString(7)  // PersonID
                );
            }
            // no user found
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
}
