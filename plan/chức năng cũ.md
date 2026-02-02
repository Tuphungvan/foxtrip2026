# FOXTRIP - FEATURES MAPPING (Old Backend)

## 1. AUTHENTICATION & AUTHORIZATION

### 1.1 User Registration & Login
**Files:**
- `routes/auth.js`
- `controllers/AuthController.js`
- `models/User.js`

**Features:**
- ✅ Đăng ký tài khoản (username, email, password, phone, address)
- ✅ Đăng nhập thường (email + password)
- ✅ Đăng nhập Google OAuth2
- ✅ Đăng nhập Facebook OAuth2
- ✅ Đăng xuất (stateless JWT)
- ✅ Check login status
- ✅ JWT token authentication (30 days expiry)
- ✅ Auto-create first user as admin
- ✅ Role-based: USER, ADMIN, SUPER_ADMIN

**Endpoints:**
```
POST   /api/auth/register
POST   /api/auth/login
POST   /api/auth/login/google
POST   /api/auth/login/facebook
POST   /api/auth/logout
GET    /api/auth/check-login-status
```

---

## 2. TOUR MANAGEMENT (PUBLIC)

### 2.1 Tour Browsing & Search
**Files:**
- `routes/site.js`
- `controllers/SiteController.js`
- `models/Tour.js`

**Features:**
- ✅ Tìm kiếm tour (keyword, province, category, price range, date range)
- ✅ Lọc tour theo vùng miền (Bắc, Trung, Nam)
- ✅ Tour hot (dựa trên số lượng đơn hàng)
- ✅ Tour giảm giá (discount > 0)
- ✅ Chi tiết tour (by slug)
- ✅ Random shuffle results
- ✅ Validate tour availability (isBookable, availableSlots, startDate)

**Endpoints:**
```
GET    /api/tours/search?q=...&province=...&category=...&priceMin=...&priceMax=...&startDate=...&endDate=...
GET    /api/tours/region/bac
GET    /api/tours/region/trung
GET    /api/tours/region/nam
GET    /api/tours/hot
GET    /api/tours/discount
GET    /api/tours/:slug
```

### 2.2 Tour Shorts (Video)
**Files:**
- `routes/tour.js`
- `controllers/TourController.js`

**Features:**
- ✅ Random short video tour (TikTok-style)

**Endpoints:**
```
GET    /api/tour/shorts
```

---

## 3. SHOPPING CART

### 3.1 Cart Management
**Files:**
- `routes/cart.js`
- `controllers/cartController.js`
- `models/Cart.js`

**Features:**
- ✅ Thêm tour vào giỏ hàng (by slug)
- ✅ Xem giỏ hàng
- ✅ Xóa tour khỏi giỏ hàng
- ✅ Tăng/giảm số lượng
- ✅ Đếm số item trong giỏ
- ✅ Tính final price (price - discount)
- ✅ Validate tour availability trước khi add

**Endpoints:**
```
POST   /api/cart/add/:slug
GET    /api/cart
DELETE /api/cart/:slug
POST   /api/cart/increase/:slug
POST   /api/cart/decrease/:slug
GET    /api/cart/count
```

---

## 4. CHECKOUT & ORDERS

### 4.1 Checkout Process
**Files:**
- `routes/checkout.js`
- `controllers/CheckoutController.js`
- `models/Order.js`

**Features:**
- ✅ Xem giỏ hàng + tổng tiền
- ✅ Đặt hàng (place order)
- ✅ Snapshot customer info at order time
- ✅ Snapshot tour info (name, price, discount, image)
- ✅ Order status: "Chờ thanh toán", "Đã thanh toán và chờ xác nhận", "Hoàn tất"
- ✅ Payment method: Mock payment
- ✅ Clear cart after order

**Endpoints:**
```
GET    /api/checkout
POST   /api/checkout/place-order
GET    /api/checkout/payment/:id
```

### 4.2 User Orders Management
**Files:**
- `routes/profile.js`
- `controllers/ProfileController.js`

**Features:**
- ✅ Xem danh sách đơn hàng của mình
- ✅ Chi tiết đơn hàng
- ✅ Hủy đơn hàng (chỉ "Chờ thanh toán")
- ✅ Thanh toán đơn hàng bằng ví Foxtrip
- ✅ Validate tour availability trước khi thanh toán
- ✅ Trừ tiền ví + cập nhật availableSlots

**Endpoints:**
```
GET    /api/profile/my-orders
GET    /api/profile/my-orders/:orderId
DELETE /api/profile/my-orders/:orderId/cancel
POST   /api/profile/my-orders/:orderId/pay
```

---

## 5. USER PROFILE

### 5.1 Profile Management
**Files:**
- `routes/profile.js`
- `controllers/ProfileController.js`
- `models/User.js`

