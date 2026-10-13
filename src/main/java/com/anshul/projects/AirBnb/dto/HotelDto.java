/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.dto;

import com.anshul.projects.AirBnb.entity.HotelContactInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Hotel property details")
public class HotelDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Sunrise Inn")
    private String name;

    @Schema(example = "Delhi")
    private String city;

    @Schema(example = "[\"https://example.com/hotel.jpg\"]")
    private String[] photos;

    @Schema(example = "[\"wifi\", \"pool\", \"breakfast\"]")
    private String[] amenties;

    private HotelContactInfo contactInfo;

    @Schema(example = "true")
    private Boolean active;
}
