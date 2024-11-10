package com.ticketingSystem.RealTime_Ticketingapp.entity;

import org.springframework.stereotype.Component;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TicketPool {

    private final BlockingQueue<String> tickets = new LinkedBlockingQueue<>(10000); // Set a large capacity to handle multiple vendors

    // Method to add a batch of tickets from a vendor
    public void addTicketsByVendor(String vendorId, int ticketCount) throws InterruptedException {
        // Add multiple tickets as a batch (e.g., 1000 tickets for each vendor)
        for (int i = 1; i <= ticketCount; i++) {
            String ticket = "Vendor_" + vendorId + "_Ticket_" + i;
            tickets.put(ticket);
        }
        System.out.println("Vendor " + vendorId + " added " + ticketCount + " tickets to the pool.");
    }

    public String purchaseTicket() throws InterruptedException {
        String ticket = tickets.take();
        System.out.println("Purchased ticket: " + ticket);
        return ticket;
    }

    public int getAvailableTickets() {
        return tickets.size();
    }

    public void removeTicket() {
        tickets.poll(); // Removes a ticket if available
    }
}
