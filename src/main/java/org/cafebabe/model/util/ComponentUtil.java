package org.cafebabe.model.util;

import org.cafebabe.model.components.Component;
import org.cafebabe.model.components.ComponentConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
}
