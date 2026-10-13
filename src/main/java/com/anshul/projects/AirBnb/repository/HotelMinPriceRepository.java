/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.repository;

import com.anshul.projects.AirBnb.dto.HotelPriceDto;
import com.anshul.projects.AirBnb.entity.Hotel;
import com.anshul.projects.AirBnb.entity.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice,Long> {


    @Query("""
        SELECT new com.anshul.projects.AirBnb.dto.HotelPriceDto(i.hotel,AVG(i.price))
        FROM HotelMinPrice i
        WHERE i.hotel.city = :city
          AND i.date BETWEEN :startDate AND :endDate
          AND i.hotel.active = true
        GROUP BY i.hotel
    """)
    Page<HotelPriceDto> findHotelWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );

    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
