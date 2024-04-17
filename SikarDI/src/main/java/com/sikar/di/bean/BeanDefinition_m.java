package com.sikar.di.bean;

import com.sikar.di.annotations.Autowired_m;
import com.sikar.di.annotations.Bean_m;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Bean Definition
public class BeanDefinition_m {
    private final Class<?> beanClass;
    private final List<Method> beanMethods;

    private final List<Field> autowiredFields;
    private final Map<String, Object> dependencies;

    public BeanDefinition_m(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.dependencies = new HashMap<>();

        beanMethods = Arrays.stream(beanClass.getMethods())
                .filter(method -> method.isAnnotationPresent(Bean_m.class))
                .toList();

        autowiredFields = Arrays.stream(beanClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired_m.class))
                .toList();
    }

    public List<Field> getAutowiredFields() {
        return autowiredFields;
    }
    public List<Method> beanMethods() {
        return beanMethods;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void addDependency(String fieldName, Object dependency) {
        dependencies.put(fieldName, dependency);
    }

    public Object getDependency(String fieldName) {
        return dependencies.get(fieldName);
    }
}

