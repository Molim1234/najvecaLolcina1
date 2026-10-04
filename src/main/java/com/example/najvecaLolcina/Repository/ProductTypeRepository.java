package com.example.najvecaLolcina.Repository;

import com.example.najvecaLolcina.Entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {

    Optional<ProductType> findProductTypeByType(String type);

}
