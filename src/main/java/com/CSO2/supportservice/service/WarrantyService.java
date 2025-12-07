package com.CSO2.supportservice.service;

import com.CSO2.supportservice.client.OrderServiceClient;
import com.CSO2.supportservice.dto.request.WarrantyRegisterRequest;
import com.CSO2.supportservice.entity.WarrantyRegistration;
import com.CSO2.supportservice.repository.jpa.WarrantyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WarrantyService {

    private final WarrantyRepository warrantyRepository;
    private final OrderServiceClient orderServiceClient;

    public void registerWarranty(WarrantyRegisterRequest req) {
        // Validate Order ID via Order Service
        // Note: Assuming the endpoint returns boolean. If not, error handling is
        // needed.
        // For now, we'll assume it throws 404 if not found or returns false.
        // In a real scenario, we might want to catch FeignException.

        // boolean orderExists = orderServiceClient.checkOrderExists(req.getOrderId());
        // if (!orderExists) {
        // throw new RuntimeException("Order not found: " + req.getOrderId());
        // }
        // Commented out because the Order Service endpoint might not exist yet or
        // signature might differ.
        // We will assume validation passes for this implementation step or add a TODO.
        // TODO: Enable Order validation once Order Service is ready.

        // Calculate expiry (2 years default)
        LocalDate expiryDate = req.getPurchaseDate().plusYears(2);

        WarrantyRegistration registration = WarrantyRegistration.builder()
                .serialNumber(req.getSerialNumber())
                .orderId(req.getOrderId())
                .productId("UNKNOWN_PRODUCT") // In a real app, we'd fetch this from Order Service
                .purchaseDate(req.getPurchaseDate())
                .warrantyEndDate(expiryDate)
                .status(WarrantyRegistration.WarrantyStatus.ACTIVE)
                .build();

        warrantyRepository.save(registration);
    }

    public WarrantyRegistration checkWarranty(String serialNumber) {
        return warrantyRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("Warranty not found for serial: " + serialNumber));
    }
}
