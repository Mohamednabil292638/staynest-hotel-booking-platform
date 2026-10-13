/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service.Impl;

import com.anshul.projects.AirBnb.Exception.ResourceNotFoundException;
import com.anshul.projects.AirBnb.dto.*;
import com.anshul.projects.AirBnb.entity.*;
import com.anshul.projects.AirBnb.repository.HotelMinPriceRepository;
import com.anshul.projects.AirBnb.repository.InventoryRepository;
import com.anshul.projects.AirBnb.repository.RoomRepository;
import com.anshul.projects.AirBnb.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.anshul.projects.AirBnb.util.AppUtils.getCurrentUser;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final ModelMapper modelMapper;

    private  final HotelMinPriceRepository hotelMinPriceRepository;

    private final RoomRepository roomRepository;

    @Override
    public void initilizeRoomForAYear(Room room) {

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {

            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .date(currentDate)
                    .city(room.getHotel().getCity())
                    .bookedCount(0)
                    .reservedCount(0)
                    .totalCount(room.getTotalCount())
                    .price(room.getBasePrice())        // BigDecimal
                    .surgeFactor(BigDecimal.ONE)
                    .closed(false)
                    .build();

            inventoryRepository.save(inventory);

            currentDate = currentDate.plusDays(1); // 🔥 IMPORTANT
        }

        log.info("Initialized inventory for room {} for 1 year", room.getId());
    }

    @Override
    public void deleteAllInventory(Room room) {
        log.info("Deleting Inventory For Room With Room Id {}",room.getId());

        inventoryRepository.deleteByRoom(room);

    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSerchRequest) {

        log.info("Serching Hotels For {} City from {} to {} ",hotelSerchRequest.getCity(),hotelSerchRequest.getStartDate(),hotelSerchRequest.getEndDate());
        Pageable pageable = PageRequest.of(hotelSerchRequest.getPage(), hotelSerchRequest.getSize());

        long dateCount= ChronoUnit.DAYS.between(hotelSerchRequest.getStartDate(),hotelSerchRequest.getEndDate())+1;

        //business logic


      Page<HotelPriceDto> hotelPage=hotelMinPriceRepository.findHotelWithAvailableInventory(hotelSerchRequest.getCity(),
                hotelSerchRequest.getStartDate(),
                hotelSerchRequest.getEndDate(),
                hotelSerchRequest.getRoomCount(),
                dateCount,
                pageable
                );

      return hotelPage;
    }

    @Override
    public List<InventoryDto> getAllInventoryByRoom(Long roomId) {
        Room room=roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("ROOM NOT FOUND WITH ID "+ roomId));

        User user=getCurrentUser();

        if(!user.equals(room.getHotel().getOwner())){
            try {
                throw new AccessDeniedException("YOU ARE NOT THE OWNER OF ROOM WITH ID "+roomId);
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("GETTING ALL INVENTORY FOR ROOM WITH ID {}",roomId);

        return inventoryRepository.findByRoomOrderByDate(room).stream()
                .map((element)->modelMapper.map(element,InventoryDto.class)).collect(Collectors.toList());


    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("UPDATING ROOM INVENTORY FOR THIS ROOM ID {}  FOR DATE RANGE {}  TO {}", roomId,updateInventoryRequestDto.getStartDate(),updateInventoryRequestDto.getStartDate(),updateInventoryRequestDto.getEndDate());
        Room room=roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("ROOM NOT FOUND WITH ID "+ roomId));

        User user=getCurrentUser();

        if(!user.equals(room.getHotel().getOwner())){
            try {
                throw new AccessDeniedException("YOU ARE NOT THE OWNER OF ROOM WITH ID "+roomId);
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }

        inventoryRepository.getInventoryAndLockBeforeUpdate(updateInventoryRequestDto.getStartDate(),updateInventoryRequestDto.getEndDate());


        inventoryRepository.updateInventory(roomId,updateInventoryRequestDto.getStartDate(),updateInventoryRequestDto.getEndDate(),updateInventoryRequestDto.getClosed(),updateInventoryRequestDto.getSurgeFactor());
    }
}
