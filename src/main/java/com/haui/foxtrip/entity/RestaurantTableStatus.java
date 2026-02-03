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
@EqualsAndHashCode(callSuper = false)
@Table(name = "restaurant_table_status", 
    indexes = {
        @Index(name = "idx_table_status_date", columnList = "restaurant_id, date"),
        @Index(name = "idx_table_status_time", columnList = "restaurant_id, date, time_slot")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_table_status", 
            columnNames = {"restaurant_id", "table_id", "date", "time_slot"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTableStatus extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    Restaurant restaurant;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    RestaurantTable table;
    
    @Column(nullable = false)
    LocalDate date;
    
    @Column(name = "time_slot", nullable = false)
    LocalTime timeSlot;
    
    @Column(name = "total_tables", nullable = false)
    Integer totalTables;
    
    @Column(name = "available_tables", nullable = false)
    Integer availableTables;
    
    @Builder.Default
    @Column(name = "is_bookable")
    Boolean isBookable = true;
}
