package com.haui.foxtrip.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "transportation_schedules", 
    indexes = {
        @Index(name = "idx_schedule_transportation", columnList = "transportation_id, departure_date"),
        @Index(name = "idx_schedule_route", columnList = "route_id, departure_date"),
        @Index(name = "idx_schedule_date", columnList = "departure_date, is_bookable")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_schedule", 
            columnNames = {"transportation_id", "route_id", "departure_date", "departure_time"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransportationSchedule extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportation_id", nullable = false)
    Transportation transportation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    TransportationRoute route;
    
    @Column(name = "departure_date", nullable = false)
    LocalDate departureDate;
    
    @Column(name = "departure_time", nullable = false)
    LocalTime departureTime;
    
    @Column(name = "arrival_time", nullable = false)
    LocalTime arrivalTime;
    
    @Column(name = "total_seats", nullable = false)
    Integer totalSeats;
    
    @Column(name = "available_seats", nullable = false)
    Integer availableSeats;
    
    @Column(name = "override_price", precision = 15, scale = 2)
    BigDecimal overridePrice;
    
    @Builder.Default
    @Column(name = "is_bookable")
    Boolean isBookable = true;
}
