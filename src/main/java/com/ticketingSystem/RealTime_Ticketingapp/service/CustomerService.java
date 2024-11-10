package com.ticketingSystem.RealTime_Ticketingapp.service;

import com.ticketingSystem.RealTime_Ticketingapp.entity.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final TicketPool ticketPool;

    @Autowired
    public CustomerService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public String consumeTicket() {
        try {
            return ticketPool.purchaseTicket();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Failed to purchase ticket.";
        }
    }
}
