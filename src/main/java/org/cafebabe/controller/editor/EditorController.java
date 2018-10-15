package org.cafebabe.controller.editor;

import java.util.List;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.controller.Controller;
import org.cafebabe.controller.ISceneController;
import org.cafebabe.controller.editor.componentlist.ComponentListController;
import org.cafebabe.controller.editor.workspace.WorkspaceController;
import org.cafebabe.model.editor.workspace.Workspace;
import org.cafebabe.model.storage.ICanSaveLoad;
import org.cafebabe.model.storage.JsonStorage;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.EditorView;
import org.cafebabe.view.editor.componentlist.ComponentListView;
import org.cafebabe.view.editor.workspace.WorkspaceView;
import org.cafebabe.view.util.FxmlUtil;

/**
 * Handles user interactions with the editor view.
 */
public class EditorController extends Controller implements ISceneController {

    private static final KeyCombination SAVE_WORKSPACE_SHORTCUT =
            new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN);
    private static final KeyCombination OPEN_WORKSPACE_SHORTCUT =
            new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN);

    private final EditorView view;
    private final ICanSaveLoad storageHandler = new JsonStorage();

    /* Public */
    public EditorController(EditorView view) {
        super(view);
        this.view = view;

        setSubviewAttachController(WorkspaceView.class, WorkspaceController.class);
        setSubviewAttachController(ComponentListView.class, ComponentListController.class);
        setupEventListeners();

        addNewWorkspace(new Workspace());
        this.view.showWorkspace(0);
        this.view.init();

        FxmlUtil.onInputEvent(view, KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }


    /* Public */
    @Override
    public View getView() {
        return this.view;
    }


    /* Private */
    private void setupEventListeners() {
        AnchorPane addNewTabButton = this.view.getAddNewTabButton();
        addNewTabButton.setOnMouseClicked(event -> {
            addNewWorkspace(new Workspace());
            event.consume();
        });

        SingleSelectionModel<Tab> model = this.view.getTabsPane().getSelectionModel();
        ReadOnlyIntegerProperty selected = model.selectedIndexProperty();
        selected.addListener((observable, oldValue, newValue) -> selectWorkspace(newValue));
    }

    private void addNewWorkspace(Workspace workspace) {
        this.view.addNewWorkspace(workspace);

        List<WorkspaceView> workspaceViews = this.view.getWorkspaceViews();
        WorkspaceView newWorkspace = workspaceViews.get(workspaceViews.size() - 1);
        Tab newTab = this.view.lastTab();

        newTab.setOnCloseRequest(event -> removeWorkspace(newWorkspace, newTab));
    }

    private void removeWorkspace(WorkspaceView workspaceView, Tab workspaceTab) {
        this.view.removeWorkspace(workspaceView);

        if (this.view.getWorkspaceViews().isEmpty()) {
            addNewWorkspace(new Workspace());
        }

        if (workspaceTab.isSelected()) {
            selectWorkspace(this.view.getWorkspaceViews().size() - 1);
        }
    }

    private void selectWorkspace(Number index) {
        // JavaFX can sometimes return an out-of-bounds index
        int i = index.intValue();
        if (i < 0 || this.view.getWorkspaceViews().size() <= i) {
            return;
        }

        this.view.showWorkspace(i);
    }

    private void handleKeyPress(KeyEvent event) {
        if (SAVE_WORKSPACE_SHORTCUT.match(event)) {
            saveCurrentWorkspace();
            event.consume();
        } else if (OPEN_WORKSPACE_SHORTCUT.match(event)) {
            loadDummyWorkspace();
            event.consume();
        }
    }

    private void saveCurrentWorkspace() {
        try {
            this.storageHandler.saveWorkspace(this.view.getCurrentWorkspaceView().getWorkspace(),
                    "asdf.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDummyWorkspace() {
        try {
            Workspace workspace = this.storageHandler.loadWorkspace("asdf.txt");
            this.view.addNewWorkspace(workspace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
