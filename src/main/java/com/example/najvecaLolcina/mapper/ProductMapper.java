package com.example.najvecaLolcina.mapper;

import com.example.najvecaLolcina.entity.Product;
import com.example.najvecaLolcina.entity.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toProductDTO(Product product){
        return new ProductDTO(product.getName(), product.getProductType().getType(), product.getPrice(), product.getDescription());
    }


}
