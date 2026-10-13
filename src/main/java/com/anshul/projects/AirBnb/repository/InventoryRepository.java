/*
 * Copyright (c) 2026 Anshul Negi
 * GitHub: https://github.com/NegiCoder
 * Unauthorized copying, modification, or distribution of this file
 * without explicit permission is prohibited.
 */

package com.anshul.projects.AirBnb.repository;

import com.anshul.projects.AirBnb.entity.Hotel;
import com.anshul.projects.AirBnb.entity.Inventory;
import com.anshul.projects.AirBnb.entity.Room;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {


    void deleteByRoom(Room room);

    @Query("""
        SELECT i.hotel
        FROM Inventory i
        WHERE i.city = :city
          AND i.date BETWEEN :startDate AND :endDate
          AND i.closed = false
          AND (i.totalCount - i.bookedCount >= :roomCount)
        GROUP BY i.hotel, i.room
        HAVING COUNT(i.date) = :dateCount
    """)
    Page<Hotel> findHotelWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );


    @Query("""
          Select i
          FROM Inventory i
          WHERE i.room.id=:roomId
          AND i.date BETWEEN :startDate AND :endDate
          AND i.closed = false
          AND (i.totalCount - i.bookedCount >= :roomCount)
             
            
            
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory>findAndLockAvailableInventory(
            @Param("roomId")Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomCount
    );



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT i
        FROM Inventory i
        WHERE i.room.id = :roomId
          AND i.date BETWEEN :startDate AND :endDate
          AND (i.totalCount - i.bookedCount) >= :numberOfRooms
          AND i.closed = false
    """)
    List<Inventory> findAndLockReservedInventory(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("numberOfRooms") int numberOfRooms
    );

    @Modifying
    @Query("""
        UPDATE Inventory i
        SET  i.reservedCount  = i.reservedCount + :numberOfRooms
        WHERE i.room.id = :roomId
          AND i.date BETWEEN :startDate AND :endDate
          AND (i.totalCount - i.bookedCount - i.reservedCount) >= :numberOfRooms
             AND i.closed = false
    """)
    void initBooking(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("numberOfRooms") int numberOfRooms
    );

    @Modifying
    @Query("""
        UPDATE Inventory i
        SET i.reservedCount = i.reservedCount - :numberOfRooms,
            i.bookedCount  = i.bookedCount  + :numberOfRooms
        WHERE i.room.id = :roomId
          AND i.date BETWEEN :startDate AND :endDate
          AND (i.totalCount - i.bookedCount) >= :numberOfRooms
          AND i.reservedCount >= :numberOfRooms
          AND i.closed = false
    """)
    void confirmBooking(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("numberOfRooms") int numberOfRooms
    );

    @Modifying
    @Query("""
        UPDATE Inventory i
        SET  i.bookedCount  = i.bookedCount  - :numberOfRooms
        WHERE i.room.id = :roomId
          AND i.date BETWEEN :startDate AND :endDate
          AND (i.totalCount - i.bookedCount) >= :numberOfRooms
             AND i.closed = false
    """)
    void cancelBooking(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("numberOfRooms") int numberOfRooms
    );



    @Modifying
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
           Select  i
           From Inventory i
          WHERE i.room.id = :roomId
          AND i.date BETWEEN :startDate AND :endDate
    """)
    List<Inventory>  getInventoryAndLockBeforeUpdate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate

    );

    @Modifying
    @Query("""
        UPDATE Inventory i
        SET  i.surgeFactor  = :surgeFactor,
             i.closed = :closed
        WHERE i.room.id = :roomId
          AND i.date BETWEEN :startDate AND :endDate
    """)
    void updateInventory(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("closed") boolean closed,
            @Param("surgeFactor")BigDecimal surgeFactor
    );

    List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate startDate, LocalDate endDate);


    List<Inventory>findByRoomOrderByDate(Room room);
}
