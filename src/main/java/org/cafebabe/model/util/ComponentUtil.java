package org.cafebabe.model.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cafebabe.controllers.util.SvgUtil;
import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.ComponentConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

public final class ComponentUtil {

    private ComponentUtil() {}

    /* Public */
    public static Component componentFactory(String displayName) {
        if (displayName == null || displayName.isEmpty()) {
            throw new RuntimeException("Component display name can not be null or empty");
        }

        Map componentMap = getComponentMap();

        if (!componentMap.containsKey(displayName)) {
            throw new RuntimeException("No such component " + displayName);
        }

        Component original = getComponentMap().get(displayName);

        try {
            Constructor ctor = original.getClass().getConstructor();
            Component instance = (Component) ctor.newInstance();
            instance.initPorts(SvgUtil.getComponentMetadata(instance));
            return instance;
        } catch (IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Component> getAllComponents() {
        Set<Constructor> constructors =
                new Reflections("org.cafebabe.model.components", new MethodAnnotationsScanner())
                .getConstructorsAnnotatedWith(ComponentConstructor.class);

        List<Component> components = new ArrayList<>();
        for (Constructor constructor : constructors) {
            try {
                if (Component.class.isAssignableFrom(constructor.getDeclaringClass())) {
                    components.add((Component) constructor.newInstance());
                }
            } catch (IllegalAccessException | InstantiationException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return components;
    }

    /* Private */
    private static Map<String, Component> getComponentMap() {
        Map<String, Component> map = new HashMap<>();

        for (Component c : getAllComponents()) {
            map.put(c.getDisplayName(), c);
        }

        return map;
    }
}
