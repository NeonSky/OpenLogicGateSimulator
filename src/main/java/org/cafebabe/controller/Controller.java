package org.cafebabe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.cafebabe.model.util.Event;
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

    protected void setSubviewAttachController(
            Class<? extends View> subviewClass,
            Class<? extends Controller> controllerClass,
            Consumer<Controller> callback) {

        this.view.getOnCreatedSubview().addListener((subview) -> {
            if (subview.getClass() == subviewClass) {
                Controller controller = instantiateController(
                        controllerClass, subviewClass, subview);
                callback.accept(controller);
            }
        });
    }

    /* Private */
    private Controller instantiateController(
            Class<? extends Controller> controllerClass,
            Class<? extends View> subviewClass, View subview) {

        Controller newChild = null;
        try {
            newChild = controllerClass.getConstructor(subviewClass).newInstance(subview);
            newChild.onDestroy.addListener(this.children::remove);
            this.children.add(newChild);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return newChild;
    }
}
