/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data

public class HotelInfoDto {

    private HotelDto hotel;
    private List<RoomDto> room;

    public HotelInfoDto(HotelDto hotel, List<RoomDto> room) {
        this.hotel = hotel;
        this.room = room;
    }


}
