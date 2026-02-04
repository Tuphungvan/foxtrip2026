package com.haui.foxtrip.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@EqualsAndHashCode(callSuper = false)
@Table(name = "tours", indexes = {
    @Index(name = "idx_tours_slug", columnList = "slug"),
    @Index(name = "idx_tours_region", columnList = "region, is_bookable"),
    @Index(name = "idx_tours_province", columnList = "province, is_bookable"),
    @Index(name = "idx_tours_dates", columnList = "start_date, end_date, is_bookable"),
    @Index(name = "idx_tours_deleted", columnList = "deleted_at")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tour extends BaseEntity {
    
    @NotBlank(message = "Tên tour không được để trống")
    @Size(max = 255, message = "Tên tour không được vượt quá 255 ký tự")
    @Column(nullable = false)
    String name;
    
    @Column(unique = true, nullable = false)
    String slug;
    
    @NotBlank(message = "Mô tả tour không được để trống")
    @Column(columnDefinition = "TEXT")
    String description;
    
    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    @Column(nullable = false)
    String province;
    
    @NotBlank(message = "Vùng miền không được để trống")
    @Column(nullable = false)
    String region; // Bắc, Trung, Nam
    
    @NotBlank(message = "Danh mục tour không được để trống")
    @Column(nullable = false)
    String category; // Biển, Văn hóa, Nghỉ dưỡng, Mạo hiểm...
    
    @NotEmpty(message = "Tour phải có ít nhất 1 ảnh")
    @Size(min = 1, message = "Tour phải có ít nhất 1 ảnh")
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]", nullable = false)
    String[] images;
    
    @NotBlank(message = "Video ID không được để trống")
    @Column(name = "video_id", nullable = false)
    String videoId;
    
    @Column(name = "short_url")
    String shortUrl;
    
    @NotNull(message = "Ngày bắt đầu không được để trống")
    @Column(name = "start_date", nullable = false)
    LocalDate startDate;
    
    @NotNull(message = "Ngày kết thúc không được để trống")
    @Column(name = "end_date", nullable = false)
    LocalDate endDate;
    
    @NotBlank(message = "Lịch trình tour không được để trống")
    @Column(columnDefinition = "TEXT", nullable = false)
    String itinerary;
    
    @NotNull(message = "Giá tour không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá tour phải lớn hơn 0")
    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal price;
    
    @Builder.Default
    @DecimalMin(value = "0.0", message = "Giảm giá không được âm")
    @DecimalMax(value = "100.0", message = "Giảm giá không được vượt quá 100%")
    @Column(precision = 5, scale = 2)
    BigDecimal discount = BigDecimal.ZERO;
    
    @NotNull(message = "Số lượng chỗ không được để trống")
    @Min(value = 1, message = "Số lượng chỗ phải lớn hơn 0")
    @Column(nullable = false)
    Integer slots;
    
    @NotNull(message = "Số lượng chỗ còn lại không được để trống")
    @Min(value = 0, message = "Số lượng chỗ còn lại không được âm")
    @Column(name = "available_slots", nullable = false)
    Integer availableSlots;
    
    @Builder.Default
    @Column(name = "is_bookable")
    Boolean isBookable = true;
}
