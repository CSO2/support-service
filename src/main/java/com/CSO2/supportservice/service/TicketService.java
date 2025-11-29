package com.CSO2.supportservice.service;

import com.CSO2.supportservice.dto.CreateTicketRequest;
import com.CSO2.supportservice.dto.TicketDTO;
import com.CSO2.supportservice.entity.SupportTicket;
import com.CSO2.supportservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

        private final TicketRepository ticketRepository;

        public TicketDTO createTicket(String userId, CreateTicketRequest req) {
                SupportTicket ticket = SupportTicket.builder()
                                .userId(userId)
                                .subject(req.getSubject())
                                .department(req.getDepartment())
                                .priority(req.getPriority() != null
                                                ? SupportTicket.TicketPriority.valueOf(req.getPriority())
                                                : SupportTicket.TicketPriority.LOW)
                                .status(SupportTicket.TicketStatus.OPEN)
                                .conversationHistory(new ArrayList<>())
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                // Add initial message
                SupportTicket.Message initialMessage = SupportTicket.Message.builder()
                                .sender("USER")
                                .message(req.getMessage())
                                .timestamp(LocalDateTime.now())
                                .build();
                ticket.getConversationHistory().add(initialMessage);

                SupportTicket savedTicket = ticketRepository.save(ticket);
                return mapToDTO(savedTicket);
        }

        public void addReply(String ticketId, String message, boolean isAdmin) {
                SupportTicket ticket = ticketRepository.findById(ticketId)
                                .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketId));

                SupportTicket.Message reply = SupportTicket.Message.builder()
                                .sender(isAdmin ? "SUPPORT" : "USER")
                                .message(message)
                                .timestamp(LocalDateTime.now())
                                .build();

                ticket.getConversationHistory().add(reply);
                ticket.setUpdatedAt(LocalDateTime.now());
                ticketRepository.save(ticket);
        }

        public List<TicketDTO> getUserTickets(String userId) {
                return ticketRepository.findByUserId(userId).stream()
                                .map(this::mapToDTO)
                                .collect(Collectors.toList());
        }

        private TicketDTO mapToDTO(SupportTicket ticket) {
                return TicketDTO.builder()
                                .id(ticket.getId())
                                .userId(ticket.getUserId())
                                .subject(ticket.getSubject())
                                .department(ticket.getDepartment())
                                .priority(ticket.getPriority().name())
                                .status(ticket.getStatus().name())
                                .conversationHistory(ticket.getConversationHistory())
                                .createdAt(ticket.getCreatedAt())
                                .updatedAt(ticket.getUpdatedAt())
                                .build();
        }
}
