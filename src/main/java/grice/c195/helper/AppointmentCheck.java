package grice.c195.helper;

import grice.c195.DAO.AppointmentsDAO;
import grice.c195.model.Appointment;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.time.LocalDateTime;

/**
 * Provides an alert when there is an appointment within 15 minutes of the user’s log-in.
 * A custom message is displayed in the user interface and include the appointment ID, date, and time.
 * If the user does not have any appointments within 15 minutes of logging in, display a custom message in the
 * user interface indicating there are no upcoming appointments.
 */
public class AppointmentCheck {
    public static void checkForAppointments(TextArea appNotesArea) {
        appNotesArea.setText("Upcoming Appointments: ");
        appNotesArea.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        ObservableList<Appointment> appointmentList = AppointmentsDAO.getAppointments("all");
        boolean upcomingAppointmentsFound = false;
        for (Appointment appointment : appointmentList) {
            LocalDateTime appStart = appointment.getLocalStartTime();
            if (appStart.isBefore(LocalDateTime.now().plusMinutes(15)) && appStart.isAfter(LocalDateTime.now().minusMinutes(15))) {
                appNotesArea.appendText(
                        "\nAppointment ID: " + appointment.getAppointmentId() +  "\n" +
                                "Appointment Date: " + appStart.toLocalDate() +  "\n" +
                                "Appointment Time: " + appStart.toLocalTime() +  "\n"
                );
                upcomingAppointmentsFound = true;
            }
        }
        if (!upcomingAppointmentsFound) {
            appNotesArea.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
            appNotesArea.appendText("\n\nNone.");
        }
    }
}
