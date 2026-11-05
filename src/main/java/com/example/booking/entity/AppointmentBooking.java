package com.example.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointment_bookings", uniqueConstraints = @UniqueConstraint(
        name = "uk_booking_staff_date_time", columnNames = {"staff_id", "appointment_date", "appointment_time"}))
public class AppointmentBooking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "service_id", nullable = false)
    private ServiceOffering service;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "staff_id", nullable = false)
    private StaffMember staff;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String clientName;
    private String clientEmail;
    private Instant createdAt = Instant.now();

    protected AppointmentBooking() {}
    public AppointmentBooking(ServiceOffering service, StaffMember staff, LocalDate appointmentDate, LocalTime appointmentTime, String clientName, String clientEmail) {
        this.service=service; this.staff=staff; this.appointmentDate=appointmentDate; this.appointmentTime=appointmentTime; this.clientName=clientName; this.clientEmail=clientEmail;
    }
    public Long getId() { return id; }
    public ServiceOffering getService() { return service; }
    public StaffMember getStaff() { return staff; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public String getClientName() { return clientName; }
    public String getClientEmail() { return clientEmail; }
    public Instant getCreatedAt() { return createdAt; }
}
