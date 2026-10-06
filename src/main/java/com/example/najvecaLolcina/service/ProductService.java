package com.example.najvecaLolcina.service;
import com.example.najvecaLolcina.entity.CreateProductRequest;
import com.example.najvecaLolcina.entity.Product;
import com.example.najvecaLolcina.repository.ProductTypeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.najvecaLolcina.entity.ProductDTO;
import com.example.najvecaLolcina.mapper.ProductMapper;
import com.example.najvecaLolcina.repository.ProductRepository;

import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;
import java.util.NoSuchElementException;


@Service
public class ProductService {

    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private ProductTypeRepository productTypeRepository;
    private JsonMapper jsonMapper;


    public ProductService(ProductRepository productRepository, ProductMapper productMapper, ProductTypeRepository productTypeRepository, JsonMapper jsonMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productTypeRepository = productTypeRepository;
        this.jsonMapper = jsonMapper;
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
        var product = productRepository.findById(id).orElseThrow(()->new NoSuchElementException("There is no product with this id"));
        var productType = productTypeRepository.findProductTypeByType(createProductRequest.getType()).orElseThrow(()->new NoSuchElementException("No type"));

        product.setQuantity(createProductRequest.getQuantity());
        product.setName(createProductRequest.getName());
        product.setPrice(createProductRequest.getPrice());
        product.setDescription(createProductRequest.getDescription());
        product.setProductType(productType);
        return productMapper.toProductDTO(productRepository.save(product));
    }

    @Transactional
    public ProductDTO patchProduct(Long id, Map<String, Object> updateText) {

        var product = productRepository.findById(id).orElseThrow(()->new NoSuchElementException("No product with this id"));

        if(updateText.containsKey("id"))
            throw new IllegalArgumentException("Patch product should not has id");

        if(updateText.containsKey("type")){
            String type = (String) updateText.get("type");
            var findType = productTypeRepository.findProductTypeByType(type).orElseThrow(()->new NoSuchElementException("There is no type with this name"));
            product.setProductType(findType);
            updateText.remove("type");
        }

        jsonMapper.updateValue(product, updateText);
        return productMapper.toProductDTO(productRepository.save(product));

    }
}
