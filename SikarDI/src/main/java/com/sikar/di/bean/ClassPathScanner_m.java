package com.sikar.di.bean;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ClassPathScanner_m {
    public static Set<Class<?>> scan() {
        String currentPackage = ClassPathScanner_m.class.getPackage().getName();
        return scan(currentPackage);
    }

    public static Set<Class<?>> scan(String basePackage) {
        Set<Class<?>> classes = new HashSet<>();
        String basePath = basePackage.replace('.', File.separatorChar);
        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

        for (String classpathEntry : classpathEntries) {
            File baseDir = new File(classpathEntry + File.separator + basePath);
            if (baseDir.exists() && baseDir.isDirectory()) {
                findClasses(baseDir, basePackage, classes);
            }
        }
        return classes;
    }

    private static void findClasses(File directory, String packageName, Set<Class<?>> classes) {
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                findClasses(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
