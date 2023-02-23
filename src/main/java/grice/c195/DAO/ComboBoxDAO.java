package grice.c195.DAO;

import grice.c195.helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;

/**
 * Data Access Object for the ComboBoxes.
 */
public class ComboBoxDAO {
    /**
     * Returns a list of all country names in the countries database table.
     */
    public static ObservableList<String> countryList() {
        ObservableList<String> countryList = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            String countryQuery = "SELECT Country FROM countries";
            PreparedStatement countryPs = JDBC.connection.prepareStatement(countryQuery);
            ResultSet countryResultSet = countryPs.executeQuery();

            while (countryResultSet.next()) {
                String country = countryResultSet.getString("Country");
                countryList.add(country);
            }
            JDBC.closeConnection();
            return countryList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryList;
    }
    /**
     * Returns a list of all state names in the first_level_divisions database table.
     */
    public static ObservableList<String> stateList(String country) {
        ObservableList<String> stateList = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            String stateQuery = "SELECT Division FROM first_level_divisions WHERE Country_ID = (SELECT Country_ID FROM countries WHERE Country = ?)";
            PreparedStatement statePs = JDBC.connection.prepareStatement(stateQuery);
            statePs.setString(1, country);
            ResultSet stateResultSet = statePs.executeQuery();

            while (stateResultSet.next()) {
                String state = stateResultSet.getString("Division");
                stateList.add(state);
            }
            JDBC.closeConnection();
            return stateList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stateList;
    }
    /**
     * Returns a list of all city names in the first_level_divisions database table.
     */
    public static ObservableList<String> contactList() {
        ObservableList<String> contactList = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            String contactQuery = "SELECT Contact_Name FROM contacts";
            PreparedStatement contactPs = JDBC.connection.prepareStatement(contactQuery);
            ResultSet contactResultSet = contactPs.executeQuery();

            while (contactResultSet.next()) {
                String contact = contactResultSet.getString("Contact_Name");
                contactList.add(contact);
            }
            JDBC.closeConnection();
            return contactList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }
    /**
     * Returns a list of all city names in the first_level_divisions database table.
     */
    public static ObservableList<String> customerList() {
        ObservableList<String> customerList = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            String customerQuery = "SELECT Customer_ID FROM customers";
            PreparedStatement customerPs = JDBC.connection.prepareStatement(customerQuery);
            ResultSet customerResultSet = customerPs.executeQuery();

            while (customerResultSet.next()) {
                String customer = customerResultSet.getString("Customer_ID");
                customerList.add(customer);
            }
            JDBC.closeConnection();
            customerList.sort(Comparator.naturalOrder());
            return customerList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }
}
