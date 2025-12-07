package com.CSO2.supportservice.service;

import com.CSO2.supportservice.dto.request.CreateTicketRequest;
import com.CSO2.supportservice.dto.response.TicketDTO;
import com.CSO2.supportservice.entity.SupportTicket;
import com.CSO2.supportservice.repository.mongo.TicketRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private SupportTicket mockTicket;

    @BeforeEach
    void setUp() {
        mockTicket = SupportTicket.builder()
                .id("ticket123")
                .userId("user123")
                .subject("Test Issue")
                .department("Technical")
                .priority(SupportTicket.TicketPriority.HIGH)
                .status(SupportTicket.TicketStatus.OPEN)
                .conversationHistory(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createTicket_ShouldSaveAndReturnDTO() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setSubject("Test Issue");
        request.setDepartment("Technical");
        request.setPriority("HIGH");
        request.setMessage("Initial message");

        when(ticketRepository.save(any(SupportTicket.class))).thenAnswer(invocation -> {
            SupportTicket saved = invocation.getArgument(0);
            saved.setId("ticket123");
            return saved;
        });

        TicketDTO result = ticketService.createTicket("user123", request);

        assertNotNull(result);
        assertEquals("ticket123", result.getId());
        assertEquals("Test Issue", result.getSubject());
        assertEquals(1, result.getConversationHistory().size());
        assertEquals("Initial message", result.getConversationHistory().get(0).getMessage());
        verify(ticketRepository, times(1)).save(any(SupportTicket.class));
    }

    @Test
    void addReply_ShouldAddMessageAndUpdateTicket() {
        when(ticketRepository.findById("ticket123")).thenReturn(Optional.of(mockTicket));
        when(ticketRepository.save(any(SupportTicket.class))).thenReturn(mockTicket);

        ticketService.addReply("ticket123", "New reply", false);

        assertEquals(1, mockTicket.getConversationHistory().size());
        assertEquals("New reply", mockTicket.getConversationHistory().get(0).getMessage());
        assertEquals("USER", mockTicket.getConversationHistory().get(0).getSender());
        verify(ticketRepository, times(1)).save(mockTicket);
    }

    @Test
    void addReply_AsAdmin_ShouldAddMessageWithSupportSender() {
        when(ticketRepository.findById("ticket123")).thenReturn(Optional.of(mockTicket));
        when(ticketRepository.save(any(SupportTicket.class))).thenReturn(mockTicket);

        ticketService.addReply("ticket123", "Admin reply", true);

        assertEquals(1, mockTicket.getConversationHistory().size());
        assertEquals("Admin reply", mockTicket.getConversationHistory().get(0).getMessage());
        assertEquals("SUPPORT", mockTicket.getConversationHistory().get(0).getSender());
        verify(ticketRepository, times(1)).save(mockTicket);
    }

    @Test
    void addReply_TicketNotFound_ShouldThrowException() {
        when(ticketRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.addReply("invalid", "msg", false));
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void getUserTickets_ShouldReturnList() {
        when(ticketRepository.findByUserId("user123")).thenReturn(List.of(mockTicket));

        List<TicketDTO> result = ticketService.getUserTickets("user123");

        assertEquals(1, result.size());
        assertEquals("ticket123", result.get(0).getId());
        verify(ticketRepository, times(1)).findByUserId("user123");
    }

    @Test
    void getAllTickets_ShouldReturnAll() {
        when(ticketRepository.findAll()).thenReturn(List.of(mockTicket));

        List<TicketDTO> result = ticketService.getAllTickets();

        assertEquals(1, result.size());
        assertEquals("ticket123", result.get(0).getId());
        verify(ticketRepository, times(1)).findAll();
    }
}
