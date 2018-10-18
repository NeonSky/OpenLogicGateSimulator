package org.cafebabe.view;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Getter;
import org.cafebabe.model.util.EmptyEvent;
import org.cafebabe.model.util.Event;
import org.cafebabe.view.util.FxmlUtil;

/**
 * Has defaults that handle notifying listeners, commonly
 * controllers, that a subview has been added. Controllers
 * may be interested in attaching sub-controllers to these
 * views.
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class View extends AnchorPane {

    @Getter private final EmptyEvent onDestroy = new EmptyEvent();
    @Getter protected final Event<View> onCreatedSubview = new Event<>();
    @Getter private boolean destructionPending;


    /* Public */

    /* JavaFX isn't really built to separate view/controller
    * This function should be called when the controller, if any,
    * has been instantiated and is ready to start listening to the view. */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
    public void init() {}

    // Since it is a JavaFX application, this default is handy.
    public void destroy() {
        if (!this.destructionPending) {
            this.destructionPending = true;
            this.onDestroy.notifyListeners();
            FxmlUtil.destroy(this);
        }
    }

    /* Protected */
    protected void addSubview(Pane subviewParent, View subview) {
        subviewParent.getChildren().add(subview);
        getOnCreatedSubview().notifyListeners(subview);
    }

    protected void addSubview(Group subviewParent, View subview) {
        subviewParent.getChildren().add(subview);
        getOnCreatedSubview().notifyListeners(subview);
    }

    protected void addSubview(View subview) {
        addSubview(this, subview);
    }

    protected void addSubviews(Pane subviewParent, List<View> subviews) {
        subviewParent.getChildren().addAll(subviews);
        subviews.forEach(getOnCreatedSubview()::notifyListeners);
    }
}
