package com.example.najvecaLolcina;

import com.example.najvecaLolcina.entity.CreateProductRequest;
import com.example.najvecaLolcina.entity.Product;
import com.example.najvecaLolcina.entity.ProductDTO;
import com.example.najvecaLolcina.entity.ProductType;
import com.example.najvecaLolcina.mapper.ProductMapper;
import com.example.najvecaLolcina.repository.ProductRepository;
import com.example.najvecaLolcina.repository.ProductTypeRepository;
import com.example.najvecaLolcina.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
@Mock
private ProductRepository productRepository;

@Mock
private ProductTypeRepository productTypeRepository;

@Mock
private ProductMapper productMapper;

@InjectMocks
private ProductService productService;

@Test
void findProductTest(){

    var product = new Product("lol", 10, BigDecimal.valueOf(10), "lol");
    product.setId(1L);
    var productDTO = new ProductDTO("lol", "Mouse", new BigDecimal(10), "lol");

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(productMapper.toProductDTO(product)).thenReturn(productDTO);

    ProductDTO result = productService.findProduct(1L);

    assertEquals(productDTO, result);

}

@Test
    void findNotExistProduct(){

    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () ->{productService.findProduct(1L);} );

}

@Test
    void addProductTestException(){
    CreateProductRequest createProductRequest = new CreateProductRequest("RazerBlackshark",
            10, BigDecimal.valueOf(10), "good headphones", "headphones");

    var product = new Product("RazerBlackshark", 10, BigDecimal.valueOf(10), "lol");
    var productType = new ProductType("headphones");

    when(productRepository.findWithSameName(createProductRequest.getName())).thenReturn(Optional.of(product));
    when(productTypeRepository.findProductTypeByType(createProductRequest.getType())).thenReturn(Optional.of(productType));
    assertThrows(IllegalArgumentException.class, ()-> {productService.addProduct(createProductRequest);});

}

    @Test
    void addProductTestExceptionTwo(){
        CreateProductRequest createProductRequest = new CreateProductRequest("RazerBlackshark",
                10, BigDecimal.valueOf(10), "good headphones", "headphones");


        when(productRepository.findWithSameName(createProductRequest.getName())).thenReturn(Optional.empty());
        when(productTypeRepository.findProductTypeByType(createProductRequest.getType())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, ()-> {productService.addProduct(createProductRequest);});

    }


    @Test
    void addProductTestFull(){
        CreateProductRequest createProductRequest = new CreateProductRequest("RazerBlackshark",
                10, BigDecimal.valueOf(10), "good headphones", "headphones");

        var productType = new ProductType("headphones");

        when(productRepository.findWithSameName(createProductRequest.getName())).thenReturn(Optional.empty());
        when(productTypeRepository.findProductTypeByType(createProductRequest.getType())).thenReturn(Optional.of(productType));

        var productDTO = new ProductDTO(createProductRequest.getName(), productType.getType(), createProductRequest.getPrice(), createProductRequest.getDescription());

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(productMapper.toProductDTO(any(Product.class)))
                .thenReturn(productDTO);

        ProductDTO result = productService.addProduct(createProductRequest);

        assertEquals(productDTO, result);

        verify(productRepository).save(any(Product.class));
    }


    @Test
    void updateProductException() {
        CreateProductRequest request = new CreateProductRequest(
                "RazerBlackshark",
                10,
                BigDecimal.valueOf(10),
                "good headphones",
                "headphones"
        );

        when(productRepository.findByIdUpdate(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> productService.updateProduct(request, 1L)
        );
    }

@Test
    void updateProductExceptionTwo(){
    var product = new Product("lol", 10, BigDecimal.valueOf(10), "lol");
    product.setId(1L);

    CreateProductRequest createProductRequest = new CreateProductRequest("RazerBlackshark",
            10, BigDecimal.valueOf(10), "good headphones", "headphones");

    when(productRepository.findByIdUpdate(1L)).thenReturn(Optional.of(product));
    when(productTypeRepository.findProductTypeByType(createProductRequest.getType())).thenReturn(Optional.empty());


    assertThrows(NoSuchElementException.class, () -> {productService.updateProduct(createProductRequest, 1L);});

}


@Test
    void updateProductFull(){

    var product = new Product("lol", 10, BigDecimal.valueOf(10), "lol");
    product.setId(1L);

    CreateProductRequest createProductRequest = new CreateProductRequest("RazerBlackshark",
            10, BigDecimal.valueOf(10), "good headphones", "headphones");

    ProductType productType = new ProductType("headphones");

    when(productRepository.findByIdUpdate(1L)).thenReturn(Optional.of(product));
    when(productTypeRepository.findProductTypeByType(createProductRequest.getType())).thenReturn(Optional.of(productType));

    var productDTO = new ProductDTO(createProductRequest.getName(), createProductRequest.getType(), createProductRequest.getPrice(), createProductRequest.getDescription());

    when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(productMapper.toProductDTO(any(Product.class))).thenReturn(productDTO);

    ProductDTO result = productService.updateProduct(createProductRequest, 1L);

    assertEquals(productDTO, result);

    ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

    verify(productRepository).save(captor.capture());

    Product savedProduct = captor.getValue();

    assertEquals(createProductRequest.getName(), savedProduct.getName());
    assertEquals(createProductRequest.getQuantity(), savedProduct.getQuantity());
    assertEquals(createProductRequest.getPrice(), savedProduct.getPrice());
    assertEquals(createProductRequest.getDescription(), savedProduct.getDescription());
    assertEquals(productType, savedProduct.getProductType());
    assertEquals(1L, savedProduct.getId());

}

@Test
    void returnAllProductsTestException(){

    Pageable pageable = PageRequest.of(0, 10);

    String type = "typeTest";

    when(productTypeRepository.findProductTypeByType(type)).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, ()->{productService.returnAllProducts(type, pageable);});

    verify(productRepository, never()).findByType(type, pageable);
    verify(productRepository, never()).findAll(pageable);
}

