package com.sikar.controllers;

import com.sikar.di.annotations.GetMapping_m;
import com.sikar.di.annotations.RequestMapping_m;
import com.sikar.di.annotations.RestController_m;

@RestController_m
public class SikarController {

    @GetMapping_m("/hello")
    public String hello() {
        return "Hello World";
    }
}
