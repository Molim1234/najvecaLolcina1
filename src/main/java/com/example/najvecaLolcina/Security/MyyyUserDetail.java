package com.example.najvecaLolcina.Security;

import com.example.najvecaLolcina.Entity.MyyyUser;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class MyyyUserDetail implements UserDetails {

    private MyyyUser myyyUser;

    public MyyyUserDetail(MyyyUser myyyUser) {
        this.myyyUser = myyyUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+myyyUser.getRole().name()));
    }

    @Override
    public @Nullable String getPassword() {
        return myyyUser.getPassword();
    }

    @Override
    public String getUsername() {
        return myyyUser.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
