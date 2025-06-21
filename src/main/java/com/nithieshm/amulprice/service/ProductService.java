package com.nithieshm.amulprice.service;

import com.nithieshm.amulprice.entity.Product;
import com.nithieshm.amulprice.repository.ProductRepository;
import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    public Product createProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Product product, long id) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        Product existingProduct = getProductById(id);

        if (product.getName() != null) {
            existingProduct.setName(product.getName());
        }
        if (product.getUrl() != null) {
            existingProduct.setUrl(product.getUrl());
        }
        if (product.getPincode() != null) {
            existingProduct.setPincode(product.getPincode());
        }
        if (product.getOutOfStockSelector() != null) {
            existingProduct.setOutOfStockSelector(product.getOutOfStockSelector());
        }
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new NotFoundException("Unable to delete as product not found with id: " + id);
        }
    }
}
