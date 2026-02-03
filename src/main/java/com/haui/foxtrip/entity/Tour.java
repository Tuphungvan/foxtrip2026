package com.haui.foxtrip.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tours", indexes = {
    @Index(name = "idx_tours_slug", columnList = "slug"),
    @Index(name = "idx_tours_region", columnList = "region, is_bookable"),
    @Index(name = "idx_tours_province", columnList = "province, is_bookable"),
    @Index(name = "idx_tours_dates", columnList = "start_date, end_date, is_bookable"),
    @Index(name = "idx_tours_deleted", columnList = "deleted_at")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tour extends BaseEntity {
    
    @Column(nullable = false)
    String name;
    
    @Column(unique = true, nullable = false)
    String slug;
    
    @Column(columnDefinition = "TEXT")
    String description;
    
    String province;
    
    String region; // Bắc, Trung, Nam
    
    String category; // Biển, Văn hóa, Nghỉ dưỡng, Mạo hiểm...
    
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]", nullable = false)
    String[] images;
    
    @Column(name = "video_id")
    String videoId;
    
    @Column(name = "short_url")
    String shortUrl;
    
    @Column(name = "start_date", nullable = false)
    LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    LocalDate endDate;
    
    @Column(columnDefinition = "TEXT")
    String itinerary;
    
    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal price;
    
    @Column(precision = 5, scale = 2)
    BigDecimal discount = BigDecimal.ZERO;
    
    @Column(nullable = false)
    Integer slots;
    
    @Column(name = "available_slots", nullable = false)
    Integer availableSlots;
    
    @Column(name = "is_bookable")
    Boolean isBookable = true;
}
