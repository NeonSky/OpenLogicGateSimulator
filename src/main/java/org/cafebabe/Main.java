package org.cafebabe;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.ResourceBundle;

public class Main extends Application {

    private static final boolean DEBUG_MOUSE_CLICKS = false;
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;

    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("OLGS");
        Parent root = FXMLLoader.load(getClass().getResource("/view/EditorView.fxml"), bundle);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("css/GlobalStyles.css");

        stage.setTitle(bundle.getString("application.name"));
        stage.setScene(scene);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);

        if(DEBUG_MOUSE_CLICKS) {
            scene.enableInputMethodEvents(true);
            scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                System.out.println("\n\nMouse clicked!\n\n");
                System.out.println(mouseEvent.toString().replace(", ", "\n"));
                System.out.println("\n\n");
            });
        }
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
