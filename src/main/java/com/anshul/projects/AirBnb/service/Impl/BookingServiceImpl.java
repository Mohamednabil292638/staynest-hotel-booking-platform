/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service.Impl;

import com.anshul.projects.AirBnb.Exception.ResourceNotFoundException;
import com.anshul.projects.AirBnb.Exception.UnAuthorizedException;
import com.anshul.projects.AirBnb.dto.*;
import com.anshul.projects.AirBnb.entity.*;
import com.anshul.projects.AirBnb.entity.Enums.BookingStatus;
import com.anshul.projects.AirBnb.repository.*;
import com.anshul.projects.AirBnb.service.BookingService;
import com.anshul.projects.AirBnb.service.CheckOutService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.anshul.projects.AirBnb.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private  final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private  final CheckOutService checkOutService;
    private final PricingServiceImpl pricingService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public BookingDto initialiseBooking(BookingRequest bookingRequest) {

        log.info("INITIALISING BOOKING FOR HOTEL {} ROON {} DATE{} - {} ",bookingRequest.getHotelId(),bookingRequest.getRoomId(),bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate());

        Hotel hotel=hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(()->new ResourceNotFoundException("Hotek not found with id "+bookingRequest.getHotelId()));
        Room room = roomRepository.findByIdAndHotelId(
                bookingRequest.getRoomId(),
                bookingRequest.getHotelId()
        ).orElseThrow(() ->
                new ResourceNotFoundException("Room not found for this hotel")
        );


        List<Inventory>inventoryList=inventoryRepository.findAndLockAvailableInventory(room.getId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomCount()
        );


        long daysCount= ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate())+1;

        if(inventoryList.size()!=daysCount){
            throw new IllegalStateException("Room is not available");
        }

        //reserve the room /update the booked count in inventory

       inventoryRepository.initBooking(room.getId(),bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate(),
                   bookingRequest.getRoomCount()
       );




        //TODO:calculate dynamic price

        BigDecimal priceForOneRoom=pricingService.calculateTotalPrice(inventoryList);
        BigDecimal totalPrice=priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequest.getRoomCount()));

        Booking booking=Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomCount(bookingRequest.getRoomCount())
                .amount(totalPrice)
                .build();


        booking=bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDto.class);

    }

    @Override
    @Transactional
    public BookingDto addGuest(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("Adding  Guest For Booking With Booking Id{}",bookingId);

        Booking booking=bookingRepository.findById(bookingId)
                .orElseThrow(()->new ResourceNotFoundException("Hotek not found with id "+bookingId));

        User user=getCurrentUser();
        if(!user.equals(booking.getUser())){
            throw new UnAuthorizedException("BOOKING DOES NOT BELONG TO THIS USER WITH ID "+user.getId());
        }

        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking Has Already Expired");
        }

        if(booking.getBookingStatus()!=BookingStatus.RESERVED) {
            throw new IllegalStateException("Booling Is Not Under Reserved Status Cannot Add Guest");
        }

        for(GuestDto guestDto:guestDtoList){
            Guest guest=modelMapper.map(guestDto,Guest.class);
            guest.setUser(getCurrentUser());
            guest=guestRepository.save(guest);
            booking.getGuest().add(guest);

        }

        booking.setBookingStatus(BookingStatus.GUEST_ADDED);
      booking= bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDto.class);

    }

    @Override
    @Transactional
    public String initiatePayement(Long bookingId) {

        Booking booking=bookingRepository.findById(bookingId)
                .orElseThrow(()->new ResourceNotFoundException("Booking Id Not Found"+bookingId));


        User user=getCurrentUser();
        if(!user.equals(booking.getUser())){
            throw new UnAuthorizedException("BOOKING DOES NOT BELONG TO THIS USER WITH ID "+user.getId());
        }

        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking Has Already Expired");
        }

        String sessionUrl=  checkOutService.getCheckOutService(booking,frontendUrl+"/payments/success",frontendUrl+"/payments/failure");


        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);

        return sessionUrl;


    }

    @Override
    @Transactional
    public void capturePayment(Event event) {

        if ("checkout.session.completed".equals(event.getType())){
            Session session=(Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if(session!=null){
                String sessionId=session.getId();

                Booking booking=bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(()->new ResourceNotFoundException("SESSION IF OF STRIPE NOT FOUND"));
                booking.setBookingStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking);

                inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(),booking.getCheckInDate()
                        ,booking.getCheckOutDate(),booking.getRoomCount());

                inventoryRepository.confirmBooking(booking.getRoom().getId(),booking.getCheckInDate()
                        ,booking.getCheckOutDate(),booking.getRoomCount());

                log.info("BOOKING CONFIRMED FOR SESSION ID {}",sessionId);
            }
        }else {
            log.info("UNHANDLED EVENT TYPE {}",event.getType());
        }


    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking=bookingRepository.findById(bookingId)
                .orElseThrow(()->new ResourceNotFoundException("Booking Id Not Found"+bookingId));


        User user=getCurrentUser();
        if(!user.equals(booking.getUser())){
            throw new UnAuthorizedException("BOOKING DOES NOT BELONG TO THIS USER WITH ID "+user.getId());
        }


        if(booking.getBookingStatus()!=BookingStatus.CONFIRMED){
            throw new IllegalStateException("ONLY CONFIRMED BOOKING CAN BE CANCELLED");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(),booking.getCheckInDate()
                ,booking.getCheckOutDate(),booking.getRoomCount());
        inventoryRepository.cancelBooking(booking.getRoom().getId(),booking.getCheckInDate()
                ,booking.getCheckOutDate(),booking.getRoomCount());

        log.info("BOOKING CANCELLED  FOR SESSION ID {}",bookingId);


        try {
            Session session=Session.retrieve(booking.getPaymentSessionId());
            RefundCreateParams refundCreateParam=RefundCreateParams.builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();


            Refund.create(refundCreateParam);

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String  getBookingStatus(Long bookingId) {
        Booking booking=bookingRepository.findById(bookingId)
                .orElseThrow(()->new ResourceNotFoundException("Booking Id Not Found"+bookingId));


        User user=getCurrentUser();
        if(!user.equals(booking.getUser())){
            throw new UnAuthorizedException("BOOKING DOES NOT BELONG TO THIS USER WITH ID "+user.getId());
        }

        return booking.getBookingStatus().name();
    }

    @Override
    public List<BookingDto> getAllBookingByHotelId(Long hotelId) throws AccessDeniedException {
        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("HOTEL NOT FOUND WITH ID "+hotelId));
        User user=getCurrentUser();

        if(!user.equals(hotel.getOwner()))throw new AccessDeniedException("YOU ARE NOT THE OWNER OF HOTEL WITH ID "+hotel);
        log.info("GETTING ALL BOOKING FOR HOTEL WITH ID {}",hotelId);


    List<Booking>bookings=   bookingRepository.findByHotel(hotel);

    return bookings.stream()
            .map((element)->modelMapper.map(element, BookingDto.class)).collect(Collectors.toList());



    }

    @Override
    public HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate) {

        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("HOTEL NOT FOUND WITH ID "+hotelId));
        User user=getCurrentUser();

        if(!user.equals(hotel.getOwner())){
            try {
                throw new AccessDeniedException("YOU ARE NOT THE OWNER OF HOTEL WITH ID "+hotel);
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("GETTING REPORT FOR HOTEL WITH ID {}",hotelId);


        LocalDateTime startDateTime=startDate.atStartOfDay();
        LocalDateTime endDateTime=endDate.atTime(LocalTime.MAX);


        List<Booking>bookings=bookingRepository.findByHotelAndCreatedAtBetween(hotel,startDateTime,endDateTime);

        Long totalConfirmedBooking=bookings.stream()
                .filter(booking -> booking.getBookingStatus()==BookingStatus.CONFIRMED)
                .count();

        BigDecimal totalRevenueOfConfirmedBooking=bookings.stream()
                .filter(booking -> booking.getBookingStatus()==BookingStatus.CONFIRMED)
                .map(Booking::getAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);


        BigDecimal avgRevenue=totalConfirmedBooking==0?BigDecimal.ZERO:
                totalRevenueOfConfirmedBooking.divide(BigDecimal.valueOf(totalConfirmedBooking), RoundingMode.HALF_UP);



        return new HotelReportDto(totalConfirmedBooking,totalRevenueOfConfirmedBooking,avgRevenue);



    }

    @Override
    public List<BookingDto> getMyBookings() {
        User user=getCurrentUser();
        return bookingRepository.findByUser(user).stream()
                .map((element)->modelMapper.map(element,BookingDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getMyProfile() {


        User user=getCurrentUser();
        log.info("GETTING USER INFO WITH USER ID {} ",user.getId());
        return modelMapper.map(user,UserDto.class);
    }


    public boolean hasBookingExpired(Booking booking){
        return  booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }


}
