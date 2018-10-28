package org.cafebabe.controller.editor;

import com.google.common.base.Strings;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import lombok.Getter;
import org.cafebabe.controller.Controller;
import org.cafebabe.controller.util.FileDialogueHelper;
import org.cafebabe.model.util.EmptyEvent;
import org.cafebabe.model.util.Event;
import org.cafebabe.view.editor.MenuBarView;
import org.cafebabe.view.util.FxmlUtil;

/**
 * Handles user interactions with the menu bar.
 */
public class MenuBarController extends Controller {

    @Getter private final EmptyEvent onSaveCurrentWorkspace = new EmptyEvent();
    @Getter private final Event<String> onSaveCurrentWorkspaceAs = new Event<>();
    @Getter private final Event<String> onLoadWorkspace = new Event<>();
    @Getter private final EmptyEvent onQuit = new EmptyEvent();

    private static final KeyCombination SAVE_WORKSPACE_SHORTCUT =
            new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN);
    private static final KeyCombination OPEN_WORKSPACE_SHORTCUT =
            new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN);

    private final MenuBarView view;

    /* Public */
    public MenuBarController(MenuBarView view) {
        super(view);
        this.view = view;
        setupEventListeners();
    }

    /* Private */
    private void setupEventListeners() {
        MenuItem openMenuItem = this.view.getOpenMenuItem();
        openMenuItem.setOnAction(event -> {
            openWorkspace();
            event.consume();
        });

        MenuItem saveMenuItem = this.view.getSaveMenuItem();
        saveMenuItem.setOnAction(event -> {
            saveCurrentWorkspace();
            event.consume();
        });

        MenuItem saveAsMenuItem = this.view.getSaveAsMenuItem();
        saveAsMenuItem.setOnAction(event -> {
            saveWorkspaceAs();
            event.consume();
        });

        MenuItem quitMenuItem = this.view.getQuitMenuItem();
        quitMenuItem.setOnAction(event -> {
            this.onQuit.notifyListeners();
            event.consume();
        });

        FxmlUtil.onInputEvent(this.view, KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent event) {
        if (SAVE_WORKSPACE_SHORTCUT.match(event)) {
            saveCurrentWorkspace();
            event.consume();
        } else if (OPEN_WORKSPACE_SHORTCUT.match(event)) {
            openWorkspace();
            event.consume();
        }
    }

    private void openWorkspace() {
        String path = FileDialogueHelper.openWorkspace(this.view.getScene().getWindow());
        if (!Strings.isNullOrEmpty(path)) {
            this.onLoadWorkspace.notifyListeners(path);
        }
    }

    private void saveCurrentWorkspace() {
        this.onSaveCurrentWorkspace.notifyListeners();
    }

    private void saveWorkspaceAs() {
        String path = FileDialogueHelper.saveWorkspace(this.view.getScene().getWindow());
        if (!Strings.isNullOrEmpty(path)) {
            this.onSaveCurrentWorkspaceAs.notifyListeners(path);
        }
    }
}
