/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service.Impl;

import com.anshul.projects.AirBnb.entity.Inventory;
import com.anshul.projects.AirBnb.service.PricingService;
import com.anshul.projects.AirBnb.strategy.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PricingServiceImpl implements PricingService {



    @Override
    public BigDecimal calculateDynamicPricing(Inventory inventory) {
        PricingStrategy basePricingStrategy=new BasePricingStrategy();


        //apply additional strategy


        basePricingStrategy=new SurgePricingStrategy(basePricingStrategy);

        basePricingStrategy=new OccupanyPricingStrategy(basePricingStrategy);

        basePricingStrategy=new UrgencyPricingStrategy(basePricingStrategy);

        basePricingStrategy=new HolidayPricingStrategy(basePricingStrategy);


        return basePricingStrategy.calculatePrice(inventory);

    }


    //return sum of price of this inventory list
    public BigDecimal calculateTotalPrice(List<Inventory>inventoryList){

       return inventoryList.stream()
                .map(this::calculateDynamicPricing)
                .reduce(BigDecimal.ZERO,BigDecimal::add);


    }
}