**Features:**
- ✅ Xem thông tin cá nhân
- ✅ Cập nhật profile (username, email, phone, address, password)
- ✅ Upload avatar (Cloudinary)
- ✅ Validate unique email/phone

**Endpoints:**
```
GET    /api/profile
GET    /api/profile/update-profile
POST   /api/profile/update-profile
POST   /api/profile/avatar
```

### 5.2 Wallet Management
**Files:**
- `routes/profile.js`
- `controllers/ProfileController.js`
- `models/Wallet.js`

**Features:**
- ✅ Xem số dư ví
- ✅ Nạp tiền vào ví (mock recharge)
- ✅ Auto-create wallet nếu chưa có

**Endpoints:**
```
GET    /api/profile/recharge-wallet
POST   /api/profile/recharge-wallet
```

### 5.3 Order History
**Files:**
- `routes/profile.js`
- `controllers/ProfileController.js`
- `models/History.js`

**Features:**
- ✅ Xem lịch sử đơn hàng đã hoàn tất
- ✅ Chi tiết lịch sử

**Endpoints:**
```
GET    /api/profile/history
GET    /api/profile/history/:historyId
```

---

## 6. ADMIN - TOUR MANAGEMENT

### 6.1 Tour CRUD
**Files:**
- `routes/admin.js`
- `controllers/AdminController.js`
- `models/Tour.js`

**Features:**
- ✅ Xem danh sách tour
- ✅ Tạo tour mới
  - Upload multiple images (Cloudinary)
  - Extract YouTube video ID
  - Auto-generate slug
  - Validate dates, slots, images
  - Normalize itinerary/description format
- ✅ Cập nhật tour
  - Xóa ảnh cũ từ Cloudinary
  - Upload ảnh mới
  - Update availableSlots, isBookable
- ✅ Xóa tour (soft delete)
  - Xóa tất cả ảnh từ Cloudinary
  - Xóa folder tour

**Endpoints:**
```
GET    /api/admin/tours
POST   /api/admin/tours
PUT    /api/admin/tours/:id
DELETE /api/admin/tours/:id
POST   /api/admin/upload-images (multiple files)
```

---

## 7. ADMIN - USER MANAGEMENT

### 7.1 User CRUD
**Files:**
- `routes/admin.js`
- `controllers/AdminController.js`
- `models/User.js`

**Features:**
- ✅ Xem danh sách user
  - Admin thường: chỉ thấy user thường
  - Super admin: thấy user + admin (trừ super admin khác)
- ✅ Tìm kiếm user (by email)
- ✅ Sắp xếp theo tên (Vietnamese last name)
- ✅ Kích hoạt/vô hiệu hóa user
  - Không thể khoá super admin
  - Admin thường không thể khoá admin khác
  - Không thể khoá chính mình
- ✅ Reset password về "000000"
- ✅ Tạo admin mới (chỉ super admin)

**Endpoints:**
```
GET    /api/admin/users?search=...&sort=asc|desc
POST   /api/admin/users/:id/activate
POST   /api/admin/users/:id/deactivate
POST   /api/admin/users/:id/reset-password
POST   /api/admin/create-admin
```

---

## 8. ADMIN - ORDER MANAGEMENT

### 8.1 Order Processing
**Files:**
- `routes/managerOrder.js`
- `controllers/ManagerOrderController.js`
- `models/Order.js`
- `models/History.js`

**Features:**
- ✅ Xem đơn hàng "Chờ thanh toán"
- ✅ Xem đơn hàng "Đã thanh toán và chờ xác nhận"
- ✅ Xem đơn hàng "Hoàn tất"
- ✅ Xác nhận đơn hàng (chuyển sang "Hoàn tất")
- ✅ Xóa đơn hàng chưa thanh toán
- ✅ Hoàn tất đơn hàng đã hết hạn (move to history)
- ✅ Tìm kiếm order theo ID
- ✅ Chi tiết đơn hàng

**Endpoints:**
```
GET    /api/manager-order/pending-payment
GET    /api/manager-order/to-confirm
GET    /api/manager-order/completed
POST   /api/manager-order/confirm/:orderId
DELETE /api/manager-order/delete/:orderId
POST   /api/manager-order/complete/:orderId
GET    /api/manager-order/search?orderId=...
GET    /api/manager-order/:orderId
```

---

## 9. ADMIN - ANALYTICS & REPORTS

### 9.1 Dashboard Overview
**Files:**
- `routes/admin.js`
- `controllers/AdminController.js`
- `models/RevenueReport.js`

**Features:**
- ✅ Tổng số tour
- ✅ Tổng số user (phân quyền theo role)
- ✅ Tổng số đơn hàng
- ✅ Doanh thu tháng hiện tại

