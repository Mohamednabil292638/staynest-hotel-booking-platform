/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.controller;

import com.anshul.projects.AirBnb.dto.BookingDto;
import com.anshul.projects.AirBnb.dto.UserDto;
import com.anshul.projects.AirBnb.dto.UserProfileUpdateRequestDto;
import com.anshul.projects.AirBnb.service.BookingService;
import com.anshul.projects.AirBnb.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Tag(name = "7. Users", description = "Requires guest@demo.com JWT")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    private final BookingService bookingService;

    @PatchMapping("/profile")
    public ResponseEntity<Void>updateProfile(@RequestBody UserProfileUpdateRequestDto profileUpdateRequestDto){

        userService.updateProfile(profileUpdateRequestDto);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "My bookings")
    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingDto>>getMyBookings(){
       return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @Operation(summary = "My profile")
    @GetMapping("/me")
    public ResponseEntity<UserDto>getMyProfile(){

        return ResponseEntity.ok(bookingService.getMyProfile());
    }



}
