package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * User favorites (tours, hotels, restaurants)
 * UniqueConstraint: User chỉ được favorite 1 lần cho 1 item
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_favorites", 
    indexes = {
        @Index(name = "idx_favorite_user", columnList = "user_id, created_at"),
        @Index(name = "idx_favorite_item", columnList = "item_type, item_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_favorite", 
            columnNames = {"user_id", "item_type", "item_id"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFavorite extends BaseEntity {
    
    @Column(name = "user_id", nullable = false)
    UUID userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    ItemType itemType;
    
    @Column(name = "item_id", nullable = false)
    UUID itemId;
}
