/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service;

import com.anshul.projects.AirBnb.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

public interface PricingService {

    public BigDecimal calculateDynamicPricing(Inventory inventory);
    public BigDecimal calculateTotalPrice(List<Inventory> inventoryList);

}
