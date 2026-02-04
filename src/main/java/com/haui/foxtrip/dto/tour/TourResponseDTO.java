package com.haui.foxtrip.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourResponseDTO {
    
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String province;
    private String region;
    private String category;
    private List<String> images;
    private String videoId;
    private String shortUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private String itinerary;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal finalPrice; // Giá sau khi giảm
    private Integer slots;
    private Integer availableSlots;
    private Boolean isBookable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
