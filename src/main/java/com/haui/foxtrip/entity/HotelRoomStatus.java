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
@EqualsAndHashCode(callSuper = false)
@Table(name = "hotel_room_status", 
    indexes = {
        @Index(name = "idx_room_status_date", columnList = "hotel_id, date"),
        @Index(name = "idx_room_status_type", columnList = "hotel_id, room_type_id, date")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_room_status", 
            columnNames = {"hotel_id", "room_type_id", "date"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelRoomStatus extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    RoomType roomType;
    
    @Column(nullable = false)
    LocalDate date;
    
    @Column(name = "total_rooms", nullable = false)
    Integer totalRooms;
    
    @Column(name = "available_rooms", nullable = false)
    Integer availableRooms;
    
    @Column(name = "override_price", precision = 15, scale = 2)
    BigDecimal overridePrice;
    
    @Builder.Default
    @Column(name = "is_bookable")
    Boolean isBookable = true;
}
