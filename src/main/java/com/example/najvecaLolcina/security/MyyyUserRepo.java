package com.example.najvecaLolcina.security;

import com.example.najvecaLolcina.entity.MyyyUser;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MyyyUserRepo extends JpaRepository<MyyyUser,Long> {

    public MyyyUser findMyyyUserByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from MyyyUser u where u.name = :name")
    MyyyUser findUserForUpdate(@Param("name") String name);

}
