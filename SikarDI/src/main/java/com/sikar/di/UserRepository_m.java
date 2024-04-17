package com.sikar.di;

import com.sikar.di.annotations.Component_m;
import com.sikar.di.annotations.Repository_m;

// Example UserRepository class
@Repository_m
public class UserRepository_m {
    public void save(String username) {
        System.out.println("Saving user: " + username);
    }
}
