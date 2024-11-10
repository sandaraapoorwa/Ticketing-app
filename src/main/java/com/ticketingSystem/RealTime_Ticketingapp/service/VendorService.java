package com.ticketingSystem.RealTime_Ticketingapp.service;
import com.ticketingSystem.RealTime_Ticketingapp.entity.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    private final TicketPool ticketPool;
    private int ticketCounter = 1;
    @Autowired
    public VendorService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }
    public void produceTicket() {
        try {
            String ticket = "Ticket-" + ticketCounter++;
            ticketPool.addTicket(ticket);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
