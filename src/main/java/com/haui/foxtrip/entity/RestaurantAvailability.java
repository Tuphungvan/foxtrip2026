package com.haui.foxtrip.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "restaurant_availability", 
    indexes = {
        @Index(name = "idx_rest_avail_date", columnList = "restaurant_id, available_date"),
        @Index(name = "idx_rest_avail_slot", columnList = "restaurant_id, available_date, time_slot")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_rest_avail", 
            columnNames = {"restaurant_id", "table_type_id", "available_date", "time_slot"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantAvailability extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    Restaurant restaurant;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_type_id", nullable = false)
    RestaurantTable tableType;
    
    @Column(name = "available_date", nullable = false)
    LocalDate availableDate;
    
    @Column(name = "time_slot", nullable = false)
    LocalTime timeSlot;
    
    @Column(name = "total_tables", nullable = false)
    Integer totalTables;
    
    @Column(name = "available_tables", nullable = false)
    Integer availableTables;
    
    @Column(name = "is_bookable")
    Boolean isBookable = true;
}
