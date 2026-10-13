/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.service.Impl;

import com.anshul.projects.AirBnb.entity.Hotel;
import com.anshul.projects.AirBnb.entity.HotelMinPrice;
import com.anshul.projects.AirBnb.entity.Inventory;
import com.anshul.projects.AirBnb.repository.HotelMinPriceRepository;
import com.anshul.projects.AirBnb.repository.HotelRepository;
import com.anshul.projects.AirBnb.repository.InventoryRepository;
import com.anshul.projects.AirBnb.service.PricingService;
import com.anshul.projects.AirBnb.service.PricingUpdateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PricingUpdateServiceImpl implements PricingUpdateService {

    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final PricingService pricingService;

    /**
     * 🔁 Runs every hour
     * Updates:
     * 1️⃣ Inventory dynamic prices
     * 2️⃣ Hotel minimum price per day
     */
    @Override
    @Scheduled(cron = "0/5 * * * * *")
    @Transactional
    public void updatePrice() {

        log.info("⏰ Pricing scheduler started");

        int page = 0;
        int batchSize = 100;

        while (true) {
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, batchSize));

            if (hotelPage.isEmpty()) {
                log.info("✅ Pricing scheduler completed. No more hotels to process.");
                break;
            }

            log.info("📦 Processing page {} with {} hotels", page, hotelPage.getNumberOfElements());

            hotelPage.getContent().forEach(this::updateHotelPrice);

            page++;
        }
    }

    /**
     * Update prices for a single hotel
     */
    @Transactional
    private void updateHotelPrice(Hotel hotel) {

        log.info("🏨 Updating prices for hotelId={}", hotel.getId());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);

        List<Inventory> inventoryList =
                inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);

        log.info("📊 Found {} inventory records for hotelId={}",
                inventoryList.size(), hotel.getId());

        updateInventoryPrice(inventoryList);

        updateHotelMinPrice(hotel, inventoryList, startDate, endDate);

        log.info("✅ Completed price update for hotelId={}", hotel.getId());
    }

    /**
     * Calculate & store minimum hotel price per date
     */
    @Transactional
    private void updateHotelMinPrice(
            Hotel hotel,
            List<Inventory> inventoryList,
            LocalDate startDate,
            LocalDate endDate
    ) {

        log.debug("🔍 Calculating min prices for hotelId={}", hotel.getId());

        Map<LocalDate, BigDecimal> dailyMinPrices =
                inventoryList.stream()
                        .collect(Collectors.groupingBy(
                                Inventory::getDate,
                                Collectors.mapping(
                                        Inventory::getPrice,
                                        Collectors.minBy(BigDecimal::compareTo)
                                )
                        ))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().orElse(BigDecimal.ZERO)
                        ));

        List<HotelMinPrice> hotelMinPrices = new ArrayList<>();

        for (Map.Entry<LocalDate, BigDecimal> entry : dailyMinPrices.entrySet()) {

            LocalDate date = entry.getKey();
            BigDecimal minPrice = entry.getValue();

            HotelMinPrice hotelMinPrice =
                    hotelMinPriceRepository
                            .findByHotelAndDate(hotel, date)
                            .orElseGet(() -> {
                                log.debug("➕ Creating new HotelMinPrice for hotelId={}, date={}",
                                        hotel.getId(), date);
                                HotelMinPrice hmp = new HotelMinPrice();
                                hmp.setHotel(hotel);
                                hmp.setDate(date);
                                return hmp;
                            });

            hotelMinPrice.setPrice(minPrice);
            hotelMinPrices.add(hotelMinPrice);
        }

        hotelMinPriceRepository.saveAll(hotelMinPrices);

        log.info("💾 Saved {} HotelMinPrice records for hotelId={}",
                hotelMinPrices.size(), hotel.getId());
    }

    /**
     * Update dynamic price for inventory
     */
    @Transactional

    private void updateInventoryPrice(List<Inventory> inventoryList) {

        log.debug("⚙️ Updating dynamic prices for {} inventory records", inventoryList.size());

        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });

        inventoryRepository.saveAll(inventoryList);

        log.debug("💾 Inventory prices updated successfully");
    }
}
