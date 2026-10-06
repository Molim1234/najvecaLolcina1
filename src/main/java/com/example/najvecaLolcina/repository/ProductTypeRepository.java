package com.example.najvecaLolcina.repository;

import com.example.najvecaLolcina.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

    Optional<ProductType> findProductTypeByType(String type);

}
