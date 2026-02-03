# Entity/Document Migration Summary

## Overview
Successfully created English versions of all Vietnamese entity and document files, with proper naming conventions and @Builder.Default annotations for fields with default values.

## Entities Created

### 1. UserProfile.java (from HoSo.java)
- **Table**: `user_profiles`
- **Key Changes**:
  - `hoTen` → `fullName`
  - `soDienThoai` → `phoneNumber`
  - `diaChi` → `address`
  - `ngaySinh` → `dateOfBirth`
  - `gioiTinh` → `gender`
  - `tuyChon` → `preferences`
  - `duLieuKhac` → `additionalData`
  - `hoatDong` → `isActive`
  - `emailXacThuc` → `emailVerified`
  - `sdtXacThuc` → `phoneVerified`
- **Added**: @Builder.Default for `isActive`, `emailVerified`, `phoneVerified`

### 2. UserFavorite.java (from YeuThich.java)
- **Table**: `user_favorites`
- **Key Changes**:
  - `loaiDichVu` → `itemType`
  - `dichVuId` → `itemId`

### 3. RevenueReport.java (from BaoCao.java)
- **Table**: `revenue_reports`
- **Key Changes**:
  - `thang` → `month`
  - `nam` → `year`
  - `tongDoanhThu` → `totalRevenue`
  - `tongDonHang` → `totalOrders`
  - `chiTiet` → `details`
- **Added**: @Builder.Default for `totalRevenue`, `totalOrders`

### 4. Transportation.java (from PhuongTien.java)
- **Table**: `transportations`
- **Key Changes**:
  - `loaiXe` → `vehicleType`
  - `giaCoban` → `basePrice`
  - `hoatDong` → `isActive`
- **Added**: @Builder.Default for `isActive`

### 5. TransportationRoute.java (from TuyenDuong.java)
- **Table**: `transportation_routes`
- **Key Changes**:
  - `tenTuyen` → `routeName`
  - `diemDi` → `departureLocation`
  - `diemDen` → `arrivalLocation`
  - `khoangCachKm` → `distanceKm`
  - `thoiGianPhut` → `durationMinutes`
  - `gia` → `price`
  - `thuTuHienThi` → `displayOrder`
  - `hoatDong` → `isActive`
- **Added**: @Builder.Default for `displayOrder`, `isActive`

### 6. TransportationSchedule.java (from LichTrinh.java)
- **Table**: `transportation_schedules`
- **Key Changes**:
  - `phuongTien` → `transportation`
  - `tuyenDuong` → `route`
  - `ngayKhoiHanh` → `departureDate`
  - `gioKhoiHanh` → `departureTime`
  - `gioDen` → `arrivalTime`
  - `tongSoCho` → `totalSeats`
  - `soChoTrong` → `availableSeats`
  - `giaGhiDe` → `overridePrice`
  - `coTheDat` → `isBookable`
- **Added**: @Builder.Default for `isBookable`
- **Added**: @EqualsAndHashCode(callSuper = false)

### 7. TransportationSeat.java (from ChoNgoi.java)
- **Table**: `transportation_seats`
- **Key Changes**:
  - `lichTrinh` → `schedule`
  - `hangGhe` → `seatClass`
  - `soCho` → `seatNumber`
  - `trangThai` → `status`

### 8. TransportationSeatClass.java (from HangGheEntity.java)
- **Table**: `transportation_seat_classes`
- **Key Changes**:
  - `phuongTien` → `transportation`
  - `tenHang` → `className`
  - `moTa` → `description`
  - `giaCong` → `additionalPrice`
  - `tongSoCho` → `totalSeats`
  - `tienNghi` → `amenities`
  - `thuTuHienThi` → `displayOrder`
  - `hoatDong` → `isActive`
- **Added**: @Builder.Default for `isActive`

### 9. OrderItem.java (from ChiTiet.java)
- **Table**: `order_items`
- **Key Changes**:
  - `loaiDichVu` → `itemType`
  - `dichVuId` → `itemId`
  - `thongTinLuu` → `savedInfo`
  - `soLuong` → `quantity`
  - `donGia` → `unitPrice`
  - `giamGia` → `discount`
  - `thanhTien` → `totalPrice`
  - `chiTietDatCho` → `bookingDetails`
  - `trangThai` → `status`
- **Added**: @Builder.Default for `quantity`, `discount`

### 10. RestaurantTableStatus.java (from TinhTrangBan.java)
- **Table**: `restaurant_table_status`
- **Key Changes**:
  - `banAn` → `table`
  - `ngay` → `date`
  - `khungGio` → `timeSlot`
  - `tongSoBan` → `totalTables`
  - `banTrong` → `availableTables`
  - `coTheDat` → `isBookable`
