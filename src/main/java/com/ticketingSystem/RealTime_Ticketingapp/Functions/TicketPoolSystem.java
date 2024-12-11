package com.ticketingSystem.RealTime_Ticketingapp.Functions;

import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
import java.util.Queue;

public class TicketPoolSystem {
    private final Queue<String> tickets = new LinkedList<>(); // Ticket queue
    private final ReentrantLock lock = new ReentrantLock(); // Lock for thread-safety
    private final int maxTicketCapacity; // Maximum capacity of the ticket pool
    private final int totalTickets; // Total number of tickets
    private int ticketsAdded = 0; // Track total number of tickets added
    private boolean running = true; // Flag to indicate system status

    public TicketPoolSystem(int maxTicketCapacity, int totalTickets) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
    }

    // Add a ticket to the pool
    public boolean addTicket(String ticket) {
        lock.lock();
        try {
            // Check if the total ticket count is reached
            if (ticketsAdded >= totalTickets) {
                System.out.println("All tickets have been added. No more tickets can be added.");
                return false;
            }
            // Check if the ticket pool is full
            if (tickets.size() < maxTicketCapacity) {
                tickets.offer(ticket);
                ticketsAdded++;
                System.out.println("Ticket added: " + ticket + ", Total tickets added: " + ticketsAdded + ", Tickets in pool: " + tickets.size());
                return true;
            } else {
                System.out.println("Ticket pool is full. Cannot add more tickets.");
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    // Purchase a ticket
    public String purchaseTicket() {
        lock.lock();
        try {
            if (!tickets.isEmpty()) {
                String ticket = tickets.poll();
                System.out.println("Ticket purchased: " + ticket + ", Tickets remaining: " + tickets.size());
                return ticket;
            } else {
                System.out.println("No tickets available for purchase.");
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    // Stop the ticket system
    public void stop() {
        lock.lock();
        try {
            running = false;
            System.out.println("Ticket system stopped.");
        } finally {
            lock.unlock();
        }
    }

    // Check if the system is running
    public boolean isRunning() {
        lock.lock();
        try {
            return running;
        } finally {
            lock.unlock();
        }
    }

    // Get the ticket count
    public int getTicketCount() {
        lock.lock();
        try {
            return tickets.size();
        } finally {
            lock.unlock();
        }
    }
}


