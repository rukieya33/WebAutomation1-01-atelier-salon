package com.example.booking.repository;

import com.example.booking.entity.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffMemberRepository extends JpaRepository<StaffMember, Long> {}
