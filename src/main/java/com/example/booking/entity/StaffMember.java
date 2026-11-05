package com.example.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff_members")
public class StaffMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String title;

    protected StaffMember() {}
    public StaffMember(String fullName, String title) { this.fullName = fullName; this.title = title; }
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getTitle() { return title; }
}
