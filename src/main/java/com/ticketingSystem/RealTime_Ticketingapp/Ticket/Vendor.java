package com.ticketingSystem.RealTime_Ticketingapp.Ticket;

public class Vendor implements Runnable {
    private final TicketSystem ticketSystem;
    private final int ticketReleaseRate;
    private int ticketCounter = 0;

    public Vendor(TicketSystem ticketSystem, int ticketReleaseRate) {
        this.ticketSystem = ticketSystem;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        while (ticketSystem.isRunning()) {
            try {
                Thread.sleep(ticketReleaseRate * 1000L);
                String ticket = "Ticket-" + (++ticketCounter);
                ticketSystem.addTicket(ticket);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
