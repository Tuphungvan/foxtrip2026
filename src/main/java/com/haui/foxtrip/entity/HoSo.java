package com.haui.foxtrip.entity;

import com.haui.foxtrip.entity.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ho_so_nguoi_dung", indexes = {
    @Index(name = "idx_ho_so_keycloak", columnList = "keycloak_user_id"),
    @Index(name = "idx_ho_so_sdt", columnList = "so_dien_thoai"),
    @Index(name = "idx_ho_so_email", columnList = "email")
})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HoSo extends BaseEntity {
    
    @Column(name = "keycloak_user_id", unique = true, nullable = false)
    UUID keycloakUserId;
    
    @Column(nullable = false)
    String email;
    
    @Column(nullable = false)
    String username;
    
    @Column(name = "ho_ten")
    String hoTen;
    
    String avatar;
    
    @Column(name = "so_dien_thoai")
    String soDienThoai;
    
    @Column(columnDefinition = "TEXT")
    String diaChi;
    
    @Column(name = "ngay_sinh")
    LocalDate ngaySinh;
    
    @Column(name = "gioi_tinh")
    String gioiTinh;
    
    @Type(JsonBinaryType.class)
    @Column(name = "tuy_chon", columnDefinition = "jsonb")
    Map<String, Object> tuyChon;
    
    @Type(JsonBinaryType.class)
    @Column(name = "du_lieu_khac", columnDefinition = "jsonb")
    Map<String, Object> duLieuKhac;
    
    @Column(name = "hoat_dong")
    Boolean hoatDong = true;
    
    @Column(name = "email_xac_thuc")
    Boolean emailXacThuc = false;
    
    @Column(name = "sdt_xac_thuc")
    Boolean sdtXacThuc = false;
}
