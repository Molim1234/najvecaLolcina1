package com.example.najvecaLolcina;

import com.example.najvecaLolcina.entity.Product;
import com.example.najvecaLolcina.entity.ProductDTO;
import com.example.najvecaLolcina.mapper.ProductMapper;
import com.example.najvecaLolcina.repository.ProductRepository;
import com.example.najvecaLolcina.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
@Mock
private ProductRepository productRepository;

@Mock
private ProductMapper productMapper;

@InjectMocks
private ProductService productService;

@Test
void findProductTest(){

    var product = new Product("lol", 10, new BigDecimal(10), "lol");
    product.setId(1L);
    var productDTO = new ProductDTO("lol", "Mouse", new BigDecimal(10), "lol");

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(productMapper.toProductDTO(product)).thenReturn(productDTO);

    ProductDTO result = productService.findProduct(1L);

    assertEquals(productDTO, result);

}

}
