package com.haui.foxtrip.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notif_user", columnList = "user_id, created_at"),
    @Index(name = "idx_notif_read", columnList = "user_id, is_read")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends BaseEntity {
    
    @Column(name = "user_id", nullable = false)
    UUID userId;
    
    @Column(nullable = false)
    String title;
    
    @Column(columnDefinition = "TEXT")
    String message;
    
    String type; // ORDER, PAYMENT, BOOKING, SYSTEM
    
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> data; // related order_id, booking_id, etc.
    
    @Column(name = "is_read")
    Boolean isRead = false;
    
    @Column(name = "read_at")
    LocalDateTime readAt;
}
