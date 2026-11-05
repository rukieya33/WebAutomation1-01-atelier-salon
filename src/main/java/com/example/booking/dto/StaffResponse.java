package com.example.booking.dto;

import com.example.booking.entity.StaffMember;

public record StaffResponse(Long id, String name, String title) {
    public static StaffResponse from(StaffMember staff) {
        return new StaffResponse(staff.getId(), staff.getFullName(), staff.getTitle());
    }
}
