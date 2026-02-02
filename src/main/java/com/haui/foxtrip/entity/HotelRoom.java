package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hotel_rooms", indexes = {
    @Index(name = "idx_room_hotel", columnList = "hotel_id")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    RoomType roomType;
    
    @Column(name = "total_rooms", nullable = false)
    Integer totalRooms;
    
    @Column(name = "price_per_night", nullable = false, precision = 15, scale = 2)
    BigDecimal pricePerNight;
    
    @Column(name = "max_guests", nullable = false)
    Integer maxGuests;
    
    @Column(columnDefinition = "TEXT")
    String description;
}
