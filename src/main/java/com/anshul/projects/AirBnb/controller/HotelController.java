/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.controller;

import com.anshul.projects.AirBnb.dto.BookingDto;
import com.anshul.projects.AirBnb.dto.HotelDto;
import com.anshul.projects.AirBnb.dto.HotelReportDto;
import com.anshul.projects.AirBnb.service.BookingService;
import com.anshul.projects.AirBnb.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "4. Admin — Hotels", description = "Requires **manager** JWT — login as manager@demo.com (admin role), then Authorize")
@SecurityRequirement(name = "bearerAuth")
public class HotelController {

    private final HotelService hotelService;
    private final BookingService bookingService;

    @Operation(summary = "Create hotel")
    @PostMapping
    public ResponseEntity<HotelDto> createNewHotel(@RequestBody HotelDto hotelDto) {
        log.info("Attempting to create a new hotel with name :{} ", hotelDto.getName());
        HotelDto hotelDto1 = hotelService.createNewHotel(hotelDto);
        return new ResponseEntity<>(hotelDto1, HttpStatus.CREATED);
    }

    @Operation(summary = "Get hotel by ID")
    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(
            @Parameter(example = "1") @PathVariable Long hotelId) {
        HotelDto hotelDto = hotelService.getHotelById(hotelId);
        return ResponseEntity.ok(hotelDto);
    }

    @Operation(summary = "Update hotel")
    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotelById(
            @Parameter(example = "1") @PathVariable Long hotelId,
            @RequestBody HotelDto hotelDto) {
        HotelDto hotelDto1 = hotelService.updateHotelById(hotelId, hotelDto);
        return ResponseEntity.ok(hotelDto1);
    }

    @Operation(summary = "Delete hotel")
    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(
            @Parameter(example = "1") @PathVariable Long hotelId) {
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Activate hotel & init inventory")
    @PatchMapping("/{hotelId}")
    public ResponseEntity<Void> activateHotel(
            @Parameter(example = "1") @PathVariable Long hotelId) {
        hotelService.activateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all hotels (manager)", description = "Good first admin test after manager login")
    @GetMapping
    public ResponseEntity<List<HotelDto>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @Operation(summary = "Bookings for a hotel")
    @GetMapping("/{hotelId}/bookings")
    public ResponseEntity<List<BookingDto>> getAllBookingByHotelId(
            @Parameter(example = "1") @PathVariable Long hotelId) throws AccessDeniedException {
        return ResponseEntity.ok(bookingService.getAllBookingByHotelId(hotelId));
    }

    @Operation(summary = "Revenue report")
    @GetMapping("/{hotelId}/reports")
    public ResponseEntity<HotelReportDto> getHotelReport(
            @Parameter(example = "1") @PathVariable Long hotelId,
            @Parameter(example = "2026-05-01") @RequestParam(required = false) LocalDate startDate,
            @Parameter(example = "2026-06-17") @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
    }
}
