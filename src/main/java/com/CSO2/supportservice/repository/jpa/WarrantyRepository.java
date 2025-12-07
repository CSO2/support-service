package com.CSO2.supportservice.repository.jpa;

import com.CSO2.supportservice.entity.WarrantyRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarrantyRepository extends JpaRepository<WarrantyRegistration, String> {
    Optional<WarrantyRegistration> findBySerialNumber(String serialNumber);
}
