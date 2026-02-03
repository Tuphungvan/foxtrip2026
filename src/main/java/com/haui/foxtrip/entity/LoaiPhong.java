package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.CacLoaiPhong;
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
@Table(name = "loai_phong", indexes = {
    @Index(name = "idx_loai_phong_hotel", columnList = "hotel_id")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoaiPhong {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    Hotel hotel;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_phong", nullable = false)
    CacLoaiPhong loaiPhong;  // ← SỬA: Dùng enum CacLoaiPhong
    
    @Column(name = "tong_so_phong", nullable = false)
    Integer tongSoPhong;
    
    @Column(name = "gia_moi_dem", nullable = false, precision = 15, scale = 2)
    BigDecimal giaMoiDem;
    
    @Column(name = "so_khach_toi_da", nullable = false)
    Integer soKhachToiDa;
    
    @Column(columnDefinition = "TEXT")
    String moTa;
}
