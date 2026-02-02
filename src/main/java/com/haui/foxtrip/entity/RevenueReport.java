package com.haui.foxtrip.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "revenue_reports", 
    indexes = {
        @Index(name = "idx_revenue_date", columnList = "year, month")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_revenue", columnNames = {"month", "year"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RevenueReport extends BaseEntity {
    
    @Column(nullable = false)
    Integer month;
    
    @Column(nullable = false)
    Integer year;
    
    @Column(name = "total_revenue", nullable = false, precision = 15, scale = 2)
    BigDecimal totalRevenue = BigDecimal.ZERO;
    
    @Column(name = "total_orders", nullable = false)
    Integer totalOrders = 0;
    
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    Map<String, Object> breakdown;
}
