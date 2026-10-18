package com.example.najvecaLolcina.service;
import com.example.najvecaLolcina.dto.CreateProductRequest;
import com.example.najvecaLolcina.dto.PatchProductRequest;
import com.example.najvecaLolcina.entity.Product;
import com.example.najvecaLolcina.repository.ProductTypeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.najvecaLolcina.dto.ProductDTO;
import com.example.najvecaLolcina.mapper.ProductMapper;
import com.example.najvecaLolcina.repository.ProductRepository;

import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.NoSuchElementException;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductTypeRepository productTypeRepository;



    public ProductService(ProductRepository productRepository, ProductMapper productMapper, ProductTypeRepository productTypeRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productTypeRepository = productTypeRepository;
    }

    public Page<ProductDTO> returnAllProducts(String type, Pageable pageable) {
        Page<Product> productPage;

        if (type != null && !type.isBlank()) {
             productTypeRepository.findProductTypeByType(type).orElseThrow(()->new NoSuchElementException("There is no this type"));
            productPage = productRepository.findByType(type, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(productMapper::toProductDTO);
    }

    public ProductDTO findProduct(Long id) {
        var product = productRepository.findById(id).orElseThrow(()-> new NoSuchElementException("No element with id "+id));
        return productMapper.toProductDTO(product);
    }

    @Transactional
    public ProductDTO addProduct(@Valid CreateProductRequest createProductRequest) {
        var optionalProduct = productRepository.findWithSameName(createProductRequest.getName());
        var optionalProductType = productTypeRepository.findProductTypeByType(createProductRequest.getType());
        if(!optionalProduct.isEmpty() || optionalProductType.isEmpty()){
            throw new IllegalArgumentException("Product with the same name already exist or type is incorrect");
        }
        var product = new Product(createProductRequest.getName(), createProductRequest.getQuantity(),
                createProductRequest.getPrice(), createProductRequest.getDescription()
                );
        var productType = optionalProductType.get();
        product.setProductType(productType);
        return productMapper.toProductDTO(productRepository.save(product));
    }

@Transactional
    public ProductDTO updateProduct(@Valid CreateProductRequest createProductRequest, Long id) {
        var product = productRepository.findByIdUpdate(id).orElseThrow(()->new NoSuchElementException("There is no product with this id"));
        var productType = productTypeRepository.findProductTypeByType(createProductRequest.getType()).orElseThrow(()->new NoSuchElementException("No type"));

        product.setQuantity(createProductRequest.getQuantity());
        product.setName(createProductRequest.getName());
        product.setPrice(createProductRequest.getPrice());
        product.setDescription(createProductRequest.getDescription());
        product.setProductType(productType);
        return productMapper.toProductDTO(productRepository.save(product));
    }

    @Transactional

    public ProductDTO patchProduct(Long id, PatchProductRequest request) {
        Product product = productRepository.findByIdUpdate(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Product not found"));

        if (request.getName() != null) {
            product.setName(request.getName());
        }

        if (request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }

        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }

        return productMapper.toProductDTO(productRepository.save(product));
    }

}
