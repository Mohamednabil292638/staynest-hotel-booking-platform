/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.strategy;

import com.anshul.projects.AirBnb.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BasePricingStrategy implements  PricingStrategy{


    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
       return  inventory.getRoom().getBasePrice();
    }
}
