package org.cafebabe.model.util;

import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.ComponentConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ComponentUtil {
    public static List<Component> getAllComponents() {
        Set<Constructor> constructors = new Reflections("org.cafebabe.model.components", new MethodAnnotationsScanner())
                .getConstructorsAnnotatedWith(ComponentConstructor.class);

        List<Component> components = new ArrayList<>();
        for(Constructor constructor: constructors) {
            try {
                if(Component.class.isAssignableFrom(constructor.getDeclaringClass())) {
                    components.add((Component) constructor.newInstance());
                }
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return components;
    }

    private static Map<String, Component> getComponentMap() {
        Map<String, Component> map = new HashMap<>();

        for (Component c : getAllComponents()) {
            map.put(c.getDisplayName(), c);
        }

        return map;
    }

    public static Component componentFactory(String displayName) {
        Map componentMap = getComponentMap();

        if (displayName == null || displayName.isEmpty()) {
            throw new RuntimeException("Component display name can not be null or empty");
        }

        if (!componentMap.containsKey(displayName)) {
            throw new RuntimeException("No such component " + displayName);
        }

        Component original = getComponentMap().get(displayName);

        try {
            Constructor ctor = original.getClass().getConstructor();
            return (Component)ctor.newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
