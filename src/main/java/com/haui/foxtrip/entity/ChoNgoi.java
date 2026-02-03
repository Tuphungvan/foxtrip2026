package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.TrangThaiGhe;
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
@Table(name = "cho_ngoi", 
    indexes = {
        @Index(name = "idx_cho_ngoi_lich_trinh", columnList = "lich_trinh_id, trang_thai"),
        @Index(name = "idx_cho_ngoi_hang", columnList = "lich_trinh_id, hang_ghe_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_cho_ngoi", columnNames = {"lich_trinh_id", "so_cho"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChoNgoi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lich_trinh_id")
    LichTrinh lichTrinh;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hang_ghe_id")
    HangGheEntity hangGhe;
    
    @Column(name = "so_cho")
    String soCho;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    TrangThaiGhe trangThai;
}
