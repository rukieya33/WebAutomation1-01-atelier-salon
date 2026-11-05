package com.example.booking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateBookingRequest(
        @NotNull @Positive Long serviceId,
        @NotNull @Positive Long staffId,
        @NotNull @FutureOrPresent LocalDate date,
        @NotNull LocalTime time,
        @NotBlank String clientName,
        @NotBlank @Email String clientEmail) {}
