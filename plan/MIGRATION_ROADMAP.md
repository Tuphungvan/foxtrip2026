# FOXTRIP - MIGRATION ROADMAP (Node.js → Spring Boot)

## 🎯 Chiến lược: Từ Core → Features → Advanced

---

## PHASE 1: FOUNDATION (Nền tảng) - 1-2 tuần

### 1.1 Infrastructure Setup ✅ (DONE)
- [x] Entities (PostgreSQL)
- [x] Documents (MongoDB)
- [x] Redis models
- [x] Enums
- [x] BaseEntity với JPA Auditing
- [x] Docker compose (PostgreSQL, MongoDB, Redis)

### 1.2 Security & Authentication (MỚI - Keycloak thay JWT)
**Priority:** ⭐⭐⭐⭐⭐ (CRITICAL - Cần trước mọi thứ)

**Old (Node.js):**
- JWT token tự quản lý
- Google/Facebook OAuth2
- Session trong memory

**New (Spring Boot):**
- Keycloak (OAuth2/OIDC)
- Spring Security + OAuth2 Resource Server
- UserProfile entity (business data)

**Tasks:**
1. Setup Keycloak server (Docker)
2. Config Spring Security OAuth2
3. Tạo UserProfileService (auto-create on first login)
4. JWT token validation
5. Role-based access control (USER, ADMIN, SUPER_ADMIN)

**Files cần tạo:**
```
security/
├── KeycloakConfig.java
├── SecurityConfig.java
├── JwtAuthenticationFilter.java
└── CustomUserDetailsService.java

service/
└── UserProfileService.java ✅ (DONE)

repository/
└── UserProfileRepository.java ✅ (DONE)
```

**Endpoints:**
```
POST   /api/auth/login          → Redirect to Keycloak
POST   /api/auth/callback       → Handle OAuth2 callback
POST   /api/auth/logout         → Keycloak logout
GET    /api/auth/me             → Get current user
```

---

## PHASE 2: CORE FEATURES (Chức năng cốt lõi) - 2-3 tuần

### 2.1 Tour Management (GIỮ NGUYÊN logic cũ)
**Priority:** ⭐⭐⭐⭐⭐ (CRITICAL)

**Old features:**
- CRUD tours (admin)
- Search, filter tours (public)
- Upload images (Cloudinary)
- Slug generation

**Tasks:**
1. TourService, TourRepository
2. TourController (REST API)
3. Cloudinary integration
4. Search & filter (Elasticsearch - optional)
5. Slug auto-generation

**Files cần tạo:**
```
service/
├── TourService.java
└── CloudinaryService.java

repository/
└── TourRepository.java ✅ (DONE)

controller/
└── TourController.java

dto/
├── TourCreateRequest.java
├── TourUpdateRequest.java
└── TourResponse.java
```

**Endpoints:**
```
# Public
GET    /api/tours                    → List tours
GET    /api/tours/search             → Search tours
GET    /api/tours/{slug}             → Get tour detail
GET    /api/tours/region/{region}    → Filter by region
GET    /api/tours/hot                → Hot tours
GET    /api/tours/discount           → Discount tours

# Admin
POST   /api/admin/tours              → Create tour
PUT    /api/admin/tours/{id}         → Update tour
DELETE /api/admin/tours/{id}         → Delete tour (soft)
POST   /api/admin/tours/images       → Upload images
```

---

### 2.2 Shopping Cart (GIỮ NGUYÊN logic, dùng Redis)
**Priority:** ⭐⭐⭐⭐

**Old features:**
- Add/remove items
- Update quantity
- Calculate total

**New approach:**
- Redis (TTL 7 days)
- Support multi-service (TOUR, TRANSPORTATION, RESTAURANT, HOTEL)

**Tasks:**
1. CartService (Redis operations)
2. CartController
3. CartItem model ✅ (DONE)

**Files cần tạo:**
```
service/
└── CartService.java

controller/
└── CartController.java

config/
└── RedisConfig.java
```

**Endpoints:**
```
POST   /api/cart/add/{itemType}/{itemId}    → Add to cart
GET    /api/cart                             → View cart
DELETE /api/cart/{itemId}                    → Remove item
PUT    /api/cart/{itemId}/quantity           → Update quantity
GET    /api/cart/count                       → Count items
DELETE /api/cart/clear                       → Clear cart
```

