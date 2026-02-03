package com.haui.foxtrip.entity;

import com.haui.foxtrip.enums.LoaiBan;
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
@Table(name = "ban_an", indexes = {
    @Index(name = "idx_ban_restaurant", columnList = "restaurant_id")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BanAn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    Restaurant restaurant;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_ban", nullable = false)
    LoaiBan loaiBan;
    
    @Column(name = "tong_so_ban", nullable = false)
    Integer tongSoBan;
    
    @Column(name = "gia_dat_ban", precision = 15, scale = 2)
    BigDecimal giaDatBan = BigDecimal.ZERO;
}
