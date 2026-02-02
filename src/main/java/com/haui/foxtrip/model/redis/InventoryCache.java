package com.haui.foxtrip.model.redis;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class InventoryCache implements Serializable {
    
    private Integer totalSlots;
    private Integer availableSlots;
    private BigDecimal price;
    private BigDecimal discount;
    private Boolean isBookable;
}
