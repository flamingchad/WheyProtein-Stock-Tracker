package com.nithieshm.amulprice.controller;


import com.nithieshm.amulprice.entity.Product;
import com.nithieshm.amulprice.service.StockMonitoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/stock")
public class RestController {

    private final StockMonitoringService stockMonitoringService;

    public RestController(StockMonitoringService stockMonitoringService) {
        this.stockMonitoringService = stockMonitoringService;
    }

    @GetMapping
    public List<Product> stock() {
        return stockMonitoringService.currentStatus();
    }
}
