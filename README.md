# 🏨 Hotel Booking Service API

## 📌 Overview
The **Hotel Booking Service** is a RESTful Spring Boot application that provides hotel management, booking operations with conflict detection, and user authentication/authorization using JWT.  
It also supports email notifications (via Mailtrap for development).

---

## 🚀 Tech Stack
- Java 17
- Spring Boot 3.5.6
- Spring Web, Spring Security (JWT), Spring Data MongoDB
- Jakarta Validation
- Spring Mail (Mailtrap for development)
- Springdoc OpenAPI (Swagger UI)

---

## 📂 Project Structure
- config/ # Security & OpenAPI config
- controller/ # REST Controllers
- domain/ # Entities (Hotel, Booking, User)
- exception/ # Custom exceptions + global handler
- payload/ # DTOs and ApiResponse
- repository/ # MongoDB Repositories
- security/ # JWT filter & utils
- service/ # Business logic services


---

## 🔄 Application Flow
1. **User Registration** → User registers and receives a confirmation email.
2. **Account Confirmation** → User confirms account via token → status changes to `ACTIVE`.
3. **Login** → User logs in with email & password, receives JWT token.
4. **Hotels** → Staff can create hotels, all users can list hotels.
5. **Bookings** → Staff/Reception can create bookings (conflict detection enabled).
6. **Notifications** → Emails sent for registration confirmation and booking alerts.
7. **Errors** → All errors return a unified `ApiResponse` format.

---

### 1. Clone Repository
```bash
git clone https://github.com/anup21panchal/HotelBokking.git
```
# 🏨 Hotel Booking Service API

## ⚙️ Configure `application.yml`

Example configuration:

```yaml
spring:
  jackson:
    default-property-inclusion: non_null
  data:
    mongodb:
      uri: ${MONGODB_URI}

  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://dev-1tbxzi5afsy5e1pd.us.auth0.com/

  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

# application-level settings
support:
  email:
    to: user@example.com

# prefix used to build confirmation URL in NotificationService
app:
  frontend:
    confirm-url-prefix: http://localhost:8080/api/auth/confirm?token=


logging:
  level:
    root: INFO

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
  packages-to-scan: com.hotel.hotel_booking_service
```

## 🚀 Build & Run
```bash
./mvnw clean install
./mvnw spring-boot:run
```

## 🔗 Swagger & OpenAPI
- Swagger UI → [http://localhost:8080/hotel/swagger-ui.html](http://localhost:8080/hotel/swagger-ui.html)
- OpenAPI JSON → [http://localhost:8080/hotel/api-docs](http://localhost:8080/hotel/api-docs)
- Deployed URL → [https://hotelbokking-11.onrender.com/hotel/swagger-ui/index.html#](https://hotelbokking-11.onrender.com/hotel/swagger-ui/index.html#)


🔑 Use the **Authorize** button in Swagger UI to provide your JWT:[Token]


---

## 🔑 Endpoints

### 🟦 Registration
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/registration/register` | Register a new user |
| GET    | `/api/registration/confirm?token=...` | Confirm registration |

### 🟩 Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/auth/login` | Login and receive JWT |

### 🏨 Hotels
| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| POST   | `/api/hotels/addHotel` | staff/reception | Create a hotel |
| GET    | `/api/hotels/getAllHotels` | any | Retrieve all hotels |

### 📅 Bookings
| Method | Endpoint | Role | Description |
|--------|----------|------|-------------|
| GET    | `/api/hotels/{hotelId}/bookings` | any authenticated | List bookings (optional date filter) |
| POST   | `/api/hotels/{hotelId}/bookings` | staff/reception | Create booking (with conflict detection) |

---

## 🧪 Example Usage

### 1. Register
```http
POST /hotel/api/registration/register
Content-Type: application/json

{
  "name": "Anup panchal",
  "email": "anup@example.com",
  "password": "secure123",
  "role": "staff"
}
```
## 2. Confirm
```http
GET /hotel/api/registration/confirm?token=<confirmation_token>
```

## 3. Login
```http
POST /hotel/api/auth/login
Content-Type: application/json

{
  "email": "anup@example.com",
  "password": "secure123"
}
Response:
    
{
  "message": "Login successful",
  "code": 200,
  "data": { "token": "eyJhbGciOiJIUzI1NiJ9..." }
}
```

## 4. Create Hotel
```http
POST /hotel/api/hotels/addHotel
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Taj Palace",
  "location": "Mumbai",
  "rooms": 50
}
```
## 📬 Error Handling

All responses follow the **ApiResponse** format.

### ✅ Success
```json
{
  "message": "Hotel created",
  "code": 201,
  "data": { "id": "123", "name": "Taj Palace", "location": "Mumbai", "rooms": 50 }
}
```

### ❌ Error
```json
{
  "message": "Hotel not found",
  "code": 404
}
```
### ⚠️ Validation Error
```json
{
  "message": "Validation failed",
  "code": 400,
  "data": [
    "email: must be a well-formed email address",
    "password: Password must be at least 6 characters long"
  ]
}

```
###  🔒 Auth Error
```json
{
  "message": "Invalid or expired token",
  "code": 401
}
```

###  🔒 Role-Based Access

- Booking Endpoints (BookingController)
- Allowed Roles → staff, reception

Any other role → 403 Forbidden
```http
{
  POST /hotel/api/hotels/{hotelId}/bookings
  Authorization: Bearer <token>
}
```

# 🧭 Step-by-Step API Flow (call these in order)

1)
```http
POST /hotel/api/registration/register
```
2)
```http
GET /hotel/api/registration/confirm?token=<confirmation_token>
```
3)
```http
POST /hotel/api/auth/login
```
4)
```http
POST /hotel/api/hotels/addHotel
```
5)
```http
GET /hotel/api/hotels/getAllHotels
```
6)
```http
GET /hotel/api/hotels/{hotelId}/bookings?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd
```
7)
```http
POST /hotel/api/hotels/{hotelId}/bookings

```




