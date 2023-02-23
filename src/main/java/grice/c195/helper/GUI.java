package grice.c195.helper;

import grice.c195.DAO.CustomerDAO;
import grice.c195.model.Appointment;
import grice.c195.model.Customer;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.sql.SQLException;

public  class GUI {
    /**
     * Updates the customerNameField, customerPhoneField, customerAddressField,customerPostalField,
     * customerCountryCombo, and customerStateCombo with the selected customer's information.
     * @param customerClientTable The table containing the customer information.
     * @param customerNameField The text field containing the customer's name.
     * @param customerPhoneField The text field containing the customer's phone number.
     * @param customerAddressField The text field containing the customer's address.
     * @param customerPostalField The text field containing the customer's postal code.
     * @param customerCountryCombo The combo box containing the customer's country.
     * @param customerStateCombo The combo box containing the customer's state.
     */
    public static void loadCustomerFields(TableView<Customer> customerClientTable, TextField customerNameField,
                                          TextField customerPhoneField, TextField customerAddressField,
                                          TextField customerPostalField, ComboBox<String> customerCountryCombo,
                                          ComboBox<String> customerStateCombo) {
        Customer selectedCustomer = customerClientTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            customerNameField.setText(selectedCustomer.getName());
            customerPhoneField.setText(selectedCustomer.getPhone());
            customerAddressField.setText(selectedCustomer.getAddress());
            customerPostalField.setText(selectedCustomer.getPostalCode());
            customerCountryCombo.setValue(selectedCustomer.getCountry());
            customerStateCombo.setValue(selectedCustomer.getDivision());
        }
    }

    public static void loadAppointmentFields(TableView<Appointment> appAllTable,
                                             ComboBox<String> appCustomerIdCombo, TextField appIdField,
                                             ComboBox<String> appContactCombo, TextField appTitleField,
                                             TextField appDescriptionField, TextField appTypeField,
                                             TextField appLocationField, DatePicker appStartDatePicker,
                                             DatePicker appEndDatePicker, TextField appStartTimeField,
                                             TextField appEndTimeField) {

        Appointment selectedAppointment = appAllTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            appCustomerIdCombo.setValue(Integer.toString(selectedAppointment.getCustomerId()));
            appIdField.setText(Integer.toString(selectedAppointment.getAppointmentId()));
            appContactCombo.setValue(selectedAppointment.getContact());
            appTitleField.setText(selectedAppointment.getTitle());
            appDescriptionField.setText(selectedAppointment.getDescription());
            appTypeField.setText(selectedAppointment.getType());
            appLocationField.setText(selectedAppointment.getLocation());
            appStartDatePicker.setValue(selectedAppointment.getLocalStartTime().toLocalDate());
            appEndDatePicker.setValue(selectedAppointment.getLocalEndTime().toLocalDate());
            appStartTimeField.setText(selectedAppointment.getLocalStartTime().toLocalTime().toString());
            appEndTimeField.setText(selectedAppointment.getLocalEndTime().toLocalTime().toString());
        }
    }


    /** Uses the values from customerNameField, customerPhoneField, customerAddressField, customerPostalField,
     * customerCountryCombo, and customerStateCombo to update the customerClientTable and saves the fields to the
     * appropriate database table database when the save button is clicked.
     *
     * @param customerClientTable The table containing the customer information.
     * @param customerNameField The text field containing the customer's name.
     * @param customerPhoneField The text field containing the customer's phone number.
     * @param customerAddressField The text field containing the customer's address.
     * @param customerPostalField The text field containing the customer's postal code.
     * @param customerCountryCombo The combo box containing the customer's country.
     * @param customerStateCombo The combo box containing the customer's state.
     * @throws SQLException If an error occurs while accessing the database.
     *
     */
    public static void updateCustomerFields(TableView<Customer> customerClientTable, TextField customerNameField,
                                            TextField customerPhoneField, TextField customerAddressField,
                                            TextField customerPostalField, ComboBox<String> customerCountryCombo,
                                            ComboBox<String> customerStateCombo) throws SQLException {

        String name = customerNameField.getText();
        String phone = customerPhoneField.getText();
        String address = customerAddressField.getText();
        String postalCode = customerPostalField.getText();
        String country = customerCountryCombo.getSelectionModel().getSelectedItem();
        String division = customerStateCombo.getSelectionModel().getSelectedItem();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || postalCode.isEmpty() || country == null || division == null) {
            // Show an error message if any required fields are empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please fill out all required fields.");
            alert.showAndWait();
            return;
        }

        // Create a new Customer object with the updated values
        Customer selectedCustomer = customerClientTable.getSelectionModel().getSelectedItem();
        Customer updatedCustomer = new Customer(selectedCustomer.getId(), name, address, postalCode, phone, division, country);

        // Update the table view with the updated customer information
        ObservableList<Customer> customerList = customerClientTable.getItems();
        customerList.set(customerList.indexOf(selectedCustomer), updatedCustomer);
        customerClientTable.refresh();

        // Save the updated customer information to the database
        if (CustomerDAO.updateCustomer(updatedCustomer)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Customer updated successfully.");
            alert.showAndWait();
            customerNameField.clear();
            customerPhoneField.clear();
            customerAddressField.clear();
            customerPostalField.clear();
            customerCountryCombo.getSelectionModel().clearSelection();
            customerStateCombo.getSelectionModel().clearSelection();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to update customer.");
            alert.showAndWait();
        }

    }
}
