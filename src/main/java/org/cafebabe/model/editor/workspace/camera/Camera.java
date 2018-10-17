package org.cafebabe.model.editor.workspace.camera;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/**
    This class makes it possible to pan and zoom on the workspace.
    It keeps track of all transforms and updates them when this
    camera is moved.
    This makes it possible for visual representations to only show
    a certain portion of the workspace at a time.
*/
public class Camera {
    private final Set<IHaveTransform> transforms = new HashSet<>();
    private Transform cameraTransform = Transform.scale(1, 1);

    /* Public */

    public void pan(double x, double y) {
        Point2D deltaPos = Point2D.ZERO;
        try {
            deltaPos = this.cameraTransform.inverseDeltaTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
        applyTransform(Transform.translate(deltaPos.getX(), deltaPos.getY()));
    }

    public void zoom(double scaleFactor, double pivotX, double pivotY) {
        Point2D pivotPoint = getInverseTransform().transform(pivotX, pivotY);

        applyTransform(
                Transform.scale(
                        scaleFactor,
                        scaleFactor,
                        pivotPoint.getX(),
                        pivotPoint.getY()
                )
        );
    }

    public void addTransform(IHaveTransform transform) {
        this.transforms.add(transform);
        updateTransforms();
    }

    public Transform getTransform() {
        return this.cameraTransform;
    }

    public Transform getInverseTransform() {
        try {
            return this.cameraTransform.createInverse();
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
            return Transform.scale(1,1);
        }
    }

    /* Private */

    private void applyTransform(Transform transform) {
        this.cameraTransform = this.cameraTransform.createConcatenation(transform);
        updateTransforms();
    }

    private void updateTransforms() {
        this.transforms.forEach(t -> t.setTransform(this.cameraTransform));
    }
}
