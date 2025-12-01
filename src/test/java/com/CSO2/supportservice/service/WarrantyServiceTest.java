package com.CSO2.supportservice.service;

import com.CSO2.supportservice.client.OrderServiceClient;
import com.CSO2.supportservice.dto.request.WarrantyRegisterRequest;
import com.CSO2.supportservice.entity.WarrantyRegistration;
import com.CSO2.supportservice.repository.WarrantyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarrantyServiceTest {

    @Mock
    private WarrantyRepository warrantyRepository;

    @Mock
    private OrderServiceClient orderServiceClient;

    @InjectMocks
    private WarrantyService warrantyService;

    private WarrantyRegistration mockWarranty;

    @BeforeEach
    void setUp() {
        mockWarranty = WarrantyRegistration.builder()
                .id("warranty123")
                .serialNumber("SN12345")
                .orderId("order123")
                .productId("prod123")
                .purchaseDate(LocalDate.now().minusDays(10))
                .warrantyEndDate(LocalDate.now().plusYears(2).minusDays(10))
                .status(WarrantyRegistration.WarrantyStatus.ACTIVE)
                .build();
    }

    @Test
    void registerWarranty_ShouldSaveRegistration() {
        WarrantyRegisterRequest request = new WarrantyRegisterRequest();
        request.setSerialNumber("SN12345");
        request.setOrderId("order123");
        request.setPurchaseDate(LocalDate.now());

        // Assuming order validation is currently commented out or mocked to pass
        // If uncommented in future, add:
        // when(orderServiceClient.checkOrderExists("order123")).thenReturn(true);

        when(warrantyRepository.save(any(WarrantyRegistration.class))).thenReturn(mockWarranty);

        warrantyService.registerWarranty(request);

        verify(warrantyRepository, times(1)).save(any(WarrantyRegistration.class));
    }

    @Test
    void checkWarranty_ShouldReturnRegistration() {
        when(warrantyRepository.findBySerialNumber("SN12345")).thenReturn(Optional.of(mockWarranty));

        WarrantyRegistration result = warrantyService.checkWarranty("SN12345");

        assertNotNull(result);
        assertEquals("SN12345", result.getSerialNumber());
        verify(warrantyRepository, times(1)).findBySerialNumber("SN12345");
    }

    @Test
    void checkWarranty_NotFound_ShouldThrowException() {
        when(warrantyRepository.findBySerialNumber("invalid")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> warrantyService.checkWarranty("invalid"));
    }
}
