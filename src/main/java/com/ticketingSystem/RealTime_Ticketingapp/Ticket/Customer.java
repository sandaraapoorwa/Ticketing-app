package com.ticketingSystem.RealTime_Ticketingapp.Ticket;

public class Customer implements Runnable {
    private final TicketSystem ticketSystem;
    private final int customerRetrievalRate;

    public Customer(TicketSystem ticketSystem, int customerRetrievalRate) {
        this.ticketSystem = ticketSystem;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        while (ticketSystem.isRunning()) {
            try {
                Thread.sleep(customerRetrievalRate * 1000L);
                ticketSystem.purchaseTicket();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
