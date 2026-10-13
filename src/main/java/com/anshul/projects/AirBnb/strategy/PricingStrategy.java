/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.strategy;

import com.anshul.projects.AirBnb.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {


    BigDecimal calculatePrice(Inventory inventory);

}
