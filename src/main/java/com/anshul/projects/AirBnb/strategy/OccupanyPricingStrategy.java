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
public class OccupanyPricingStrategy implements PricingStrategy{

    private final PricingStrategy pricingStrategy;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price=pricingStrategy.calculatePrice(inventory);

        double occupanyRate =
                inventory.getBookedCount() * 1.0 / inventory.getTotalCount();

        if(occupanyRate>0.8){
            price=price.multiply(BigDecimal.valueOf(1.2));
        }

        return price;
    }
}
