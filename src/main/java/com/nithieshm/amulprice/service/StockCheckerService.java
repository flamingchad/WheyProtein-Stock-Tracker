package com.nithieshm.amulprice.service;

import com.nithieshm.amulprice.entity.Product;
import com.nithieshm.amulprice.repository.ProductRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class StockCheckerService {

    private static final Logger logger = LoggerFactory.getLogger(StockCheckerService.class);

    private final ProductRepository productRepository;

    private final int DELAYINSECONDS = 10;

    private final int SCHEDULEDELAY = 60000;

    @Autowired
    public StockCheckerService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Scheduled(fixedDelay = SCHEDULEDELAY)
    @Async
    public void startChecking() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            WebDriver driver = null;
            try {
                driver = setupDriver();
                navigateDriver(driver, product.getUrl(), product);
            } catch (Exception e) {
                logger.error("Error processing product {}: {}", product.getId(), e.getMessage(), e);
            } finally {
                if (driver != null) {
                    driver.quit();
                }
            }
        }
    }

    public WebDriver setupDriver() {
        WebDriver driver = null;
        try {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless", "--disable-logging", "--silent", "--no-sandbox", "--disable-gpu",
                    "--disable-extensions", "--disable-web-security", "--allow-running-insecure-content",
                    "--window-size=1920,1080", "--disable-dev-shm-usage",
                    "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(chromeOptions);

        } catch (Exception e) {
            logger.error("Error while setting up chrome driver {}", e.getMessage(), e);
        }
        return driver;
    }

    public void navigateDriver(WebDriver driver, String url, Product product) {
        driver.get(url);
        pincodeNavigation(driver, url, product);
    }

    public void pincodeNavigation(WebDriver driver, String url, Product product) {
        try {
            WebElement searchInput = driver.findElement(By.id("search"));
            searchInput.clear();
            searchInput.sendKeys(product.getPincode());
        } catch (Exception e) {
            logger.error("Error during entering pincode {}", e.getMessage(), e);
        }

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement pincodeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[@role='button']//p[text()='" + product.getPincode() + "']")));
            pincodeButton.click();
        } catch (Exception e) {
            logger.error("Error during selecting pincode {}", e.getMessage(), e);
        }
        threadDelay(driver, url, product);
    }

    public void threadDelay(WebDriver driver, String url, Product product) {
        try {
            Thread.sleep(DELAYINSECONDS * 1000);
            checkStatus(driver, url, product);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted during delay {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error while adding delay to thread {}", e.getMessage(), e);
        }
    }

    public void checkStatus(WebDriver driver, String url, Product product) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        } catch (Exception e) {
            logger.error("Error during waiting for body to be loaded {}", e.getMessage(), e);
        }

        boolean isSoldout = !driver.findElements(By.xpath(
                "//div[contains(@class, 'alert-danger') and text()='Sold Out']")).isEmpty();

        boolean isCartDisabled = !driver.findElements(By.xpath(
                "//div[contains(@class, 'cart-checkout') and contains(@class, 'disabled') and text()='Add to Cart']")).isEmpty();

        String title = driver.getTitle();

        if (isSoldout || isCartDisabled) {
            logger.info("{} is out of stock", title);
            product.setInStock(false);
        } else {
            logger.info("{} is in stock", title);
            product.setInStock(true);
        }
        product.setLastChecked(LocalDateTime.now());
        productRepository.save(product);
    }
}
