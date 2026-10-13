/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service.Impl;

import com.anshul.projects.AirBnb.Exception.ResourceNotFoundException;
import com.anshul.projects.AirBnb.Exception.UnAuthorizedException;
import com.anshul.projects.AirBnb.dto.HotelDto;
import com.anshul.projects.AirBnb.dto.HotelInfoDto;
import com.anshul.projects.AirBnb.dto.RoomDto;
import com.anshul.projects.AirBnb.entity.Hotel;
import com.anshul.projects.AirBnb.entity.Room;
import com.anshul.projects.AirBnb.entity.User;
import com.anshul.projects.AirBnb.repository.HotelRepository;
import com.anshul.projects.AirBnb.repository.RoomRepository;
import com.anshul.projects.AirBnb.service.HotelService;
import com.anshul.projects.AirBnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.anshul.projects.AirBnb.util.AppUtils.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private  final RoomRepository roomRepository;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating new hotel with name: {}", hotelDto.getName());

        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false); // business rule

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        hotel = hotelRepository.save(hotel);

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Fetching hotel with id: {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel Not Found With Id " + id));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorizedException("THIS USER DOES NOT OWN THIS HOTEL with id "+ user.getId());
        }

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating hotel with id: {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel Not Found With Id " + id));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorizedException("THIS USER DOES NOT OWN THIS HOTEL with id "+ user.getId());
        }

        // 🔥 IMPORTANT: map DTO → existing entity
        modelMapper.map(hotelDto, hotel);

        hotel.setActive(true); // business decision

        hotel = hotelRepository.save(hotel);

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long hotelId) {

        log.info("Deleting hotel with id: {}", hotelId);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel Not Found With Id " + hotelId));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorizedException("THIS USER DOES NOT OWN THIS HOTEL with id "+ user.getId());
        }

        // ✅ STEP 1: Get all rooms
        List<Room> rooms = roomRepository.findByHotel(hotel);

        // ✅ STEP 2: Delete inventory for each room
        for (Room room : rooms) {
            inventoryService.deleteAllInventory(room);
        }

        // ✅ STEP 3: Delete all rooms
        roomRepository.deleteAll(rooms);

        // ✅ STEP 4: Delete hotel
        hotelRepository.delete(hotel);

        log.info("Hotel {} deleted successfully", hotelId);
    }


    @Override
    public void activateHotel(Long id) {
        log.info("Activating hotel with id: {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Hotel Not Found With Id " + id));

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!user.equals(hotel.getOwner())){
            throw  new UnAuthorizedException("THIS USER DOES NOT OWN THIS HOTEL with id "+ user.getId());
        }

        hotel.setActive(true);


        for(Room room: hotel.getRooms()){
            inventoryService.initilizeRoomForAYear(room);
        }

    }


    //public method
    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel Not Found By Id+"+hotelId));


        List<RoomDto>room=hotel.getRooms()
                .stream().
                map((element)->modelMapper.map(element,RoomDto.class))
                .toList();


        return new HotelInfoDto(modelMapper.map(hotel,HotelDto.class),room);
    }

    @Override
    public List<HotelDto> getAllHotels() {


        User user=getCurrentUser();
        log.info("GETTING ALL HOTELS FOR ADMIN USER WITH ID  {}",user.getId());

        List<Hotel>hotels=hotelRepository.findByOwner(user);
        return (List<HotelDto>) hotels.stream()
                .map((element)->modelMapper.map(element,HotelDto.class))
                .collect(Collectors.toList());
    }
}
