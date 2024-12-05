package com.ticketingSystem.RealTime_Ticketingapp.Function;

public class Customer implements Runnable {
    private final int customerID;
    private final TicketSystem ticketSystem;
    private final int customerRetrievalRate;
    public Customer(int customerID, TicketSystem ticketSystem, int customerRetrievalRate) {
        this.customerID = customerID;
        this.ticketSystem = ticketSystem;
        this.customerRetrievalRate = customerRetrievalRate;
    }
    @Override
    public void run() {
        while (ticketSystem.isRunning()) {
            try {
                // Wait for the specified retrieval rate (in milliseconds)
                Thread.sleep(customerRetrievalRate);
                // Attempt to purchase a ticket
                String ticket = ticketSystem.purchaseTicket(); // Will return ticket or null
                if (ticket != null) {
                    System.out.println("Customer " + customerID + " successfully purchased a ticket: " + ticket);
                } else {
                    System.out.println("Customer " + customerID + " could not purchase a ticket. Ticket pool might be empty.");
                }
            } catch (InterruptedException e) {
                // Handle thread interruption gracefully
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
