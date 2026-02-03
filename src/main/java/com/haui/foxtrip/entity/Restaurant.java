package com.haui.foxtrip.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "restaurants", indexes = {
    @Index(name = "idx_restaurant_slug", columnList = "slug"),
    @Index(name = "idx_restaurant_province", columnList = "province, is_active"),
    @Index(name = "idx_restaurant_cuisine", columnList = "cuisine_type, is_active"),
    @Index(name = "idx_restaurant_deleted", columnList = "deleted_at")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Restaurant extends BaseEntity {
    
    @Column(nullable = false)
    String name;
    
    @Column(unique = true, nullable = false)
    String slug;
    
    @Column(columnDefinition = "TEXT")
    String description;
    
    @Column(name = "cuisine_type")
    String cuisineType;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    String address;
    
    @Column(nullable = false)
    String province;
    
    @Column(precision = 10, scale = 7)
    BigDecimal latitude;
    
    @Column(precision = 10, scale = 7)
    BigDecimal longitude;
    
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    String[] images;
    
    @Column(name = "price_range")
    String priceRange;
    
    @Column(name = "opening_time", nullable = false)
    LocalTime openingTime;
    
    @Column(name = "closing_time", nullable = false)
    LocalTime closingTime;
    
    @Builder.Default
    @Column(name = "is_active")
    Boolean isActive = true;
}
