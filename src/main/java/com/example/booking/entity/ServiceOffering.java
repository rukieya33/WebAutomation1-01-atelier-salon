package com.example.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "service_offerings")
public class ServiceOffering {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private int duration;
    private BigDecimal price;
    private String description;
    private String icon;
    private String tone;
    private boolean active = true;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "default_staff_id", nullable = false)
    private StaffMember defaultStaff;

    protected ServiceOffering() {}
    public ServiceOffering(String name, String category, int duration, BigDecimal price, String description, String icon, String tone, StaffMember defaultStaff) {
        this.name=name; this.category=category; this.duration=duration; this.price=price; this.description=description; this.icon=icon; this.tone=tone; this.defaultStaff=defaultStaff; this.active=true;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getDuration() { return duration; }
    public BigDecimal getPrice() { return price; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
    public String getTone() { return tone; }
    public boolean isActive() { return active; }
    public StaffMember getDefaultStaff() { return defaultStaff; }
}
