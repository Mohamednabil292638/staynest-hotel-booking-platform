/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Room type within a hotel")
public class RoomDto {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Deluxe King")
    private String type;

    @Schema(example = "4500")
    private BigDecimal basePrice;

    @Schema(example = "[\"https://example.com/room.jpg\"]")
    private String[] photos;

    @Schema(example = "[\"ac\", \"tv\", \"minibar\"]")
    private String[] amenties;

    @Schema(example = "10")
    private Integer totalCount;

    @Schema(example = "2")
    private Integer capacity;
}
