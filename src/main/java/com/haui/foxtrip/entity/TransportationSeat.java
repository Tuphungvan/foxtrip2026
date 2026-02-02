package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transportation_seats", 
    indexes = {
        @Index(name = "idx_schedule_status", columnList = "schedule_id, status"),
        @Index(name = "idx_schedule_class", columnList = "schedule_id, seat_class_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_seat", columnNames = {"schedule_id", "seat_number"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransportationSeat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    TransportationSchedule schedule;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_class_id")
    TransportationSeatClass seatClass;
    
    @Column(name = "seat_number")
    String seatNumber;
    
    @Enumerated(EnumType.STRING)
    SeatStatus status;
}
