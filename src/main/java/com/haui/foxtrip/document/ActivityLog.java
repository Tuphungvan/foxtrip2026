package com.haui.foxtrip.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@Document(collection = "activity_logs")
@CompoundIndex(name = "user_time_idx", def = "{'userId': 1, 'timestamp': -1}")
@CompoundIndex(name = "resource_time_idx", def = "{'resourceType': 1, 'resourceId': 1, 'timestamp': -1}")
public class ActivityLog {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    private String userEmail;
    private String userRole;
    
    @Indexed
    private String action; // ORDER_CREATED, TOUR_UPDATED...
    
    @Indexed
    private String resourceType;
    
    private String resourceId;
    
    private ChangeDetails changeDetails;
    
    private Map<String, Object> additionalData;
    
    private String ipAddress;
    private String userAgent;
    
    @Indexed
    private LocalDateTime timestamp;
    
    @Data
    @Builder
    public static class ChangeDetails {
        private Map<String, Object> before;
        private Map<String, Object> after;
    }
}
