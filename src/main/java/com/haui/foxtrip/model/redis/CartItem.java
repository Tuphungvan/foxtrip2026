package com.haui.foxtrip.model.redis;

import com.haui.foxtrip.enums.ItemType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class CartItem implements Serializable {
    
    private ItemType itemType;
    private String itemId;
    private String name;
    private String slug;
    private BigDecimal price;
    private BigDecimal discount;
    private Integer quantity;
    private String image;
    private Map<String, Object> bookingDetails;
}
