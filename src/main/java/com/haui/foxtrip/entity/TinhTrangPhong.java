package com.haui.foxtrip.entity;

import com.haui.foxtrip.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tinh_trang_phong", 
    indexes = {
        @Index(name = "idx_tinh_trang_phong_ngay", columnList = "hotel_id, ngay"),
        @Index(name = "idx_tinh_trang_phong_loai", columnList = "hotel_id, loai_phong_id, ngay")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_tinh_trang_phong", 
            columnNames = {"hotel_id", "loai_phong_id", "ngay"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TinhTrangPhong extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loai_phong_id", nullable = false)
    LoaiPhong loaiPhong;
    
    @Column(name = "ngay", nullable = false)
    LocalDate ngay;
    
    @Column(name = "tong_so_phong", nullable = false)
    Integer tongSoPhong;
    
    @Column(name = "phong_trong", nullable = false)
    Integer phongTrong;
    
    @Column(name = "gia_ghi_de", precision = 15, scale = 2)
    BigDecimal giaGhiDe;
    
    @Column(name = "co_the_dat")
    Boolean coTheDat = true;
}
