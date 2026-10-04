package com.example.najvecaLolcina.Mapper;

import com.example.najvecaLolcina.Entity.Product;
import com.example.najvecaLolcina.Entity.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toProductDTO(Product product){
        return new ProductDTO(product.getName(), product.getProductType().getType(), product.getPrice(), product.getDescription());
    }


}
