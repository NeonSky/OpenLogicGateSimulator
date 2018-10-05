package org.cafebabe.viewmodel;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

/**
    This class makes it possible to pan and zoom on the workspace.
    It keeps track of all "Transformables" (Component- and Wire controllers)
    And updates their FXML transforms when the camera is moved.
*/
public class Camera {
    private final Set<ITransformable> transformables = new HashSet<>();
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

    public Transform getInverseTransform() {
        try {
            return this.cameraTransform.createInverse();
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
            return Transform.scale(1,1);
        }
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

    public void addTransformable(ITransformable transformable) {
        this.transformables.add(transformable);
        updateTransformables();
    }

    public Transform getTransform() {
        return this.cameraTransform;
    }

    /* Private */

    private void applyTransform(Transform transform) {
        this.cameraTransform = this.cameraTransform.createConcatenation(transform);
        updateTransformables();
    }

    private void updateTransformables() {
        this.transformables.forEach(t -> t.setTransform(this.cameraTransform));
    }
}
