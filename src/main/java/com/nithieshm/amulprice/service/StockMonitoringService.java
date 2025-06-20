package com.nithieshm.amulprice.service;

import com.nithieshm.amulprice.entity.Product;
import com.nithieshm.amulprice.repository.ProductRepository;
import com.nithieshm.amulprice.repository.StockHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockMonitoringService {
    private final ProductRepository productRepository;
    private final StockHistoryRepository stockHistoryRepository;
    private final StockCheckerService stockCheckerService;

    public StockMonitoringService(ProductRepository productRepository, StockHistoryRepository stockHistoryRepository, StockCheckerService stockCheckerService) {
        this.productRepository = productRepository;
        this.stockHistoryRepository = stockHistoryRepository;
        this.stockCheckerService = stockCheckerService;
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

    public boolean currentStatus() {
        List<Product> allProducts = getAllProducts();
        if (allProducts.isEmpty()) {
            return false; // or throw exception, based on your use case
        }
        return allProducts.get(0).isInStock();
    }
}
