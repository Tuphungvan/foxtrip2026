# FOXTRIP - DATABASE DESIGN (Mở rộng từ backend cũ)

## 🎯 MỞ RỘNG: Tour → Tour + Transportation + Restaurant + Hotel

---

## POSTGRESQL (Core Transactional Data)

### 1. TOURS (Giữ nguyên từ backend cũ + mở rộng)

#### tours
```sql
id                  UUID PK
name                VARCHAR NOT NULL
slug                VARCHAR UNIQUE NOT NULL
description         TEXT
province            VARCHAR
region              VARCHAR (Bắc, Trung, Nam)
category            VARCHAR (Biển, Văn hóa, Nghỉ dưỡng...)
images              TEXT[] (array URLs)
video_id            VARCHAR (YouTube)
short_url           VARCHAR (YouTube Shorts)
start_date          DATE NOT NULL
end_date            DATE NOT NULL
itinerary           TEXT
price               DECIMAL NOT NULL
discount            DECIMAL DEFAULT 0
slots               INTEGER NOT NULL
available_slots     INTEGER NOT NULL
is_bookable         BOOLEAN DEFAULT TRUE
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()
deleted_at          TIMESTAMP

-- Indexes
INDEX idx_tours_slug ON tours(slug)
INDEX idx_tours_region ON tours(region, is_bookable)
INDEX idx_tours_province ON tours(province, is_bookable)
INDEX idx_tours_dates ON tours(start_date, end_date, is_bookable)
INDEX idx_tours_deleted ON tours(deleted_at) WHERE deleted_at IS NULL
```
**Storage:** PostgreSQL
**Lý do:** Giữ nguyên logic cũ, ACID cho booking, migrate sang PostgreSQL

---

### 2. TRANSPORTATION (MỚI - Phương tiện di chuyển)

#### transportation
```sql
id                  UUID PK
name                VARCHAR NOT NULL (VD: "Tàu SE1 Hà Nội - Sài Gòn")
slug                VARCHAR UNIQUE NOT NULL
vehicle_type        VARCHAR NOT NULL (TRAIN, BUS, FERRY, PRIVATE_CAR)
description         TEXT
images              TEXT[]
price_base          DECIMAL NOT NULL (giá cơ bản, có thể override theo route)
is_active           BOOLEAN DEFAULT TRUE
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()
deleted_at          TIMESTAMP

-- Indexes
INDEX idx_transport_slug ON transportation(slug)
INDEX idx_transport_type ON transportation(vehicle_type, is_active)
INDEX idx_transport_deleted ON transportation(deleted_at) WHERE deleted_at IS NULL
```
**Storage:** PostgreSQL
**Lý do:** Core data, cần ACID

#### transportation_routes (Tuyến đường - List)
```sql
id                  UUID PK
transportation_id   UUID FK → transportation NOT NULL
route_name          VARCHAR NOT NULL (VD: "Hà Nội - Vinh")
departure_location  VARCHAR NOT NULL
arrival_location    VARCHAR NOT NULL
distance_km         INTEGER
duration_minutes    INTEGER NOT NULL
price               DECIMAL NOT NULL (giá cho tuyến này)
display_order       INTEGER DEFAULT 0
is_active           BOOLEAN DEFAULT TRUE

-- Indexes
INDEX idx_route_transport ON transportation_routes(transportation_id, is_active)
INDEX idx_route_locations ON transportation_routes(departure_location, arrival_location)
```
**Storage:** PostgreSQL
**Ví dụ Tàu Hà Nội - Sài Gòn:**
- Route 1: Hà Nội → Vinh (300km, 5h, 200k)
- Route 2: Vinh → Huế (400km, 6h, 250k)
- Route 3: Huế → Đà Nẵng (100km, 2h, 100k)

#### transportation_schedules (Lịch chạy)
```sql
id                  UUID PK
transportation_id   UUID FK → transportation NOT NULL
route_id            UUID FK → transportation_routes NOT NULL
departure_date      DATE NOT NULL
departure_time      TIME NOT NULL
arrival_time        TIME NOT NULL
total_seats         INTEGER NOT NULL
available_seats     INTEGER NOT NULL
price_override      DECIMAL (nullable, override route price)
is_bookable         BOOLEAN DEFAULT TRUE
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()

-- Indexes
INDEX idx_schedule_transport ON transportation_schedules(transportation_id, departure_date)
INDEX idx_schedule_route ON transportation_schedules(route_id, departure_date)
INDEX idx_schedule_date ON transportation_schedules(departure_date, is_bookable)
UNIQUE (transportation_id, route_id, departure_date, departure_time)
```
**Storage:** PostgreSQL
**Lý do:** Cần update available_seats real-time, ACID critical

#### transportation_seat_classes (Hạng ghế)
```sql
id                  UUID PK
transportation_id   UUID FK → transportation
class_name          VARCHAR (VIP, A, B, C, SLEEPER, STANDARD)
description         TEXT
price_modifier      DECIMAL (+ hoặc - từ base price)
total_seats         INTEGER (tổng số ghế hạng này)
amenities           TEXT[] (AC, WiFi, Meal, Blanket...)
display_order       INTEGER
is_active           BOOLEAN

INDEX idx_transport_class ON (transportation_id, is_active)
```
**Ví dụ:**
- Tàu: VIP (giường nằm khoang 2), A (giường nằm khoang 4), B (ghế ngồi mềm), C (ghế ngồi cứng)
- Xe: VIP (ghế massage), A (ghế nằm), B (ghế ngồi)

#### transportation_seats (Chỗ ngồi cụ thể)
```sql
id                  UUID PK
schedule_id         UUID FK → transportation_schedules
seat_class_id       UUID FK → transportation_seat_classes
seat_number         VARCHAR (VD: "A1", "B2", "12")
status              ENUM (AVAILABLE, BOOKED, BLOCKED)

INDEX idx_schedule_status ON (schedule_id, status)
INDEX idx_schedule_class ON (schedule_id, seat_class_id)
UNIQUE (schedule_id, seat_number)
```
**Lý do:** Track từng chỗ ngồi cụ thể, mỗi schedule có sơ đồ ghế riêng

---

### 3. RESTAURANTS (MỚI - Nhà hàng)

