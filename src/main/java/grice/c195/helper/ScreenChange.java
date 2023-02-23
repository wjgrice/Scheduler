package grice.c195.helper;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
/**
 * Class used to change scenes easily
 */
public class ScreenChange {
    /**
     * Simple method used to change scenes easily
     * @param path to FMXL file to load
     * @param actionEvent event to pull stage from
     */
    public static void newView(String path, ActionEvent actionEvent) throws IOException {
        // Load the new scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(ScreenChange.class.getResource(path)));
        // Set the new scene
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        // Set the stage to the new scene
        stage.setScene(scene);
        stage.show();
    }
}
