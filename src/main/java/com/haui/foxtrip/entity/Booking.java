package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.BookingStatus;
import com.haui.foxtrip.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings", indexes = {
    @Index(name = "idx_booking_code", columnList = "booking_code"),
    @Index(name = "idx_booking_user", columnList = "user_id, booking_date"),
    @Index(name = "idx_booking_item", columnList = "item_type, item_id"),
    @Index(name = "idx_booking_status", columnList = "status, booking_date"),
    @Index(name = "idx_booking_qr", columnList = "qr_code_data")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", unique = true, nullable = false)
    ChiTiet orderItem;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    ItemType itemType;
    
    @Column(name = "item_id", nullable = false)
    UUID itemId;
    
    @Column(name = "user_id", nullable = false)
    UUID userId;
    
    @Column(name = "booking_code", unique = true, nullable = false)
    String bookingCode;
    
    @Column(name = "booking_date", nullable = false)
    LocalDate bookingDate;
    
    @Column(name = "booking_time")
    LocalTime bookingTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    BookingStatus status;
    
    @Column(name = "check_in_at")
    LocalDateTime checkInAt;
    
    @Column(name = "check_out_at")
    LocalDateTime checkOutAt;
    
    @Column(name = "checked_in_by")
    UUID checkedInBy;
    
    @Column(name = "qr_code_data", nullable = false)
    String qrCodeData;
    
    @Column(name = "qr_scanned_at")
    LocalDateTime qrScannedAt;
    
    @Column(columnDefinition = "TEXT")
    String notes;
}