@Test
    void returnAllProductsFull(){
    Pageable pageable = PageRequest.of(0, 10);

    var product = new Product("lol", 10, BigDecimal.valueOf(10), "lol");
    product.setId(1L);

    Page<Product> productPage = new PageImpl<>(
            List.of(product),
            pageable,
            1
    );

    ProductDTO dto = new ProductDTO("lol", "Mouse", BigDecimal.valueOf(10), "lol");

    when(productRepository.findAll(pageable)).thenReturn(productPage);
    when(productMapper.toProductDTO(product)).thenReturn(dto);

    Page<ProductDTO> result =
            productService.returnAllProducts(null, pageable);

    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getContent().size());
    assertEquals(dto, result.getContent().get(0));
    assertEquals(0, result.getNumber());
    assertEquals(10, result.getSize());
}

@Test
    void returnAllPorductsFullTwo(){

    Pageable pageable = PageRequest.of(0, 10);

    var product = new Product("lol", 10, BigDecimal.valueOf(10), "lol");
    product.setId(1L);

    String type = "Keyboard";
    ProductType productType = new ProductType("Keyboard");

    Page<Product> productPage = new PageImpl<>(
            List.of(product),
            pageable,
            1
    );

    ProductDTO dto = new ProductDTO("lol", "Keyboard", BigDecimal.valueOf(10), "lol");

    when(productTypeRepository.findProductTypeByType(type)).thenReturn(Optional.of(productType));

    when(productRepository.findByType(type, pageable)).thenReturn(productPage);

    when(productMapper.toProductDTO(product)).thenReturn(dto);

    Page<ProductDTO> result = productService.returnAllProducts(type, pageable);

    assertEquals(1, result.getTotalElements());
    assertEquals(dto, result.getContent().get(0));

    verify(productRepository).findByType(type, pageable);
    verify(productRepository, never()).findAll(pageable);
}






}
