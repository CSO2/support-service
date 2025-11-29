package com.CSO2.supportservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketRequest {
    private String subject;
    private String message;
    private String department;
    private String priority; // Optional, default to LOW if null
}
