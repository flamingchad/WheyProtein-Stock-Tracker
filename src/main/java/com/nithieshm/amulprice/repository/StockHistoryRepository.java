package com.nithieshm.amulprice.repository;

import com.nithieshm.amulprice.entity.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Integer> {
}
