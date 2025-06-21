# AmulPrice - Stock Checker Application

A Spring Boot application that automatically monitors product stock availability on e-commerce websites using Selenium WebDriver. The application periodically checks product availability based on pincode and provides REST API endpoints for product management.

## Features

- **Automated Stock Monitoring**: Periodically checks product availability every 60 seconds
- **Pincode-based Availability**: Monitors stock for specific locations using pincode
- **REST API**: Complete CRUD operations for product management
- **Selenium Integration**: Uses headless Chrome browser for web scraping
- **Database Persistence**: Stores product information and stock status
- **Asynchronous Processing**: Non-blocking stock checking operations

## Technology Stack

- **Framework**: Spring Boot
- **Database**: JPA/Hibernate
- **Web Scraping**: Selenium WebDriver
- **Browser**: Chrome (Headless mode)
- **Scheduling**: Spring Scheduling
- **Validation**: Bean Validation (JSR-303)

## Project Structure

```
com.nithieshm.amulprice/
├── controller/
│   └── ProductController.java
├── entity/
│   └── Product.java
├── repository/
│   └── ProductRepository.java
└── service/
    ├── ProductService.java
    └── StockCheckerService.java
```

## API Endpoints

### Product Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/stock` | Get all products |
| GET | `/api/v1/stock/{id}` | Get product by ID |
| POST | `/api/v1/stock` | Create new product |
| PUT | `/api/v1/stock/{id}` | Update existing product |
| DELETE | `/api/v1/stock/{id}` | Delete product |

### Request/Response Examples

#### Create Product
```json
POST /api/v1/stock
{
  "name": "Product Name",
  "url": "https://example.com/product",
  "pincode": "500001",
  "outOfStockSelector": ".sold-out"
}
```

#### Response
```json
{
  "id": 1,
  "name": "Product Name",
  "url": "https://example.com/product",
  "pincode": "500001",
  "outOfStockSelector": ".sold-out",
  "inStock": true,
  "lastChecked": "2025-06-21T10:30:00"
}
```

## Product Entity

The `Product` entity contains the following fields:

- `id`: Unique identifier (auto-generated)
- `name`: Product name (max 64 characters)
- `url`: Product URL
- `pincode`: 6-digit pincode for location-based checking
- `outOfStockSelector`: CSS selector for out-of-stock detection
- `isInStock`: Current stock status
- `lastChecked`: Timestamp of last stock check

## Stock Checking Logic

The application automatically:

1. **Fetches all products** from the database every 60 seconds
2. **Opens each product URL** in headless Chrome browser
3. **Enters the pincode** to check location-specific availability
4. **Detects stock status** using multiple methods:
    - Looks for "Sold Out" alert messages
    - Checks if "Add to Cart" button is disabled
5. **Updates database** with current stock status and timestamp

## Prerequisites

- Java 11 or higher
- Maven
- Chrome browser installed
- Database (PostgreSQL)

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd amulprice
   ```

2. **Configure database** in `application.properties`
   ```properties
   spring.datasource.url=jdbc:h2:mem:testdb
   spring.datasource.driver-class-name=org.h2.Driver
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## Configuration

### Chrome Driver Setup
The application uses WebDriverManager for automatic Chrome driver management. Chrome options are configured for:
- Headless operation
- Disabled logging and GPU
- Custom user agent
- Window size optimization

### Scheduling Configuration
Stock checking runs every 60 seconds (configurable via `SCHEDULED_DELAY` constant).

## Usage Examples

1. **Add a product to monitor**:
   ```bash
   curl -X POST http://localhost:8080/api/v1/stock \
   -H "Content-Type: application/json" \
   -d '{
     "name": "Amul Milk 1L",
     "url": "https://amul.com/products/milk-1l",
     "pincode": "500001"
   }'
   ```

2. **Check all products**:
   ```bash
   curl http://localhost:8080/api/v1/stock
   ```

3. **Get specific product**:
   ```bash
   curl http://localhost:8080/api/v1/stock/1
   ```

## Error Handling

The application includes comprehensive error handling:
- Product not found exceptions
- Invalid input validation
- Selenium WebDriver errors
- Thread interruption handling
- Graceful driver cleanup

## Logging

The application uses SLF4J for logging with different levels:
- INFO: Stock status updates
- ERROR: Exception handling and error scenarios
- Comprehensive logging for debugging web scraping issues

## Future Enhancements

- Email/SMS notifications for stock availability
- Multiple website support
- Stock history tracking
- Web dashboard for monitoring
- Docker containerization
- Rate limiting and proxy support

