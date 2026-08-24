# 🎧 Support Service

> Customer support tickets, warranty registration, and store locations for the CS02 E-Commerce Platform

## 📋 Overview

The Support Service handles all customer support operations including support ticket management, warranty registration and lookup, and store location information. It uses both PostgreSQL and MongoDB for different data storage needs.

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 4.0.0 |
| Primary Database | PostgreSQL | 15 |
| Document Storage | MongoDB | Latest |
| Security | Spring Security | Latest |
| Service Communication | Spring Cloud OpenFeign | Latest |
| Build Tool | Maven | 3.x |

## 🚀 API Endpoints

### Support Tickets

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/support/tickets` | Yes | Create support ticket |
| `GET` | `/api/support/tickets` | Yes | Get user's tickets |
| `GET` | `/api/support/tickets/{id}` | Yes | Get ticket details |
| `POST` | `/api/support/tickets/{id}/reply` | Yes | Add reply to ticket |
| `PUT` | `/api/support/tickets/{id}/status` | Admin | Update ticket status |
| `GET` | `/api/support/tickets/all` | Admin | Get all tickets |
| `PUT` | `/api/support/tickets/{id}/assign` | Admin | Assign ticket to agent |

### Warranty

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/warranty/register` | Yes | Register product warranty |
| `GET` | `/api/warranty/check` | Yes | Check warranty status |
| `GET` | `/api/warranty/{serialNumber}` | Yes | Get warranty by serial |
| `GET` | `/api/warranty/user` | Yes | Get user's warranties |
| `PUT` | `/api/warranty/{id}/claim` | Yes | Submit warranty claim |

### Store Locations

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/support/stores` | No | Get all store locations |
| `GET` | `/api/support/stores/{id}` | No | Get store details |
| `GET` | `/api/support/stores/nearby` | No | Get nearby stores |
| `POST` | `/api/support/stores` | Admin | Create store location |
| `PUT` | `/api/support/stores/{id}` | Admin | Update store |
| `DELETE` | `/api/support/stores/{id}` | Admin | Delete store |

## 📊 Data Models

### SupportTicket (MongoDB)

```java
{
  "id": "string",
  "userId": "uuid",
  "orderNumber": "CS02-20240115-001",
  "subject": "Defective GPU received",
  "category": "PRODUCT_ISSUE | ORDER_ISSUE | SHIPPING | RETURNS | GENERAL",
  "priority": "LOW | MEDIUM | HIGH | URGENT",
  "status": "OPEN | IN_PROGRESS | WAITING_CUSTOMER | RESOLVED | CLOSED",
  "description": "string",
  "attachments": ["string"],
  "replies": [
    {
      "id": "string",
      "userId": "uuid",
      "userName": "string",
      "isStaff": boolean,
      "message": "string",
      "createdAt": "datetime"
    }
  ],
  "assignedTo": "uuid",
  "createdAt": "datetime",
  "updatedAt": "datetime",
  "resolvedAt": "datetime"
}
```

### Warranty (PostgreSQL)

```java
{
  "id": "uuid",
  "userId": "uuid",
  "productId": "string",
  "productName": "string",
  "serialNumber": "string",
  "orderId": "uuid",
  "orderNumber": "string",
  "purchaseDate": "date",
  "expirationDate": "date",
  "warrantyType": "STANDARD | EXTENDED | PREMIUM",
  "status": "ACTIVE | EXPIRED | CLAIMED | VOID",
  "createdAt": "datetime"
}
```

### StoreLocation (MongoDB)

```java
{
  "id": "string",
  "name": "CS02 Downtown",
  "address": {
    "street": "123 Main St",
    "city": "San Francisco",
    "state": "CA",
    "zipCode": "94102",
    "country": "USA"
  },
  "phone": "+1-555-123-4567",
  "email": "downtown@cs02.com",
  "hours": {
    "monday": "9:00 AM - 9:00 PM",
    "tuesday": "9:00 AM - 9:00 PM",
    ...
  },
  "coordinates": {
    "latitude": 37.7749,
    "longitude": -122.4194
  },
  "services": ["SALES", "REPAIRS", "TRADE_IN", "PC_BUILDING"],
  "isActive": true,
  "imageUrl": "string"
}
```

## 🔧 Configuration

### Application Properties

```yaml
server:
  port: 8085

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/CSO2_support_service
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
  data:
    mongodb:
      uri: mongodb://localhost:27017/CSO2_support_service

feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000

order-service:
  url: http://localhost:8083
```

### Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `SPRING_DATASOURCE_URL` | No | `jdbc:postgresql://localhost:5432/CSO2_support_service` | PostgreSQL URL |
| `SPRING_DATASOURCE_USERNAME` | No | `postgres` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | No | `postgres` | Database password |
| `SPRING_DATA_MONGODB_URI` | No | `mongodb://localhost:27017` | MongoDB URI |
| `ORDER_SERVICE_URL` | No | `http://localhost:8083` | Order service URL |
| `SERVER_PORT` | No | `8085` | Service port |

## 📦 Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

## 🔌 Feign Clients

### OrderServiceClient

```java
@FeignClient(name = "order-service", url = "${order-service.url}")
public interface OrderServiceClient {
    @GetMapping("/api/orders/{id}")
    OrderDTO getOrder(@PathVariable String id);
    
    @GetMapping("/api/orders")
    List<OrderDTO> getUserOrders(@RequestHeader("X-User-Id") String userId);
}
```

