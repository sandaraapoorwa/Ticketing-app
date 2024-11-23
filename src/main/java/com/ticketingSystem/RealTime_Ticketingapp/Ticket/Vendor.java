package com.ticketingSystem.RealTime_Ticketingapp.Ticket;

public class Vendor implements Runnable {
    private final int vendorID; // Unique ID for the vendor
    private final TicketSystem ticketSystem; // Shared ticket system
    private final int ticketReleaseRate; // Rate at which tickets are released (in milliseconds)
    private int ticketCounter = 0; // Tracks tickets added by this vendor

    public Vendor(TicketSystem ticketSystem, int vendorID, int ticketReleaseRate, int ticketCounter) {
        this.ticketSystem = ticketSystem;
        this.vendorID = vendorID;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketCounter=ticketCounter;
    }

    @Override
    public void run() {
        while (ticketSystem.isRunning()) {
            try {
                String ticketId = "Vendor" + vendorID + "_Ticket" + (++ticketCounter); // Unique ticket ID
                boolean added = ticketSystem.addTicket(ticketId); // Add ticket to the pool

                if (added) {
                    System.out.println("Vendor " + vendorID + " added ticket: " + ticketId);
                } else {
                    System.out.println("Vendor " + vendorID + ": Ticket pool is full. Stopping ticket release.");
                }

                Thread.sleep(ticketReleaseRate); // Wait before releasing the next ticket
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorID + " interrupted.");
                break;
            }
        }
        System.out.println("Vendor " + vendorID + " has stopped.");
    }
}
