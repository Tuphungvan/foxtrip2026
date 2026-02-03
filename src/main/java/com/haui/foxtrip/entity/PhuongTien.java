package com.haui.foxtrip.entity;

import com.haui.foxtrip.entity.BaseEntity;
import com.haui.foxtrip.enums.LoaiXe;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "phuong_tien", indexes = {
    @Index(name = "idx_phuong_tien_slug", columnList = "slug"),
    @Index(name = "idx_phuong_tien_loai", columnList = "loai_xe, hoat_dong"),
    @Index(name = "idx_phuong_tien_deleted", columnList = "deleted_at")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhuongTien extends BaseEntity {
    
    @Column(nullable = false)
    String name;
    
    @Column(unique = true, nullable = false)
    String slug;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_xe", nullable = false)
    LoaiXe loaiXe;
    
    @Column(columnDefinition = "TEXT")
    String description;
    
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    String[] images;
    
    @Column(name = "gia_co_ban", nullable = false, precision = 15, scale = 2)
    BigDecimal giaCoban;
    
    @Column(name = "hoat_dong")
    Boolean hoatDong = true;
}