#### restaurants
```sql
id                  UUID PK
name                VARCHAR NOT NULL
slug                VARCHAR UNIQUE NOT NULL
description         TEXT
cuisine_type        VARCHAR (Vietnamese, Chinese, Western...)
address             TEXT NOT NULL
province            VARCHAR NOT NULL
latitude            DECIMAL
longitude           DECIMAL
images              TEXT[]
price_range         VARCHAR ($$, $$$)
opening_time        TIME NOT NULL
closing_time        TIME NOT NULL
is_active           BOOLEAN DEFAULT TRUE
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()
deleted_at          TIMESTAMP

-- Indexes
INDEX idx_restaurant_slug ON restaurants(slug)
INDEX idx_restaurant_province ON restaurants(province, is_active)
INDEX idx_restaurant_cuisine ON restaurants(cuisine_type, is_active)
INDEX idx_restaurant_deleted ON restaurants(deleted_at) WHERE deleted_at IS NULL
```
**Storage:** PostgreSQL
**Lý do:** Core data, cần ACID

#### restaurant_tables (Bàn ăn - Không cần số bàn cụ thể)
```sql
id                  UUID PK
restaurant_id       UUID FK → restaurants NOT NULL
table_type          VARCHAR NOT NULL (2_SEATS, 4_SEATS, 6_SEATS, VIP_ROOM)
total_tables        INTEGER NOT NULL (tổng số bàn loại này)
price_per_table     DECIMAL DEFAULT 0 (giá đặt bàn, có thể 0)

-- Indexes
INDEX idx_table_restaurant ON restaurant_tables(restaurant_id)
```
**Storage:** PostgreSQL

#### restaurant_availability (Khung giờ đặt bàn)
```sql
id                  UUID PK
restaurant_id       UUID FK → restaurants NOT NULL
table_type_id       UUID FK → restaurant_tables NOT NULL
available_date      DATE NOT NULL
time_slot           TIME NOT NULL (VD: 11:00, 12:00, 18:00, 19:00, 20:00)
total_tables        INTEGER NOT NULL
available_tables    INTEGER NOT NULL
is_bookable         BOOLEAN DEFAULT TRUE
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()

-- Indexes
INDEX idx_rest_avail_date ON restaurant_availability(restaurant_id, available_date)
INDEX idx_rest_avail_slot ON restaurant_availability(restaurant_id, available_date, time_slot)
UNIQUE (restaurant_id, table_type_id, available_date, time_slot)
```
**Storage:** PostgreSQL
**Lý do:** Chỉ track số lượng bàn trống, không cần biết bàn số mấy, cần ACID

---

### 4. HOTELS (MỚI - Khách sạn)

#### hotels
```sql
id                  UUID PK
name                VARCHAR NOT NULL
slug                VARCHAR UNIQUE NOT NULL
description         TEXT
star_rating         INTEGER CHECK (star_rating BETWEEN 1 AND 5)
address             TEXT NOT NULL
province            VARCHAR NOT NULL
latitude            DECIMAL
longitude           DECIMAL
images              TEXT[]
check_in_time       TIME DEFAULT '14:00'
check_out_time      TIME DEFAULT '12:00'
is_active           BOOLEAN DEFAULT TRUE
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()
deleted_at          TIMESTAMP

-- Indexes
INDEX idx_hotel_slug ON hotels(slug)
INDEX idx_hotel_province ON hotels(province, is_active)
INDEX idx_hotel_rating ON hotels(star_rating, is_active)
INDEX idx_hotel_deleted ON hotels(deleted_at) WHERE deleted_at IS NULL
```
**Storage:** PostgreSQL
**Lý do:** Core data, cần ACID

#### hotel_rooms (Loại phòng - Không cần số phòng cụ thể)
```sql
id                  UUID PK
hotel_id            UUID FK → hotels NOT NULL
room_type           VARCHAR NOT NULL (STANDARD, DELUXE, SUITE, VIP)
total_rooms         INTEGER NOT NULL (tổng số phòng loại này)
price_per_night     DECIMAL NOT NULL
max_guests          INTEGER NOT NULL
description         TEXT

-- Indexes
INDEX idx_room_hotel ON hotel_rooms(hotel_id)
```
**Storage:** PostgreSQL

#### hotel_availability (Phòng trống theo ngày)
```sql
id                  UUID PK
hotel_id            UUID FK → hotels NOT NULL
room_type_id        UUID FK → hotel_rooms NOT NULL
available_date      DATE NOT NULL (ngày check-in)
total_rooms         INTEGER NOT NULL
available_rooms     INTEGER NOT NULL
price_override      DECIMAL (nullable)
is_bookable         BOOLEAN DEFAULT TRUE
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()

-- Indexes
INDEX idx_hotel_avail_date ON hotel_availability(hotel_id, available_date)
INDEX idx_hotel_avail_room ON hotel_availability(hotel_id, room_type_id, available_date)
UNIQUE (hotel_id, room_type_id, available_date)
```
**Storage:** PostgreSQL
**Lý do:** Chỉ track số phòng trống, không cần biết phòng số mấy, cần ACID

---

### 5. ORDERS (Mở rộng từ backend cũ)

#### orders
```sql
id                  UUID PK
order_number        VARCHAR UNIQUE NOT NULL
user_id             UUID NOT NULL (Keycloak)
total_amount        DECIMAL NOT NULL
discount_amount     DECIMAL DEFAULT 0
final_amount        DECIMAL NOT NULL
status              VARCHAR NOT NULL (PENDING, CONFIRMED, COMPLETED, CANCELLED)
payment_status      VARCHAR NOT NULL (PENDING, PAID, FAILED, EXPIRED)
customer_info       JSONB NOT NULL (snapshot: name, phone, email, address)
notes               TEXT
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()
confirmed_at        TIMESTAMP
completed_at        TIMESTAMP

-- Indexes
INDEX idx_order_number ON orders(order_number)
INDEX idx_order_user ON orders(user_id, created_at DESC)
INDEX idx_order_status ON orders(status, payment_status)
INDEX idx_order_date ON orders(created_at DESC)
```
**Storage:** PostgreSQL
**Lý do:** Financial data, ACID critical
**Thay đổi:** Bỏ `paymentMethod` (chuyển sang payments table)

---

