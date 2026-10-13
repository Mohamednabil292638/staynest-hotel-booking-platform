/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service.Impl;

import com.anshul.projects.AirBnb.Exception.ResourceNotFoundException;
import com.anshul.projects.AirBnb.Exception.UnAuthorizedException;
import com.anshul.projects.AirBnb.dto.RoomDto;
import com.anshul.projects.AirBnb.entity.Hotel;
import com.anshul.projects.AirBnb.entity.Room;
import com.anshul.projects.AirBnb.entity.User;
import com.anshul.projects.AirBnb.repository.HotelRepository;
import com.anshul.projects.AirBnb.repository.RoomRepository;
import com.anshul.projects.AirBnb.service.InventoryService;
import com.anshul.projects.AirBnb.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.anshul.projects.AirBnb.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel Not Found With Id " + hotelId));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorizedException("THIS USER DOES NOT OWN THIS HOTEL with id "+ hotelId);
        }

        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);

        room = roomRepository.save(room);

        if(hotel.getActive()){
            inventoryService.initilizeRoomForAYear(room);
        }

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel Not Found With Id " + hotelId));


        return roomRepository.findByHotel(hotel)
                .stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long hotelId, Long roomId) {

        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Room Not Found With Id " + roomId));

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public void deleteRoomById(Long hotelId, Long roomId) {

        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Room Not Found With Id " + roomId));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      if(!user.equals(room.getHotel().getOwner())){
          throw  new UnAuthorizedException("THIS USER DOES NOT OWN THIS HOTEL WITH ID "+ hotelId);
      }

        inventoryService.deleteAllInventory(room);
        roomRepository.delete(room);
    }

    @Override
    @Transactional
    public RoomDto updateRoomById(Long hotelId, Long roomId, RoomDto roomDto) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel Not Found With Id " + hotelId));

        User user= getCurrentUser();

        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorizedException("THIS USER DOES NOT OWN THIS HOTEL with id "+ user.getId());
        }

        Room room=roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("ROOM NOT FOUND WITH ID "+roomId));

        modelMapper.map(roomDto,Room.class);
        room.setId(roomId);

        room=roomRepository.save(room);
        return modelMapper.map(room,RoomDto.class);

        //todo if price or inventory is updated then update inventory for this room



    }
}
