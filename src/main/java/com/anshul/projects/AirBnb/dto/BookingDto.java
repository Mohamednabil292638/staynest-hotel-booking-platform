/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.dto;

import com.anshul.projects.AirBnb.entity.Enums.BookingStatus;
import com.anshul.projects.AirBnb.entity.Guest;
import com.anshul.projects.AirBnb.entity.Hotel;
import com.anshul.projects.AirBnb.entity.Room;
import com.anshul.projects.AirBnb.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;



@Data
public class BookingDto {


    private Long id;
    private Integer roomCount;
    private BigDecimal amount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private Set<GuestDto>guests;


}
