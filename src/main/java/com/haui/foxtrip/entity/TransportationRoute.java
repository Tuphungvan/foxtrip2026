package com.haui.foxtrip.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transportation_routes", indexes = {
    @Index(name = "idx_route_transport", columnList = "transportation_id, is_active"),
    @Index(name = "idx_route_locations", columnList = "departure_location, arrival_location")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransportationRoute {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportation_id", nullable = false)
    Transportation transportation;
    
    @Column(name = "route_name", nullable = false)
    String routeName;
    
    @Column(name = "departure_location", nullable = false)
    String departureLocation;
    
    @Column(name = "arrival_location", nullable = false)
    String arrivalLocation;
    
    @Column(name = "distance_km")
    Integer distanceKm;
    
    @Column(name = "duration_minutes", nullable = false)
    Integer durationMinutes;
    
    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal price;
    
    @Column(name = "display_order")
    Integer displayOrder = 0;
    
    @Column(name = "is_active")
    Boolean isActive = true;
}
