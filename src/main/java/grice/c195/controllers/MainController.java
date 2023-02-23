package grice.c195.controllers;

import grice.c195.helper.LangLocalization;
import grice.c195.helper.ScreenChange;
import grice.c195.DAO.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * This class is the controller for the main login screen.
 * It handles user login attempts and tracks user activity by recording all user log-in attempts,
 * dates, and time stamps and whether each attempt was successful in a file named login_activity.txt.
 * Append each new record to the existing file, and save to the root folder of the application.
 */
public class MainController implements Initializable {

    @FXML
    Button mainLoginBtn; // Reference to login button
    @FXML
    Label mainUsernameLabel; // Reference to username label
    @FXML
    Label mainPasswordLabel; // Reference to password label
    @FXML
    Label mainLoginWarningLabel; // Reference to log in warning label
    @FXML
    Label mainZoneIdLabel; // Reference to zone id label
    @FXML
    TextField mainUsernameInput; // Reference to username input field
    @FXML
    PasswordField mainPasswordInput; // Reference to password input field

    /**
     * Initializes the controller class.
     * Sets the text of the login button, username label, and password label using the language bundle.
     * Sets the text of the zone id label using the system default zone id.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the text of the login button, username label, and password label using the language bundle
        mainLoginBtn.setText(LangLocalization.getLangBundle().getString("login"));
        mainUsernameLabel.setText(LangLocalization.getLangBundle().getString("username"));
        mainPasswordLabel.setText(LangLocalization.getLangBundle().getString("password"));
        mainZoneIdLabel.setText(ZoneId.systemDefault().toString());

        // For testing purposes
        mainUsernameInput.setText("admin"); // For testing purposes
        mainPasswordInput.setText("admin"); // For testing purposes
    }

    /**
     * Handles the click event of the login button.
     * Displays a warning message if either the username or password input field is empty, clears the warning message otherwise.
     * If both the username and password are entered, queries the database for a matching user, and displays a warning message if not found.
     * Logs all successful and failed login attempts in a file named login_activity.txt in the root folder of the application.
     *
     * @param actionEvent The event that occurred.
     * @throws SQLException If an error occurs while accessing the database.
     * @throws IOException If an error occurs while writing to the login_activity.txt file.
     */

    public void loginClick(ActionEvent actionEvent) throws SQLException, IOException {
        // Check if the username or password input field is empty
        if (mainUsernameInput.getText().isEmpty() || mainPasswordInput.getText().isEmpty()) {
            mainLoginWarningLabel.setText(LangLocalization.getLangBundle().getString("warningTxt")); // Display a warning message
            return;
        } else {
            mainLoginWarningLabel.setText(""); // Clear the warning message
        }
        // Query the database for a matching user
        int userId = UserDAO.validateUser(mainUsernameInput.getText(), mainPasswordInput.getText());
        // Check if the user was found in the database
        if (userId != 0) {
            // Track user login activity
            try {
                File loginActivityFile = new File("login_activity.txt");
                if (!loginActivityFile.exists()) {
                    loginActivityFile.createNewFile();
                }
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String logMessage = String.format("%s - User %s logged in successfully.\n", dtf.format(now), mainUsernameInput.getText());
                BufferedWriter writer = new BufferedWriter(new FileWriter(loginActivityFile, true));
                writer.write(logMessage);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            CustomerController.setUserId(userId); // Pass the userID to the CustomerController
            ScreenChange.newView("/grice/c195/customerScreen.fxml", actionEvent); // Load the customer screen
        } else {
            // Track failed login attempts
            try {
                File loginActivityFile = new File("login_activity.txt");
                if (!loginActivityFile.exists()) {
                    loginActivityFile.createNewFile();
                }
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String logMessage = String.format("%s - User %s login attempt failed.\n", dtf.format(now), mainUsernameInput.getText());
                BufferedWriter writer = new BufferedWriter(new FileWriter(loginActivityFile, true));
                writer.write(logMessage);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mainLoginWarningLabel.setText(LangLocalization.getLangBundle().getString("warningTxt"));
        }
    }
}