package com.example.booking.dto;

import com.example.booking.entity.ServiceOffering;

import java.math.BigDecimal;

public record ServiceResponse(
        Long id, String name, String category, Long staffId, String staff,
        int duration, BigDecimal price, String description, String icon, String tone) {
    public static ServiceResponse from(ServiceOffering service) {
        return new ServiceResponse(service.getId(), service.getName(), service.getCategory(),
                service.getDefaultStaff().getId(), service.getDefaultStaff().getFullName(),
                service.getDuration(), service.getPrice(), service.getDescription(),
                service.getIcon(), service.getTone());
    }
}
