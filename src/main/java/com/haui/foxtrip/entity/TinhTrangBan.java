package com.haui.foxtrip.entity;

import com.haui.foxtrip.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tinh_trang_ban", 
    indexes = {
        @Index(name = "idx_tinh_trang_ban_ngay", columnList = "restaurant_id, ngay"),
        @Index(name = "idx_tinh_trang_ban_khung_gio", columnList = "restaurant_id, ngay, khung_gio")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_tinh_trang_ban", 
            columnNames = {"restaurant_id", "ban_an_id", "ngay", "khung_gio"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TinhTrangBan extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    Restaurant restaurant;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ban_an_id", nullable = false)
    BanAn banAn;
    
    @Column(name = "ngay", nullable = false)
    LocalDate ngay;
    
    @Column(name = "khung_gio", nullable = false)
    LocalTime khungGio;
    
    @Column(name = "tong_so_ban", nullable = false)
    Integer tongSoBan;
    
    @Column(name = "ban_trong", nullable = false)
    Integer banTrong;
    
    @Column(name = "co_the_dat")
    Boolean coTheDat = true;
}
