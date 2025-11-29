package com.CSO2.supportservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyRegisterRequest {
    private String serialNumber;
    private String orderId;
    private LocalDate purchaseDate;
}
