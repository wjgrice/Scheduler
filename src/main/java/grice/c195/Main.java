package grice.c195;

import grice.c195.helper.LangLocalization;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the C195 project.
 * Launches the JavaFX application and displays the login screen.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by loading the login screen FXML file and displaying the login screen.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 475, 300);
        stage.setTitle(LangLocalization.getLangBundle().getString("title"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
