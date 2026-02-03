package com.haui.foxtrip.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "chat_messages")
@CompoundIndex(name = "room_date_idx", def = "{'roomId': 1, 'createdAt': -1}")
public class ChatMessage {
    
    @Id
    private String id;
    
    @Indexed
    private String roomId;
    
    @Indexed
    private String senderId;
    
    private String senderName;
    private String senderType; // USER, ADMIN, BOT
    
    private String messageType; // TEXT, IMAGE, FILE
    private String content;
    
    private List<Attachment> attachments;
    
    @Builder.Default
    private Boolean isBotMessage = false;
    private BotContext botContext;
    
    private LocalDateTime readAt;
    
    @Indexed
    private LocalDateTime createdAt;
    
    @Data
    @Builder
    public static class Attachment {
        private String type;
        private String url;
        private String filename;
    }
    
    @Data
    @Builder
    public static class BotContext {
        private String intent;
        private Double confidence;
    }
}
