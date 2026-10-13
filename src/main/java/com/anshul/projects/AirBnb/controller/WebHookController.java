/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.controller;

import com.anshul.projects.AirBnb.service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebHookController {


    private final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    private String endPointSecret;


    @PostMapping("/payment")
    public ResponseEntity<Void>capturePayment(@RequestBody String payload, @RequestHeader ("Stripe-Signature") String sigHeader){

        try {

            Event event= Webhook.constructEvent(payload,sigHeader,endPointSecret);

            bookingService.capturePayment(event);
            return ResponseEntity.noContent().build();
        }
        catch (SignatureVerificationException  e){
            throw  new RuntimeException(e);
        }

    }

}
