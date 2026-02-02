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
@Table(name = "hotels", indexes = {
    @Index(name = "idx_hotel_slug", columnList = "slug"),
    @Index(name = "idx_hotel_province", columnList = "province, is_active"),
    @Index(name = "idx_hotel_rating", columnList = "star_rating, is_active"),
    @Index(name = "idx_hotel_deleted", columnList = "deleted_at")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hotel extends BaseEntity {
    
    @Column(nullable = false)
    String name;
    
    @Column(unique = true, nullable = false)
    String slug;
    
    @Column(columnDefinition = "TEXT")
    String description;
    
    @Column(name = "star_rating")
    Integer starRating;
    
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
    
    @Column(name = "check_in_time")
    LocalTime checkInTime = LocalTime.of(14, 0);
    
    @Column(name = "check_out_time")
    LocalTime checkOutTime = LocalTime.of(12, 0);
    
    @Column(name = "is_active")
    Boolean isActive = true;
}