## 🏃 Running the Service

### Local Development

```bash
cd backend/support-service

# Using Maven Wrapper
./mvnw spring-boot:run

# Or with Maven
mvn spring-boot:run
```

### Docker

```bash
cd backend/support-service

# Build JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t cs02/support-service .

# Run container
docker run -p 8085:8085 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/CSO2_support_service \
  -e SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/CSO2_support_service \
  -e ORDER_SERVICE_URL=http://order-service:8083 \
  cs02/support-service
```

## 🗄️ Database Requirements

### PostgreSQL
- Running on port `5432`
- Database: `CSO2_support_service`
- Used for: Warranty records (transactional data)

### MongoDB
- Running on port `27017`
- Database: `CSO2_support_service`
- Used for: Support tickets (documents), Store locations

### Database Schema (PostgreSQL)

```sql
-- warranties table
CREATE TABLE warranties (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    product_id VARCHAR(100),
    product_name VARCHAR(255),
    serial_number VARCHAR(100) UNIQUE,
    order_id UUID,
    order_number VARCHAR(50),
    purchase_date DATE,
    expiration_date DATE,
    warranty_type VARCHAR(20),
    status VARCHAR(20),
    created_at TIMESTAMP
);
```

## ✅ Features - Completion Status

| Feature | Status | Notes |
|---------|--------|-------|
| Support ticket creation | ✅ Complete | Full ticket creation |
| Ticket listing (user) | ✅ Complete | User's own tickets |
| Ticket listing (admin) | ✅ Complete | All tickets view |
| Ticket replies | ✅ Complete | Threaded conversation |
| Ticket status updates | ✅ Complete | Admin workflow |
| Ticket assignment | ✅ Complete | Agent assignment |
| Warranty registration | ✅ Complete | Product registration |
| Warranty lookup | ✅ Complete | By serial number |
| Warranty status check | ✅ Complete | Active/expired |
| Store locations | ✅ Complete | CRUD operations |
| Nearby stores | ✅ Complete | Location-based |

### **Overall Completion: 95%** ✅

## ❌ Not Implemented / Future Enhancements

| Feature | Priority | Notes |
|---------|----------|-------|
| File attachments | Medium | Ticket attachments |
| Email notifications | Medium | Ticket updates via email |
| Live chat integration | Low | Real-time support |
| Ticket SLA tracking | Low | Response time monitoring |
| Knowledge base | Low | Self-service articles |
| Warranty claims workflow | Medium | Full claim process |
| Store appointment booking | Low | Service appointments |
| Support analytics | Low | Ticket metrics |

## 📁 Project Structure

```
support-service/
├── src/
│   ├── main/
│   │   ├── java/com/cs02/support/
│   │   │   ├── SupportServiceApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── TicketController.java
│   │   │   │   ├── WarrantyController.java
│   │   │   │   └── StoreController.java
│   │   │   ├── model/
│   │   │   │   ├── SupportTicket.java
│   │   │   │   ├── TicketReply.java
│   │   │   │   ├── Warranty.java
│   │   │   │   └── StoreLocation.java
│   │   │   ├── repository/
│   │   │   │   ├── TicketRepository.java
│   │   │   │   ├── WarrantyRepository.java
│   │   │   │   └── StoreRepository.java
│   │   │   ├── service/
│   │   │   │   ├── TicketService.java
│   │   │   │   ├── WarrantyService.java
│   │   │   │   └── StoreService.java
│   │   │   └── client/
│   │   │       └── OrderServiceClient.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── pom.xml
├── Dockerfile
└── README.md
```

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

# Test ticket endpoints
curl -X POST -H "X-User-Id: user123" -H "Content-Type: application/json" \
  http://localhost:8085/api/support/tickets \
  -d '{"subject": "Issue with order", "category": "ORDER_ISSUE", "description": "..."}'

curl -H "X-User-Id: user123" http://localhost:8085/api/support/tickets

# Test warranty endpoints
curl -X POST -H "X-User-Id: user123" -H "Content-Type: application/json" \
  http://localhost:8085/api/warranty/register \
  -d '{"productId": "prod123", "serialNumber": "SN123456"}'

curl -H "X-User-Id: user123" http://localhost:8085/api/warranty/check?serialNumber=SN123456

# Test store endpoints
curl http://localhost:8085/api/support/stores
curl "http://localhost:8085/api/support/stores/nearby?lat=37.7749&lng=-122.4194"
```

## 🔗 Related Services

- [API Gateway](../api-gateway/README.md) - Routes `/api/support/*` and `/api/warranty/*`
- [Order Service](../order-service/README.md) - Order verification for tickets
- [Notifications Service](../notifications-service/README.md) - Ticket update notifications
- [User Identity Service](../user-identity-service/README.md) - User verification

## 📝 Notes

- Service runs on port **8085**
- Uses **hybrid database** approach (PostgreSQL + MongoDB)
- PostgreSQL for transactional warranty data
- MongoDB for flexible ticket documents and store locations
- Tickets support threaded replies
- Warranty expiration is calculated based on purchase date
- Store locations support geospatial queries for nearby search