#### order_items (Mở rộng - Support multi-service)
```sql
id                  UUID PK
order_id            UUID FK → orders NOT NULL
item_type           VARCHAR NOT NULL (TOUR, TRANSPORTATION, RESTAURANT, HOTEL)
item_id             UUID NOT NULL (tour_id, transportation_id, restaurant_id, hotel_id)

-- Snapshot
item_snapshot       JSONB NOT NULL (name, description, images)

-- Pricing
quantity            INTEGER DEFAULT 1
unit_price          DECIMAL NOT NULL
discount            DECIMAL DEFAULT 0
final_price         DECIMAL NOT NULL

-- Booking details (flexible per type)
booking_details     JSONB NOT NULL

status              VARCHAR NOT NULL (PENDING, CONFIRMED, CANCELLED)
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()

-- Indexes
INDEX idx_item_order ON order_items(order_id)
INDEX idx_item_type ON order_items(item_type, item_id)
INDEX idx_item_status ON order_items(status)
```
**Storage:** PostgreSQL
**Lý do:** Financial data, ACID critical

**booking_details examples:**
```json
// Tour (giữ nguyên logic cũ)
{
  "tour_date": "2026-03-01",
  "participants": [
    {"name": "Nguyen Van A", "age": 30, "phone": "0901234567"}
  ]
}

// Transportation
{
  "schedule_id": "uuid",
  "route_name": "Hà Nội - Vinh",
  "departure_date": "2026-03-01",
  "departure_time": "08:00",
  "arrival_time": "13:00",
  "seat_class": "VIP",
  "seats": [
    {"seat_number": "A1", "passenger_name": "Nguyen Van A", "passenger_id": "123456789"},
    {"seat_number": "A2", "passenger_name": "Tran Thi B", "passenger_id": "987654321"}
  ]
}

// Restaurant
{
  "reservation_date": "2026-03-01",
  "time_slot": "19:00",
  "table_type": "4_SEATS",
  "guests_count": 4,
  "special_requests": "Window seat, birthday cake"
}

// Hotel
{
  "check_in": "2026-03-01",
  "check_out": "2026-03-03",
  "nights": 2,
  "room_type": "DELUXE",
  "guests": [
    {"name": "Nguyen Van A", "age": 30}
  ],
  "special_requests": "High floor"
}
```

---

### 6. BOOKINGS (MỚI - Operational tracking)

#### bookings
```sql
id                  UUID PK
order_item_id       UUID UNIQUE FK → order_items NOT NULL
item_type           VARCHAR NOT NULL (TOUR, TRANSPORTATION, RESTAURANT, HOTEL)
item_id             UUID NOT NULL
user_id             UUID NOT NULL (Keycloak)

booking_code        VARCHAR UNIQUE NOT NULL (for QR)
booking_date        DATE NOT NULL
booking_time        TIME (nullable)

status              VARCHAR NOT NULL (PENDING, CONFIRMED, CHECKED_IN, COMPLETED, CANCELLED, NO_SHOW)

check_in_at         TIMESTAMP
check_out_at        TIMESTAMP
checked_in_by       UUID (admin)

qr_code_data        VARCHAR NOT NULL (booking_code)
qr_scanned_at       TIMESTAMP

notes               TEXT
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()

-- Indexes
INDEX idx_booking_code ON bookings(booking_code)
INDEX idx_booking_user ON bookings(user_id, booking_date DESC)
INDEX idx_booking_item ON bookings(item_type, item_id)
INDEX idx_booking_status ON bookings(status, booking_date)
INDEX idx_booking_qr ON bookings(qr_code_data)
```
**Storage:** PostgreSQL
**Lý do:** Operational data, cần ACID
**Mục đích:** Tách biệt financial (order) vs operational (booking)

---

### 7. PAYMENTS (MỚI - Thay thế paymentMethod trong Order)

#### payments
```sql
id                  UUID PK
order_id            UUID FK → orders NOT NULL
payment_method      VARCHAR NOT NULL (BANK_TRANSFER, QR_BANKING, E_WALLET)
payment_provider    VARCHAR DEFAULT 'sepay'
amount              DECIMAL NOT NULL
transaction_id      VARCHAR UNIQUE
sepay_order_id      VARCHAR
status              VARCHAR NOT NULL (PENDING, SUCCESS, FAILED, EXPIRED)
gateway_response    JSONB
paid_at             TIMESTAMP
expires_at          TIMESTAMP NOT NULL (15 phút)
created_at          TIMESTAMP DEFAULT NOW()
updated_at          TIMESTAMP DEFAULT NOW()

-- Indexes
INDEX idx_payment_order ON payments(order_id)
INDEX idx_payment_transaction ON payments(transaction_id)
INDEX idx_payment_status ON payments(status, created_at DESC)
INDEX idx_payment_expires ON payments(expires_at) WHERE status = 'PENDING'
```
**Storage:** PostgreSQL
**Lý do:** Financial data, ACID critical

---

### 8. VOUCHERS (MỚI)

#### vouchers
```sql
id                      UUID PK
code                    VARCHAR UNIQUE NOT NULL
name                    VARCHAR NOT NULL
description             TEXT
discount_type           VARCHAR NOT NULL (PERCENTAGE, FIXED_AMOUNT)
discount_value          DECIMAL NOT NULL
min_order_amount        DECIMAL DEFAULT 0
max_discount_amount     DECIMAL
usage_limit             INTEGER (null = unlimited)
usage_per_user          INTEGER DEFAULT 1
valid_from              TIMESTAMP NOT NULL
valid_to                TIMESTAMP NOT NULL
applicable_item_types   VARCHAR[] (TOUR, TRANSPORTATION, RESTAURANT, HOTEL, ALL)
status                  VARCHAR NOT NULL (ACTIVE, INACTIVE, EXPIRED)
created_by              UUID (admin)
created_at              TIMESTAMP DEFAULT NOW()
updated_at              TIMESTAMP DEFAULT NOW()

-- Indexes
INDEX idx_voucher_code ON vouchers(code)
INDEX idx_voucher_status ON vouchers(status, valid_from, valid_to)
INDEX idx_voucher_dates ON vouchers(valid_from, valid_to) WHERE status = 'ACTIVE'
```
**Storage:** PostgreSQL
**Lý do:** Cần ACID cho voucher usage tracking

#### voucher_usage
```sql
id              UUID PK
voucher_id      UUID FK → vouchers NOT NULL
user_id         UUID NOT NULL (Keycloak)
order_id        UUID FK → orders NOT NULL
discount_amount DECIMAL NOT NULL
used_at         TIMESTAMP DEFAULT NOW()

-- Indexes
INDEX idx_usage_voucher ON voucher_usage(voucher_id, used_at DESC)
INDEX idx_usage_user ON voucher_usage(user_id, used_at DESC)
INDEX idx_usage_order ON voucher_usage(order_id)
```
**Storage:** PostgreSQL
**Lý do:** Audit trail, cần ACID

