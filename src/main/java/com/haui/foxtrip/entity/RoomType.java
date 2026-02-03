package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.RoomCategory;
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
@Table(name = "room_types", indexes = {
    @Index(name = "idx_room_type_hotel", columnList = "hotel_id")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    RoomCategory category;
    
    @Column(name = "total_rooms", nullable = false)
    Integer totalRooms;
    
    @Column(name = "price_per_night", nullable = false, precision = 15, scale = 2)
    BigDecimal pricePerNight;
    
    @Column(name = "max_guests", nullable = false)
    Integer maxGuests;
    
    @Column(columnDefinition = "TEXT")
    String description;
}
