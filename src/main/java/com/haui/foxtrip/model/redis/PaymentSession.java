package com.haui.foxtrip.model.redis;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentSession implements Serializable {
    
    private String orderId;
    private String sepayOrderId;
    private String paymentUrl;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
}