---

### 9. REVENUE_REPORTS (Giữ từ backend cũ)

#### revenue_reports
```sql
id              UUID PK
month           INTEGER NOT NULL CHECK (month BETWEEN 1 AND 12)
year            INTEGER NOT NULL CHECK (year >= 2024)
total_revenue   DECIMAL NOT NULL DEFAULT 0
total_orders    INTEGER NOT NULL DEFAULT 0
breakdown       JSONB NOT NULL (by item_type)
created_at      TIMESTAMP DEFAULT NOW()
updated_at      TIMESTAMP DEFAULT NOW()

-- Indexes
UNIQUE (month, year)
INDEX idx_revenue_date ON revenue_reports(year DESC, month DESC)
```
**Storage:** PostgreSQL
**Lý do:** Financial reporting, cần ACID

**breakdown example:**
```json
{
  "TOUR": {"revenue": 50000000, "orders": 100},
  "TRANSPORTATION": {"revenue": 20000000, "orders": 150},
  "RESTAURANT": {"revenue": 10000000, "orders": 80},
  "HOTEL": {"revenue": 30000000, "orders": 60}
}
```

---

## REDIS (Cache & Session)

### 1. Cart (Mở rộng multi-service)
```redis
Key: cart:{userId}
Type: Hash
TTL: 7 days

Field: item-{uuid}
Value: {
  "item_type": "TOUR",
  "item_id": "uuid",
  "name": "Tour Hạ Long",
  "slug": "tour-ha-long",
  "price": 5000000,
  "discount": 10,
  "quantity": 2,
  "image": "url",
  "booking_details": {...}
}
```
**Lý do:** Session data, không cần persist, TTL auto-cleanup

---

### 2. Inventory Cache (Real-time availability)

#### Tour Inventory
```redis
Key: inventory:tour:{tourId}
Type: Hash
TTL: 1 hour
Fields:
  - slots: 50
  - available_slots: 30
  - price: 5000000
  - discount: 10
  - is_bookable: true
```

#### Transportation Schedule Inventory
```redis
Key: inventory:transport:schedule:{scheduleId}
Type: Hash
TTL: 30 minutes
Fields:
  - total_seats: 100
  - available_seats: 45
  - price: 500000
  - is_bookable: true
```

#### Transportation Seat Status
```redis
Key: inventory:transport:seats:{scheduleId}
Type: Hash
TTL: 30 minutes
Field: {seatNumber} (VD: "A1", "B2")
Value: "AVAILABLE" | "BOOKED" | "BLOCKED"
```

#### Restaurant Availability
```redis
Key: inventory:restaurant:{restaurantId}:{date}:{timeSlot}:{tableTypeId}
Type: Hash
TTL: 1 hour
Fields:
  - total_tables: 10
  - available_tables: 5
  - price_per_table: 0
```

#### Hotel Availability
```redis
Key: inventory:hotel:{hotelId}:{roomTypeId}:{date}
Type: Hash
TTL: 1 hour
Fields:
  - total_rooms: 20
  - available_rooms: 8
  - price: 1000000
  - is_bookable: true
```

**Lý do:** High-read operations, reduce DB load, TTL auto-refresh

---

### 3. Payment Session
```redis
Key: payment:{orderId}
Type: Hash
TTL: 15 minutes
Fields:
  - sepay_order_id: "SEP123456"
  - payment_url: "https://..."
  - amount: 10000000
  - status: "PENDING"
  - created_at: "2026-02-02T10:00:00Z"
```
**Lý do:** Temporary payment session, auto-expire

---

### 4. Voucher Counter (Rate limiting)
```redis
# Global usage counter
Key: voucher:{code}:usage
Type: String (counter)
TTL: voucher expiry
Value: 150 (current usage count)

# Per-user usage counter
Key: voucher:{code}:user:{userId}
Type: String (counter)
TTL: voucher expiry
Value: 2 (user usage count)
```
**Lý do:** Fast atomic increment, prevent over-usage

---

### 5. Session & Auth (Keycloak integration)
```redis
Key: session:{sessionId}
Type: Hash
TTL: 30 days
Fields:
  - user_id: "keycloak-uuid"
  - role: "USER"
  - email: "user@example.com"
  - last_activity: "2026-02-02T10:00:00Z"
```
**Lý do:** Fast session lookup, reduce Keycloak calls

---

### 6. Rate Limiting
```redis
# API rate limit
Key: ratelimit:api:{userId}:{endpoint}
Type: String (counter)
TTL: 1 minute
Value: 10 (request count)

# OTP rate limit
Key: ratelimit:otp:{email}
Type: String (counter)
TTL: 5 minutes
Value: 3 (OTP send count)
```
**Lý do:** Prevent abuse, DDoS protection

---

### 7. Search Cache
```redis
Key: search:{query_hash}
Type: String (JSON)
TTL: 5 minutes
Value: {"results": [...], "total": 100}
```
**Lý do:** Cache popular searches, reduce Elasticsearch load

---

## MONGODB (Flexible Schema Data)

