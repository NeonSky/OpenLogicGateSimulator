package org.cafebabe.controller.util;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * Shows various dialogues related to managing save files.
 */
public final class FileDialogueHelper {

    @SuppressWarnings("checkstyle:linelength")
    private static final List<FileChooser.ExtensionFilter> EXTENSION_FILTERS = Collections.singletonList(
            new FileChooser.ExtensionFilter("Circuit files", "*.olgs")
    );


    private FileDialogueHelper() {
    }

    public static File saveWorkspace(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Circuit File");
        fileChooser.getExtensionFilters().addAll(EXTENSION_FILTERS);
        return fileChooser.showSaveDialog(window);
    }

    public static String openWorkspace(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Circuit File");
        fileChooser.getExtensionFilters().addAll(EXTENSION_FILTERS);
        return fileChooser.showOpenDialog(window).getPath();
    }

    public static Optional<ButtonType> showUnsavedChangesAlert() {
        ButtonType save = new ButtonType("Save");
        ButtonType dontSave = new ButtonType("Don't Save");
        ButtonType cancel = new ButtonType("Cancel");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "You have unsaved changes, do you want to save them?",
                save, dontSave, cancel);
        alert.setResizable(true);
        return alert.showAndWait();
    }
}
