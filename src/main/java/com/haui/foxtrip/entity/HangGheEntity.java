package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.HangGhe;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hang_ghe", indexes = {
    @Index(name = "idx_hang_ghe_phuong_tien", columnList = "phuong_tien_id, hoat_dong")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HangGheEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phuong_tien_id")
    PhuongTien phuongTien;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ten_hang")
    HangGhe tenHang;
    
    @Column(columnDefinition = "TEXT")
    String moTa;
    
    @Column(name = "gia_cong", precision = 15, scale = 2)
    BigDecimal giaCong;
    
    @Column(name = "tong_so_cho")
    Integer tongSoCho;
    
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "tien_nghi", columnDefinition = "text[]")
    String[] tienNghi;
    
    @Column(name = "thu_tu_hien_thi")
    Integer thuTuHienThi;
    
    @Column(name = "hoat_dong")
    Boolean hoatDong = true;
}
