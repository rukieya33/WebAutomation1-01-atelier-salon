package com.example.booking.controller;

import com.example.booking.dto.BookingResponse;
import com.example.booking.dto.CreateBookingRequest;
import com.example.booking.dto.ServiceResponse;
import com.example.booking.dto.StaffResponse;
import com.example.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) { this.bookingService = bookingService; }

    @GetMapping("/services")
    public List<ServiceResponse> services(@RequestParam(required = false) String q,
                                          @RequestParam(required = false) String category,
                                          @RequestParam(required = false) Long staffId) {
        return bookingService.searchServices(q, category, staffId);
    }

    @GetMapping("/categories")
    public List<String> categories() { return bookingService.categories(); }

    @GetMapping("/staff")
    public List<StaffResponse> staff() { return bookingService.staffMembers(); }

    @GetMapping("/availability")
    public List<LocalTime> availability(@RequestParam Long serviceId,
                                        @RequestParam(required = false) Long staffId,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return bookingService.availableTimes(serviceId, staffId, date);
    }

    @PostMapping("/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody CreateBookingRequest request) {
        return bookingService.createBooking(request);
    }
}
