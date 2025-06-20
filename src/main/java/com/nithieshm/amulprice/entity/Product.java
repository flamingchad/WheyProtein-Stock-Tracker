package com.nithieshm.amulprice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;
    private boolean useSelenium;

    //stock checking
    private String outOfStockSelector;
    private boolean isInStock;
    private LocalDateTime lastChecked;

    //getters and setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUseSelenium() {
        return useSelenium;
    }

    public void setUseSelenium(boolean useSelenium) {
        this.useSelenium = useSelenium;
    }

    public String getOutOfStockSelector() {
        return outOfStockSelector;
    }

    public void setOutOfStockSelector(String outOfStockSelector) {
        this.outOfStockSelector = outOfStockSelector;
    }

    public boolean isInStock() {
        return isInStock;
    }

    public void setInStock(boolean inStock) {
        isInStock = inStock;
    }

    public LocalDateTime getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(LocalDateTime lastChecked) {
        this.lastChecked = lastChecked;
    }
}
