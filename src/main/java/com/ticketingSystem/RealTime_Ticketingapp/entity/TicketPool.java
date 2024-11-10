package com.ticketingSystem.RealTime_Ticketingapp.entity;

import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class TicketPool {
    private final Queue<String> tickets = new LinkedList<>();
    private final int MAX_CAPACITY = 10;

    // Adds a ticket to the pool if capacity allows
    public synchronized void addTicket(String ticket) throws InterruptedException {
        while (tickets.size() == MAX_CAPACITY) {
            System.out.println("Ticket pool is full. Vendor is waiting...");
            wait();
        }
        tickets.add(ticket);
        System.out.println("Added ticket: "+ticket);
        notifyAll();
    }

    // Allows customers to purchase a ticket if one is available
    public synchronized String purchaseTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            System.out.println("No tickets available. Customer is waiting...");
            wait();
        }
        String ticket = tickets.poll();
        System.out.println("Purchased ticket: " + ticket);
        notifyAll();
        return ticket;
    }
}
