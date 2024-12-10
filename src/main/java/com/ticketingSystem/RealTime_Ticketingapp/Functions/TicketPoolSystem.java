package com.ticketingSystem.RealTime_Ticketingapp.Functions;
import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
import java.util.Queue;

public class TicketPoolSystem {
    private final Queue<String> tickets = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final int maxTicketCapacity;
    final int totalTickets;  // Total tickets to be produced
    private int ticketsProduced = 0;  // Counter for the tickets produced
    private boolean running = true;

    public TicketPoolSystem(int maxTicketCapacity, int totalTickets) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
    }

    // Add a ticket to the pool if under the total ticket limit
    public boolean addTicket(String ticket) {
        lock.lock();
        try {
            if (ticketsProduced < totalTickets && tickets.size() < maxTicketCapacity) {
                tickets.offer(ticket);
                ticketsProduced++;
                System.out.println("Ticket added: " + ticket + ", Total tickets: " + ticketsProduced);
                return true;
            } else if (ticketsProduced >= totalTickets) {
                System.out.println("Total ticket limit reached. No more tickets can be produced.");
                return false;
            } else {
                System.out.println("Cannot add ticket: Ticket pool is full.");
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

    public int getTicketsProduced() {
        return ticketsProduced;
    }
}