### 1. service_contents (Nội dung chi tiết)
```javascript
{
  _id: ObjectId,
  item_type: "TOUR", // TOUR, TRANSPORTATION, RESTAURANT, HOTEL
  item_id: "uuid",
  
  // Rich content
  description_html: "<p>...</p>",
  highlights: ["Tham quan Vịnh Hạ Long", "Khám phá hang động"],
  
  // Tour specific
  itinerary_detailed: [
    {
      day: 1,
      title: "Ngày 1: Hà Nội - Hạ Long",
      activities: [
        {time: "08:00", description: "Khởi hành từ Hà Nội"},
        {time: "12:00", description: "Ăn trưa tại nhà hàng"}
      ],
      meals: ["Trưa", "Tối"],
      accommodation: "Khách sạn 4 sao"
    }
  ],
  includes: ["Khách sạn 4 sao", "Vé tham quan", "Bảo hiểm"],
  excludes: ["Tiền tip", "Chi phí cá nhân"],
  
  // Transportation specific
  vehicle_info: {
    seats_layout: "2-2", // bus: 2-2, train: 4-berth
    total_seats: 45,
    amenities: ["AC", "WiFi", "Toilet", "USB Charging"],
    vehicle_images: ["url1", "url2"]
  },
  route_details: {
    stops: [
      {name: "Hà Nội", arrival: null, departure: "08:00"},
      {name: "Ninh Bình", arrival: "10:00", departure: "10:15"},
      {name: "Vinh", arrival: "13:00", departure: null}
    ]
  },
  
  // Restaurant specific
  menu: [
    {
      category: "Khai vị",
      items: [
        {name: "Gỏi cuốn", price: 50000, description: "..."}
      ]
    }
  ],
  chef_info: {name: "...", bio: "..."},
  ambiance: ["Romantic", "Family-friendly"],
  
  // Hotel specific
  facilities: ["wifi", "pool", "gym", "spa", "restaurant"],
  room_details: {
    amenities: ["TV", "AC", "Minibar", "Safe"],
    bed_type: "King bed",
    room_size: "35 sqm"
  },
  policies: {
    check_in: "14:00",
    check_out: "12:00",
    cancellation: "Free cancellation 24h before",
    pets: false,
    smoking: false
  },
  nearby_attractions: ["Hồ Hoàn Kiếm (2km)", "Phố cổ (1km)"],
  
  // SEO
  seo: {
    meta_title: "...",
    meta_description: "...",
    keywords: ["du lịch", "hạ long"]
  },
  
  created_at: ISODate("2026-01-01T00:00:00Z"),
  updated_at: ISODate("2026-02-01T00:00:00Z")
}

// Indexes
db.service_contents.createIndex({item_type: 1, item_id: 1}, {unique: true})
db.service_contents.createIndex({item_type: 1, updated_at: -1})
```
**Storage:** MongoDB
**Lý do:** Flexible schema, rich content, mỗi service type có structure khác nhau

---

### 2. reviews (Đánh giá)
```javascript
{
  _id: ObjectId,
  item_type: "TOUR",
  item_id: "uuid",
  user_id: "keycloak-id",
  user_name: "Nguyen Van A", // snapshot
  user_avatar: "url",
  order_id: "uuid",
  order_item_id: "uuid",
  
  rating: 5, // 1-5
  title: "Tuyệt vời!",
  content: "Tour rất đáng giá tiền, hướng dẫn viên nhiệt tình...",
  images: ["url1", "url2"],
  
  verified_purchase: true,
  status: "APPROVED", // PENDING, APPROVED, REJECTED
  
  helpful_count: 10, // số người thấy hữu ích
  
  admin_response: {
    content: "Cảm ơn bạn đã đánh giá...",
    responded_by: "admin-id",
    responded_at: ISODate("2026-02-02T10:00:00Z")
  },
  
  created_at: ISODate("2026-02-01T10:00:00Z"),
  updated_at: ISODate("2026-02-02T10:00:00Z")
}

// Indexes
db.reviews.createIndex({item_type: 1, item_id: 1, created_at: -1})
db.reviews.createIndex({user_id: 1, created_at: -1})
db.reviews.createIndex({status: 1, created_at: -1})
db.reviews.createIndex({rating: 1, verified_purchase: 1})
```
**Storage:** MongoDB
**Lý do:** High volume, flexible content (images, text), không cần ACID

---

### 3. chat_messages (Chat)
```javascript
{
  _id: ObjectId,
  room_id: "support-123",
  sender_id: "keycloak-id",
  sender_name: "Nguyen Van A",
  sender_type: "USER", // USER, ADMIN, BOT
  
  message_type: "TEXT", // TEXT, IMAGE, FILE
  content: "Tôi muốn hỏi về tour Hạ Long",
  
  attachments: [
    {type: "image", url: "...", filename: "..."}
  ],
  
  is_bot_message: false,
  bot_context: {intent: "tour_inquiry", confidence: 0.95},
  
  read_at: ISODate("2026-02-02T10:05:00Z"),
  
  created_at: ISODate("2026-02-02T10:00:00Z")
}

// Indexes
db.chat_messages.createIndex({room_id: 1, created_at: -1})
db.chat_messages.createIndex({sender_id: 1, created_at: -1})
db.chat_messages.createIndex({created_at: -1}) // TTL index for cleanup
```
**Storage:** MongoDB
**Lý do:** High write volume, time-series data, flexible schema

---

### 4. chat_rooms
```javascript
{
  _id: "support-123",
  user_id: "keycloak-id",
  user_name: "Nguyen Van A",
  user_email: "user@example.com",
  
  assigned_to: "admin-id",
  assigned_to_name: "Admin Nguyen",
  
  status: "OPEN", // OPEN, CLOSED, RESOLVED
  priority: "NORMAL", // LOW, NORMAL, HIGH, URGENT
  subject: "Hỏi về tour Hạ Long",
  
  tags: ["tour", "inquiry"],
  
  last_message: {
    content: "...",
    sender_type: "ADMIN",
    created_at: ISODate("2026-02-02T10:00:00Z")
  },
  
  unread_count: 2,
  
  created_at: ISODate("2026-02-01T10:00:00Z"),
  updated_at: ISODate("2026-02-02T10:00:00Z"),
  closed_at: null
}

// Indexes
db.chat_rooms.createIndex({user_id: 1, status: 1})
db.chat_rooms.createIndex({assigned_to: 1, status: 1})
db.chat_rooms.createIndex({status: 1, updated_at: -1})
db.chat_rooms.createIndex({priority: 1, status: 1})
```
**Storage:** MongoDB
**Lý do:** Flexible schema, real-time updates

---

### 5. activity_logs (Audit trail)
```javascript
{
  _id: ObjectId,
  user_id: "keycloak-id",
  user_email: "user@example.com",
  user_role: "ADMIN",
  
  action: "ORDER_CREATED", // ORDER_CREATED, TOUR_UPDATED, USER_DELETED...
  resource_type: "ORDER",
  resource_id: "uuid",
  
  changes: {
    before: {status: "PENDING"},
    after: {status: "CONFIRMED"}
  },
  
  metadata: {
    order_number: "ORD-001",
    total_amount: 10000000
  },
  
  ip_address: "192.168.1.1",
  user_agent: "Mozilla/5.0...",
  
  timestamp: ISODate("2026-02-02T10:00:00Z")
}

// Indexes
db.activity_logs.createIndex({user_id: 1, timestamp: -1})
db.activity_logs.createIndex({resource_type: 1, resource_id: 1, timestamp: -1})
db.activity_logs.createIndex({action: 1, timestamp: -1})
db.activity_logs.createIndex({timestamp: -1}) // TTL index for cleanup
```
**Storage:** MongoDB
**Lý do:** High write volume, time-series data, audit trail không cần ACID

