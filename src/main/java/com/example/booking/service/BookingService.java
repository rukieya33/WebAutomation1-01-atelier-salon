package com.example.booking.service;

import com.example.booking.dto.BookingResponse;
import com.example.booking.dto.CreateBookingRequest;
import com.example.booking.dto.ServiceResponse;
import com.example.booking.dto.StaffResponse;
import com.example.booking.entity.AppointmentBooking;
import com.example.booking.entity.ServiceOffering;
import com.example.booking.entity.StaffMember;
import com.example.booking.exception.BookingConflictException;
import com.example.booking.repository.AppointmentBookingRepository;
import com.example.booking.repository.ServiceOfferingRepository;
import com.example.booking.repository.StaffMemberRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private static final List<LocalTime> DAILY_SLOTS = List.of(
            LocalTime.of(9, 0), LocalTime.of(10, 30), LocalTime.of(12, 0),
            LocalTime.of(13, 30), LocalTime.of(15, 0), LocalTime.of(16, 30));

    private final ServiceOfferingRepository services;
    private final StaffMemberRepository staff;
    private final AppointmentBookingRepository bookings;

    public BookingService(ServiceOfferingRepository services, StaffMemberRepository staff,
                          AppointmentBookingRepository bookings) {
        this.services = services;
        this.staff = staff;
        this.bookings = bookings;
    }

    @Transactional(readOnly = true)
    public List<ServiceResponse> searchServices(String query, String category, Long staffId) {
        String normalizedQuery = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        return services.findByActiveTrueOrderByCategoryAscNameAsc().stream()
                .filter(s -> category == null || category.isBlank() || category.equalsIgnoreCase(s.getCategory()))
                .filter(s -> staffId == null || staffId.equals(s.getDefaultStaff().getId()))
                .filter(s -> normalizedQuery.isEmpty() || (s.getName() + " " + s.getCategory() + " " +
                        s.getDefaultStaff().getFullName() + " " + s.getDescription())
                        .toLowerCase(Locale.ROOT).contains(normalizedQuery))
                .map(ServiceResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> categories() {
        return services.findByActiveTrueOrderByCategoryAscNameAsc().stream()
                .map(ServiceOffering::getCategory).distinct().sorted().toList();
    }

    @Transactional(readOnly = true)
    public List<StaffResponse> staffMembers() {
        return staff.findAll().stream().sorted((a, b) -> a.getFullName().compareToIgnoreCase(b.getFullName()))
                .map(StaffResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<LocalTime> availableTimes(Long serviceId, Long staffId, LocalDate date) {
        ServiceOffering service = services.findById(serviceId)
                .filter(ServiceOffering::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + serviceId));
        Long chosenStaffId = staffId == null ? service.getDefaultStaff().getId() : staffId;
        if (!staff.existsById(chosenStaffId)) throw new IllegalArgumentException("Staff member not found: " + chosenStaffId);
        Set<LocalTime> booked = bookings.findByStaffIdAndAppointmentDate(chosenStaffId, date).stream()
                .map(AppointmentBooking::getAppointmentTime).collect(Collectors.toSet());
        return DAILY_SLOTS.stream()
                .filter(time -> !booked.contains(time))
                .filter(time -> !date.equals(LocalDate.now()) || time.isAfter(LocalTime.now()))
                .toList();
    }

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        ServiceOffering service = services.findById(request.serviceId())
                .filter(ServiceOffering::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + request.serviceId()));
        StaffMember chosenStaff = staff.findById(request.staffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found: " + request.staffId()));
        if (!DAILY_SLOTS.contains(request.time())) {
            throw new IllegalArgumentException("Choose one of the available appointment times.");
        }
        if (request.date().isBefore(LocalDate.now()) ||
                (request.date().equals(LocalDate.now()) && !request.time().isAfter(LocalTime.now()))) {
            throw new IllegalArgumentException("Appointment time must be in the future.");
        }
        if (bookings.existsByStaffIdAndAppointmentDateAndAppointmentTime(chosenStaff.getId(), request.date(), request.time())) {
            throw new BookingConflictException("That specialist is already booked at this time. Please choose another time.");
        }
        try {
            AppointmentBooking saved = bookings.saveAndFlush(new AppointmentBooking(service, chosenStaff, request.date(),
                    request.time(), request.clientName().trim(), request.clientEmail().trim().toLowerCase(Locale.ROOT)));
            return BookingResponse.from(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new BookingConflictException("That specialist is already booked at this time. Please choose another time.");
        }
    }
}

