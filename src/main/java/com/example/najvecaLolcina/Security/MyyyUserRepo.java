package com.example.najvecaLolcina.Security;

import com.example.najvecaLolcina.Entity.MyyyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyyyUserRepo extends JpaRepository<MyyyUser,Long> {

    public MyyyUser findMyyyUserByName(String name);

}
