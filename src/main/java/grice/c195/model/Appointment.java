package grice.c195.model;

import java.time.LocalDateTime;

/**
 * This class represents an appointment.
 */
public class Appointment {
        private final int id;
        private final String title;
        private final String description;
        private final String type;
        private final String contact;
        private final String location;
        private final LocalDateTime start;
        private final LocalDateTime end;
        private final int customerId;
        private final int userId;

    /**
     * Constructor for creating an appointment object.
     *
     * @param id          The appointment ID.
     * @param title       The title of the appointment.
     * @param description The description of the appointment.
     * @param type        The type of the appointment.
     * @param contact     The contact for the appointment.
     * @param location    The location of the appointment.
     * @param start    The UTC start time of the appointment.
     * @param end      The UTC end time of the appointment.
     * @param customerId  The ID of the customer associated with the appointment.
     * @param userId      The ID of the user associated with the appointment.
     */
    public Appointment(int id, String title, String description, String type, String contact, String location, LocalDateTime start, LocalDateTime end, int customerId, int userId) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.type = type;
            this.contact = contact;
            this.location = location;
            this.start = start;
            this.end = end;
            this.customerId = customerId;
            this.userId = userId;
    }

    /**
    * Returns the ID of the appointment.
    *
    * @return The ID of the appointment.
    */
    public int getId() {

        return id;
    }
    /**
    * Returns the title of the appointment.
    * @return The title of the appointment.
    */
    public String getTitle() {

        return title;
    }
    /**
    * Returns the description of the appointment.
    * @return The description of the appointment.
    */
    public String getDescription() {

        return description;
    }
    /**
     * Returns the type of the appointment.
     * @return The type of the appointment.
     */
    public String getType() {

        return type;
    }
    /**
     * Returns the contact for the appointment.
     * @return The contact for the appointment.
     */
    public String getContact() {

        return contact;
    }
    /**
     * Returns the location of the appointment.
     * @return The location of the appointment.
     */
    public String getLocation() {
        return location;
    }
    /**
     * Returns the start time of the appointment.
     * @return The start time of the appointment.
     */
    public LocalDateTime getStart() {
        return start;
    }
    /**
     * Returns the end time of the appointment.
     * @return The end time of the appointment.
     */
    public LocalDateTime getEnd() {
        return end;
    }
    /**
     * Returns the start time of the appointment.
     * @return The start time of the appointment.
     */
    public int getCustomerId() {

        return customerId;
    }
    /** Returns User ID
     * @return User ID
     */
    public int getUserId() {
        return userId;
    }
    /**
     * Returns the end time of the appointment.
     * @return The end time of the appointment.
     */
    public int getAppointmentId() {
        return id;
    }
    /**
     * Returns the duration of the appointment in minutes.
     * @return the duration of the appointment in minutes.
     */
    public int getDuration() {
        return (int) java.time.Duration.between(start, end).toMinutes();
    }
}