---

### 6. ai_chat_history (AI Chatbot - Groq)
```javascript
{
  _id: ObjectId,
  user_id: "keycloak-id",
  session_id: "session-123",
  
  messages: [
    {
      role: "user",
      content: "Tôi muốn đi du lịch Hạ Long",
      timestamp: ISODate("2026-02-02T10:00:00Z")
    },
    {
      role: "assistant",
      content: "Chúng tôi có nhiều tour Hạ Long...",
      timestamp: ISODate("2026-02-02T10:00:05Z")
    }
  ],
  
  context: {
    intent: "tour_search",
    entities: {location: "Hạ Long"},
    suggested_tours: ["uuid1", "uuid2"]
  },
  
  created_at: ISODate("2026-02-02T10:00:00Z"),
  updated_at: ISODate("2026-02-02T10:05:00Z")
}

// Indexes
db.ai_chat_history.createIndex({user_id: 1, created_at: -1})
db.ai_chat_history.createIndex({session_id: 1})
db.ai_chat_history.createIndex({created_at: -1}) // TTL index
```
**Storage:** MongoDB
**Lý do:** Flexible schema, conversation history, không cần ACID

---

## ELASTICSEARCH (Full-text Search)

### services_search (Tất cả services)

**Index:** services_search

**Mappings:**
```json
{
  "item_type": "TOUR",
  "item_id": "uuid",
  "name": "Tour Hạ Long 3N2Đ",
  "slug": "tour-ha-long-3n2d",
  "description": "...",
  
  "price": 5000000,
  "discount": 10,
  
  "province": "Quảng Ninh",
  "region": "Bắc",
  
  "rating": 4.5,
  "total_reviews": 150,
  
  "is_bookable": true,
  
  "created_at": "2026-01-01T00:00:00Z"
}
```

```json
{
  "mappings": {
    "properties": {
      "item_type": {"type": "keyword"},
      "item_id": {"type": "keyword"},
      "name": {
        "type": "text",
        "analyzer": "vietnamese",
        "fields": {
          "keyword": {"type": "keyword"}
        }
      },
      "slug": {"type": "keyword"},
      "description": {
        "type": "text",
        "analyzer": "vietnamese"
      },
      "price": {"type": "double"},
      "discount": {"type": "integer"},
      "final_price": {"type": "double"},
      
      "province": {"type": "keyword"},
      "region": {"type": "keyword"},
      "category": {"type": "keyword"},
      
      "rating": {"type": "float"},
      "total_reviews": {"type": "integer"},
      
      "is_bookable": {"type": "boolean"},
      "is_active": {"type": "boolean"},
      
      "tags": {"type": "keyword"},
      
      "created_at": {"type": "date"},
      "updated_at": {"type": "date"}
    }
  }
}
```

**Document example:**
```json
{
  "item_type": "TOUR",
  "item_id": "uuid-123",
  "name": "Tour Hạ Long 3 Ngày 2 Đêm",
  "slug": "tour-ha-long-3n2d",
  "description": "Khám phá vẻ đẹp kỳ vĩ của Vịnh Hạ Long...",
  
  "price": 5000000,
  "discount": 10,
  "final_price": 4500000,
  
  "province": "Quảng Ninh",
  "region": "Bắc",
  "category": "Biển",
  
  "rating": 4.5,
  "total_reviews": 150,
  
  "is_bookable": true,
  "is_active": true,
  
  "tags": ["biển", "du thuyền", "hang động"],
  
  "created_at": "2026-01-01T00:00:00Z",
  "updated_at": "2026-02-01T00:00:00Z"
}
```

**Lý do dùng Elasticsearch:**
- ✅ Full-text search với Vietnamese analyzer
- ✅ Faceted search (filter by province, category, price range)
- ✅ Fuzzy search (typo tolerance)
- ✅ Aggregations (count by region, category)
- ✅ Fast search across all service types
- ✅ Relevance scoring

**Sync strategy:**
- PostgreSQL → Elasticsearch (via Change Data Capture hoặc Application-level sync)
- Update Elasticsearch khi create/update/delete service
- Rebuild index nếu cần (background job)

---

## 🔄 LUỒNG HOẠT ĐỘNG

### 1. User book Tour (Giữ nguyên logic cũ)
```
1. User search tour → Elasticsearch
2. User view tour → PostgreSQL + MongoDB
3. User add to cart → Redis
4. User checkout → Create order + order_items
5. Payment (SePay) → Create payment
6. Confirm → Update tour.available_slots
7. Create booking → Send email with QR
```

### 2. User book Transportation (MỚI)
```
1. User search "Hà Nội - Vinh" → PostgreSQL
2. User chọn tàu SE1, ngày 2026-03-01, 08:00
3. User chọn chỗ A1, A2 → Check transportation_seats
4. Add to cart → Redis
5. Checkout → Create order_items (item_type: TRANSPORTATION)
6. Payment → Update transportation_seats (status: BOOKED)
7. Create booking → Send QR
```

### 3. User book Restaurant (MỚI)
```
1. User search restaurant → Elasticsearch
2. User chọn nhà hàng, ngày 2026-03-01, 19:00, bàn 4 người
3. Check restaurant_availability
4. Add to cart → Redis
5. Checkout → Create order_items (item_type: RESTAURANT)
6. Payment → Update restaurant_availability (available_tables - 1)
7. Create booking → Send QR
```

### 4. User book Hotel (MỚI)
```
1. User search hotel → Elasticsearch
2. User chọn hotel, check-in 2026-03-01, check-out 2026-03-03, Deluxe room
3. Check hotel_availability (2 ngày)
4. Add to cart → Redis
5. Checkout → Create order_items (item_type: HOTEL)
6. Payment → Update hotel_availability (available_rooms - 1 cho 2 ngày)
7. Create booking → Send QR
```

### 5. Multi-service Order (MỚI)
```
User có thể book:
- 1 Tour + 1 Transportation + 1 Restaurant + 1 Hotel
→ 1 Order với 4 order_items
→ 1 Payment
→ 4 Bookings (4 QR codes)
```

---

## 🎯 QUYẾT ĐỊNH QUAN TRỌNG

### Order Strategy: **GOM CHUNG 1 ORDER**

**Lý do:**
- ✅ User trải nghiệm tốt (1 lần thanh toán)
- ✅ Dễ quản lý (1 order number)
- ✅ Dễ apply voucher (cho cả order)
- ✅ Dễ tracking (1 order status)

