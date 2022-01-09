package eu.builderscoffee.expresso.utils;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class ReflectionUtils {

    public static <T> List<Class<? extends T>> reflect(Package packageObj, Class<? extends T> clazz) {
        val reflections = new Reflections(packageObj.getName());
        return reflections.getSubTypesOf(clazz).stream().collect(Collectors.toList());
    }

    public static <T> List<? extends T> reflectInstances(Package packageObj, Class<? extends T> clazz) {
        return reflect(packageObj, clazz).stream()
                .filter(loopClazz -> !Modifier.isAbstract(loopClazz.getModifiers()))
                .map(loopClazz -> {
                    try {
                        return loopClazz.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
