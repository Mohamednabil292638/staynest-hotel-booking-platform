/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.strategy;

import com.anshul.projects.AirBnb.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class UrgencyPricingStrategy implements  PricingStrategy{

    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {

         BigDecimal price=pricingStrategy.calculatePrice(inventory);

        LocalDateTime today=LocalDateTime.now();

        if (!inventory.getDate().isBefore(ChronoLocalDate.from(today)) && inventory.getDate().isBefore(ChronoLocalDate.from(today.plusDays(7)))){
            price=price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;

    }
}
