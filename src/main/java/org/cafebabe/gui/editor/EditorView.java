package org.cafebabe.gui.editor;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.cafebabe.gui.editor.componentlist.ComponentListController;
import org.cafebabe.gui.editor.workspace.WorkspaceController;
import org.cafebabe.gui.util.FxmlUtil;
import org.cafebabe.util.EmptyEvent;
import org.cafebabe.util.Event;


/**
 * The editor visuals.
 */
class EditorView extends AnchorPane {

    @FXML private AnchorPane sidebarAnchorPane;
    @FXML private AnchorPane workspacesPane;
    @FXML private TabPane tabsPane;
    @FXML private AnchorPane addNewWorkspaceButton;

    final EmptyEvent onNewWorkspaceButton = new EmptyEvent();
    final Event<Number> onSelectTab = new Event<>();

    private int workspaceCounter;


    EditorView() {
        FxmlUtil.attachFxml(this, "/view/EditorView.fxml");

        initializeSidebar();
        initializeWorkspace();
        initializeTabPane();
    }


    /* Package-Private */
    void showWorkspace(WorkspaceController workspaceController) {
        this.workspacesPane.getChildren().clear();
        this.workspacesPane.getChildren().add(workspaceController.getView());
    }

    void removeWorkspace(WorkspaceController workspaceController) {
        this.workspacesPane.getChildren().remove(workspaceController.getView());
    }

    Tab addWorkspaceTab() {
        this.workspaceCounter++;
        Tab tab = new Tab();
        tab.setText("Workspace " + this.workspaceCounter);
        tab.setClosable(true);
        tab.setOnClosed(javafx.event.Event::consume);
        this.tabsPane.getTabs().add(tab);
        this.tabsPane.getSelectionModel().select(tab);
        return tab;
    }


    /* Private */
    private void initializeSidebar() {
        this.sidebarAnchorPane.getChildren().add(new ComponentListController().getView());
    }

    private void initializeWorkspace() {
        this.workspacesPane.requestLayout();
    }

    private void initializeTabPane() {
        SingleSelectionModel<Tab> model = this.tabsPane.getSelectionModel();
        ReadOnlyIntegerProperty selected = model.selectedIndexProperty();

        selected.addListener((observable, oldValue, newValue) -> {
            this.onSelectTab.notifyListeners(newValue);
        });

        this.addNewWorkspaceButton.setOnMouseClicked(event -> {
            event.consume();
            this.onNewWorkspaceButton.notifyListeners();
        });
    }


}
