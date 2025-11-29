package com.CSO2.supportservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "support_tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportTicket {

    @Id
    private String id;

    private String userId;
    private String subject;
    private String description;
    private String department;

    private TicketPriority priority;
    private TicketStatus status;

    @Builder.Default
    private List<Message> conversationHistory = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum TicketPriority {
        LOW, MEDIUM, HIGH
    }

    public enum TicketStatus {
        OPEN, CLOSED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String sender; // "USER" or "SUPPORT"
        private String message;
        private LocalDateTime timestamp;
    }
}
