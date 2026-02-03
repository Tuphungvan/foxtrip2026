package com.haui.foxtrip.entity;

import com.haui.foxtrip.entity.BaseEntity;
import com.haui.foxtrip.enums.ItemType;
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
@Table(name = "yeu_thich", 
    indexes = {
        @Index(name = "idx_yeu_thich_user", columnList = "user_id, created_at"),
        @Index(name = "idx_yeu_thich_dich_vu", columnList = "loai_dich_vu, dich_vu_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_yeu_thich", 
            columnNames = {"user_id", "loai_dich_vu", "dich_vu_id"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class YeuThich extends BaseEntity {
    
    @Column(name = "user_id", nullable = false)
    UUID userId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_dich_vu", nullable = false)
    ItemType loaiDichVu;
    
    @Column(name = "dich_vu_id", nullable = false)
    UUID dichVuId;
}
