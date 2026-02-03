package com.haui.foxtrip.entity;

import com.haui.foxtrip.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lich_trinh", 
    indexes = {
        @Index(name = "idx_lich_trinh_phuong_tien", columnList = "phuong_tien_id, ngay_khoi_hanh"),
        @Index(name = "idx_lich_trinh_tuyen", columnList = "tuyen_duong_id, ngay_khoi_hanh"),
        @Index(name = "idx_lich_trinh_ngay", columnList = "ngay_khoi_hanh, co_the_dat")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_lich_trinh", 
            columnNames = {"phuong_tien_id", "tuyen_duong_id", "ngay_khoi_hanh", "gio_khoi_hanh"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LichTrinh extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phuong_tien_id", nullable = false)
    PhuongTien phuongTien;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tuyen_duong_id", nullable = false)
    TuyenDuong tuyenDuong;
    
    @Column(name = "ngay_khoi_hanh", nullable = false)
    LocalDate ngayKhoiHanh;
    
    @Column(name = "gio_khoi_hanh", nullable = false)
    LocalTime gioKhoiHanh;
    
    @Column(name = "gio_den", nullable = false)
    LocalTime gioDen;
    
    @Column(name = "tong_so_cho", nullable = false)
    Integer tongSoCho;
    
    @Column(name = "so_cho_trong", nullable = false)
    Integer soChoTrong;
    
    @Column(name = "gia_ghi_de", precision = 15, scale = 2)
    BigDecimal giaGhiDe;
    
    @Column(name = "co_the_dat")
    Boolean coTheDat = true;
}
