package com.haui.foxtrip.document;

import com.haui.foxtrip.enums.ItemType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "noi_dung_dich_vu")
@CompoundIndex(name = "item_type_id_idx", def = "{'loaiDichVu': 1, 'dichVuId': 1}", unique = true)
public class NoiDung {
    
    @Id
    private String id;
    
    @Indexed
    private ItemType loaiDichVu;
    
    private String dichVuId;
    
    // Nội dung phong phú
    private String moTaHtml;
    private List<String> diemNhan;
    
    // Tour specific
    private List<NgayHanhTrinh> lichTrinhChiTiet;
    private List<String> baoGom;
    private List<String> khongBaoGom;
    
    // Transportation specific
    private ThongTinXe thongTinXe;
    private ChiTietTuyen chiTietTuyen;
    
    // Restaurant specific
    private List<DanhMucThucDon> thucDon;
    private ThongTinDauBep thongTinDauBep;
    private List<String> khongKhi;
    
    // Hotel specific
    private List<String> tienIch;
    private ChiTietPhong chiTietPhong;
    private ChinhSach chinhSach;
    private List<String> diaDiemGanDo;
    
    // SEO
    private ThongTinSeo seo;
    
    @Indexed
    private LocalDateTime createdAt;
    
    @Indexed
    private LocalDateTime updatedAt;
    
    @Data
    public static class NgayHanhTrinh {
        private Integer ngay;
        private String tieuDe;
        private List<HoatDong> hoatDongs;
        private List<String> buaAn;
        private String choO;
    }
    
    @Data
    public static class HoatDong {
        private String thoiGian;
        private String moTa;
    }
    
    @Data
    public static class ThongTinXe {
        private String soCho;
        private Integer tongSoCho;
        private List<String> tienNghi;
        private List<String> anhXe;
    }
    
    @Data
    public static class ChiTietTuyen {
        private List<DiemDung> diemDungs;
    }
    
    @Data
    public static class DiemDung {
        private String ten;
        private String gioDen;
        private String gioKhoiHanh;
    }
    
    @Data
    public static class DanhMucThucDon {
        private String danhMuc;
        private List<MonAn> monAns;
    }
    
    @Data
    public static class MonAn {
        private String ten;
        private Double gia;
        private String moTa;
    }
    
    @Data
    public static class ThongTinDauBep {
        private String ten;
        private String tieuSu;
    }
    
    @Data
    public static class ChiTietPhong {
        private List<String> tienNghi;
        private String loaiGiuong;
        private String dienTich;
    }
    
    @Data
    public static class ChinhSach {
        private String gioNhanPhong;
        private String gioTraPhong;
        private String huyPhong;
        private Boolean choPhepThuCung;
        private Boolean choPhepHutThuoc;
    }
    
    @Data
    public static class ThongTinSeo {
        private String tieuDe;
        private String moTa;
        private List<String> tuKhoa;
    }
}
