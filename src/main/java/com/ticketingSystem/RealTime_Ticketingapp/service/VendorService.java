package com.ticketingSystem.RealTime_Ticketingapp.service;

import com.ticketingSystem.RealTime_Ticketingapp.entity.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    private final TicketPool ticketPool;

    @Autowired
    public VendorService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    // Method for a vendor to add a bulk number of tickets
    public void produceTickets(String vendorId, int ticketCount) {
        try {
            // Adding multiple tickets to the pool by specifying vendorId and ticket count
            ticketPool.addTicketsByVendor(vendorId, ticketCount);
            System.out.println("Vendor " + vendorId + " successfully added " + ticketCount + " tickets.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to add tickets for vendor " + vendorId);
        }
    }
}
