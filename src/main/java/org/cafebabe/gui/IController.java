package org.cafebabe.gui;

import javafx.scene.Node;

public interface IController {
    Node getView();
    void destroy();
}
