/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.config;

import com.anshul.projects.AirBnb.entity.*;
import com.anshul.projects.AirBnb.entity.Enums.Role;
import com.anshul.projects.AirBnb.repository.HotelRepository;
import com.anshul.projects.AirBnb.repository.InventoryRepository;
import com.anshul.projects.AirBnb.repository.RoomRepository;
import com.anshul.projects.AirBnb.repository.UserRepository;
import com.anshul.projects.AirBnb.service.PricingUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DemoDataSeeder implements CommandLineRunner {

    public static final String MANAGER_EMAIL = "manager@demo.com";
    public static final String GUEST_EMAIL = "guest@demo.com";
    public static final String DEMO_PASSWORD = "Demo@123";

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final PricingUpdateService pricingUpdateService;

    @Value("${app.demo.seed:false}")
    private boolean seedDemoData;

    @Override
    public void run(String... args) {
        if (!seedDemoData) {
            return;
        }

        if (userRepository.findByEmail(MANAGER_EMAIL).isPresent()) {
            log.info("Demo data already present — skipping seed");
            return;
        }

        log.info("Seeding demo hotels, rooms, and users for portfolio demo");

        User manager = createUser(MANAGER_EMAIL, "Demo Manager", Set.of(Role.HOTEL_MANAGER, Role.GUEST));
        createUser(GUEST_EMAIL, "Demo Guest", Set.of(Role.GUEST));

        Hotel tajView = createHotel(
                manager,
                "Taj View Residency",
                "Delhi",
                new String[]{"https://images.unsplash.com/photo-1566073771259-6a8506099945"},
                new String[]{"wifi", "pool", "breakfast"},
                "Connaught Place, New Delhi",
                "+91-11-4000-1111",
                "stay@tajview.demo",
                "28.6315,77.2167"
        );

        Hotel goaRetreat = createHotel(
                manager,
                "Goa Beach Retreat",
                "Goa",
                new String[]{"https://images.unsplash.com/photo-1520250497591-112f2f40a3f4"},
                new String[]{"wifi", "beach-access", "spa"},
                "Calangute Beach Road, Goa",
                "+91-832-255-7788",
                "hello@goaretreat.demo",
                "15.5439,73.7553"
        );

        Hotel bangaloreHub = createHotel(
                manager,
                "Bangalore Tech Hub Inn",
                "Bangalore",
                new String[]{"https://images.unsplash.com/photo-1551882547-ff40c63fe5fa"},
                new String[]{"wifi", "workspace", "gym"},
                "Koramangala 5th Block, Bangalore",
                "+91-80-4000-2222",
                "book@techhub.demo",
                "12.9352,77.6245"
        );

        seedRoom(tajView, "Deluxe King", new BigDecimal("4500"), 8, 2);
        seedRoom(tajView, "Standard Twin", new BigDecimal("3200"), 12, 2);
        seedRoom(goaRetreat, "Sea View Suite", new BigDecimal("6200"), 6, 3);
        seedRoom(goaRetreat, "Garden Room", new BigDecimal("4100"), 10, 2);
        seedRoom(bangaloreHub, "Executive Room", new BigDecimal("3800"), 15, 2);
        seedRoom(bangaloreHub, "Pod Bunk", new BigDecimal("1800"), 20, 1);

        pricingUpdateService.updatePrice();

        log.info("Demo seed complete — manager: {} / guest: {} (password: {})",
                MANAGER_EMAIL, GUEST_EMAIL, DEMO_PASSWORD);
    }

    private User createUser(String email, String name, Set<Role> roles) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(DEMO_PASSWORD));
        user.setRoles(roles);
        return userRepository.save(user);
    }

    private Hotel createHotel(
            User owner,
            String name,
            String city,
            String[] photos,
            String[] amenities,
            String address,
            String phone,
            String email,
            String location
    ) {
        HotelContactInfo contact = new HotelContactInfo();
        contact.setAddress(address);
        contact.setPhoneNumber(phone);
        contact.setEmail(email);
        contact.setLocation(location);

        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setCity(city);
        hotel.setPhotos(photos);
        hotel.setAmenties(amenities);
        hotel.setContactInfo(contact);
        hotel.setActive(true);
        hotel.setOwner(owner);
        return hotelRepository.save(hotel);
    }

    private void seedRoom(Hotel hotel, String type, BigDecimal basePrice, int totalCount, int capacity) {
        Room room = new Room();
        room.setHotel(hotel);
        room.setType(type);
        room.setBasePrice(basePrice);
        room.setPhotos(new String[]{"https://images.unsplash.com/photo-1631049307264-da0ec9d70304"});
        room.setAmenties(new String[]{"ac", "tv", "minibar"});
        room.setTotalCount(totalCount);
        room.setCapacity(capacity);
        room = roomRepository.save(room);
        seedInventory(room, 60);
    }

    private void seedInventory(Room room, int days) {
        LocalDate date = LocalDate.now();
        List<Inventory> batch = new ArrayList<>(days);
        for (int i = 0; i < days; i++) {
            batch.add(Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .date(date.plusDays(i))
                    .city(room.getHotel().getCity())
                    .bookedCount(0)
                    .reservedCount(0)
                    .totalCount(room.getTotalCount())
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .closed(false)
                    .build());
        }
        inventoryRepository.saveAll(batch);
    }
}
