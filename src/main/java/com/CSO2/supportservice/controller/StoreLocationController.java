package com.CSO2.supportservice.controller;

import com.CSO2.supportservice.entity.StoreLocation;
import com.CSO2.supportservice.service.StoreLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support/stores")
public class StoreLocationController {

    @Autowired
    private StoreLocationService storeLocationService;

    @GetMapping
    public List<StoreLocation> getAllLocations() {
        return storeLocationService.getAllLocations();
    }

    @PostMapping
    public ResponseEntity<StoreLocation> createLocation(@RequestBody StoreLocation location) {
        return ResponseEntity.ok(storeLocationService.createLocation(location));
    }
}
