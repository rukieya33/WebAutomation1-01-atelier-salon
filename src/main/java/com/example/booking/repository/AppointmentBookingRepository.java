package com.example.booking.repository;

import com.example.booking.entity.AppointmentBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentBookingRepository extends JpaRepository<AppointmentBooking, Long> {
    boolean existsByStaffIdAndAppointmentDateAndAppointmentTime(Long staffId, LocalDate date, LocalTime time);
    List<AppointmentBooking> findByStaffIdAndAppointmentDate(Long staffId, LocalDate date);
}
