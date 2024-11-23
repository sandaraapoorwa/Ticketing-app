package com.ticketingSystem.RealTime_Ticketingapp.Ticket;
import java.util.LinkedList;
import java.util.Queue;
public class TicketSystem {
    private final Queue<String> tickets = new LinkedList<String>();
    private final int maxTicketCapacity;
    private boolean running = true;

    public TicketSystem(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public synchronized boolean addTicket(String ticket) {
        if (tickets.size() < maxTicketCapacity) {
            tickets.offer(ticket);
            System.out.println("Ticket added: " + ticket);
            notifyAll(); // Notify waiting consumers
            return true;
        }
        System.out.println("Ticket pool is full! Ticket not added.");
        return false;
    }

    public synchronized String purchaseTicket() {
        while (tickets.isEmpty() && running) {
            try {
                wait(); // Wait until a ticket is added
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (!tickets.isEmpty()) {
            String ticket = tickets.poll();
            System.out.println("Ticket purchased: " + ticket);
            return ticket;
        }
        return null;
    }

    public synchronized int getTicketCount() {
        return tickets.size();
    }

    public synchronized void stop() {
        running = false;
        notifyAll(); // Wake up all threads
    }
    public boolean isRunning() {
        return running;
    }
}
