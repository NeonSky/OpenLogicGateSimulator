package org.cafebabe.view.editor;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import lombok.Getter;
import org.cafebabe.view.View;
import org.cafebabe.view.util.FxmlUtil;

/**
 * This is the menu bar of the editor, which provides some basic actions.
 */
public class MenuBarView extends View {

    @Getter @FXML private MenuItem openMenuItem;
    @Getter @FXML private MenuItem saveMenuItem;
    @Getter @FXML private MenuItem saveAsMenuItem;
    @Getter @FXML private MenuItem quitMenuItem;

    public MenuBarView() {
        FxmlUtil.attachFxml(this, "/view/MenuBarView.fxml");
    }
}
