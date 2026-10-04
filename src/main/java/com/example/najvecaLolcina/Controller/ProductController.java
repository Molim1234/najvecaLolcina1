package com.example.najvecaLolcina.Controller;


import com.example.najvecaLolcina.Entity.CreateProductRequest;
import com.example.najvecaLolcina.Entity.Product;
import com.example.najvecaLolcina.Entity.ProductDTO;
import com.example.najvecaLolcina.Service.ProductService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public Page<ProductDTO> returnAllProducts(
            @RequestParam(required = false, name = "type") String type,
            Pageable pageable
    ) {
        return productService.returnAllProducts(type, pageable);
    }

    @GetMapping("/product/{id}")
    public ProductDTO findProductById(@PathVariable Long id){
        return productService.findProduct(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/product/add")
    public ProductDTO createProduct(@RequestBody @Valid CreateProductRequest createProductRequest){
        return productService.addProduct(createProductRequest);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/product/update/{id}")
    public ProductDTO updateProduct(@RequestBody @Valid CreateProductRequest createProductRequest, @PathVariable Long id){
        return productService.updateProduct(createProductRequest, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/product/patch/{id}")
    public ProductDTO patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updateText){
            return productService.patchProduct(id, updateText);
    }

}
