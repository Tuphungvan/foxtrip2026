package com.haui.foxtrip.document;

import com.haui.foxtrip.enums.ItemType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "reviews")
@CompoundIndex(name = "item_date_idx", def = "{'itemType': 1, 'itemId': 1, 'createdAt': -1}")
public class DanhGia {
    
    @Id
    private String id;
    
    @Indexed
    private ItemType itemType;
    
    private String itemId;
    
    @Indexed
    private String userId;
    
    private String userName;
    private String userAvatar;
    
    private String orderId;
    private String orderItemId;
    
    @Indexed
    private Integer rating; // 1-5
    
    private String title;
    private String content;
    private List<String> images;
    
    private Boolean verifiedPurchase;
    
    @Indexed
    private String status; // PENDING, APPROVED, REJECTED
    
    private Integer helpfulCount = 0;
    
    private AdminResponse adminResponse;
    
    @Indexed
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @Data
    public static class AdminResponse {
        private String content;
        private String respondedBy;
        private LocalDateTime respondedAt;
    }
}
