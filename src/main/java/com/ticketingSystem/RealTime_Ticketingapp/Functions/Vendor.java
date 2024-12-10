package com.ticketingSystem.RealTime_Ticketingapp.Functions;

public class Vendor implements Runnable {
    private final int vendorID; // Unique ID for the vendor
    private final TicketPoolSystem ticketPoolSystem; // Shared ticket system
    private final int ticketReleaseRate; // Rate at which tickets are released (in milliseconds)
    private int ticketCounter = 0;  // Ticket counter for the vendor

    public Vendor(TicketPoolSystem ticketPoolSystem, int vendorID, int ticketReleaseRate, int i) {
        this.ticketPoolSystem = ticketPoolSystem;
        this.vendorID = vendorID;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        while (ticketPoolSystem.isRunning()) {
            // Check if the total tickets produced has reached the limit
            if (ticketPoolSystem.getTicketsProduced() >= ticketPoolSystem.totalTickets) {
                System.out.println("Vendor " + vendorID + ": Total ticket limit reached. Stopping ticket release.");
                break;  // Exit the loop once the total ticket limit is reached
            }

            try {
                String ticketId = "Vendor" + vendorID + "_Ticket" + (++ticketCounter); // Unique ticket ID
                boolean added = ticketPoolSystem.addTicket(ticketId); // Add ticket to the pool
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
