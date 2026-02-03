package com.haui.foxtrip.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "ai_chat_history")
public class LichSuAI {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    @Indexed
    private String sessionId;
    
    private List<Message> messages;
    
    private Context context;
    
    @Indexed
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @Data
    public static class Message {
        private String role; // user, assistant
        private String content;
        private LocalDateTime timestamp;
    }
    
    @Data
    public static class Context {
        private String intent;
        private Map<String, Object> entities;
        private List<String> suggestedTours;
    }
}
