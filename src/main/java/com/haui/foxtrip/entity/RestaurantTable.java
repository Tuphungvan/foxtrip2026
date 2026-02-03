package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.TableType;
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
@Table(name = "restaurant_tables", indexes = {
    @Index(name = "idx_table_restaurant", columnList = "restaurant_id")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    Restaurant restaurant;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "table_type", nullable = false)
    TableType tableType;
    
    @Column(name = "total_tables", nullable = false)
    Integer totalTables;
    
    @Builder.Default
    @Column(name = "reservation_fee", precision = 15, scale = 2)
    BigDecimal reservationFee = BigDecimal.ZERO;
}
