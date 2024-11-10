package com.ticketingSystem.RealTime_Ticketingapp.service;

import com.ticketingSystem.RealTime_Ticketingapp.entity.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private final TicketPool ticketPool;

    @Autowired
    public TicketService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @Async("taskExecutor")
    public void purchaseTicket() {
        ticketPool.removeTicket();  // Customer purchases a ticket
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
