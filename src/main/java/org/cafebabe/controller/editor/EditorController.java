package org.cafebabe.controller.editor;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.cafebabe.controller.Controller;
import org.cafebabe.controller.ISceneController;
import org.cafebabe.controller.editor.componentlist.ComponentListController;
import org.cafebabe.controller.editor.workspace.WorkspaceController;
import org.cafebabe.model.editor.workspace.Workspace;
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

    private final List<FileChooser.ExtensionFilter> extensionFilters = Arrays.asList(
            new FileChooser.ExtensionFilter("Circuit files", "*.olgs")
    );

    private final EditorView view;

    /* Public */
    public EditorController(EditorView view) {
        super(view);
        this.view = view;

        setSubviewAttachController(WorkspaceView.class, WorkspaceController.class);
        setSubviewAttachController(ComponentListView.class, ComponentListController.class);
        setupEventListeners();

        this.view.init();
        addNewWorkspace();

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
            addNewWorkspace();
            event.consume();
        });

        SingleSelectionModel<Tab> model = this.view.getTabsPane().getSelectionModel();
        ReadOnlyIntegerProperty selected = model.selectedIndexProperty();
        selected.addListener((observable, oldValue, newValue) -> {
            selectWorkspace(newValue);
        });
    }

    private void addNewWorkspace() {
        this.view.getEditor().createNewWorkspace();
        addWorkspace(this.view.getEditor().getCurrentWorkspace());
    }

    private void addWorkspace(Workspace workspace) {
        this.view.addWorkspaceView(workspace);
        List<WorkspaceView> workspaceViews = this.view.getWorkspaceViews();
        WorkspaceView newWorkspace = workspaceViews.get(workspaceViews.size() - 1);
        Tab newTab = this.view.lastTab();

        newTab.setOnCloseRequest(event -> removeWorkspace(newWorkspace, newTab));
    }

    private void removeWorkspace(WorkspaceView workspaceView, Tab workspaceTab) {
        this.view.removeWorkspace(workspaceView);

        if (this.view.getWorkspaceViews().isEmpty()) {
            addNewWorkspace();
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
            saveWorkspace();
            event.consume();
        } else if (OPEN_WORKSPACE_SHORTCUT.match(event)) {
            openWorkspace();
            event.consume();
        }
    }

    private void saveWorkspace() {
        Workspace currentWorkspace = this.view.getEditor().getCurrentWorkspace();
        if (currentWorkspace.isSaved()) {
            this.view.getEditor().saveCurrentWorkspace(currentWorkspace.getPath());
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Circuit File");
            fileChooser.getExtensionFilters().addAll(this.extensionFilters);
            String path = fileChooser.showSaveDialog(this.view.getScene().getWindow()).getPath();

            if (Strings.isNullOrEmpty(path)) {
                return;
            }

            // Append olgs file extension if not present
            if (!path.contains(".olgs")) {
                StringBuilder pathBuilder = new StringBuilder(path);
                pathBuilder.append(".olgs");
                path = pathBuilder.toString();
            }

            this.view.getEditor().saveCurrentWorkspace(path);
        }
    }

    private void openWorkspace() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Circuit File");
        fileChooser.getExtensionFilters().addAll(this.extensionFilters);
        String path = fileChooser.showOpenDialog(this.view.getScene().getWindow()).getPath();

        if (Strings.isNullOrEmpty(path)) {
            return;
        }

        Workspace workspace = this.view.getEditor().loadWorkspace(path);
        this.addWorkspace(workspace);
    }
}
