package com.haui.foxtrip.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
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
    private String action; // ORDER_CREATED, TOUR_UPDATED, USER_DELETED...
    
    @Indexed
    private String resourceType;
    
    private String resourceId;
    
    private Changes changes;
    
    private Map<String, Object> metadata;
    
    private String ipAddress;
    private String userAgent;
    
    @Indexed
    private LocalDateTime timestamp;
    
    @Data
    public static class Changes {
        private Map<String, Object> before;
        private Map<String, Object> after;
    }
}
