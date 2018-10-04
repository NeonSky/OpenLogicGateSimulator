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
