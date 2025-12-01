package com.CSO2.supportservice.controller;

import com.CSO2.supportservice.dto.request.CreateTicketRequest;
import com.CSO2.supportservice.dto.response.TicketDTO;
import com.CSO2.supportservice.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private SupportController supportController;

    private TicketDTO mockTicketDTO;

    @BeforeEach
    void setUp() {
        mockTicketDTO = TicketDTO.builder()
                .id("ticket123")
                .userId("user123")
                .subject("Test Issue")
                .department("Technical")
                .priority("HIGH")
                .status("OPEN")
                .conversationHistory(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createTicket_ShouldReturnTicketDTO() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setSubject("Test Issue");
        request.setDepartment("Technical");
        request.setPriority("HIGH");
        request.setMessage("Help needed");

        when(ticketService.createTicket(eq("user123"), any(CreateTicketRequest.class))).thenReturn(mockTicketDTO);

        ResponseEntity<TicketDTO> response = supportController.createTicket("user123", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ticket123", response.getBody().getId());
        verify(ticketService, times(1)).createTicket(eq("user123"), any(CreateTicketRequest.class));
    }

    @Test
    void addReply_ShouldReturnSuccessMessage() {
        Map<String, String> payload = Map.of("message", "Reply message");

        doNothing().when(ticketService).addReply("ticket123", "Reply message", false);

        ResponseEntity<String> response = supportController.addReply("ticket123", payload, false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reply added successfully", response.getBody());
        verify(ticketService, times(1)).addReply("ticket123", "Reply message", false);
    }

    @Test
    void getUserTickets_ShouldReturnListOfTickets() {
        when(ticketService.getUserTickets("user123")).thenReturn(List.of(mockTicketDTO));

        ResponseEntity<List<TicketDTO>> response = supportController.getUserTickets("user123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(ticketService, times(1)).getUserTickets("user123");
    }

    @Test
    void getAllTickets_AsAdmin_ShouldReturnAllTickets() {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        UserDetails adminUser = new User("admin", "password", authorities);

        when(ticketService.getAllTickets()).thenReturn(List.of(mockTicketDTO));

        ResponseEntity<List<TicketDTO>> response = supportController.getAllTickets(adminUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(ticketService, times(1)).getAllTickets();
    }

    @Test
    void getAllTickets_AsUser_ShouldReturnForbidden() {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails normalUser = new User("user", "password", authorities);

        ResponseEntity<List<TicketDTO>> response = supportController.getAllTickets(normalUser);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(ticketService, never()).getAllTickets();
    }
}
