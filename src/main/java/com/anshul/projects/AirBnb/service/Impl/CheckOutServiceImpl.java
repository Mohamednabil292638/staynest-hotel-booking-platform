/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service.Impl;

import com.anshul.projects.AirBnb.entity.Booking;
import com.anshul.projects.AirBnb.entity.User;
import com.anshul.projects.AirBnb.repository.BookingRepository;
import com.anshul.projects.AirBnb.service.CheckOutService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class CheckOutServiceImpl implements CheckOutService {

    private final BookingRepository bookingRepository;


    @Override
    public String getCheckOutService(Booking booking, String successUrl, String failureUrl) {

        log.info("CREATING SESSION FOR BOOKING WITH ID ",booking.getId());
        User user = (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        try {
            CustomerCreateParams customerParam = CustomerCreateParams.builder()
                    .setName(Objects.requireNonNull(user).getName())
                    .setEmail(user.getEmail())
                    .build();

            Customer customer = Customer.create(customerParam);

            SessionCreateParams sessionCreateParams = SessionCreateParams.builder()

                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(String.valueOf(customer))
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount(booking.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(booking.getHotel().getName()+" "+booking.getRoom().getType())
                                                                    .setDescription("Booking Id"+booking.getId())
                                                                    .build()

                                                    )
                                                    .build()
                                    ).build()

                    )
                    .build();




            Session session = Session.create(sessionCreateParams);

            booking.setPaymentSessionId(session.getId());

            bookingRepository.save(booking);

            log.info("SESSION CREATED SUCESSFULLY FOR BOOKING ID ",booking.getId());

            return session.getUrl();

        } catch (StripeException e) {
            throw new RuntimeException(e);
        }


    }

}
