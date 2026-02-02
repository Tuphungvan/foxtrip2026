package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transportation", indexes = {
    @Index(name = "idx_transport_slug", columnList = "slug"),
    @Index(name = "idx_transport_type", columnList = "vehicle_type, is_active"),
    @Index(name = "idx_transport_deleted", columnList = "deleted_at")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transportation extends BaseEntity {
    
    @Column(nullable = false)
    String name;
    
    @Column(unique = true, nullable = false)
    String slug;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    VehicleType vehicleType;
    
    @Column(columnDefinition = "TEXT")
    String description;
    
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    String[] images;
    
    @Column(name = "price_base", nullable = false, precision = 15, scale = 2)
    BigDecimal priceBase;
    
    @Column(name = "is_active")
    Boolean isActive = true;
}
