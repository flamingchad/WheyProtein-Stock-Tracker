package com.nithieshm.amulprice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 6)
    @Size(min = 6, max = 6)
    @NotNull
    private String pincode;

    @NotNull
    @Column(length = 64)
    private String name;
    @NotNull
    private String url;

    //stock checking
    private String outOfStockSelector;
    private boolean isInStock;

    // modified later, will be used with Telegram/WhatsApp/Email to provide stock alert.
    private boolean prevStockStatus;
    private LocalDateTime lastChecked;

    //getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @Size(min = 6, max = 6) @NotNull String getPincode() {
        return pincode;
    }

    public void setPincode(@Size(min = 6, max = 6) @NotNull String pincode) {
        this.pincode = pincode;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getUrl() {
        return url;
    }

    public void setUrl(@NotNull String url) {
        this.url = url;
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

    public boolean isPrevStockStatus() {
        return prevStockStatus;
    }

    public void setPrevStockStatus(boolean prevStockStatus) {
        this.prevStockStatus = prevStockStatus;
    }
}
