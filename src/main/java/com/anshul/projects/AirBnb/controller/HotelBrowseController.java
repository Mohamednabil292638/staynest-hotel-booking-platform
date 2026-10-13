/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.controller;

import com.anshul.projects.AirBnb.dto.HotelInfoDto;
import com.anshul.projects.AirBnb.dto.HotelPriceDto;
import com.anshul.projects.AirBnb.dto.HotelSearchRequest;
import com.anshul.projects.AirBnb.service.HotelService;
import com.anshul.projects.AirBnb.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
@Slf4j
@Tag(name = "2. Hotels (public)", description = "No login required — start here to search demo hotels in Delhi, Goa, Bangalore")
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @Operation(summary = "Search hotels by city & dates", description = "Query params are pre-filled. Click **Try it out** → **Execute**.")
    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> serchHotels(
            @ModelAttribute HotelSearchRequest request
    ) {
        log.info("Searching hotels with request: {}", request);
        Page<HotelPriceDto> page = inventoryService.searchHotels(request);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Hotel details + rooms")
    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(
            @Parameter(description = "Hotel ID from search", example = "1")
            @PathVariable Long hotelId) {
        HotelInfoDto hotelInfoDto = hotelService.getHotelInfoById(hotelId);
        return ResponseEntity.ok(hotelInfoDto);
    }
}
