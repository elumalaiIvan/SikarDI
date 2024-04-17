package com.sikar.di.bean;


import com.sikar.di.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Bean Factory
public class BeanFactory_m {
    private static BeanFactory_m beanFactoryM;

    public static BeanFactory_m beanFactoryInstance() {
        if (beanFactoryM == null) {
            beanFactoryM = new BeanFactory_m();
        }
        return beanFactoryM;
    }

    private void BeanDefinition_m() { }
    public static Class[] injectionCapableAnnotations = {
            Component_m.class,
            Configuration_m.class,
            Repository_m.class,
            RestController_m.class,
            Service_m.class
    };

    private final Map<String, Object> singletonBeans;

    // store the injectable beans definitions
    private final Map<String, BeanDefinition_m> beanDefinitions;


    public BeanFactory_m() {
        this.singletonBeans = new HashMap<>();
        this.beanDefinitions = new HashMap<>();
    }

    public Map<String, BeanDefinition_m> getBeanDefinitions() {
        return new HashMap<>(beanDefinitions);
    }

    public Map<String, Object> getBeanInstances() {
        return new HashMap<>(singletonBeans);
    }

    public void registerBeanDefinition(BeanDefinition_m beanDefinitionM) {
        var beanName = beanDefinitionM.getBeanClass().getSimpleName();
        // if precedence given, we should alter this should update to true
        var shouldUpdate = false;
        if (shouldUpdate || !beanDefinitions.containsKey(beanName)) {
            System.out.println("bean registered with the key: " + beanDefinitionM.getBeanClass().getSimpleName());
            beanDefinitions.put(beanName, beanDefinitionM);
        }
        Object instance;
        try {
            instance = getBean(beanDefinitionM.getBeanClass());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setAutowiredFields(instance, beanDefinitionM);
        registerBeansFromTheMethodsOfTheBean(instance, beanDefinitionM);
    }

    public <T> T getBean(Class<T> beanClass)  {
        var beanName = beanClass.getSimpleName();

        // dependency not resolved exception
        if (!beanDefinitions.containsKey(beanName)) {
            if (!isInjectionCapable(beanClass))
                throw new IllegalArgumentException("Bean '" + beanName + "' not found");
            // so we don't wait for the order of bean registration
            registerBeanDefinition(new BeanDefinition_m(beanClass));
        }

        BeanDefinition_m beanDefinitionM = beanDefinitions.get(beanName);

        // Check if the bean is singleton
        //Assume all beans are singletons, we can extend this to support scope logics
        if (singletonBeans.containsKey(beanName)) {
            return (T) singletonBeans.get(beanName);
        }

        // Create bean instance
        Constructor<?> constructor = beanDefinitionM.getBeanClass().getDeclaredConstructors()[0];
        Object[] args = constructor.getParameterTypes().length > 0 ? resolveDependencies(constructor) : null;
        T bean = null;
        try {
            bean = (T) constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // Add bean to singleton beans map if it's singleton
        if (isSingleton(beanDefinitionM)) {
            singletonBeans.put(beanName, bean);
        }

        return bean;
    }

    private void setAutowiredFields(Object instance, BeanDefinition_m beanDefinitionM) {
        beanDefinitionM.getAutowiredFields().forEach(field -> {
                System.out.println("autowired field value is set: " + field.getName());
                field.setAccessible(true);
            try {
                field.set(instance, getBean(field.getType()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("cant set the bean to autowired variable",e);
            }
        });
    }

    private void registerBeansFromTheMethodsOfTheBean(Object instance, BeanDefinition_m beanDefinitionM) {
        try {
            beanDefinitionM.beanMethods().stream()
                    .filter(method -> method.isAnnotationPresent(Bean_m.class))
                    .forEach(method -> {
                        var args = resolveDependencies(method);
                        try {
                           var returndObject = method.invoke(instance, args);
                           //@Order will be considered while getting object our of singleton
                           singletonBeans.put(method.getReturnType().getSimpleName(), returndObject);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            // ApplicationFactory is implementation of beanFactory
                            // Arguments are not registered with applicationContext/beanFactory exception
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> boolean isInjectionCapable(Class<T> clazz) {
        return Arrays.stream(injectionCapableAnnotations)
                .anyMatch(clazz::isAnnotationPresent);
    }



    private boolean isSingleton(BeanDefinition_m beanDefinitionM) {
        return true;
    }

    private Object[] resolveDependencies(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .map(this::getBeansSafely)
                .toArray();
    }
    private Object[] resolveDependencies(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(this::getBeansSafely)
                .toArray();
    }

    private Object getBeansSafely(Class<?> paramType) {
        return getBean(paramType);
    }

}
