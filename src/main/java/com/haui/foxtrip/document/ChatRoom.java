package com.haui.foxtrip.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "chat_rooms")
@CompoundIndex(name = "user_status_idx", def = "{'userId': 1, 'status': 1}")
@CompoundIndex(name = "assigned_status_idx", def = "{'assignedTo': 1, 'status': 1}")
public class ChatRoom {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    private String userName;
    private String userEmail;
    
    @Indexed
    private String assignedTo;
    
    private String assignedToName;
    
    @Indexed
    private String status; // OPEN, CLOSED, RESOLVED
    
    @Indexed
    private String priority; // LOW, NORMAL, HIGH, URGENT
    
    private String subject;
    private List<String> tags;
    
    private LastMessage lastMessage;
    
    private Integer unreadCount = 0;
    
    private LocalDateTime createdAt;
    
    @Indexed
    private LocalDateTime updatedAt;
    
    private LocalDateTime closedAt;
    
    @Data
    public static class LastMessage {
        private String content;
        private String senderType;
        private LocalDateTime createdAt;
    }
}
