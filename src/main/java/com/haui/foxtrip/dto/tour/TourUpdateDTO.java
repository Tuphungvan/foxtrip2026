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
public class TourUpdateDTO {
    
    @Size(max = 255, message = "Tên tour không được vượt quá 255 ký tự")
    private String name;
    
    private String description;
    
    private String province;
    
    private String region;
    
    private String category;
    
    private String videoId;
    
    private String shortUrl;
    
    @Future(message = "Ngày bắt đầu phải là ngày trong tương lai")
    private LocalDate startDate;
    
    @Future(message = "Ngày kết thúc phải là ngày trong tương lai")
    private LocalDate endDate;
    
    private String itinerary;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá tour phải lớn hơn 0")
    private BigDecimal price;
    
    @DecimalMin(value = "0.0", message = "Giảm giá không được âm")
    @DecimalMax(value = "100.0", message = "Giảm giá không được vượt quá 100%")
    private BigDecimal discount;
    
    @Min(value = 1, message = "Số lượng chỗ phải lớn hơn 0")
    private Integer slots;
    
    private Boolean isBookable;
}
