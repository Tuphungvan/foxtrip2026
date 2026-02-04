package com.haui.foxtrip.dto.tour;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO cho video shorts của tour
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourShortDTO {
    
    private UUID id;
    private String name;
    private String slug;
    private String shortUrl;
    private String thumbnail; // Ảnh đầu tiên
    private BigDecimal price;
}
