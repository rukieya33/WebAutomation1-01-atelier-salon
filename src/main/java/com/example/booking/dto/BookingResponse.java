package com.example.booking.dto;

import com.example.booking.entity.AppointmentBooking;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public record BookingResponse(
        Long id, String service, Long serviceId, String staff, Long staffId,
        LocalDate date, LocalTime time, int duration, BigDecimal price,
        String clientName, String clientEmail, Instant createdAt) {
    public static BookingResponse from(AppointmentBooking booking) {
        return new BookingResponse(booking.getId(), booking.getService().getName(), booking.getService().getId(),
                booking.getStaff().getFullName(), booking.getStaff().getId(), booking.getAppointmentDate(),
                booking.getAppointmentTime(), booking.getService().getDuration(), booking.getService().getPrice(),
                booking.getClientName(), booking.getClientEmail(), booking.getCreatedAt());
    }
}
