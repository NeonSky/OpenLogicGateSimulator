package org.cafebabe.viewmodel;

import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Point2D;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

public class Camera {
    private final Set<ITransformable> transformables = new HashSet<>();
    private Transform cameraTransform = Transform.scale(1, 1);

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
        Point2D pivotPoint = Point2D.ZERO;
        try {
            pivotPoint = this.cameraTransform.inverseTransform(pivotX, pivotY);
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
        applyTransform(
                Transform.scale(
                        scaleFactor,
                        scaleFactor,
                        pivotPoint.getX(),
                        pivotPoint.getY()
                )
        );
    }

    public void rotate(double angle, double pivotX, double pivotY) {
        applyTransform(Transform.rotate(angle, pivotX, pivotY));
    }

    public void addTransformable(ITransformable transformable) {
        this.transformables.add(transformable);
        updateTransformables();
    }

    public Transform getTransform() {
        return this.cameraTransform;
    }

    private void applyTransform(Transform transform) {
        this.cameraTransform = this.cameraTransform.createConcatenation(transform);
        updateTransformables();
    }

    private void updateTransformables() {
        this.transformables.forEach(t -> t.setTransform(this.cameraTransform));
    }
}
