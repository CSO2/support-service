package com.CSO2.supportservice.service;

import com.CSO2.supportservice.entity.StoreLocation;
import com.CSO2.supportservice.repository.jpa.StoreLocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreLocationService {

    @Autowired
    private StoreLocationRepository storeLocationRepository;

    public List<StoreLocation> getAllLocations() {
        return storeLocationRepository.findAll();
    }

    public StoreLocation createLocation(StoreLocation location) {
        return storeLocationRepository.save(location);
    }
}
