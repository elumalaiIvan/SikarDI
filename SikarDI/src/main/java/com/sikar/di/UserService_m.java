package com.sikar.di;

import com.sikar.di.annotations.Component_m;

@Component_m
public class UserService_m {
    private final UserRepository_m userRepository;

    public UserService_m(UserRepository_m userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(String username) {
        userRepository.save(username);
    }

    public String getUser() {
        return "Elumalai";
    }
}

