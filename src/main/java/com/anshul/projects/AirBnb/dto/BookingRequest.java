/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Start a new booking (requires guest JWT)")
public class BookingRequest {

    @Schema(description = "Hotel ID from search results", example = "1")
    private long hotelId;

    @Schema(description = "Room ID for the hotel", example = "1")
    private long roomId;

    @Schema(description = "Check-in", example = "2026-06-20")
    private LocalDate checkInDate;

    @Schema(description = "Check-out", example = "2026-06-22")
    private LocalDate checkOutDate;

    @Schema(description = "Rooms to book", example = "1")
    private Integer roomCount;
}
