package com.ticketingSystem.RealTime_Ticketingapp.service;
import com.ticketingSystem.RealTime_Ticketingapp.Cli.TicketQueue;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private int totalTickets;  // Track total tickets released
    private long startTime;    // Start time for ticket release rate
    private final TicketQueue ticketQueue;  // The queue holding tickets
    private final int maxTicketCapacity;  // Maximum ticket capacity

    public TicketService(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.ticketQueue = new TicketQueue(maxTicketCapacity);
        this.totalTickets = 0;
        this.startTime = System.currentTimeMillis();
    }

    public synchronized boolean isFull() {
        return ticketQueue.getAvailableTickets() >= maxTicketCapacity;
    }

    public synchronized boolean isEmpty() {
        return ticketQueue.getAvailableTickets() == 0;
    }

    public synchronized String releaseTicket(long ticketId) {
        if (ticketQueue.addTicket(ticketId)) {
            totalTickets++;
            return "Ticket " + ticketId + " released!";
        }
        return "Failed to release ticket. Max capacity reached.";
    }

    public synchronized String purchaseTicket() {
        if (ticketQueue.removeTicket()) {
            return "Ticket purchased!";
        }
        return "No tickets available to purchase.";
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public double getTicketReleaseRate() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return elapsed > 0 ? (double) totalTickets / elapsed : 0.0;
    }

    public double getCustomerRetrievalRate() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        return elapsed > 0 ? (double) (totalTickets - getAvailableTickets()) / elapsed : 0.0;
    }

    public int getAvailableTickets() {
        return ticketQueue.getAvailableTickets();
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
}

