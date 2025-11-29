package com.CSO2.supportservice.repository;

import com.CSO2.supportservice.entity.SupportTicket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends MongoRepository<SupportTicket, String> {
    List<SupportTicket> findByUserId(String userId);
}
