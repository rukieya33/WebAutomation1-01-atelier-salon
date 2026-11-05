package com.example.booking.config;

import com.example.booking.entity.ServiceOffering;
import com.example.booking.entity.StaffMember;
import com.example.booking.repository.ServiceOfferingRepository;
import com.example.booking.repository.StaffMemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DemoDataConfig {
    @Bean
    CommandLineRunner seedDemoData(StaffMemberRepository staffRepository,
                                   ServiceOfferingRepository serviceRepository) {
        return args -> {
            if (staffRepository.count() > 0 || serviceRepository.count() > 0) return;

            StaffMember maya = staffRepository.save(new StaffMember("Maya Chen", "Senior stylist"));
            StaffMember theo = staffRepository.save(new StaffMember("Theo James", "Style specialist"));
            StaffMember nina = staffRepository.save(new StaffMember("Nina Patel", "Massage therapist"));
            StaffMember amara = staffRepository.save(new StaffMember("Amara Lewis", "Skin therapist"));

            serviceRepository.saveAll(List.of(
                    new ServiceOffering("Signature Haircut", "Hair", 45, new BigDecimal("58.00"), "A considered cut, shaped around you.", "✂", "peach", maya),
                    new ServiceOffering("Haircut & Style", "Hair", 60, new BigDecimal("72.00"), "Cut, finish and a little fresh perspective.", "✦", "lavender", theo),
                    new ServiceOffering("Deep Tissue Massage", "Massage", 60, new BigDecimal("95.00"), "Focused pressure to release built-up tension.", "◉", "sage", nina),
                    new ServiceOffering("Calm & Restore Facial", "Skin", 50, new BigDecimal("82.00"), "A gentle reset for your skin and senses.", "✿", "rose", amara),
                    new ServiceOffering("Brow Shape & Tint", "Skin", 30, new BigDecimal("38.00"), "Soft definition, tailored to your features.", "⌁", "sand", maya),
                    new ServiceOffering("Express Blowout", "Hair", 35, new BigDecimal("44.00"), "A polished finish, ready for wherever next.", "〰", "blue", theo),
                    new ServiceOffering("Aromatherapy Massage", "Massage", 75, new BigDecimal("110.00"), "Slow, soothing bodywork with botanical oils.", "❋", "mint", nina),
                    new ServiceOffering("Glow Renewal Facial", "Skin", 65, new BigDecimal("105.00"), "A brightening treatment with a lasting glow.", "☼", "yellow", amara)
            ));
        };
    }
}
