package com.haui.foxtrip.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hotel_availability", 
    indexes = {
        @Index(name = "idx_hotel_avail_date", columnList = "hotel_id, available_date"),
        @Index(name = "idx_hotel_avail_room", columnList = "hotel_id, room_type_id, available_date")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_hotel_avail", 
            columnNames = {"hotel_id", "room_type_id", "available_date"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelAvailability extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    HotelRoom roomType;
    
    @Column(name = "available_date", nullable = false)
    LocalDate availableDate;
    
    @Column(name = "total_rooms", nullable = false)
    Integer totalRooms;
    
    @Column(name = "available_rooms", nullable = false)
    Integer availableRooms;
    
    @Column(name = "price_override", precision = 15, scale = 2)
    BigDecimal priceOverride;
    
    @Column(name = "is_bookable")
    Boolean isBookable = true;
}
