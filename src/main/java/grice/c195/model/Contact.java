package grice.c195.model;

import grice.c195.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a contact for the application.
 */
public class Contact {
    private final int id;

    /**
     * Creates a new contact object with the provided information.
     *
     * @param id The contact ID.
     */
    public Contact(int id) {
        this.id = id;
    }

    /**
     * Gets the name of a contact given their ID.
     *
     * @param contactId The ID of the contact to get the name of.
     * @return The name of the contact.
     * @throws SQLException If an error occurs while accessing the database.
     */
    public static String getContactNameById(int contactId) throws SQLException {
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("SELECT Contact_Name FROM contacts WHERE Contact_ID = ?");
            statement.setInt(1, contactId);
            ResultSet contactResultSet = statement.executeQuery();
            if (contactResultSet.next()) {
                return contactResultSet.getString("Contact_Name");
            }
            return "";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "";
        } finally {
            JDBC.closeConnection();
        }
    }

    /**
     * Gets the ID of a contact given their name.
     *
     * @param name The name of the contact to get the ID of.
     * @return The ID of the contact.
     * @throws SQLException If an error occurs while accessing the database.
     */
    public static int getContactIdByName(String name) throws SQLException {
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("SELECT Contact_ID FROM contacts WHERE Contact_Name = ?");
            statement.setString(1, name);
            ResultSet contactResultSet = statement.executeQuery();
            if (contactResultSet.next()) {
                return contactResultSet.getInt("Contact_ID");
            }
            return -1;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        } finally {
            JDBC.closeConnection();
        }
    }

    /**
     * Gets the ID of the contact.
     *
     * @return The ID of the contact.
     */
    public int getId() {
        return id;
    }
}