**Endpoints:**
```
GET    /api/admin/overview
```

### 9.2 Revenue Reports
**Files:**
- `routes/admin.js`
- `controllers/AdminController.js`
- `models/RevenueReport.js`

**Features:**
- ✅ Báo cáo doanh thu theo tháng/năm
- ✅ Tổng doanh thu
- ✅ Tổng số đơn hàng hoàn tất

**Endpoints:**
```
GET    /api/admin/revenue
```

---

## 10. AI CHATBOT

### 10.1 Chatbot Support
**Files:**
- `routes/chatbot.js`
- `controllers/ChatbotController.js`
- `config/queryGroq.js`

**Features:**
- ✅ AI chatbot hỗ trợ người dùng
- ✅ Query Groq API (AI model)
- ✅ Context-aware responses

**Endpoints:**
```
POST   /api/chatbot/chat
```

---

## 11. OTP VERIFICATION

### 11.1 OTP Management
**Files:**
- `routes/otp.js`
- `controllers/OTPController.js`
- `utils/emailService.js`

**Features:**
- ✅ Gửi OTP qua email
- ✅ Xác thực OTP code
- ✅ Email service integration

**Endpoints:**
```
POST   /api/otp/send
POST   /api/otp/verify
```

---

## 12. QR CODE SYSTEM

### 12.1 QR Code Generation
**Files:**
- `utils/sendOrderEmail.js`

**Features:**
- ✅ Tạo QR code cho mỗi đơn hàng (order ID)
- ✅ Gửi QR code qua email khi xác nhận đơn hàng
- ✅ QR code dùng để admin/staff xác thực khách hàng tại địa điểm
- ✅ QR format: PNG, 300x300px
- ✅ Embed trong email HTML (cid:qrcode)

**Use Case:**
- Admin xác nhận đơn hàng → Gửi email có QR code
- Khách hàng show QR code tại địa điểm du lịch
- Staff scan QR để verify order ID

**Note:** 
- ❌ Backend cũ chưa có API scan/verify QR code
- ❌ Chưa có mobile app để scan QR
- 💡 Cần implement: API verify QR code (check order exists, status, user info)

---

## 13. BACKGROUND JOBS

### 12.1 Scheduled Tasks
**Files:**
- `jobs/autoCancelOrders.js`
- `jobs/moveCompletedOrders.js`

**Features:**
- ✅ Auto-cancel orders chưa thanh toán sau X giờ
- ✅ Auto-move completed orders to history

---

## 14. MIDDLEWARES

### 13.1 Authentication & Authorization
**Files:**
- `middlewares/isAuthenticated.js`
- `middlewares/isAdmin.js`
- `middlewares/ensureActive.js`
- `middlewares/uploadAvatar.js`

**Features:**
- ✅ JWT token verification
- ✅ Admin role check
- ✅ Super admin role check
- ✅ Active user check
- ✅ Cloudinary upload integration

---

## 15. UTILITIES & SERVICES

### 14.1 Email Service
**Files:**
- `utils/emailService.js`
- `utils/sendOrderEmail.js`

**Features:**
- ✅ Send email (Nodemailer)
- ✅ Order confirmation email
- ✅ OTP email

### 14.2 Image Upload Service
**Files:**
- `services/uploadImageService.js`
- `config/cloudinary.js`

**Features:**
- ✅ Upload images to Cloudinary
- ✅ Delete images from Cloudinary
- ✅ Delete tour folder
- ✅ Multer + CloudinaryStorage

---

## SUMMARY

**Total Features:** 15 modules

**Core Functionalities:**
1. ✅ Authentication (Normal + OAuth2)
2. ✅ Tour browsing & search
3. ✅ Shopping cart
4. ✅ Checkout & orders
5. ✅ User profile & wallet
6. ✅ Admin tour management
7. ✅ Admin user management
8. ✅ Admin order management
9. ✅ Analytics & reports
10. ✅ AI chatbot
11. ✅ OTP verification
12. ✅ QR code generation (email)
13. ✅ Background jobs
14. ✅ Cloudinary integration
15. ✅ Email service

**Missing Features (Cần implement):**
- ❌ QR code scan/verify API
- ❌ Real-time chat (chỉ có AI chatbot)
- ❌ Voucher system
- ❌ Payment gateway integration (VNPay, Momo)
- ❌ Hotel/Restaurant/Flight booking
- ❌ Review & rating system
- ❌ Notification system
- ❌ Advanced analytics

**Tech Stack:**
- Node.js + Express
- MongoDB + Mongoose
- JWT authentication
- Cloudinary (image storage)
- Nodemailer (email)
- Groq API (AI chatbot)
- Google OAuth2
- Facebook OAuth2
