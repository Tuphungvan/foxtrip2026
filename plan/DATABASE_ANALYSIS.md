# PHÂN TÍCH DATABASE - MỨC ĐỘ CẦN THIẾT

## 📊 TỔNG QUAN

**PostgreSQL Entities:** 23 entities  
**MongoDB Documents:** 6 documents  
**Redis Models:** 3 models  

---

## 🔴 CỐT LÕI - BẮT BUỘC (Priority 1)

### 1. TOUR MODULE
**Entities:**
- ✅ `Tour` - Thông tin tour cơ bản
- ✅ `Order` - Đơn hàng
- ✅ `ChiTiet` - Chi tiết đơn hàng (OrderItem)
- ✅ `Booking` - Booking sau khi thanh toán
- ✅ `Payment` - Thanh toán

**Documents:**
- ✅ `NoiDung` - Nội dung phong phú (HTML, lịch trình chi tiết, SEO)
- ✅ `DanhGia` - Review từ khách hàng

**Redis:**
- ✅ `CartItem` - Giỏ hàng tạm
- ✅ `PaymentSession` - Session thanh toán SePay

**Mức độ:** ⭐⭐⭐⭐⭐ **BẮT BUỘC**

---

### 2. TRANSPORTATION MODULE
**Entities:**
- ✅ `PhuongTien` - Xe/tàu/máy bay
- ✅ `TuyenDuong` - Tuyến đường (A → B)
- ✅ `LichTrinh` - Lịch trình cụ thể (ngày, giờ, giá)
- ✅ `HangGheEntity` - Hạng ghế (Economy, Business...)
- ✅ `ChoNgoi` - Chỗ ngồi cụ thể (A1, B2...)

**Quan hệ:**
```
PhuongTien (1) ----< TuyenDuong (N)
PhuongTien (1) ----< HangGheEntity (N)
TuyenDuong (1) ----< LichTrinh (N)
LichTrinh (1) ----< ChoNgoi (N)
```

**Mức độ:** ⭐⭐⭐⭐⭐ **BẮT BUỘC**

---

### 3. HOTEL MODULE
**Entities:**
- ✅ `Hotel` - Khách sạn
- ✅ `LoaiPhongEntity` - Loại phòng (Single, Double, Suite...)
- ✅ `TinhTrangPhong` - Tình trạng phòng theo ngày (availability calendar)

**Quan hệ:**
```
Hotel (1) ----< LoaiPhongEntity (N)
Hotel + LoaiPhongEntity + Ngày (1) ----< TinhTrangPhong (1)
```

**Mức độ:** ⭐⭐⭐⭐⭐ **BẮT BUỘC**

---

### 4. RESTAURANT MODULE
**Entities:**
- ✅ `Restaurant` - Nhà hàng
- ✅ `BanAn` - Loại bàn (2 người, 4 người, VIP...)
- ✅ `TinhTrangBan` - Tình trạng bàn theo ngày + khung giờ

**Quan hệ:**
```
Restaurant (1) ----< BanAn (N)
Restaurant + BanAn + Ngày + Giờ (1) ----< TinhTrangBan (1)
```

**Mức độ:** ⭐⭐⭐⭐⭐ **BẮT BUỘC**

---

## 🟡 QUAN TRỌNG (Priority 2)

### 5. USER & AUTH
**Entities:**
- ✅ `HoSo` - Profile người dùng (sync từ Keycloak)
- ✅ `YeuThich` - Danh sách yêu thích (wishlist)
- ✅ `Notification` - Thông báo

**Mức độ:** ⭐⭐⭐⭐ **QUAN TRỌNG**
- HoSo: Cần để lưu thông tin bổ sung (avatar, địa chỉ, SĐT...)
- YeuThich: Nice to have, không cấp thiết
- Notification: Quan trọng cho UX

---

### 6. VOUCHER & PROMOTION
**Entities:**
- ✅ `Voucher` - Mã giảm giá
- ✅ `VoucherUsage` - Lịch sử sử dụng voucher

**Mức độ:** ⭐⭐⭐⭐ **QUAN TRỌNG**
- Cần cho marketing, tăng conversion
- Có thể làm sau khi core features xong

---

## 🟢 HỖ TRỢ (Priority 3)

### 7. CHAT & SUPPORT
**Documents:**
- ⚠️ `TinNhan` (ChatMessage) - Tin nhắn chat
- ⚠️ `ChatRoom` - Phòng chat support
- ⚠️ `LichSuAI` (AiChatHistory) - Lịch sử chat với AI bot

