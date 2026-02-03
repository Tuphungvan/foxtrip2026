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
@Table(name = "tuyen_duong", indexes = {
    @Index(name = "idx_tuyen_phuong_tien", columnList = "phuong_tien_id, hoat_dong"),
    @Index(name = "idx_tuyen_diem", columnList = "diem_di, diem_den")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TuyenDuong {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phuong_tien_id", nullable = false)
    PhuongTien phuongTien;
    
    @Column(name = "ten_tuyen", nullable = false)
    String tenTuyen;
    
    @Column(name = "diem_di", nullable = false)
    String diemDi;
    
    @Column(name = "diem_den", nullable = false)
    String diemDen;
    
    @Column(name = "khoang_cach_km")
    Integer khoangCachKm;
    
    @Column(name = "thoi_gian_phut", nullable = false)
    Integer thoiGianPhut;
    
    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal gia;
    
    @Column(name = "thu_tu_hien_thi")
    Integer thuTuHienThi = 0;
    
    @Column(name = "hoat_dong")
    Boolean hoatDong = true;
}