**Implementation:**
- 1 Order có nhiều order_items
- Mỗi order_item có item_type khác nhau
- Mỗi order_item tạo 1 booking riêng
- Mỗi booking có QR code riêng

**Ví dụ:**
```
Order #ORD-001
├─ Item 1: Tour Hạ Long (TOUR) → Booking #BK-001 → QR-001
├─ Item 2: Tàu SE1 (TRANSPORTATION) → Booking #BK-002 → QR-002
├─ Item 3: Nhà hàng ABC (RESTAURANT) → Booking #BK-003 → QR-003
└─ Item 4: Hotel XYZ (HOTEL) → Booking #BK-004 → QR-004

Total: 10,000,000 VND
Voucher: -500,000 VND
Final: 9,500,000 VND
Payment: 1 lần (SePay)
```

---

---

## 📊 TỔNG KẾT

### Storage Distribution

**PostgreSQL Tables:** 18 tables
- Tours: 1 (giữ nguyên)
- Transportation: 5 (mới: transportation, routes, schedules, seat_classes, seats)
- Restaurant: 3 (mới: restaurants, tables, availability)
- Hotel: 3 (mới: hotels, rooms, availability)
- Orders: 3 (mở rộng: orders, order_items, bookings)
- Payments: 1 (mới)
- Vouchers: 2 (mới)
- Revenue: 1 (giữ nguyên)

**MongoDB Collections:** 6
- service_contents (rich content)
- reviews (user reviews)
- chat_messages (real-time chat)
- chat_rooms (chat sessions)
- activity_logs (audit trail)
- ai_chat_history (AI chatbot - Groq)

**Redis Keys:** 7 patterns
- cart (shopping cart)
- inventory (availability cache)
- payment (payment sessions)
- voucher (usage counters)
- session (user sessions)
- ratelimit (rate limiting)
- search (search cache)

**Elasticsearch Indexes:** 1
- services_search (all service types)

---

### 🎯 STORAGE STRATEGY (Tại sao chọn SQL/NoSQL/Redis/Elasticsearch?)

#### PostgreSQL (Core Transactional Data)
**Dùng cho:**
- ✅ Tours, Transportation, Restaurant, Hotel (master data)
- ✅ Orders, Order Items, Bookings (financial + operational)
- ✅ Payments (financial transactions)
- ✅ Vouchers (usage tracking)
- ✅ Availability (inventory management)

**Lý do:**
- 🔒 **ACID transactions** critical cho booking & payment
- 🔗 **Foreign keys** đảm bảo referential integrity
- 📊 **Complex queries** với JOIN, aggregation
- 💰 **Financial data** không được sai
- 🎯 **Inventory management** cần atomic updates (available_slots, available_seats)

**Ví dụ critical transactions:**
```sql
-- Book tour: Phải atomic
BEGIN;
  UPDATE tours SET available_slots = available_slots - 2 WHERE id = ? AND available_slots >= 2;
  INSERT INTO order_items (...);
  INSERT INTO bookings (...);
COMMIT;
```

---

#### MongoDB (Flexible Schema Data)
**Dùng cho:**
- ✅ service_contents (rich content, mỗi service type khác nhau)
- ✅ reviews (flexible content: text, images, ratings)
- ✅ chat_messages (high write volume, time-series)
- ✅ activity_logs (audit trail, không cần ACID)
- ✅ ai_chat_history (conversation history)

**Lý do:**
- 📝 **Flexible schema**: Mỗi service type có structure khác nhau
- 📈 **High write volume**: Chat messages, activity logs
- 🕐 **Time-series data**: Logs, chat history
- 🚫 **Không cần ACID**: Review bị mất không ảnh hưởng financial
- 🔍 **Nested documents**: Itinerary, menu, facilities

**Ví dụ flexible schema:**
```javascript
// Tour có itinerary_detailed
{item_type: "TOUR", itinerary_detailed: [...]}

// Transportation có vehicle_info
{item_type: "TRANSPORTATION", vehicle_info: {...}}

// Restaurant có menu
{item_type: "RESTAURANT", menu: [...]}
```

---

#### Redis (Cache & Session)
**Dùng cho:**
- ✅ Cart (session data, TTL 7 days)
- ✅ Inventory cache (reduce DB load)
- ✅ Payment sessions (TTL 15 minutes)
- ✅ Voucher counters (atomic increment)
- ✅ Rate limiting (prevent abuse)
- ✅ Search cache (popular queries)

**Lý do:**
- ⚡ **Ultra-fast**: In-memory, sub-millisecond latency
- 🔄 **TTL auto-cleanup**: Cart, payment sessions tự xóa
- 🔢 **Atomic operations**: INCR cho voucher counter
- 📉 **Reduce DB load**: Cache inventory, search results
- 🚫 **Không cần persist**: Cart mất không sao, user add lại

**Ví dụ critical use cases:**
```redis
# Voucher counter (prevent over-usage)
INCR voucher:SUMMER2026:usage
GET voucher:SUMMER2026:usage  # Check limit

# Inventory cache (reduce PostgreSQL load)
HGET inventory:tour:uuid-123 available_slots
```

---

#### Elasticsearch (Full-text Search)
**Dùng cho:**
- ✅ services_search (all service types)

**Lý do:**
- 🔍 **Full-text search**: Vietnamese analyzer, fuzzy search
- 🎯 **Faceted search**: Filter by province, category, price
- 📊 **Aggregations**: Count by region, category
- ⚡ **Fast search**: Inverted index
- 🌐 **Multi-field search**: Search across name, description, tags

**Ví dụ search queries:**
```json
// Full-text search
GET /services_search/_search
{
  "query": {
    "multi_match": {
      "query": "tour hạ long",
      "fields": ["name^2", "description"]
    }
  }
}

// Faceted search
GET /services_search/_search
{
  "query": {...},
  "aggs": {
    "by_region": {"terms": {"field": "region"}},
    "by_category": {"terms": {"field": "category"}}
  }
}
```

---

### 🔄 DATA FLOW (Luồng dữ liệu)

#### 1. User Search Tour
```
User → Elasticsearch (search) → Return results
User → PostgreSQL (get details) → Return tour info
User → MongoDB (get rich content) → Return itinerary, includes/excludes
```

