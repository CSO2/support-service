package com.CSO2.supportservice.controller;

import com.CSO2.supportservice.dto.request.WarrantyRegisterRequest;
import com.CSO2.supportservice.entity.WarrantyRegistration;
import com.CSO2.supportservice.service.WarrantyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warranty")
@RequiredArgsConstructor
public class WarrantyController {

    private final WarrantyService warrantyService;

    @PostMapping("/register")
    public ResponseEntity<String> registerWarranty(@RequestBody WarrantyRegisterRequest req) {
        warrantyService.registerWarranty(req);
        return ResponseEntity.ok("Warranty registered successfully");
    }

    @GetMapping("/check/{serial}")
    public ResponseEntity<WarrantyRegistration> checkWarranty(@PathVariable String serial) {
        return ResponseEntity.ok(warrantyService.checkWarranty(serial));
    }
}
