package grice.c195.controllers;

import grice.c195.DAO.AppointmentsDAO;
import grice.c195.DAO.ComboBoxDAO;
import grice.c195.DAO.CustomerDAO;
import grice.c195.helper.AppointmentCheck;
import grice.c195.helper.GUI;
import grice.c195.helper.InputValidation;
import grice.c195.helper.ScreenChange;
import grice.c195.model.Appointment;
import grice.c195.model.Customer;
import grice.c195.helper.TableUpdater;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    private static int userId; // Stores the user id of the logged-in user
    @FXML private TextField appStartTimeField; // Reference to start time input field
    @FXML public TextField customerPhoneField; // Reference to phone input field
    @FXML private TableView<Customer> customerClientTable; // Reference to customer table
    @FXML public TextField customerNameField; // Reference to name input field
    @FXML public TextField customerAddressField; // Reference to address input field
    @FXML public TextField customerPostalField; // Reference to postal code input field
    @FXML public ComboBox<String> customerCountryCombo; // Reference to country combo box
    @FXML public ComboBox<String> customerStateCombo; // Reference to state combo box
    @FXML private TableView<Appointment> appAllTable; // Reference to all appointments table
    @FXML private TableColumn<Appointment, Integer> appAllIdColumn; // Reference to all appointments id column
    @FXML private TableColumn<Appointment, Integer> appAllUserColumn; // Reference to all appointments user id column
    @FXML private TableColumn<Appointment, Integer> appAllCustomerIdColumn; // Reference to all appointments customer id column
    @FXML private TableColumn<Appointment, String> appAllTitleColumn; // Reference to all appointments title column
    @FXML private TableColumn<Appointment, String> appAllDescriptionColumn; // Reference to all appointments description column
    @FXML private TableColumn<Appointment, String> appAllLocationColumn; // Reference to all appointments location column
    @FXML private TableColumn<Appointment, String> appAllContactColumn; // Reference to all appointments contact column
    @FXML private TableColumn<Appointment, String> appAllTypeColumn; // Reference to all appointments type column
    @FXML private TableColumn<Appointment, Date> appAllStartColumn; // Reference to all appointments start column
    @FXML private TableColumn<Appointment, Date> appAllEndColumn; // Reference to all appointments end column
    @FXML private TableView<Appointment> appWeeklyTable; // Reference to weekly appointments table
    @FXML private TableColumn<Appointment, Integer> appWeeklyIdColumn; // Reference to weekly appointments id column
    @FXML private TableColumn<Appointment, Integer> appWeeklyUserIdColumn; // Reference to weekly appointments user id column
    @FXML private TableColumn<Appointment, Integer> appWeeklyCustomerIdColumn; // Reference to weekly appointments customer id column
    @FXML private TableColumn<Appointment, String> appWeeklyTitleColumn; // Reference to weekly appointments title column
    @FXML private TableColumn<Appointment, String> appWeeklyDescriptionColumn; // Reference to weekly appointments description column
    @FXML private TableColumn<Appointment, String> appWeeklyLocationColumn; // Reference to weekly appointments location column
    @FXML private TableColumn<Appointment, String> appWeeklyContactColumn; // Reference to weekly appointments contact column
    @FXML private TableColumn<Appointment, String> appWeeklyTypeColumn; // Reference to weekly appointments type column
    @FXML private TableColumn<Appointment, Date> appWeeklyStartColumn; // Reference to weekly appointments start column
    @FXML private TableColumn<Appointment, Date> appWeeklyEndColumn; // Reference to weekly appointments end column
    @FXML private TableView<Appointment> appMonthlyTable; // Reference to monthly appointments table
    @FXML private TableColumn<Appointment, Integer> appMonthlyIdColumn; // Reference to monthly appointments id column
    @FXML private TableColumn<Appointment, Integer> appMonthlyUserIdColumn; // Reference to monthly appointments user id column
    @FXML private TableColumn<Appointment, Integer> appMonthlyCustomerIdColumn; // Reference to monthly appointments customer id column
    @FXML private TableColumn<Appointment, String> appMonthlyTitleColumn; // Reference to monthly appointments title column
    @FXML private TableColumn<Appointment, String> appMonthlyDescriptionColumn; // Reference to monthly appointments description column
    @FXML private TableColumn<Appointment, String> appMonthlyLocationColumn; // Reference to monthly appointments location column
    @FXML private TableColumn<Appointment, String> appMonthlyContactColumn; // Reference to monthly appointments contact column
    @FXML private TableColumn<Appointment, String> appMonthlyTypeColumn; // Reference to monthly appointments type column
    @FXML private TableColumn<Appointment, Date> appMonthlyStartColumn; // Reference to monthly appointments start column
    @FXML private TableColumn<Appointment, Date> appMonthlyEndColumn; // Reference to monthly appointments end column
    @FXML private TableColumn<Customer, Integer> customerIDColumn; // Reference to customer id column
    @FXML private TableColumn<Customer, String> customerNameColumn; // Reference to customer name column
    @FXML private TableColumn<Customer, String> customerAddressColumn; // Reference to customer address column
    @FXML private TableColumn<Customer, String> customerPostalColumn; // Reference to customer postal code column
    @FXML private TableColumn<Customer, String> customerPhoneColumn; // Reference to customer phone column
    @FXML private TableColumn<Customer, String> customerDivColumn; // Reference to customer division column
    @FXML private TableColumn<Customer, String> customerCountryColumn; // Reference to customer country column
    @FXML private ComboBox<String> appContactCombo; // Reference to contact combo box
    @FXML private TextField appIdField; // Reference to appointment id input field
    @FXML private TextField appTitleField; // Reference to appointment title input field
    @FXML private TextField appDescriptionField; // Reference to appointment description input field
    @FXML private TextField appTypeField; // Reference to appointment type input field
    @FXML private TextField appLocationField; // Reference to appointment location input field
    @FXML private DatePicker appStartDatePicker; // Reference to appointment start date input field
    @FXML private DatePicker appEndDatePicker; // Reference to appointment end date input field
    @FXML private TextField appEndTimeField; // Reference to appointment end time input field
    @FXML private ComboBox<String> appCustomerIdCombo; // Reference to customer id combo box
    @FXML private TextArea appNotesArea; // Reference to appointment notes input field

    /**
     * Initializes the controller class and sets up the input validation for the fields that require it.
     *
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCustomerCombo();
        setCountryCombo();
        updateCustomerTableView();
        updateAppsAll();
        updateAppsWeekly();
        updateAppsMonthly();
        listenForTableSelections();
        AppointmentCheck.checkForAppointments(appNotesArea);
    }
    /**
     * Adds listeners to the all tables to load the selected appointment or customer into the appointment or customer
     * fields based on the table that has been clicked upon.
     * <p>
     * LAMBDA: In this method, lambda expressions act as event listeners for table selections. They are used to detect when a
     * row is selected in the appointment or customer tables, and load the relevant data into the appropriate fields.
     * By utilizing lambda expressions, the code is concise and easier to read, making separate listener classes unnecessary.
     */
    private void listenForTableSelections() {
        // Listens for a selection in the appointment table and loads the selected customer into the customer fields
        appAllTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                GUI.loadAppointmentFields(appAllTable, appCustomerIdCombo,appIdField, appContactCombo, appTitleField,
                        appDescriptionField, appTypeField, appLocationField, appStartDatePicker,
                        appEndDatePicker, appStartTimeField, appEndTimeField);
            }
        });
        appWeeklyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                GUI.loadAppointmentFields(appWeeklyTable, appCustomerIdCombo,appIdField, appContactCombo, appTitleField,
                        appDescriptionField, appTypeField, appLocationField, appStartDatePicker,
                        appEndDatePicker, appStartTimeField, appEndTimeField);
            }
        });
        appMonthlyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                GUI.loadAppointmentFields(appMonthlyTable, appCustomerIdCombo,appIdField, appContactCombo, appTitleField,
                        appDescriptionField, appTypeField, appLocationField, appStartDatePicker,
                        appEndDatePicker, appStartTimeField, appEndTimeField);
            }
        });
        // Listens for a selection in the customer table and loads the selected customer into the customer fields
        customerClientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                GUI.loadCustomerFields(customerClientTable, customerNameField, customerPhoneField, customerAddressField,
                        customerPostalField, customerCountryCombo, customerStateCombo);
            }
        });
    }

    /** Passes the user form Customer related fields and Tableview to the GUI class to update the customerClientTable
     * and saves the fields to the appropriate database table database when the save button is clicked.
     * @throws SQLException if there is an error with the database
     */
    public void customerUpdate() throws SQLException {
        // Passes the user form Customer related fields and Tableview to the GUI class to update the customerClientTable
        GUI.updateCustomerFields(customerClientTable, customerNameField, customerPhoneField, customerAddressField,
                                 customerPostalField, customerCountryCombo, customerStateCombo);
    }
    /**
     * Clears the customer related fields
     */
    public void customerClearFields() {
        customerNameField.clear();
        customerPhoneField.clear();
        customerAddressField.clear();
        customerPostalField.clear();
        customerCountryCombo.getSelectionModel().clearSelection();
        customerStateCombo.getSelectionModel().clearSelection();
    }

    /**
     * Deletes the selected customer from the customer table view and the database only if the customer has no appointments.
     */
    public void customerDelete() {
        Customer selectedCustomer = customerClientTable.getSelectionModel().getSelectedItem();
        // Check if a customer is selected
        if (selectedCustomer != null) {
            ObservableList<Appointment> customerAppointments = AppointmentsDAO.getAppointmentsByCustomer(selectedCustomer);
            // Check if the customer has appointments
            if (customerAppointments.isEmpty()) {
                // The customer has no appointments, delete the customer
                CustomerDAO.deleteCustomer(selectedCustomer);
                // Remove the customer from the table view
                customerClientTable.getItems().remove(selectedCustomer);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, selectedCustomer.getName() + " has been deleted");
                alert.showAndWait();
            } else {
                // The customer has appointments, show an alert message
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete customer with appointments");
                alert.showAndWait();
            }
        } else {
            // No customer is selected, show an alert message
            Alert alert = new Alert(Alert.AlertType.ERROR, "No customer selected");
            alert.showAndWait();
        }
        setCustomerCombo();
    }


    /**
     * Logs the user out and returns to the login screen
     * @param actionEvent the event that triggers the method
     */
    public void logOut(ActionEvent actionEvent) throws IOException {
        userId = 0;
        ScreenChange.newView("/grice/c195/loginScreen.fxml", actionEvent);
    }

    /**
     * Sets the userId variable to the id of the user that is currently logged in.
     * @param id the id of the user that is currently logged in.
     */
    public static void setUserId(int id) {
        userId = id;
    }

    /**
     * Updates the customer table view
     */
    private void updateCustomerTableView() {
        ObservableList<Customer> customerList = CustomerDAO.getAllCustomers();
        // Passes the user form Customer related fields and Tableview to the GUI class to update the customerClientTable
        TableUpdater.customerTableUpdate(customerClientTable, customerIDColumn, customerNameColumn, customerAddressColumn,
                customerPostalColumn, customerPhoneColumn, customerCountryColumn, customerDivColumn, customerList);
    }

    /**
     * Pulls available divisions from the database based on the previously selected country.
     */
    public void countryClick() {
        // Get the selected country from the country combo box
        String selectedCountry = customerCountryCombo.getSelectionModel().getSelectedItem();
        customerStateCombo.setItems(ComboBoxDAO.stateList(selectedCountry));
        customerStateCombo.getSelectionModel().selectFirst();
    }
    /**
     * Sets the country combo box to the list of countries in the database
     */
    private void setCountryCombo() {

        customerCountryCombo.setItems(ComboBoxDAO.countryList());
    }
    /**
     * Sets the customer combo box to the list of customers in the database
     */
    private void setCustomerCombo() {
        appCustomerIdCombo.setItems(ComboBoxDAO.customerList());
    }

    /**
     * Saves a new customer to the database using the customer fields and an autogenerated customer_ID from the database.
     */
    public void customerSave() {
        String name = customerNameField.getText();
        String address = customerAddressField.getText();
        String postalCode = customerPostalField.getText();
        String phone = customerPhoneField.getText();
        String division = customerStateCombo.getSelectionModel().getSelectedItem();
        String country = customerCountryCombo.getSelectionModel().getSelectedItem();
        CustomerDAO.addCustomer(name, address, postalCode, phone, division, country);
        setCustomerCombo();
        updateCustomerTableView();
    }
    /**
     * Updates the ALL version of appointment table view
     */
    public void updateAppsAll() {
        ObservableList<Appointment> appList = AppointmentsDAO.getAppointments("all");
        TableUpdater.appsTableUpdate(appAllTable, appAllIdColumn, appAllUserColumn, appAllCustomerIdColumn, appAllTitleColumn,
                        appAllDescriptionColumn, appAllLocationColumn, appAllContactColumn, appAllTypeColumn,
                        appAllStartColumn, appAllEndColumn, appList);
    }

    /**
     * Updates the WEEKLY version appointment table view
     */
    public void updateAppsWeekly() {
        ObservableList<Appointment> appList = AppointmentsDAO.getAppointments("week");
        TableUpdater.appsTableUpdate(appWeeklyTable, appWeeklyIdColumn, appWeeklyUserIdColumn, appWeeklyCustomerIdColumn,
                        appWeeklyTitleColumn, appWeeklyDescriptionColumn, appWeeklyLocationColumn, appWeeklyContactColumn,
                        appWeeklyTypeColumn, appWeeklyStartColumn, appWeeklyEndColumn, appList);
    }
    /**
     * Updates the MONTHLY version appointment table view
     */
    public void updateAppsMonthly() {
        ObservableList<Appointment> appList = AppointmentsDAO.getAppointments("month");
        TableUpdater.appsTableUpdate(appMonthlyTable, appMonthlyIdColumn, appMonthlyUserIdColumn, appMonthlyCustomerIdColumn,
                        appMonthlyTitleColumn, appMonthlyDescriptionColumn, appMonthlyLocationColumn, appMonthlyContactColumn,
                        appMonthlyTypeColumn, appMonthlyStartColumn, appMonthlyEndColumn, appList);
    }

    /**
     * Saves a new appointment to the database using the appointment fields and an autogenerated appointment_ID from the database.
     * Updates the table views with the new appointment.
     */
    public void appSave() {

        if (appValidationCheck(appCustomerIdCombo, appStartDatePicker, appStartTimeField, appEndDatePicker, appEndTimeField)) {
            return;
        }
        AppointmentsDAO.addAppointment(userId ,appCustomerIdCombo, appContactCombo, appTitleField, appDescriptionField,
                appTypeField, appLocationField, appStartDatePicker, appEndDatePicker, appStartTimeField, appEndTimeField);
        updateAppsAll();
        updateAppsWeekly();
        updateAppsMonthly();
        AppointmentCheck.checkForAppointments(appNotesArea);
    }

    /**
     * Deletes the selected appointment from the database.
     * Updates the table views with the new appointment.
     */
    public void appDelete() {
        if(appIdField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected");
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            return;
        }
        AppointmentsDAO.deleteAppointment(appIdField);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Canceled\n" + "Appointment ID: " + appIdField.getText() + " Type: " + appTypeField.getText());
        alert.showAndWait();
        appClearFields();
        updateAppsAll();
        updateAppsWeekly();
        updateAppsMonthly();
        AppointmentCheck.checkForAppointments(appNotesArea);
    }
    /**
     * Updates the selected appointment in the database with the appointment fields.
     * Updates the table views with the new appointment.
     */
    public void appUpdate() {
        if (appValidationCheck(appCustomerIdCombo, appStartDatePicker, appStartTimeField, appEndDatePicker, appEndTimeField)) {
            return;
        }
        AppointmentsDAO.updateAppointment(appIdField, userId, appCustomerIdCombo, appContactCombo, appTitleField,
                appDescriptionField, appTypeField, appLocationField, appStartDatePicker, appEndDatePicker, appStartTimeField,
                appEndTimeField);
        updateAppsAll();
        updateAppsWeekly();
        updateAppsMonthly();
    }
    /**
     * Clears the appointment fields.
     */
    public void appClearFields() {
        appCustomerIdCombo.setValue(null);
        appIdField.clear();
        appContactCombo.setValue(null);
        appTitleField.clear();
        appDescriptionField.clear();
        appTypeField.clear();
        appLocationField.clear();
        appStartDatePicker.getEditor().clear();
        appEndDatePicker.getEditor().clear();
        appStartTimeField.clear();
        appEndTimeField.clear();
    }
    /**
     * Sets the contact combo boxes with the list of contacts from the database.
     */
    public void setContactCombo() {
        appContactCombo.setItems(ComboBoxDAO.contactList());
    }

    /**
     * Validates the appointment fields for logic errors.
     * @param customerIdCombo The customer ID combo box
     * @param startDateTimePicker The start date picker
     * @param startTimeField The start time field
     * @param endDateTimePicker The end date picker
     * @param endTimeField The end time field
     * @return Returns true if there is an error, false if there is not.
     */
    private boolean appValidationCheck(ComboBox<String> customerIdCombo, DatePicker startDateTimePicker,TextField startTimeField,
                             DatePicker endDateTimePicker,TextField endTimeField) {
        //Check if time start time is in valid format
        if(InputValidation.isValidTime(startTimeField)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid time");
            alert.setContentText("Please enter a valid starting time in the 24 Hour format HH:MM");
            alert.showAndWait();
            return true;
        }
        // Check if time end time is in valid format
        if(InputValidation.isValidTime(endTimeField)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid time");
            alert.setContentText("Please enter a valid ending time in the 24 Hour format HH:MM");
            alert.showAndWait();
            return true;
        }

        LocalDate startDate = startDateTimePicker.getValue();
        LocalDate endDate = endDateTimePicker.getValue();
        LocalTime startTime = LocalTime.parse(startTimeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(endTimeField.getText(), DateTimeFormatter.ofPattern("HH:mm"));

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);


        // Check if the appointment start time is before the end time
        if(startDateTime.isAfter(endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid appointment time");
            alert.setContentText("Appointment start time must be before the end time.");
            alert.showAndWait();
            return true;
        }

        // Check if the appointment is within business hours
        if(!AppointmentsDAO.isBusinessHours(startDateTime, endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid appointment time");
            alert.setContentText("Appointments must be scheduled between 8:00 AM and 10:00 PM EST.");
            alert.showAndWait();
            return true;
        }

        // Check if the appointment overlaps with another appointment
        if(AppointmentsDAO.isOverlapping(Integer.parseInt(customerIdCombo.getValue()), startDateTime, endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Overlapping appointment");
            alert.setContentText("Customers has overlapping appointments during the requested time.");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    /**
     * Sends application to Reports screen.
     * @param actionEvent The action event
     * @throws IOException The exception
     */
    public void reportsScreen(ActionEvent actionEvent) throws IOException {
        ScreenChange.newView("/grice/c195/Reports.fxml", actionEvent);
    }
}

