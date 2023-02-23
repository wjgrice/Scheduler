package grice.c195.DAO;

import grice.c195.helper.JDBC;
import grice.c195.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class contains methods for retrieving and updating customer data in the database.
 */
public class CustomerDAO {
    /**
     * Retrieves all customers from the database and returns them as an observable list.
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try {
            JDBC.openConnection();
            String query =  "SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, d.Division, co.Country " +
                    "FROM customers c " +
                    "JOIN first_level_divisions d ON c.Division_ID = d.Division_ID " +
                    "JOIN countries co ON d.Country_ID = co.Country_ID;";
            PreparedStatement ps = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Customer customer = new Customer(
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Address"),
                        resultSet.getString("Postal_Code"),
                        resultSet.getString("Phone"),
                        resultSet.getString("Division"),
                        resultSet.getString("Country"));
                customerList.add(customer);
            }

            JDBC.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerList;
    }

    /**
     * Updates a customer in the database.
     * @param customer The customer to be updated.
     * @return true if the customer was successfully updated, false otherwise.
     */
    public static boolean updateCustomer(Customer customer) throws SQLException {
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("UPDATE customers SET Customer_Name = ?, " +
                    "Address = ?, Postal_Code = ?, Phone = ?, Division_Id = ? WHERE Customer_Id = ?");
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setString(3, customer.getPostalCode());
            statement.setString(4, customer.getPhone());
            statement.setInt(5, getDivisionID(customer.getDivision()));
            statement.setInt(6, customer.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            JDBC.closeConnection();
        }

        return false;

    }

    /**
     * Retrieve a customer Division_Id from the first_level_divisions database table given the division.
     */
    public static int getDivisionID(String division) throws SQLException {
        int division_ID = 0;
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("SELECT Division_ID " +
                    "FROM first_level_divisions " +
                    "WHERE Division = ?");
            statement.setString(1, division);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                division_ID = resultSet.getInt("Division_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            JDBC.closeConnection();
        }

        return division_ID;
    }

    /**
     * Adds a customer to the database.
     * @param name The customer's name.
     * @param address The customer's address.
     * @param postalCode The customer's postal code.
     * @param phone The customer's phone number.
     * @param division The customer's division.
     * @param country The customer's country.
     */
    public static void addCustomer(String name, String address, String postalCode, String phone, String division, String country) {

        if(name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty() || division.isEmpty() || country.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill out all required fields.");
            alert.showAndWait();
            return;
        }

        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                    "VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, address);
            statement.setString(3, postalCode);
            statement.setString(4, phone);
            statement.setInt(5, getDivisionID(division));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.closeConnection();
        }
    }

    /**
     * Deletes a customer from the database.
     * @param selectedCustomer The customer to be deleted.
     */
    public static void deleteCustomer(Customer selectedCustomer) {
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("DELETE FROM customers WHERE Customer_ID = ?");
            statement.setInt(1, selectedCustomer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.closeConnection();
        }
    }
}
