package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.SeatClass;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transportation_seat_classes", indexes = {
    @Index(name = "idx_seat_class_transportation", columnList = "transportation_id, is_active")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransportationSeatClass {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportation_id")
    Transportation transportation;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "class_name")
    SeatClass className;
    
    @Column(columnDefinition = "TEXT")
    String description;
    
    @Column(name = "additional_price", precision = 15, scale = 2)
    BigDecimal additionalPrice;
    
    @Column(name = "total_seats")
    Integer totalSeats;
    
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    String[] amenities;
    
    @Column(name = "display_order")
    Integer displayOrder;
    
    @Builder.Default
    @Column(name = "is_active")
    Boolean isActive = true;
}
