package org.cafebabe.view.editor.workspace.circuit.component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;
import javafx.util.Pair;
import lombok.Getter;
import net.javainthebox.caraibe.svg.SvgContent;
import org.cafebabe.controller.editor.workspace.circuit.selection.ComponentDragDropHandler;
import org.cafebabe.controller.editor.workspace.circuit.selection.ISelectable;
import org.cafebabe.model.editor.util.SvgUtil;
import org.cafebabe.model.editor.workspace.camera.IHaveTransform;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.editor.workspace.circuit.component.ComponentData;
import org.cafebabe.model.editor.workspace.circuit.component.connection.InputPort;
import org.cafebabe.model.editor.workspace.circuit.component.connection.OutputPort;
import org.cafebabe.model.editor.workspace.circuit.component.position.Position;
import org.cafebabe.model.util.EmptyEvent;
import org.cafebabe.view.View;
import org.cafebabe.view.editor.workspace.circuit.component.port.InPortView;
import org.cafebabe.view.editor.workspace.circuit.component.port.OutPortView;
import org.cafebabe.view.editor.workspace.circuit.component.port.PortView;
import org.cafebabe.view.util.ColorUtil;
import org.cafebabe.view.util.FxmlUtil;

/**
 * Provides a visual representation of a component with its ports.
 */
@SuppressWarnings({"PMD.TooManyMethods", "PMD.ExcessiveImports"})
public class ComponentView extends View implements IHaveTransform, ISelectable {

    public final ComponentDragDropHandler componentDragDropHandler;

    @FXML private Group componentSvgContainer;
    @FXML private Group svgGroup;

    @Getter private final Component component;
    @Getter private final List<PortView> portViews = new ArrayList<>();
    private final EmptyEvent onUpdateStyle = new EmptyEvent();
    private boolean isSelected;
    private Transform transform = Transform.scale(1, 1);
    private static final String[] CSS_KEYWORDS = new String[]{
            "visible-if-",
            "visible-unless-",
            "trigger-"
    };


    @SuppressFBWarnings(value = "UR_UNINIT_READ",
            justification = "SpotBugs believes @FXML fields are always null")
    public ComponentView(
            Component component,
            ComponentDragDropHandler componentDragDropHandler) {

        FxmlUtil.attachFxml(this, "/view/ComponentView.fxml");

        this.component = component;
        this.componentDragDropHandler = componentDragDropHandler;

        this.svgGroup.getChildren().addAll(this.portViews);
        this.svgGroup.setPickOnBounds(false);

        SvgContent svg = SvgUtil.loadComponentSvg(component);
        this.componentSvgContainer.getChildren().setAll(svg);
        svg.setPickOnBounds(false);

        svg.selectNodesWithClasses("component-body").forEach(
                ComponentView::setDefaultStyle
        );

        initTransforms();
        initComponentMethods();
        updateVisualState();
        updatePosition(this.component.getTrackablePosition());
    }

    /* Public */
    @Override
    public void setTransform(Transform transform) {
        this.transform = transform;
        updateTransform();
    }

    public void addPortsFromMetadata(ComponentData componentMetadata,
                                     Component component) {
        componentMetadata.inPortMetadata.forEach(m -> {
            InPortView view = new InPortView(
                    (InputPort) component.getPort(m.name),
                    m.x,
                    m.y);
            this.portViews.add(view);
            addSubview(this.svgGroup, view);
        });

        componentMetadata.outPortMetadata.forEach(m -> {
            OutPortView view = new OutPortView(
                    (OutputPort) component.getPort(m.name),
                    m.x,
                    m.y);
            this.portViews.add(view);
            addSubview(this.svgGroup, view);
        });
    }

    @Override
    public void select() {
        setSelected(true);
        updateVisualState();
    }

    @Override
    public void deselect() {
        setSelected(false);
        updateVisualState();
    }

    public void updatePosition(Position position) {
        this.setTranslateX(position.getX());
        this.setTranslateY(position.getY());
        this.updateTransform();
    }

    public SvgContent getComponentSvg() {
        return (SvgContent) this.componentSvgContainer.getChildren().get(0);
    }

    public boolean isIntersecting(Bounds bounds) {
        Bounds localBounds = this.parentToLocal(bounds);
        Bounds groupBounds = this.svgGroup.parentToLocal(localBounds);
        Bounds containerBounds = this.componentSvgContainer.parentToLocal(groupBounds);
        return getComponentSvg().intersects(containerBounds);
    }

    public void highlightInputPorts() {
        this.portViews.forEach((p) ->
                p.setHighlighted(p.getPort() instanceof InputPort)
        );
    }

    public void highlightOutputPorts() {
        this.portViews.forEach((p) ->
                p.setHighlighted(p.getPort() instanceof OutputPort)
        );
    }

