package com.nithieshm.amulprice.service;

import com.nithieshm.amulprice.entity.Product;
import com.nithieshm.amulprice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockMonitoringService {
    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(StockCheckerService.class);

    public StockMonitoringService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product>getAllProducts() {
        return productRepository.findAll();
    }

//    public void deserializeStock() {
//        List<Product> allProducts = getAllProducts();
//        for (Product product : allProducts) {
//            String url = product.getUrl();
//            stockCheckerService.startChecking(url);
//        }
//        currentStatus();
//    }

    public List<Product> currentStatus() {
        return getAllProducts();
    }
}
