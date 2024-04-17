package com.sikar.services;

public class Employee {
    private final String name;
    private final Integer age;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Employee(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
