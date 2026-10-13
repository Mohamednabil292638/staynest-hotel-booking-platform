/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service;

import com.anshul.projects.AirBnb.dto.*;
import com.stripe.model.Event;
import org.jspecify.annotations.Nullable;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingService {


     BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuest(Long bookingId, List<GuestDto> guestDtoList);

    String initiatePayement(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);

    List<BookingDto>  getAllBookingByHotelId(Long hotelId) throws AccessDeniedException;

     HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

     List<BookingDto> getMyBookings();

     UserDto getMyProfile();
}
