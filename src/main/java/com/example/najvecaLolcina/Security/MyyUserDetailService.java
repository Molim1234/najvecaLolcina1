package com.example.najvecaLolcina.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MyyUserDetailService implements UserDetailsService {

    @Autowired
    private MyyyUserRepo myyyUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = myyyUserRepo.findMyyyUserByName(username);

        if(user==null){
            throw new NoSuchElementException("No element");
        }

        return new MyyyUserDetail(user);
    }
}
