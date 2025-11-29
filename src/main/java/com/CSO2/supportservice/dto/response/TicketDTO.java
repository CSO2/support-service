package com.CSO2.supportservice.dto.response;

import com.CSO2.supportservice.entity.SupportTicket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private String id;
    private String userId;
    private String subject;
    private String department;
    private String priority;
    private String status;
    private List<SupportTicket.Message> conversationHistory;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
