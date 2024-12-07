package com.ticketingSystem.RealTime_Ticketingapp.Functions;
import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
import java.util.Queue;

public class TicketPoolSystem {
    private final Queue<String> tickets = new LinkedList<>(); // Ticket queue
    private final ReentrantLock lock = new ReentrantLock(); // Lock for thread-safety
    private final int maxTicketCapacity; // Maximum capacity of the ticket pool
    private boolean running = true; // Flag to indicate system status

    public TicketPoolSystem(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }
    // Add a ticket to the pool
    public boolean addTicket(String ticket) {
        lock.lock();
        try {
            if (tickets.size() < maxTicketCapacity) {
                tickets.offer(ticket);
                System.out.println("Ticket added: " + ticket + ", Total tickets: " + tickets.size());
                return true;
            } else {
                System.out.println("Cannot add ticket: Ticket pool is full (Capacity: " + maxTicketCapacity + ")");
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
    // Get the current ticket count
    public int getTicketCount() {
        lock.lock();
        try {
            return tickets.size();
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

    // Get the max ticket capacity
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
}
