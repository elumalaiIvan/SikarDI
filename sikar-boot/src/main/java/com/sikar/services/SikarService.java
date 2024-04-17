package com.sikar.services;

import com.sikar.di.annotations.Service_m;

@Service_m
public class SikarService {
    public Employee[] getSikarEmployees() {
        return new Employee[] { new Employee("elumalai",20), new Employee("kiruma", 23)};
    }
}
