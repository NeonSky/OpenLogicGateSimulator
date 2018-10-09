package org.cafebabe;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point of the application.
 * Simply boots up the JavaFX framework.
 */
public class Main extends Application {

    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;
    private static final String ENTRY_POINT = "/view/RootView.fxml";
    private static final String GLOBAL_CSS = "css/GlobalStyles.css";

    /* Public */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("OLGS");
        Parent root = FXMLLoader.load(getClass().getResource(ENTRY_POINT), bundle);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(GLOBAL_CSS);

        stage.setTitle(bundle.getString("application.name"));
        stage.setScene(scene);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);

        stage.show();
    }
}