    public void unhighlightPorts() {
        this.portViews.forEach((p) -> p.setHighlighted(false));
    }

    /* Private */

    private static void setDefaultStyle(Node n) {
        Shape shape = (Shape) n;
        shape.setStrokeLineCap(StrokeLineCap.SQUARE);
        shape.setStrokeWidth(3);
        shape.setFill(ColorUtil.OFFWHITE);
    }

    private void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void updateVisualState() {
        Color newColor = this.isSelected ? ColorUtil.SELECTED : Color.BLACK;
        SvgContent svg = getComponentSvg();
        svg.selectNodesWithClasses("component-body").forEach(
                n -> ((Shape) n).setStroke(newColor)
        );

        this.onUpdateStyle.notifyListeners();
    }

    private void updateTransform() {
        resetTransforms();
        transformInBase(this.transform, this.getLocalToParentTransform());
    }

    private void transformInBase(Transform transform, Transform base) {
        try {
            this.getTransforms().set(0, base.createInverse());
            this.getTransforms().set(1, transform);
            this.getTransforms().set(2, base);
        } catch (NonInvertibleTransformException e) {
            e.printStackTrace();
        }
    }

    private void initTransforms() {
        for (int i = 0; i < 3; i++) {
            this.getTransforms().add(i, Transform.scale(1, 1));
        }
    }

    private void resetTransforms() {
        for (int i = 0; i < 3; i++) {
            this.getTransforms().set(i, Transform.scale(1, 1));
        }
    }

    private void initComponentMethods() {
        Collection<String> allStyleClasses =
                getComponentSvg().getStyleClassesRecursive();
        for (String cssClass : allStyleClasses) {
            boolean invert;
            if (cssClass.startsWith("visible-if-")) {
                invert = false;
            } else if (cssClass.startsWith("visible-unless-")) {
                invert = true;
            } else {
                continue;
            }
            Collection<Node> targets = getComponentSvg().selectNodesWithClasses(cssClass);
            this.onUpdateStyle.addListener(() -> {
                        Pair<Object, Class> res = null;
                        try {
                            res = cssClassToComponentMethod(cssClass).call();

                            if (res.getValue() != boolean.class) {
                                throw new RuntimeException("Method bound to class \"" + cssClass
                                        + "\" does not return a boolean, actual type: \""
                                        + res.getValue().getName() + "\"");
                            }
                            for (Node target : targets) {
                                target.setVisible(invert == (boolean) res.getKey());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    public Callable<Pair<Object, Class>> cssClassToComponentMethod(String cssClass) {
        for (String keyword: CSS_KEYWORDS) {
            if (cssClass.startsWith(keyword)) {
                String[] methodAndArgs = cssClass.substring(keyword.length()).split("_");
                if (methodAndArgs.length == 1) {
                    return () -> callComponentMethod(methodAndArgs[0]);
                }
                return () -> callComponentMethod(methodAndArgs[0], methodAndArgs[1]);
            }
        }
        return null;
    }

    private Pair<Object, Class> callComponentMethod(String methodName) {
        return callComponentMethod(methodName, null);
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    private Pair<Object, Class> callComponentMethod(String methodName, String argument) {
        List<Method> methods = Arrays.stream(this.component.getClass().getDeclaredMethods()).filter(
                m -> m.getName().equals(methodName)
        ).filter(m -> m.getParameterCount() == ((argument == null) ? 0 : 1)).collect(Collectors
                .toList());
        Method method;
        if (methods.size() > 1) {
            throw new RuntimeException("Ambiguous method! \"" + methodName + "\"");
        } else {
            method = methods.get(0);
        }

        Object res = inferArgumentAndCallMethod(method, argument);
        Class c = method.getReturnType();
        return new Pair<>(res, c);
    }

    private Object inferArgumentAndCallMethod(Method method, String argument) {
        Object res;
        try {
            switch (method.getParameterCount()) {
                case 0:
                    res = method.invoke(this.component);
                    break;
                case 1:
                    Object argObj = parseMethodParameterToExpectedType(method, argument);
                    res = method.invoke(this.component, argObj);
                    break;
                default:
                    throw new RuntimeException("Too many method parameters");
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private Object parseMethodParameterToExpectedType(Method method, String argument) {
        Object argObj;
        switch (method.getParameterTypes()[0].getName()) {
            case "String":
                argObj = argument;
                break;
            case "Float":
                argObj = Float.parseFloat(argument);
                break;
            case "Double":
                argObj = Double.parseDouble(argument);
                break;
            case "Integer":
                argObj = Integer.parseInt(argument);
                break;
            default:
                throw new RuntimeException("Invalid argument type");
        }
        return argObj;
    }
}
