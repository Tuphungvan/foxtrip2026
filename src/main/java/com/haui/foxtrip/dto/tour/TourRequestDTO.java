package com.haui.foxtrip.dto.tour;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourRequestDTO {
    
    @NotBlank(message = "Tên tour không được để trống")
    @Size(max = 255, message = "Tên tour không được vượt quá 255 ký tự")
    private String name;
    
    @NotBlank(message = "Mô tả tour không được để trống")
    private String description;
    
    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    private String province;
    
    @NotBlank(message = "Vùng miền không được để trống")
    private String region;
    
    @NotBlank(message = "Danh mục tour không được để trống")
    private String category;
    
    @NotBlank(message = "Video ID không được để trống")
    private String videoId;
    
    private String shortUrl;
    
    @NotNull(message = "Ngày bắt đầu không được để trống")
    @Future(message = "Ngày bắt đầu phải là ngày trong tương lai")
    private LocalDate startDate;
    
    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải là ngày trong tương lai")
    private LocalDate endDate;
    
    @NotBlank(message = "Lịch trình tour không được để trống")
    private String itinerary;
    
    @NotNull(message = "Giá tour không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá tour phải lớn hơn 0")
    private BigDecimal price;
    
    @DecimalMin(value = "0.0", message = "Giảm giá không được âm")
    @DecimalMax(value = "100.0", message = "Giảm giá không được vượt quá 100%")
    private BigDecimal discount;
    
    @NotNull(message = "Số lượng chỗ không được để trống")
    @Min(value = 1, message = "Số lượng chỗ phải lớn hơn 0")
    private Integer slots;
    
    private Boolean isBookable;
}
