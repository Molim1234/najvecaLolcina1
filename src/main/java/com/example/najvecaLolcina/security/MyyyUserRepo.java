package com.example.najvecaLolcina.security;

import com.example.najvecaLolcina.entity.MyyyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyyyUserRepo extends JpaRepository<MyyyUser,Long> {

    public MyyyUser findMyyyUserByName(String name);

}
