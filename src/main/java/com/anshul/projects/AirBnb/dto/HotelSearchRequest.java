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
@Schema(description = "Hotel search filters (query parameters)")
public class HotelSearchRequest {

    @Schema(description = "City name", example = "Delhi")
    private String city;

    @Schema(description = "Check-in date", example = "2026-06-20")
    private LocalDate startDate;

    @Schema(description = "Check-out date", example = "2026-06-22")
    private LocalDate endDate;

    @Schema(description = "Number of rooms needed", example = "1")
    private Integer roomCount;

    @Schema(description = "Page number (0-based)", example = "0")
    private Integer page = 0;

    @Schema(description = "Page size", example = "10")
    private Integer size = 10;
}
