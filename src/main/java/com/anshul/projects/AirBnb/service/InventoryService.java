/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service;

import com.anshul.projects.AirBnb.dto.*;
import com.anshul.projects.AirBnb.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InventoryService {

    void initilizeRoomForAYear(Room room);

    void deleteAllInventory(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSerchRequest);

    List<InventoryDto> getAllInventoryByRoom(Long roomId);

    void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto);
}
