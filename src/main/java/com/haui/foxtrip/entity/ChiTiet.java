package com.haui.foxtrip.entity;

import com.haui.foxtrip.entity.BaseEntity;
import com.haui.foxtrip.enums.ItemType;
import com.haui.foxtrip.enums.OrderStatus;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chi_tiet_don_hang", indexes = {
    @Index(name = "idx_chi_tiet_order", columnList = "order_id"),
    @Index(name = "idx_chi_tiet_loai", columnList = "loai_dich_vu, dich_vu_id"),
    @Index(name = "idx_chi_tiet_trang_thai", columnList = "trang_thai")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChiTiet extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "loai_dich_vu", nullable = false)
    ItemType loaiDichVu;
    
    @Column(name = "dich_vu_id", nullable = false)
    UUID dichVuId;
    
    @Type(JsonBinaryType.class)
    @Column(name = "thong_tin_luu", columnDefinition = "jsonb", nullable = false)
    Map<String, Object> thongTinLuu;
    
    @Column(name = "so_luong", nullable = false)
    Integer soLuong = 1;
    
    @Column(name = "don_gia", nullable = false, precision = 15, scale = 2)
    BigDecimal donGia;
    
    @Column(name = "giam_gia", precision = 15, scale = 2)
    BigDecimal giamGia = BigDecimal.ZERO;
    
    @Column(name = "thanh_tien", nullable = false, precision = 15, scale = 2)
    BigDecimal thanhTien;
    
    @Type(JsonBinaryType.class)
    @Column(name = "chi_tiet_dat_cho", columnDefinition = "jsonb", nullable = false)
    Map<String, Object> chiTietDatCho;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false)
    OrderStatus trangThai;
}
