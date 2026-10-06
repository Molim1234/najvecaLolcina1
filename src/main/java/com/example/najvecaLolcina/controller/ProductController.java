package com.example.najvecaLolcina.controller;


import com.example.najvecaLolcina.entity.CreateProductRequest;
import com.example.najvecaLolcina.entity.ProductDTO;
import com.example.najvecaLolcina.service.ProductService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.Map;

@RestController
@RequestMapping("/api")
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

    @GetMapping("/products/{id}")
    public ProductDTO findProductById(@PathVariable Long id){
        return productService.findProduct(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/products")
    public ProductDTO createProduct(@RequestBody @Valid CreateProductRequest createProductRequest){
        return productService.addProduct(createProductRequest);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/products/{id}")
    public ProductDTO updateProduct(@RequestBody @Valid CreateProductRequest createProductRequest, @PathVariable Long id){
        return productService.updateProduct(createProductRequest, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/products/{id}")
    public ProductDTO patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updateText){
            return productService.patchProduct(id, updateText);
    }

}
