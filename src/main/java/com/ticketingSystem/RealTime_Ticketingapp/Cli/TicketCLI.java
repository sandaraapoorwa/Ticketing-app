package com.ticketingSystem.RealTime_Ticketingapp.Cli;
import com.ticketingSystem.RealTime_Ticketingapp.service.TicketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class TicketCLI implements CommandLineRunner {
    private final TicketService ticketService;
    private volatile boolean running = true;
    private long customerId = 1; // Counter for unique customer IDs

    public TicketCLI(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'start' to begin, 'status' to view stats, or 'stop' to terminate.");

        while (running) {
            try {
                String command = InputValidator.validateCommand(scanner.nextLine(), List.of("start", "status", "stop"));
                if ("start".equals(command)) {
                    startOperations();
                } else if ("status".equals(command)) {
                    displayStatus();
                } else if ("stop".equals(command)) {
                    stopOperations();
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        scanner.close();
    }

    private void startOperations() {
        System.out.println("Ticket operations started!");
        new Thread(this::producer).start();
        new Thread(this::consumer).start();
    }

    private void stopOperations() {
        System.out.println("Stopping operations...");
        running = false;
    }

    private void producer() {
        long ticketId = 1;
        while (running) {
            synchronized (ticketService) {
                while (ticketService.isFull()) {
                    try {
                        ticketService.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println(ticketService.releaseTicket(ticketId++));
                ticketService.notifyAll();
            }
            try {
                Thread.sleep(200); // Simulates ticket release rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void consumer() {
        while (running) {
            synchronized (ticketService) {
                while (ticketService.isEmpty()) {
                    try {
                        ticketService.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println(purchaseTicketWithCustomerId());
                ticketService.notifyAll();
            }
            try {
                Thread.sleep(300); // Simulates ticket purchase rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private String purchaseTicketWithCustomerId() {
        String ticketResponse = ticketService.purchaseTicket();
        if (ticketResponse.startsWith("Ticket purchased!")) {
            long currentCustomerId = customerId++;
            int remainingTickets = ticketService.getAvailableTickets();
            return "Customer " + currentCustomerId + " successfully purchased a ticket.\n" +
                    "Ticket removed. Tickets remaining: " + remainingTickets;
        }
        return "No tickets available for purchase.";
    }

    private void displayStatus() {
        System.out.println("---- Ticketing System Status ----");
        System.out.println("Total Tickets Released: " + ticketService.getTotalTickets());
        System.out.println("Ticket Release Rate (tickets/sec): " + String.format("%.2f", ticketService.getTicketReleaseRate()));
        System.out.println("Customer Retrieval Rate (approx): " + String.format("%.2f", ticketService.getCustomerRetrievalRate()));
        System.out.println("Available Tickets: " + ticketService.getAvailableTickets());
        System.out.println("Max Ticket Capacity: " + ticketService.getMaxTicketCapacity());
        System.out.println("----------------------------------");
    }
}


