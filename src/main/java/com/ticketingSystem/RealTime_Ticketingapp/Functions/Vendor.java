package com.ticketingSystem.RealTime_Ticketingapp.Functions;

public class Vendor implements Runnable {
    private final int vendorID; // Unique ID for the vendor
    private final TicketPoolSystem ticketPoolSystem; // Shared ticket system
    private final int ticketReleaseRate; // Rate at which tickets are released (in milliseconds)
    private int ticketCounter = 0; // Counter to generate unique ticket IDs

    // Constructor to initialize vendor with the required information
    public Vendor(TicketPoolSystem ticketPoolSystem, int vendorID, int ticketReleaseRate, int i) {
        this.ticketPoolSystem = ticketPoolSystem;
        this.vendorID = vendorID;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        // Continue running as long as the system is not stopped
        while (ticketPoolSystem.isRunning()) {
            try {
                // Generate a unique ticket ID for this vendor
                String ticketId = "Vendor" + vendorID + "_Ticket" + (++ticketCounter);

                // Try to add the ticket to the pool
                boolean added = ticketPoolSystem.addTicket(ticketId);

                // Output the result of the add attempt
                if (added) {
                    System.out.println("Vendor " + vendorID + " added ticket: " + ticketId);
                } else {
                    System.out.println("Vendor " + vendorID + ": Ticket pool is full. Stopping ticket release.");
                    break; // Stop adding tickets if the pool is full
                }

                // Wait before releasing the next ticket based on the release rate
                Thread.sleep(ticketReleaseRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle interruption
                System.out.println("Vendor " + vendorID + " interrupted.");
                break; // Stop if interrupted
            }
        }
        System.out.println("Vendor " + vendorID + " has stopped.");
    }
}