#### 2. User Book Tour
```
User → Redis (check cart) → Get cart items
User → PostgreSQL (check availability) → tours.available_slots
User → PostgreSQL (create order) → BEGIN TRANSACTION
  ├─ INSERT orders
  ├─ INSERT order_items
  ├─ UPDATE tours.available_slots (atomic)
  └─ INSERT bookings
User → PostgreSQL (COMMIT) → Success
User → Redis (clear cart) → DEL cart:{userId}
User → Redis (update inventory cache) → Update available_slots
```

#### 3. User Apply Voucher
```
User → PostgreSQL (check voucher) → vouchers table
User → Redis (check usage) → INCR voucher:{code}:usage
User → Redis (check user usage) → INCR voucher:{code}:user:{userId}
User → PostgreSQL (create voucher_usage) → INSERT voucher_usage
```

#### 4. Admin Update Tour
```
Admin → PostgreSQL (update tour) → UPDATE tours
Admin → Elasticsearch (sync) → Update services_search index
Admin → Redis (invalidate cache) → DEL inventory:tour:{tourId}
Admin → MongoDB (update content) → Update service_contents
```

---

### 🎯 QUYẾT ĐỊNH QUAN TRỌNG

#### 1. Order Strategy: **GOM CHUNG 1 ORDER**

**Lý do:**
- ✅ User trải nghiệm tốt (1 lần thanh toán)
- ✅ Dễ quản lý (1 order number)
- ✅ Dễ apply voucher (cho cả order)
- ✅ Dễ tracking (1 order status)

**Implementation:**
- 1 Order có nhiều order_items
- Mỗi order_item có item_type khác nhau
- Mỗi order_item tạo 1 booking riêng
- Mỗi booking có QR code riêng

**Ví dụ:**
```
Order #ORD-001
├─ Item 1: Tour Hạ Long (TOUR) → Booking #BK-001 → QR-001
├─ Item 2: Tàu SE1 (TRANSPORTATION) → Booking #BK-002 → QR-002
├─ Item 3: Nhà hàng ABC (RESTAURANT) → Booking #BK-003 → QR-003
└─ Item 4: Hotel XYZ (HOTEL) → Booking #BK-004 → QR-004

Total: 10,000,000 VND
Voucher: -500,000 VND
Final: 9,500,000 VND
Payment: 1 lần (SePay)
```

---

#### 2. Roles: USER, ADMIN, SUPER_ADMIN

**Không có STAFF, MANAGER** (đơn giản hóa cho đồ án)

**Phân quyền:**
- **USER**: Book services, view orders, chat support
- **ADMIN**: Quản lý tours, transportation, restaurant, hotel, orders, users
- **SUPER_ADMIN**: Tất cả quyền của ADMIN + tạo admin mới

---

#### 3. Payment: SePay (Không phải VNPay/Momo)

**Lý do:**
- SePay là payment gateway (cổng thanh toán)
- Không cần wallet (tiền thật)
- User thanh toán trực tiếp qua SePay

**Optional:** Loyalty Points system (tích điểm, không phải tiền thật)

---

#### 4. Transportation: Routes + Seat Classes + Seat Map

**Lý do:**
- Tàu/xe có nhiều tuyến (Hà Nội → Vinh → Huế → Đà Nẵng)
- Mỗi tuyến có giá khác nhau
- Có hạng ghế (VIP, A, B, C)
- Cần track từng chỗ ngồi cụ thể (seat map)

---

#### 5. Restaurant & Hotel: Chỉ track số lượng

**Lý do:**
- Không cần biết bàn số mấy, phòng số mấy
- Chỉ cần biết còn bao nhiêu bàn/phòng trống
- Đơn giản hóa cho đồ án

---

### 🚀 MỞ RỘNG TỪ BACKEND CŨ

**Backend cũ (Node.js + MongoDB):**
- ✅ Tour booking
- ✅ User authentication (Normal + OAuth2)
- ✅ Shopping cart
- ✅ Order management
- ✅ Admin dashboard
- ✅ AI chatbot (Groq)
- ✅ QR code generation

**Backend mới (Spring Boot + Hybrid DB):**
- ✅ **Giữ nguyên**: Tour booking logic
- ✅ **Mở rộng**: + Transportation + Restaurant + Hotel
- ✅ **Mở rộng**: Multi-service order (gom chung 1 order)
- ✅ **Mở rộng**: Voucher system
- ✅ **Mở rộng**: Payment gateway (SePay)
- ✅ **Mở rộng**: Review & rating
- ✅ **Mở rộng**: Real-time chat (admin-user)
- ✅ **Mở rộng**: Activity logs (audit trail)
- ✅ **Công nghệ**: PostgreSQL + MongoDB + Redis + Elasticsearch + Keycloak

---

### 📝 NOTES

**Keycloak:**
- Quản lý user authentication & authorization
- Không cần bảng users trong PostgreSQL
- Chỉ lưu user_id (Keycloak UUID) trong orders, bookings, reviews...

**Flyway:**
- Database migration tool
- Versioning cho PostgreSQL schema
- Auto-run migrations khi deploy

**Cloudinary:**
- Image storage (giữ nguyên từ backend cũ)
- Upload tour images, user avatars, review images

**Email Service:**
- Nodemailer (giữ nguyên từ backend cũ)
- Send order confirmation, QR code, OTP

**AI Chatbot:**
- Groq API (giữ nguyên từ backend cũ)
- Context-aware responses
- Lưu conversation history trong MongoDB

---

## 🎓 KẾT LUẬN

Thiết kế database này:
- ✅ **Mở rộng từ backend cũ** (Tour → Tour + Transportation + Restaurant + Hotel)
- ✅ **Hybrid architecture** (PostgreSQL + MongoDB + Redis + Elasticsearch)
- ✅ **ACID cho financial data** (Orders, Payments, Vouchers)
- ✅ **Flexible schema cho content** (MongoDB)
- ✅ **Cache cho performance** (Redis)
- ✅ **Full-text search** (Elasticsearch)
- ✅ **Đơn giản hóa cho đồ án** (không quá phức tạp)
- ✅ **Có thể mở rộng sau** (Provider Abstraction Layer)

**Phù hợp cho đồ án tốt nghiệp** vì:
- 🎯 Scope rõ ràng, không quá rộng
- 🎯 Áp dụng nhiều công nghệ (PostgreSQL, MongoDB, Redis, Elasticsearch, Keycloak)
- 🎯 Có tính thực tế (multi-service booking)
- 🎯 Có thể demo được (mock provider service)
- 🎯 Có thể mở rộng sau (nếu muốn làm thực tế)
