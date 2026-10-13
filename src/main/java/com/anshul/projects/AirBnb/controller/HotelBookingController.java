/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.controller;

import com.anshul.projects.AirBnb.dto.BookingDto;
import com.anshul.projects.AirBnb.dto.BookingRequest;
import com.anshul.projects.AirBnb.dto.GuestDto;
import com.anshul.projects.AirBnb.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "3. Bookings", description = "Requires **guest** JWT — login as guest@demo.com first, then Authorize")
@SecurityRequirement(name = "bearerAuth")
public class HotelBookingController {

    private final BookingService bookingService;

    @Operation(summary = "Start a booking", description = "Use hotelId/roomId from search or hotel info. Body is pre-filled.")
    @PostMapping("/init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingRequest bookingRequest) {
        log.info("Booking request received: {}", bookingRequest);
        return ResponseEntity.ok(bookingService.initialiseBooking(bookingRequest));
    }

    @Operation(summary = "Add guests to booking")
    @PostMapping("/{bookingId}/addGuest")
    public ResponseEntity<BookingDto> addGuest(
            @Parameter(example = "1") @PathVariable Long bookingId,
            @RequestBody List<GuestDto> guestDtoList) {
        return ResponseEntity.ok(bookingService.addGuest(bookingId, guestDtoList));
    }

    @Operation(summary = "Initiate Stripe payment")
    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<Map<String, String>> initiatePayment(
            @Parameter(example = "1") @PathVariable Long bookingId) {
        String sessionUrl = bookingService.initiatePayement(bookingId);
        return ResponseEntity.ok(Map.of("sessionUrl", sessionUrl));
    }

    @Operation(summary = "Cancel booking")
    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @Parameter(example = "1") @PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get booking status")
    @PostMapping("/{bookingId}/status")
    public ResponseEntity<Map<String, String>> getBookingStatus(
            @Parameter(example = "1") @PathVariable Long bookingId) {
        return ResponseEntity.ok(Map.of("STATUS ", bookingService.getBookingStatus(bookingId)));
    }
}
