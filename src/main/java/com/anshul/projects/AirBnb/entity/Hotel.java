/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter

public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @Column(nullable = false)
    private String name;


    private  String city;

    @Column(columnDefinition = "TEXT")
    private  String[] photos;

    @Column(columnDefinition = "TEXT")
    private  String[] amenties;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private  LocalDateTime updatedAt;


    @Embedded
    private HotelContactInfo contactInfo;


    @Column(nullable = false)
    private Boolean active;

    @ManyToOne
    private  User owner;

    @OneToMany(mappedBy = "hotel")
    @JsonManagedReference
    private List<Room>rooms;
}
