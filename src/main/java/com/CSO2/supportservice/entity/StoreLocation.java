package com.CSO2.supportservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_locations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String phone;

    private String email;

    @Column(nullable = false)
    private String openingHours; // e.g., "Mon-Fri: 9am-6pm"

    private Double latitude;
    private Double longitude;
}
