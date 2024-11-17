package com.ticketingSystem.RealTime_Ticketingapp.Cli;

import java.util.LinkedList;
import java.util.Queue;

public class TicketQueue {
    private final int maxCapacity;
    private final Queue<Long> queue;

    public TicketQueue(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.queue = new LinkedList<>();
    }

    public synchronized boolean addTicket(long ticketId) {
        if (queue.size() < maxCapacity) {
            queue.add(ticketId);
            notifyAll(); // Notify consumers that a ticket is available
            return true;
        }
        return false; // Cannot add ticket, capacity reached
    }

    public synchronized boolean removeTicket() {
        while (queue.isEmpty()) {
            try {
                wait(); // Wait for tickets to be added
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false; // Thread interrupted
            }
        }
        queue.poll(); // Remove a ticket from the queue
        notifyAll(); // Notify producers that space is available
        return true;
    }

    public synchronized int getAvailableTickets() {
        return queue.size();
    }
}
