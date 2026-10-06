package com.example.najvecaLolcina.repository;

import com.example.najvecaLolcina.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.productType.type = :type")
    Page<Product> findByType(@Param("type") String type, Pageable pageable);

    @Query("select p from Product p where p.name=:name")
    public Optional<Product> findWithSameName(String name);

}