---

### 2.3 Order & Checkout (MỞ RỘNG - Multi-service)
**Priority:** ⭐⭐⭐⭐⭐

**Old features:**
- Place order (tour only)
- Order status tracking
- Mock payment

**New features:**
- Multi-service order (1 order, nhiều items)
- Real payment (SePay)
- Booking với QR code

**Tasks:**
1. OrderService, OrderItemService, BookingService
2. PaymentService (SePay integration)
3. QR code generation
4. Email service (order confirmation)

**Files cần tạo:**
```
service/
├── OrderService.java
├── OrderItemService.java
├── BookingService.java
├── PaymentService.java
├── SepayService.java
├── QRCodeService.java
└── EmailService.java

repository/
├── OrderRepository.java
├── OrderItemRepository.java
├── BookingRepository.java
└── PaymentRepository.java

controller/
├── CheckoutController.java
└── OrderController.java
```

**Endpoints:**
```
# Checkout
GET    /api/checkout                     → View cart summary
POST   /api/checkout/place-order         → Create order
POST   /api/checkout/payment/{orderId}   → Process payment

# User orders
GET    /api/orders                       → My orders
GET    /api/orders/{orderId}             → Order detail
DELETE /api/orders/{orderId}/cancel      → Cancel order
GET    /api/orders/{orderId}/qr          → Get QR code

# Admin
GET    /api/admin/orders                 → All orders
PUT    /api/admin/orders/{id}/confirm    → Confirm order
```

---

## PHASE 3: NEW FEATURES (Tính năng mới) - 2-3 tuần

### 3.1 Transportation Booking (MỚI)
**Priority:** ⭐⭐⭐⭐

**Tasks:**
1. TransportationService (CRUD)
2. RouteService, ScheduleService
3. SeatService (seat map, booking)
4. Availability management

**Files cần tạo:**
```
service/
├── TransportationService.java
├── TransportationRouteService.java
├── TransportationScheduleService.java
└── TransportationSeatService.java

repository/
├── TransportationRepository.java
├── TransportationRouteRepository.java
├── TransportationScheduleRepository.java
└── TransportationSeatRepository.java

controller/
├── TransportationController.java (public)
└── AdminTransportationController.java
```

**Endpoints:**
```
# Public
GET    /api/transportation                           → List vehicles
GET    /api/transportation/{id}/routes               → Routes
GET    /api/transportation/search                    → Search schedules
GET    /api/transportation/schedules/{id}/seats      → Seat map

# Admin
POST   /api/admin/transportation                     → Create vehicle
POST   /api/admin/transportation/{id}/routes         → Add route
POST   /api/admin/transportation/{id}/schedules      → Add schedule
```

---

### 3.2 Restaurant Booking (MỚI)
**Priority:** ⭐⭐⭐

**Tasks:**
1. RestaurantService (CRUD)
2. TableService
3. AvailabilityService (time slots)

**Files cần tạo:**
```
service/
├── RestaurantService.java
├── RestaurantTableService.java
└── RestaurantAvailabilityService.java

repository/
├── RestaurantRepository.java
├── RestaurantTableRepository.java
└── RestaurantAvailabilityRepository.java

controller/
├── RestaurantController.java
└── AdminRestaurantController.java
```

---

### 3.3 Hotel Booking (MỚI)
**Priority:** ⭐⭐⭐

**Tasks:**
1. HotelService (CRUD)
2. RoomService
3. AvailabilityService (date range)

**Files cần tạo:**
```
service/
├── HotelService.java
├── HotelRoomService.java
└── HotelAvailabilityService.java

repository/
├── HotelRepository.java
├── HotelRoomRepository.java
└── HotelAvailabilityRepository.java

controller/
├── HotelController.java
└── AdminHotelController.java
```

---

### 3.4 Voucher System (MỚI)
**Priority:** ⭐⭐⭐

**Tasks:**
1. VoucherService (CRUD, validation)
2. VoucherUsageService (tracking)
3. Redis counter (rate limiting)

