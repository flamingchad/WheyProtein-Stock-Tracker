package com.nithieshm.amulprice;

import com.nithieshm.amulprice.service.StockCheckerService;
import com.nithieshm.amulprice.service.StockMonitoringService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AmulPriceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmulPriceApplication.class, args);
    }

}