**Mức độ:** ⭐⭐⭐ **NICE TO HAVE**
- Không cấp thiết cho đồ án
- Có thể làm sau hoặc bỏ qua
- Nếu làm: Chỉ cần TinNhan + ChatRoom, bỏ LichSuAI

---

### 8. ADMIN & REPORTING
**Entities:**
- ⚠️ `BaoCao` (RevenueReport) - Báo cáo doanh thu

**Documents:**
- ⚠️ `NhatKy` (ActivityLog) - Nhật ký hoạt động

**Mức độ:** ⭐⭐ **KHÔNG CẦN THIẾT**
- BaoCao: Có thể query trực tiếp từ Order
- NhatKy: Quá phức tạp cho đồ án, có thể bỏ

---

### 9. REDIS CACHE
**Models:**
- ✅ `CartItem` - **CẦN** (giỏ hàng)
- ✅ `PaymentSession` - **CẦN** (thanh toán)
- ⚠️ `InventoryCache` - **KHÔNG CẦN** (có thể query trực tiếp)

---

## 📋 QUAN HỆ CHÍNH

### Order Flow
```
User → CartItem (Redis) → Order → ChiTiet → Payment → Booking
                                      ↓
                                  Voucher (optional)
```

### Service Booking
```
Tour/Hotel/Restaurant/Transportation
    ↓
NoiDung (MongoDB - rich content)
    ↓
ChiTiet (trong Order)
    ↓
Booking (sau khi thanh toán)
```

### Availability Management
```
Hotel → LoaiPhongEntity → TinhTrangPhong (ngày)
Restaurant → BanAn → TinhTrangBan (ngày + giờ)
PhuongTien → TuyenDuong → LichTrinh (ngày + giờ) → ChoNgoi
```

---

## ✅ KHUYẾN NGHỊ

### GIỮ LẠI (20 entities + 4 documents + 2 redis)

**PostgreSQL (20):**
1. Tour
2. PhuongTien
3. TuyenDuong
4. LichTrinh
5. HangGheEntity
6. ChoNgoi
7. Hotel
8. LoaiPhongEntity
9. TinhTrangPhong
10. Restaurant
11. BanAn
12. TinhTrangBan
13. Order
14. ChiTiet
15. Booking
16. Payment
17. Voucher
18. VoucherUsage
19. HoSo
20. Notification

**MongoDB (4):**
1. NoiDung (ServiceContent)
2. DanhGia (Review)
3. TinNhan (ChatMessage) - optional
4. ChatRoom - optional

**Redis (2):**
1. CartItem
2. PaymentSession

### CÓ THỂ BỎ (3 entities + 2 documents + 1 redis)

**PostgreSQL:**
- ❌ `YeuThich` (UserFavorite) - Nice to have, không cấp thiết
- ❌ `BaoCao` (RevenueReport) - Query từ Order được
- ❌ BaseEntity - Đã có rồi

**MongoDB:**
- ❌ `LichSuAI` (AiChatHistory) - Quá phức tạp
- ❌ `NhatKy` (ActivityLog) - Không cần cho đồ án

**Redis:**
- ❌ `InventoryCache` - Query trực tiếp từ DB được

---

## 🎯 ROADMAP TRIỂN KHAI

### Phase 1: Core Booking (4-5 tuần)
1. Tour + NoiDung + DanhGia
2. Order + ChiTiet + Payment + Booking
3. CartItem + PaymentSession (Redis)
4. HoSo + Notification

### Phase 2: Transportation (2 tuần)
5. PhuongTien + TuyenDuong + LichTrinh
6. HangGheEntity + ChoNgoi

### Phase 3: Hotel & Restaurant (2 tuần)
7. Hotel + LoaiPhongEntity + TinhTrangPhong
8. Restaurant + BanAn + TinhTrangBan

### Phase 4: Voucher & Chat (1-2 tuần - optional)
9. Voucher + VoucherUsage
10. TinNhan + ChatRoom (nếu còn thời gian)

---

## 💡 KẾT LUẬN

**Tổng cộng cần:** 20 entities + 4 documents + 2 redis models  
**Có thể bỏ:** 3 entities + 2 documents + 1 redis model  

**Mức độ phức tạp:** Vừa phải, phù hợp đồ án tốt nghiệp  
**Thời gian ước tính:** 9-10 tuần (theo MIGRATION_ROADMAP.md)
