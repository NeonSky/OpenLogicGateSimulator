package org.cafebabe.controller.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import lombok.Getter;
import org.cafebabe.controller.Controller;
import org.cafebabe.controller.ISceneController;
import org.cafebabe.controller.editor.componentlist.ComponentListController;
import org.cafebabe.controller.editor.workspace.WorkspaceController;
import org.cafebabe.controller.util.FileDialogueHelper;
import org.cafebabe.model.editor.Editor;
import org.cafebabe.model.editor.workspace.Workspace;
import org.cafebabe.view.editor.EditorView;
import org.cafebabe.view.editor.MenuBarView;
import org.cafebabe.view.editor.componentlist.ComponentListView;
import org.cafebabe.view.editor.workspace.WorkspaceView;

/**
 * Handles user interactions with the editor view.
 */
public class EditorController extends Controller implements ISceneController {

    @Getter private final EditorView view;


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

    /* Private */
    private void setupEventListeners() {
        Platform.runLater(() -> this.view.getScene().getWindow().setOnCloseRequest(event -> {
            boolean cancelled = !this.saveAllWorkspaces();
            if (cancelled) {
                event.consume();
            }
        }));

        AnchorPane addNewTabButton = this.view.getAddNewTabButton();
        addNewTabButton.setOnMouseClicked(event -> {
            addNewWorkspace();
            event.consume();
        });

        SingleSelectionModel<Tab> model = this.view.getTabsPane().getSelectionModel();
        ReadOnlyIntegerProperty selected = model.selectedIndexProperty();
        selected.addListener((observable, oldValue, newValue) -> selectWorkspace(newValue));

        Platform.runLater(() -> this.view.getScene().getWindow().setOnCloseRequest(event -> {
            boolean cancelled = !this.saveAllWorkspaces();
            if (cancelled) {
                event.consume();
            }
        }));
    }

    private void initMenuBarController(MenuBarController menuBarController) {
        Editor editor = this.view.getEditor();

        menuBarController.getOnSaveCurrentWorkspace().addListener(() -> {
            Workspace workspace = this.view.getCurrentWorkspaceView().getWorkspace();
            saveWorkspace(workspace);
        });

        menuBarController.getOnSaveCurrentWorkspaceAs().addListener((path) -> {
            editor.saveWorkspace(this.view.getCurrentWorkspaceView().getWorkspace(), path);
        });

        menuBarController.getOnLoadWorkspace().addListener((path) -> {
            Workspace workspace = this.view.getEditor().loadWorkspace(path);
            this.addWorkspace(workspace);
        });

        menuBarController.getOnQuit().addListener(() -> {
            this.saveAllWorkspaces();
            Platform.exit();
        });
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

        newTab.setOnCloseRequest(event -> {
            boolean cancelled = !closeWorkspace(newWorkspace);
            if (cancelled) {
                event.consume();
            }
        });
    }

    private void removeWorkspace(WorkspaceView workspaceView) {
        boolean wasTabSelected = this.view.getWorkspaceTab(workspaceView).isSelected();
        int workspacePosition = this.view.getWorkspaceViews().indexOf(workspaceView);
        this.view.removeWorkspace(workspaceView);

        if (this.view.getWorkspaceViews().isEmpty()) {
            addNewWorkspace();
        }

        if (wasTabSelected) {
            selectWorkspace(workspacePosition);
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

    private boolean saveAllWorkspaces() {
        List<WorkspaceView> workspaceViews = new ArrayList<>(this.view.getWorkspaceViews());
        for (WorkspaceView workspaceView : workspaceViews) {
            boolean wasCanceled = !closeWorkspace(workspaceView);
            if (wasCanceled) {
                return false;
            }
        }
        return true;
    }

    private void saveWorkspace(Workspace workspace) {
        Editor editor = this.view.getEditor();
        if (workspace.isSaved()) {
            editor.saveWorkspace(this.view.getCurrentWorkspaceView().getWorkspace());
        } else {
            File file = FileDialogueHelper.saveWorkspace(this.view.getScene().getWindow());

            if (!Objects.isNull(file)) {
                String path = file.getPath();
                editor.saveWorkspace(this.view.getCurrentWorkspaceView().getWorkspace(), path);
            }
        }
    }

    private boolean closeWorkspace(WorkspaceView workspaceView) {
        Workspace workspace = workspaceView.getWorkspace();

        if (workspace.isSaved()) {
            saveWorkspace(workspace);
        } else if (!workspace.isEmpty() && !workspace.isSaved()) {
            Optional<ButtonType> option = FileDialogueHelper.showUnsavedChangesAlert();

            String response = option.get().getText();
            switch (response) {
                case "Save":
                    saveWorkspace(workspace);
                    break;
                case "Cancel":
                    return false;
                default:
            }
        }

        removeWorkspace(workspaceView);
        return true;
    }

}
