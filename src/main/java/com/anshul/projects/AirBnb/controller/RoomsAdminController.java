/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.controller;

import com.anshul.projects.AirBnb.dto.RoomDto;
import com.anshul.projects.AirBnb.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "5. Admin — Rooms", description = "Requires manager@demo.com JWT")
@SecurityRequirement(name = "bearerAuth")
public class RoomsAdminController {

    private final RoomService roomService;

    @Operation(summary = "Create room")
    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(
            @Parameter(example = "1") @PathVariable Long hotelId,
            @RequestBody RoomDto roomDto
    ) {
        log.info("Creating room for hotelId: {}", hotelId);
        return new ResponseEntity<>(
                roomService.createNewRoom(hotelId, roomDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(
            @PathVariable Long hotelId,
            @PathVariable Long roomId
    ) {
        return ResponseEntity.ok(roomService.getRoomById(hotelId, roomId));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoomById(
            @PathVariable Long hotelId,
            @PathVariable Long roomId) {
        roomService.deleteRoomById(hotelId, roomId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDto>updateRoomById(@PathVariable  Long hotelId, @PathVariable Long roomId,
                                                 @RequestBody RoomDto roomDto){
        return ResponseEntity.ok(roomService.updateRoomById(hotelId,roomId,roomDto));


    }

}
