package com.CSO2.supportservice.config;

import com.CSO2.supportservice.entity.SupportTicket;
import com.CSO2.supportservice.entity.WarrantyRegistration;
import com.CSO2.supportservice.repository.jpa.WarrantyRepository;
import com.CSO2.supportservice.repository.mongo.TicketRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final WarrantyRepository warrantyRepository;
    private final TicketRepository ticketRepository;

    @Override
    public void run(String... args) throws Exception {
        if (warrantyRepository.count() == 0) {
            WarrantyRegistration w1 = new WarrantyRegistration();
            w1.setOrderId("order-123");
            w1.setProductId("product-456");
            w1.setSerialNumber("SN-789");
            w1.setPurchaseDate(LocalDate.now().minusMonths(1));
            w1.setWarrantyEndDate(LocalDate.now().plusMonths(11));
            w1.setStatus(WarrantyRegistration.WarrantyStatus.ACTIVE);

            warrantyRepository.save(w1);
            System.out.println("Warranty data seeded!");
        }

        if (ticketRepository.count() == 0) {
            SupportTicket t1 = new SupportTicket();
            t1.setUserId("user-1");
            t1.setSubject("Issue with laptop");
            t1.setDescription("Screen flickering");
            t1.setStatus(SupportTicket.TicketStatus.OPEN);
            t1.setPriority(SupportTicket.TicketPriority.HIGH);
            t1.setCreatedAt(LocalDateTime.now());

            ticketRepository.save(t1);
            System.out.println("Ticket data seeded!");
        }
    }
}
