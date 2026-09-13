package com.eventpilot.backend.repository;

import com.eventpilot.backend.entity.Booking;
import com.eventpilot.backend.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByStatus(BookingStatus status);
}