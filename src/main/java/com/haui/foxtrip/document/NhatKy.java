package com.haui.foxtrip.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "nhat_ky_hoat_dong")
@CompoundIndex(name = "user_time_idx", def = "{'userId': 1, 'thoiGian': -1}")
@CompoundIndex(name = "resource_time_idx", def = "{'loaiTaiNguyen': 1, 'taiNguyenId': 1, 'thoiGian': -1}")
public class NhatKy {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    private String userEmail;
    private String vaiTro;
    
    @Indexed
    private String hanhDong; // ORDER_CREATED, TOUR_UPDATED...
    
    @Indexed
    private String loaiTaiNguyen;
    
    private String taiNguyenId;
    
    private ThayDoi thayDoi;
    
    private Map<String, Object> duLieuKhac;
    
    private String diaChiIp;
    private String userAgent;
    
    @Indexed
    private LocalDateTime thoiGian;
    
    @Data
    public static class ThayDoi {
        private Map<String, Object> truoc;
        private Map<String, Object> sau;
    }
}
