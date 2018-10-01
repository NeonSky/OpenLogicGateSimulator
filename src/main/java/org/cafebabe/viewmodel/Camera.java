package org.cafebabe.viewmodel;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.transform.Transform;

public class Camera {
    private final Set<ITransformable> transformables = new HashSet<>();
    private Transform cameraTransform = Transform.scale(1, 1);

    public void pan(double x, double y) {
        applyTransform(Transform.translate(x, y));
    }

    public void zoom(double scaleFactor) {
        applyTransform(Transform.scale(scaleFactor, scaleFactor));
    }

    public void rotate(double angle, double pivotX, double pivotY) {
        applyTransform(Transform.rotate(angle, pivotX, pivotY));
    }

    public void addTransformable(ITransformable transformable) {
        this.transformables.add(transformable);
    }

    private void applyTransform(Transform transform) {
        this.cameraTransform = this.cameraTransform.createConcatenation(transform);
        updateTransformables();
    }

    private void updateTransformables() {
        this.transformables.forEach(t -> t.setTransform(this.cameraTransform));
    }
}
