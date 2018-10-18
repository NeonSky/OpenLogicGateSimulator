package org.cafebabe.view.editor.workspace.circuit.component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import javafx.scene.Node;
import javafx.util.Pair;
import net.javainthebox.caraibe.svg.SvgContent;
import org.cafebabe.model.editor.workspace.circuit.component.Component;
import org.cafebabe.model.util.EmptyEvent;

/**
 * This class makes it possible to have special css classes in the SVG files for components.
 * These css classes can, through their name, refer to different methods in the component class.
 * Examples:
 * "visible-if-foo" Would look for a method  with the signature "boolean foo();
 * And the parts of the component with that class would only be visible if that method returned
 * true.
 * "visible-unless-foo" is the same, but inverts the response.
 * "trigger-foo" simply calls "sometype foo();" and discards the response.
 */
public final class CssToComponentMethodReflector {
    private static final String[] CSS_KEYWORDS = new String[]{
            "visible-if-",
            "visible-unless-",
            "trigger-"
    };
    private CssToComponentMethodReflector(){}

    static void initComponentMethods(SvgContent componentSvg, EmptyEvent onUpdateStyle, Component
            component) {
        Collection<String> allStyleClasses = componentSvg.getStyleClassesRecursive();
        for (String cssClass : allStyleClasses) {
            if (cssClass.startsWith("visible-if-") || cssClass.startsWith("visible-unless-")) {
                Collection<Node> targets = componentSvg.selectNodesWithClasses(cssClass);
                onUpdateStyle.addListener(createSvgStyleModifier(cssClass, targets, component));
            }
        }
    }

    private static Runnable createSvgStyleModifier(String cssClass, Collection<Node> targets,
                                                   Component
                                                           component) {
        boolean invert = cssClass.startsWith("visible-unless-");
        Callable<Pair<Object, Class>> getIsVisible = cssClassToComponentMethod(cssClass, component);
        return () -> {
            try {
                Pair<Object, Class> res = getIsVisible.call();

                if (res.getValue() != boolean.class) {
                    throw new RuntimeException("Method bound to class \"" + cssClass
                            + "\" does not return a boolean, actual type: \""
                            + res.getValue().getName() + "\"");
                }
                for (Node target : targets) {
                    target.setVisible(invert != (boolean) res.getKey());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    public static Callable<Pair<Object, Class>> cssClassToComponentMethod(String cssClass,
                                                                          Component component) {
        for (String keyword : CSS_KEYWORDS) {
            if (cssClass.startsWith(keyword)) {
                String[] methodAndArgs = cssClass.substring(keyword.length()).split("_");
                if (methodAndArgs.length == 1) {
                    return () -> callComponentMethod(methodAndArgs[0], component);
                }
                return () -> callComponentMethod(methodAndArgs[0], methodAndArgs[1], component);
            }
        }
        return null;
    }

    private static Pair<Object, Class> callComponentMethod(String methodName, Component component) {
        return callComponentMethod(methodName, null, component);
    }

    @SuppressWarnings({"PMD.AvoidLiteralsInIfCondition", "PMD.BrokenNullCheck"})
    private static Pair<Object, Class> callComponentMethod(String methodName, String argument,
                                                           Component
                                                                   component) {
        List<Method> methods = new ArrayList<>();
        for (Method m : component.getClass().getDeclaredMethods()) {
            if (Objects.equals(m.getName(), methodName)
                    && m.getParameterCount()
                    == (argument == null ? 0 : 1)) {
                methods.add(m);
            }
        }
        Method method;
        if (methods.size() > 1) {
            throw new RuntimeException("Ambiguous method! \"" + methodName + "\"");
        } else {
            method = methods.get(0);
        }

        Object res = inferArgumentAndCallMethod(method, argument, component);
        Class c = method.getReturnType();
        return new Pair<>(res, c);
    }

    private static Object inferArgumentAndCallMethod(Method method, String argument, Component
            component) {
        Object res;
        try {
            switch (method.getParameterCount()) {
                case 0:
                    res = method.invoke(component);
                    break;
                case 1:
                    Object argObj = parseMethodParameterToExpectedType(method, argument);
                    res = method.invoke(component, argObj);
                    break;
                default:
                    throw new RuntimeException("Too many method parameters");
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private static Object parseMethodParameterToExpectedType(Method method, String argument) {
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