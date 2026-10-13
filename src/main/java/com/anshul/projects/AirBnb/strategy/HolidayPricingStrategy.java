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


@RequiredArgsConstructor
public class HolidayPricingStrategy implements  PricingStrategy {

    private  final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {

        BigDecimal price=pricingStrategy.calculatePrice(inventory);

        boolean isTodayHoliday=true;  //check with local data for holiday like we have array of holidays

        if(isTodayHoliday){
            price=price.multiply(BigDecimal.valueOf(1.25));
        }

        return  price;

    }
}