**Files cần tạo:**
```
service/
├── VoucherService.java
└── VoucherUsageService.java

repository/
├── VoucherRepository.java
└── VoucherUsageRepository.java

controller/
├── VoucherController.java (public - apply)
└── AdminVoucherController.java (CRUD)
```

---

## PHASE 4: ADVANCED FEATURES (Nâng cao) - 1-2 tuần

### 4.1 AI Chatbot (GIỮ NGUYÊN - Groq API)
**Priority:** ⭐⭐

**Old features:**
- Groq API integration
- Context-aware responses

**Tasks:**
1. ChatbotService (Groq API)
2. AiChatHistoryService (MongoDB)

**Files cần tạo:**
```
service/
├── ChatbotService.java
└── AiChatHistoryService.java

repository/
└── AiChatHistoryRepository.java

controller/
└── ChatbotController.java
```

---

### 4.2 Real-time Chat (MỚI)
**Priority:** ⭐⭐

**Tasks:**
1. WebSocket configuration
2. ChatRoomService, ChatMessageService (MongoDB)
3. Admin chat panel

**Files cần tạo:**
```
config/
└── WebSocketConfig.java

service/
├── ChatRoomService.java
└── ChatMessageService.java

repository/
├── ChatRoomRepository.java
└── ChatMessageRepository.java

controller/
└── ChatController.java
```

---

### 4.3 Review & Rating (MỚI)
**Priority:** ⭐⭐

**Tasks:**
1. ReviewService (MongoDB)
2. Rating aggregation
3. Admin moderation

**Files cần tạo:**
```
service/
└── ReviewService.java

repository/
└── ReviewRepository.java

controller/
├── ReviewController.java
└── AdminReviewController.java
```

---

### 4.4 Admin Dashboard (GIỮ NGUYÊN + MỞ RỘNG)
**Priority:** ⭐⭐⭐

**Old features:**
- Overview stats
- Revenue reports
- User management

**New features:**
- Multi-service stats
- Advanced analytics

**Tasks:**
1. DashboardService
2. RevenueReportService
3. UserManagementService

---

### 4.5 Notification System (MỚI)
**Priority:** ⭐

**Tasks:**
1. NotificationService
2. Email notifications
3. Push notifications (optional)

---

## PHASE 5: OPTIMIZATION & DEPLOYMENT - 1 tuần

### 5.1 Performance
- Elasticsearch integration (search)
- Redis caching strategy
- Database indexing optimization

### 5.2 Testing
- Unit tests (JUnit)
- Integration tests
- API tests (Postman/REST Assured)

### 5.3 Documentation
- Swagger/OpenAPI
- README
- API documentation

### 5.4 Deployment
- Docker compose production
- CI/CD (optional)
- Monitoring (optional)

---

## 📊 TIMELINE SUMMARY

| Phase | Duration | Priority |
|-------|----------|----------|
| Phase 1: Foundation | 1-2 tuần | ⭐⭐⭐⭐⭐ |
| Phase 2: Core Features | 2-3 tuần | ⭐⭐⭐⭐⭐ |
| Phase 3: New Features | 2-3 tuần | ⭐⭐⭐⭐ |
| Phase 4: Advanced | 1-2 tuần | ⭐⭐ |
| Phase 5: Optimization | 1 tuần | ⭐⭐⭐ |
| **TOTAL** | **7-11 tuần** | |

---

## 🎯 RECOMMENDED ORDER (Ưu tiên cao → thấp)

1. **Keycloak + Security** (CRITICAL - Cần trước mọi thứ)
2. **Tour Management** (Core business)
3. **Cart + Order + Payment** (Core business)
4. **Transportation** (New feature - high value)
5. **Restaurant + Hotel** (New feature - medium value)
6. **Voucher** (Business logic)
7. **AI Chatbot** (Nice to have)
8. **Real-time Chat** (Nice to have)
9. **Review & Rating** (Nice to have)
10. **Admin Dashboard** (Management)
11. **Notification** (Enhancement)

---

## 🚀 NEXT STEPS

**Bắt đầu với Phase 1.2: Keycloak + Security**
- Setup Keycloak Docker
- Config Spring Security
- Implement authentication flow
- Test với Postman

**Sau đó:** Phase 2.1 - Tour Management (core business logic)
