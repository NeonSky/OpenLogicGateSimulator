package org.cafebabe.controller.editor;

import com.google.common.base.Strings;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.controller.Controller;
import org.cafebabe.controller.ISceneController;
import org.cafebabe.controller.editor.componentlist.ComponentListController;
import org.cafebabe.controller.editor.workspace.WorkspaceController;
import org.cafebabe.model.editor.Editor;
import org.cafebabe.model.editor.workspace.Workspace;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.EditorView;
import org.cafebabe.view.editor.MenuBarView;
import org.cafebabe.view.editor.componentlist.ComponentListView;
import org.cafebabe.view.editor.workspace.WorkspaceView;

/**
 * Handles user interactions with the editor view.
 */
public class EditorController extends Controller implements ISceneController {

    private final EditorView view;

    /* Public */
    public EditorController(EditorView view) {
        super(view);
        this.view = view;

        setSubviewAttachController(WorkspaceView.class, WorkspaceController.class);
        setSubviewAttachController(ComponentListView.class, ComponentListController.class);
        setSubviewAttachController(MenuBarView.class, MenuBarController.class,
                (c) -> this.initMenuBarController((MenuBarController)c));
        setupEventListeners();

        this.view.init();
        addNewWorkspace();
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
        selected.addListener((observable, oldValue, newValue) -> selectWorkspace(newValue));
    }

    private void initMenuBarController(MenuBarController menuBarController) {
        Editor editor = this.view.getEditor();

        menuBarController.getOnSaveCurrentWorkspace().addListener(() -> {
            Workspace workspace = this.view.getCurrentWorkspaceView().getWorkspace();
            if (Strings.isNullOrEmpty(workspace.getPath())) {
                menuBarController.saveWorkspaceAs();
            } else {
                editor.saveWorkspace(this.view.getCurrentWorkspaceView().getWorkspace());
            }
        });

        menuBarController.getOnSaveCurrentWorkspaceAs().addListener((path) -> {
            editor.saveWorkspace(this.view.getCurrentWorkspaceView().getWorkspace(), path);
        });

        menuBarController.getOnLoadWorkspace().addListener((path) -> {
            Workspace workspace = this.view.getEditor().loadWorkspace(path);
            this.addWorkspace(workspace);
        });

        menuBarController.getOnQuit().addListener(this::saveAndQuit);
    }

    private void addNewWorkspace() {
        Workspace workspace = this.view.getEditor().createNewWorkspace();
        addWorkspace(workspace);
    }

    private void addWorkspace(Workspace workspace) {
        this.view.addWorkspaceView(workspace);
        List<WorkspaceView> workspaceViews = this.view.getWorkspaceViews();
        WorkspaceView newWorkspace = workspaceViews.get(workspaceViews.size() - 1);
        Tab newTab = this.view.lastTab();

        newTab.setOnCloseRequest(event -> closeWorkspace(newWorkspace));
    }

    private void removeWorkspace(WorkspaceView workspaceView) {
        boolean wasTabSelected = this.view.getWorkspaceTab(workspaceView).isSelected();
        this.view.removeWorkspace(workspaceView);

        if (this.view.getWorkspaceViews().isEmpty()) {
            addNewWorkspace();
        }

        if (wasTabSelected) {
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

    private void saveAndQuit() {
        for (WorkspaceView workspaceView : this.view.getWorkspaceViews()) {
            boolean wasCanceled = !closeWorkspace(workspaceView);
            if (wasCanceled) {
                return;
            }
        }

        Platform.exit();
    }

    private boolean closeWorkspace(WorkspaceView workspaceView) {
        Workspace workspace = workspaceView.getWorkspace();

        if (!workspace.isEmpty() && !workspace.isSaved()) {
            Optional<ButtonType> option = showUnsavedChangesAlert();
            String response = option.get().getText();

            switch (response) {
                case "Save":
                    this.view.getEditor().saveWorkspace(workspace);
                    break;
                case "Cancel":
                    return false;
                default:
                    return true;
            }
        }

        removeWorkspace(workspaceView);
        return false;
    }

    private Optional<ButtonType> showUnsavedChangesAlert() {
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
