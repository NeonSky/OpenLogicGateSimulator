package org.cafebabe.controller;

import java.util.ArrayList;
import java.util.List;

import org.cafebabe.model.editor.util.Event;
import org.cafebabe.view.View;

/**
 * Abstracts away the controller part of the MVC structure.
 * Enables a way to attach controllers to added sub views.
 * This way, we can have a controller per view if we want.
 */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class Controller {

    private final Event<Controller> onDestroy = new Event<>();
    private final View view;
    private final List<Controller> children;
    private boolean destructionPending;


    public Controller(View view) {
        this.view = view;
        this.children = new ArrayList<>();

        view.onDestroy.addListener(this::destroy);
    }

    /* Public */
    public void destroy() {
        if (!this.destructionPending) {
            this.destructionPending = true;
            this.view.destroy();
            this.onDestroy.notifyListeners(this);
        }
    }

    /* Protected */
    protected void setSubviewAttachController(
            Class<? extends View> subviewClass, Class<? extends Controller> controllerClass) {

        this.view.getOnCreatedSubview().addListener((subview) -> {
            if (subview.getClass() == subviewClass) {
                instantiateController(controllerClass, subviewClass, subview);
            }
        });
    }

    /* Private */
    private void instantiateController(
            Class<? extends Controller> controllerClass,
            Class<? extends View> subviewClass, View subview) {

        try {
            Controller newChild = controllerClass.getConstructor(subviewClass).newInstance(subview);
            newChild.onDestroy.addListener(this.children::remove);
            this.children.add(newChild);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }
}
