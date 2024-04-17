package com.sikar.di;

import com.sikar.di.annotations.Autowired_m;
import com.sikar.di.annotations.GetMapping_m;
import com.sikar.di.annotations.RequestMapping_m;
import com.sikar.di.annotations.RestController_m;

@RestController_m
@RequestMapping_m("/di")
public class UserController_m {

    // will not work as RestController registration is done by actual beanFactory
    @Autowired_m
    UserService_m userServiceM;

    @GetMapping_m("/user")
    public String userName() {
        return userServiceM.getUser();
    }

}
