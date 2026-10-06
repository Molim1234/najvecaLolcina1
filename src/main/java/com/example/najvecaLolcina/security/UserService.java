package com.example.najvecaLolcina.security;


import com.example.najvecaLolcina.entity.MyyyUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private MyyyUserRepo myyyUserRepo;
    private JWtServicee JWTServicee;
    private AuthenticationManager manager;


    public UserService(MyyyUserRepo myyyUserRepo, JWtServicee JWTServicee, AuthenticationManager manager) {
        this.myyyUserRepo = myyyUserRepo;
        this.JWTServicee = JWTServicee;
        this.manager = manager;
    }

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);


    public MyyyUser reg(MyyyUser myyyUser) {
        myyyUser.setRole(role.USER);
        myyyUser.setPassword(encoder.encode(myyyUser.getPassword()));
        return myyyUserRepo.save(myyyUser);
    }

    public String returnToken(MyyyUser myyyUser) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(myyyUser.getName(), myyyUser.getPassword()));

        if(!authentication.isAuthenticated()){
            throw new NoSuchElementException();
        }
        return JWTServicee.createToken(myyyUser.getName());


    }

    public void setAdmin(Long id) {
        var user = myyyUserRepo.findById(id).orElseThrow(()-> new NoSuchElementException("No element"));
        user.setRole(role.ADMIN);
        myyyUserRepo.save(user);
    }

}
