package com.CSO2.supportservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "warranty_registrations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Column(nullable = false)
    private LocalDate warrantyEndDate;

    @Enumerated(EnumType.STRING)
    private WarrantyStatus status;

    public enum WarrantyStatus {
        ACTIVE, EXPIRED
    }
}