- **Added**: @Builder.Default for `isBookable`
- **Added**: @EqualsAndHashCode(callSuper = false)

### 11. HotelRoomStatus.java (from TinhTrangPhong.java)
- **Table**: `hotel_room_status`
- **Key Changes**:
  - `loaiPhong` → `roomType`
  - `ngay` → `date`
  - `tongSoPhong` → `totalRooms`
  - `phongTrong` → `availableRooms`
  - `giaGhiDe` → `overridePrice`
  - `coTheDat` → `isBookable`
- **Added**: @Builder.Default for `isBookable`
- **Added**: @EqualsAndHashCode(callSuper = false)

### 12. RoomType.java (from LoaiPhong.java)
- **Table**: `room_types`
- **Key Changes**:
  - `loaiPhong` → `category`
  - `tongSoPhong` → `totalRooms`
  - `giaMoiDem` → `pricePerNight`
  - `soKhachToiDa` → `maxGuests`
  - `moTa` → `description`

### 13. RestaurantTable.java (from BanAn.java)
- **Table**: `restaurant_tables`
- **Key Changes**:
  - `loaiBan` → `tableType`
  - `tongSoBan` → `totalTables`
  - `giaDatBan` → `reservationFee`
- **Added**: @Builder.Default for `reservationFee`

## Documents Created

### 1. Review.java (from DanhGia.java)
- **Collection**: `reviews`
- **Key Changes**: All field names translated to English
- **Added**: @Builder annotation and @Builder.Default for `helpfulCount`

### 2. ActivityLog.java (from NhatKy.java)
- **Collection**: `activity_logs`
- **Key Changes**:
  - `vaiTro` → `userRole`
  - `hanhDong` → `action`
  - `loaiTaiNguyen` → `resourceType`
  - `taiNguyenId` → `resourceId`
  - `thayDoi` → `changeDetails`
  - `duLieuKhac` → `additionalData`
  - `diaChiIp` → `ipAddress`
  - `thoiGian` → `timestamp`
- **Added**: @Builder annotation

### 3. ServiceContent.java (from NoiDung.java)
- **Collection**: `service_contents`
- **Key Changes**:
  - `loaiDichVu` → `serviceType`
  - `dichVuId` → `serviceId`
  - `moTaHtml` → `descriptionHtml`
  - `diemNhan` → `highlights`
  - All nested class names and fields translated
- **Added**: @Builder annotation for all nested classes

### 4. ChatMessage.java (from TinNhan.java)
- **Collection**: `chat_messages`
- **Key Changes**: All field names translated to English
- **Added**: @Builder annotation and @Builder.Default for `isBotMessage`

### 5. AiChatHistory.java (from LichSuAI.java)
- **Collection**: `ai_chat_history`
- **Key Changes**: All field names translated to English
- **Added**: @Builder annotation

## Updated References

### Services
- Created `UserProfileService.java` (replaces HoSoService.java)
- Updated `UserSyncService.java` to use `UserProfile`

### Repositories
- Created `UserProfileRepository.java` (replaces HoSoRepository.java)

### Controllers
- Updated `AuthController.java` to use `UserProfile`
- Updated `ProfileController.java` to use `UserProfileService`

### Entities
- Updated `Voucher.java`: `LoaiGiam` → `DiscountType`, added @Builder.Default
- Updated `Booking.java`: `ChiTiet` → `OrderItem`
- Updated `Hotel.java`: Added @Builder.Default for default values
- Updated `Restaurant.java`: Added @Builder.Default for `isActive`
- Updated `Order.java`: Added @Builder.Default for `discountAmount`

## Key Improvements

1. **Consistent Naming**: All entities and documents now use English names
2. **@Builder.Default**: Added to all fields with default values to fix Lombok warnings
3. **@EqualsAndHashCode(callSuper = false)**: Added where needed for entities extending BaseEntity
4. **Type Safety**: Updated all references to use the new English entity names
5. **Enum Usage**: Ensured proper enum usage (e.g., DiscountType instead of LoaiGiam)

## Files to Keep (Vietnamese originals)
The Vietnamese files are still in place and can be removed once migration is complete:
- HoSo.java, YeuThich.java, BaoCao.java, PhuongTien.java, TuyenDuong.java
- LichTrinh.java, ChoNgoi.java, HangGheEntity.java, ChiTiet.java
- TinhTrangBan.java, TinhTrangPhong.java, LoaiPhong.java, BanAn.java
- DanhGia.java, NhatKy.java, NoiDung.java, TinNhan.java, LichSuAI.java
- HoSoService.java, HoSoRepository.java

## Next Steps
1. Update any remaining references to Vietnamese entities in other parts of the codebase
2. Run tests to ensure all functionality works with new entity names
3. Update database migration scripts if needed
4. Remove old Vietnamese entity files once migration is verified
