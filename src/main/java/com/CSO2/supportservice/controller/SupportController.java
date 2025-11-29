package com.CSO2.supportservice.controller;

import com.CSO2.supportservice.dto.CreateTicketRequest;
import com.CSO2.supportservice.dto.TicketDTO;
import com.CSO2.supportservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class SupportController {

    private final TicketService ticketService;

    @PostMapping("/tickets")
    public ResponseEntity<TicketDTO> createTicket(@RequestHeader("X-User-Id") String userId,
            @RequestBody CreateTicketRequest req) {
        return ResponseEntity.ok(ticketService.createTicket(userId, req));
    }

    @PostMapping("/tickets/{id}/reply")
    public ResponseEntity<String> addReply(@PathVariable String id,
            @RequestBody Map<String, String> payload,
            @RequestParam(defaultValue = "false") boolean isAdmin) {
        String message = payload.get("message");
        ticketService.addReply(id, message, isAdmin);
        return ResponseEntity.ok("Reply added successfully");
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDTO>> getUserTickets(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(ticketService.getUserTickets(userId));
    }
}
