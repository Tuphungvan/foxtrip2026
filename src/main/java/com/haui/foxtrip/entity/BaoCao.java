package com.haui.foxtrip.entity;

import com.haui.foxtrip.entity.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bao_cao_doanh_thu", 
    indexes = {
        @Index(name = "idx_bao_cao_thoi_gian", columnList = "nam, thang")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_bao_cao", columnNames = {"thang", "nam"})
    }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaoCao extends BaseEntity {
    
    @Column(name = "thang", nullable = false)
    Integer thang;
    
    @Column(name = "nam", nullable = false)
    Integer nam;
    
    @Column(name = "tong_doanh_thu", nullable = false, precision = 15, scale = 2)
    BigDecimal tongDoanhThu = BigDecimal.ZERO;
    
    @Column(name = "tong_don_hang", nullable = false)
    Integer tongDonHang = 0;
    
    @Type(JsonBinaryType.class)
    @Column(name = "chi_tiet", columnDefinition = "jsonb", nullable = false)
    Map<String, Object> chiTiet;
}
