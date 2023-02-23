package grice.c195.DAO;

import grice.c195.helper.JDBC;
import grice.c195.model.Appointment;
import grice.c195.model.Contact;
import grice.c195.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object for the appointments table.
 */
public class AppointmentsDAO {
    /**
     * Retrieves all appointments from the database and returns them as an observable list.
     * @param timeFrame String of either "all", "week", or "month".
     * @return appointmentList ObservableList<Appointment> of all appointments.
     */
    public static ObservableList<Appointment> getAppointments(String timeFrame) {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String query = switch (timeFrame) {
            case "all" ->
                    "SELECT a.Appointment_ID, a.Title, a.Description, a.Type, c.Contact_Name,  a.Location, a.Start, a.End, a.Customer_ID, a.User_ID " +
                            "FROM appointments a " +
                            "JOIN contacts c ON a.Contact_ID = c.Contact_ID;";
            case "week" ->
                    "SELECT a.Appointment_ID, a.Title, a.Description, a.Type, c.Contact_Name,  a.Location, a.Start, a.End, a.Customer_ID, a.User_ID " +
                            "FROM appointments a " +
                            "JOIN contacts c ON a.Contact_ID = c.Contact_ID " +
                            "WHERE a.Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY);";
            case "month" ->
                    "SELECT a.Appointment_ID, a.Title, a.Description, a.Type, c.Contact_Name,  a.Location, a.Start, a.End, a.Customer_ID, a.User_ID " +
                            "FROM appointments a " +
                            "JOIN contacts c ON a.Contact_ID = c.Contact_ID " +
                            "WHERE a.Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 1 MONTH);";
            default -> "";
        };

        try {
            JDBC.openConnection();
            PreparedStatement ps = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Type"),
                        resultSet.getString("Contact_Name"),
                        resultSet.getString("Location"),
                        resultSet.getTimestamp("Start"),
                        resultSet.getTimestamp("End"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"));
                appointmentList.add(appointment);
            }

            JDBC.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointmentList;
    }
    /**
     * Returns a list of all Contact names in the contacts database table.
     * @param UserId The ID of the user currently logged in.
     * @param appCustomerIDCombo The ComboBox containing the customer ID.
     * @param appContactCombo The ComboBox containing the contact name.
     * @param appTitleField The TextField containing the appointment title.
     * @param appDescriptionField The TextField containing the appointment description.
     * @param appTypeField The TextField containing the appointment type.
     * @param appLocationField The TextField containing the appointment location.
     * @param appStartDatePicker The DatePicker containing the appointment start date.
     * @param appEndDatePicker The DatePicker containing the appointment end date.
     * @param appStartTimeField The TextField containing the appointment start time.
     * @param appEndTimeField The TextField containing the appointment end time.
     */
    public static void addAppointment(int UserId, ComboBox<String> appCustomerIDCombo, ComboBox<String> appContactCombo, TextField appTitleField,
                                      TextField appDescriptionField, TextField appTypeField, TextField appLocationField,
                                      DatePicker appStartDatePicker, DatePicker appEndDatePicker,
                                      TextField appStartTimeField, TextField appEndTimeField) {

        if (appCustomerIDCombo.getValue() == null || appContactCombo.getValue().isEmpty() || appTitleField.getText().isEmpty() ||
                appDescriptionField.getText().isEmpty() || appTypeField.getText().isEmpty() || appLocationField.getText().isEmpty() ||
                appStartDatePicker.getValue() == null || appEndDatePicker.getValue() == null || appStartTimeField.getText().isEmpty() ||
                appEndTimeField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill out all required fields.");
            alert.showAndWait();
            return;
        }

        try {
            String startTime = timeConverter(appStartDatePicker.getValue() + " " + appStartTimeField.getText()).toString();
            String endTime = timeConverter(appEndDatePicker.getValue() + " " + appEndTimeField.getText()).toString();

            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("INSERT INTO appointments (Customer_ID, Contact_ID, " +
                    "Title, Description, Type, Location, Start, End, User_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, Integer.parseInt(appCustomerIDCombo.getValue()));
            statement.setInt(2, Contact.getContactIdByName(appContactCombo.getValue()));
            statement.setString(3, appTitleField.getText());
            statement.setString(4, appDescriptionField.getText());
            statement.setString(5, appTypeField.getText());
            statement.setString(6, appLocationField.getText());
            statement.setString(7, startTime);
            statement.setString(8, endTime);
            statement.setInt(9, UserId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a time from local time to UTC time.
     * @param time The time to convert.
     * @return The converted time.
     */
    private static LocalDateTime timeConverter(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(time, formatter);
        ZoneId localZoneId = ZoneId.systemDefault();

        ZonedDateTime localZonedDateTime = ZonedDateTime.of(startDateTime, localZoneId);
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZonedDateTime utcZonedDateTime = localZonedDateTime.withZoneSameInstant(utcZoneId);
        return utcZonedDateTime.toLocalDateTime();
    }
    /**
     * Updates an appointment in the database.
     * @param appIdField The appointment ID field.
     * @param userId The user ID.
     * @param appCustomerIDCombo The customer ID combo box.
     * @param appContactCombo The contact combo box.
     * @param appTitleField The title field.
     * @param appDescriptionField The description field.
     * @param appTypeField The type field.
     * @param appLocationField The location field.
     * @param appStartDatePicker The start date picker.
     * @param appEndDatePicker The end date picker.
     * @param appStartTimeField The start time field.
     * @param appEndTimeField The end time field.
     */
    public static void updateAppointment(TextField appIdField, int userId, ComboBox<String> appCustomerIDCombo,
                                         ComboBox<String> appContactCombo, TextField appTitleField,
                                         TextField appDescriptionField, TextField appTypeField,
                                         TextField appLocationField, DatePicker appStartDatePicker,
                                         DatePicker appEndDatePicker, TextField appStartTimeField,
                                         TextField appEndTimeField) {

        if (appCustomerIDCombo.getValue() == null || appContactCombo.getValue().isEmpty() || appTitleField.getText().isEmpty() ||
                appDescriptionField.getText().isEmpty() || appTypeField.getText().isEmpty() || appLocationField.getText().isEmpty() ||
                appStartDatePicker.getValue() == null || appEndDatePicker.getValue() == null || appStartTimeField.getText().isEmpty() ||
                appEndTimeField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please fill out all required fields.");
            alert.showAndWait();
            return;
        }
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("UPDATE appointments SET Customer_ID = ?, Contact_ID = ?, " +
                    "Title = ?, Description = ?, Type = ?, Location = ?, Start = ?, End = ?, User_ID = ? " +
                    "WHERE Appointment_ID = ?");
            statement.setInt(1, Integer.parseInt(appCustomerIDCombo.getValue()));
            statement.setInt(2, Contact.getContactIdByName(appContactCombo.getValue()));
            statement.setString(3, appTitleField.getText());
            statement.setString(4, appDescriptionField.getText());
            statement.setString(5, appTypeField.getText());
            statement.setString(6, appLocationField.getText());
            statement.setString(7, appStartDatePicker.getValue() + " " + appStartTimeField.getText());
            statement.setString(8, appEndDatePicker.getValue() + " " + appEndTimeField.getText());
            statement.setInt(9, userId);
            statement.setInt(10, Integer.parseInt(appIdField.getText()));
            statement.executeUpdate();
            JDBC.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deletes an appointment.
     * @param appIdField The appointment ID field.
     */
    public static void deleteAppointment(TextField appIdField) {
        try {
            int appointmentId = Integer.parseInt(appIdField.getText());
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?");
            statement.setInt(1, appointmentId);
            statement.executeUpdate();
            JDBC.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method returns a list of appointments by month.
     * @param selectedCustomer The selected customer.
     * @return appointments The list of appointments.
     */
    public static ObservableList<Appointment> getAppointmentsByCustomer(Customer selectedCustomer) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE Customer_ID = ?");
            statement.setInt(1, selectedCustomer.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Type"),
                        Contact.getContactNameById(resultSet.getInt("Contact_ID")),
                        resultSet.getString("Location"),
                        resultSet.getTimestamp("Start"),
                        resultSet.getTimestamp("End"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"));
                appointments.add(appointment);
            }
            JDBC.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * This method checks if the appointment is within business hours.
     *
     * @param startDateTime The start date and time of the appointment that is based on the user's local time zone.
     * @param endDateTime   The end date and time of the appointment that is based on the user's local time zone.
     * @return Returns true if the appointment is within business hours, false if not.
     */
    public static boolean isBusinessHours(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalTime businessStart = LocalTime.of(8, 0);
        LocalTime businessEnd = LocalTime.of(22, 0);

        // Convert startDateTime and endDateTime to Eastern Time Zone
        ZoneId easternZoneId = ZoneId.of("America/New_York");
        ZonedDateTime easternStartDateTime = ZonedDateTime.of(startDateTime, easternZoneId);
        ZonedDateTime easternEndDateTime = ZonedDateTime.of(endDateTime, easternZoneId);

        // Get the time of the appointment in Eastern Time
        LocalTime easternAppointmentStart = easternStartDateTime.toLocalTime();
        LocalTime easternAppointmentEnd = easternEndDateTime.toLocalTime();

        // Check if the appointment is within business hours
        return !easternAppointmentStart.isBefore(businessStart) && !easternAppointmentEnd.isAfter(businessEnd);
    }

    /**
     * This method checks if the appointment overlaps with another appointment for the same customer.
     * @param customerId The customer ID of the appointment.
     * @param startDateTime The start date and time of the appointment that is based on the user's local time zone.
     * @param endDateTime The end date and time of the appointment that is based on the user's local time zone.
     * @return Returns true if the appointment overlaps with another appointment for the same customer, false if not.
     */
    public static boolean isOverlapping(int customerId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Convert start and end times to UTC
        ZonedDateTime startUTC = startDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"));

        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("SELECT COUNT(*) FROM appointments " +
                    "WHERE Customer_ID = ? AND (? BETWEEN Start AND End OR ? BETWEEN Start AND End OR " +
                    "Start BETWEEN ? AND ? OR End BETWEEN ? AND ?)");
            statement.setInt(1, customerId);
            statement.setTimestamp(2, Timestamp.valueOf(startUTC.toLocalDateTime()));
            statement.setTimestamp(3, Timestamp.valueOf(endUTC.toLocalDateTime()));
            statement.setTimestamp(4, Timestamp.valueOf(startUTC.toLocalDateTime()));
            statement.setTimestamp(5, Timestamp.valueOf(endUTC.toLocalDateTime()));
            statement.setTimestamp(6, Timestamp.valueOf(startUTC.toLocalDateTime()));
            statement.setTimestamp(7, Timestamp.valueOf(endUTC.toLocalDateTime()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBC.closeConnection();
        }

        return false;
    }

    /**
     * This method returns a list of unique appointment types.
     *
     * @return Returns a list of unique appointment types.
     */
    public static ObservableList<String> getAllAppointmentTypes() {
        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("SELECT DISTINCT Type FROM appointments");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                appointmentTypes.add(resultSet.getString("Type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBC.closeConnection();
        }
        appointmentTypes.sort(String::compareToIgnoreCase);
        return appointmentTypes;
    }
    /**
     * This method returns a list of appointments by month.
     *
     * @param selectedMonth The selected month.
     * @return Returns a list of appointments by month.
     */
    public static ObservableList<Appointment> getAppointmentsByMonth(String selectedMonth) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("January", 1);
        monthMap.put("February", 2);
        monthMap.put("March", 3);
        monthMap.put("April", 4);
        monthMap.put("May", 5);
        monthMap.put("June", 6);
        monthMap.put("July", 7);
        monthMap.put("August", 8);
        monthMap.put("September", 9);
        monthMap.put("October", 10);
        monthMap.put("November", 11);
        monthMap.put("December", 12);
        int month = monthMap.getOrDefault(selectedMonth, 0);

        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE MONTH(Start) = ?");
            statement.setInt(1, month);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Type"),
                        Contact.getContactNameById(resultSet.getInt("Contact_ID")),
                        resultSet.getString("Location"),
                        resultSet.getTimestamp("Start"),
                        resultSet.getTimestamp("End"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"));
                appointments.add(appointment);
            }
            JDBC.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * This method returns a list of appointments by type.
     *
     * @param selectedType The selected type.
     * @return Returns a list of appointments by type.
     */
    public static ObservableList<Appointment> getAppointmentsByType(String selectedType) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE Type = ?");
            statement.setString(1, selectedType);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Type"),
                        Contact.getContactNameById(resultSet.getInt("Contact_ID")),
                        resultSet.getString("Location"),
                        resultSet.getTimestamp("Start"),
                        resultSet.getTimestamp("End"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"));
                appointments.add(appointment);
            }
            JDBC.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * This method returns a list of appointments by month and type.
     *
     * @param selectedMonth The selected month.
     * @param selectedType  The selected type.
     * @return Returns a list of appointments by month and type.
     */
    public static ObservableList<Appointment> getAppointmentsByMonthAndType(String selectedMonth, String selectedType) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("January", 1);
        monthMap.put("February", 2);
        monthMap.put("March", 3);
        monthMap.put("April", 4);
        monthMap.put("May", 5);
        monthMap.put("June", 6);
        monthMap.put("July", 7);
        monthMap.put("August", 8);
        monthMap.put("September", 9);
        monthMap.put("October", 10);
        monthMap.put("November", 11);
        monthMap.put("December", 12);
        int month = monthMap.getOrDefault(selectedMonth, 0);
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE MONTH(Start) = ? AND Type = ?");
            statement.setInt(1, month);
            statement.setString(2, selectedType);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Type"),
                        Contact.getContactNameById(resultSet.getInt("Contact_ID")),
                        resultSet.getString("Location"),
                        resultSet.getTimestamp("Start"),
                        resultSet.getTimestamp("End"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"));
                appointments.add(appointment);
            }
            JDBC.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * This method returns a list of appointments by contact.
     *
     * @param selectedContact The selected contact.
     * @return Returns a list of appointments by contact.
     */
    public static ObservableList<Appointment> getAppointmentsByContact(String selectedContact) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            PreparedStatement statement = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE Contact_ID = ?");
            statement.setInt(1, Contact.getContactIdByName(selectedContact));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Type"),
                        Contact.getContactNameById(resultSet.getInt("Contact_ID")),
                        resultSet.getString("Location"),
                        resultSet.getTimestamp("Start"),
                        resultSet.getTimestamp("End"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"));
                appointments.add(appointment);
            }
            JDBC.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    /**
     * This method returns an observable list of all appointments from the database based on the passed in string.  The
     * String can be "All Time", "Week", "Month", "Quarter", "Year", or "Last Year".
     * @param selectedTimeFrame The selected time frame.
     * @return Returns an observable list of all appointments from the database based on the passed in string.
     */
    public static ObservableList<Appointment> getAppointmentsTimeFrame(String selectedTimeFrame) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            JDBC.openConnection();
            PreparedStatement statement = null;
            switch (selectedTimeFrame) {
                case "All Time" -> statement = JDBC.connection.prepareStatement("SELECT * FROM appointments");
                case "Week" -> {
                    statement = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE Start BETWEEN ? AND ?");
                    statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minusDays(7)));
                    statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                }
                case "Month" -> {
                    statement = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE Start BETWEEN ? AND ?");
                    statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().withDayOfMonth(1)));
                    statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                }
                case "Quarter" -> {
                    statement = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE Start BETWEEN ? AND ?");
                    statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
                    statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                }
                case "Year" -> {
                    statement = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE Start BETWEEN ? AND ?");
                    statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().withDayOfYear(1)));
                    statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                }
                case "Last Year" -> {
                    statement = JDBC.connection.prepareStatement("SELECT * FROM appointments WHERE Start BETWEEN ? AND ?");
                    statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minusYears(1).withDayOfYear(1)));
                    statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().minusYears(1).withDayOfYear(365)));
                }
            }
            if (statement != null) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Appointment appointment = new Appointment(
                            resultSet.getInt("Appointment_ID"),
                            resultSet.getString("Title"),
                            resultSet.getString("Description"),
                            resultSet.getString("Type"),
                            Contact.getContactNameById(resultSet.getInt("Contact_ID")),
                            resultSet.getString("Location"),
                            resultSet.getTimestamp("Start"),
                            resultSet.getTimestamp("End"),
                            resultSet.getInt("Customer_ID"),
                            resultSet.getInt("User_ID"));
                    appointments.add(appointment);
                }
            }
            JDBC.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * This method returns a count of the total hours for appointments of the list passed in.
     * @param appointments List of appointments
     * @return minutes of appointments
     */
    public static Integer getTotalMinutes(ObservableList<Appointment> appointments) {
        int totalHours = 0;
        for (Appointment appointment : appointments) {
            totalHours += appointment.getDuration();
        }
        return totalHours;
    }
}
