package com.example.najvecaLolcina.security;


import com.example.najvecaLolcina.entity.MyyyUser;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public MyyyUser register(@Valid @RequestBody MyyyUser myyyUser){
        return userService.reg(myyyUser);
    }


    @PostMapping("/login")
    public String login(@Valid @RequestBody MyyyUser myyyUser){
        return userService.returnToken(myyyUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public void setUserAdmin(@PathVariable Long id){
         userService.setAdmin(id);
    }




}
