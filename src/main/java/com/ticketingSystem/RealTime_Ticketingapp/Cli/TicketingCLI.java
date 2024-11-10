package com.ticketingSystem.RealTime_Ticketingapp.Cli;

import com.ticketingSystem.RealTime_Ticketingapp.entity.Customer;
import com.ticketingSystem.RealTime_Ticketingapp.entity.TicketPool;
import com.ticketingSystem.RealTime_Ticketingapp.service.TicketService;
import com.ticketingSystem.RealTime_Ticketingapp.service.CustomerService;
import com.ticketingSystem.RealTime_Ticketingapp.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class TicketingCLI implements CommandLineRunner {

    private final CustomerService customerService;
    private final TicketService ticketService;
    private final VendorService vendorService;
    private final TicketPool ticketPool;

    @Autowired
    public TicketingCLI(CustomerService customerService, TicketService ticketService, VendorService vendorService, TicketPool ticketPool) {
        this.customerService = customerService;
        this.ticketService = ticketService;
        this.vendorService = vendorService;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Ticketing CLI!");

        boolean running = true;

        while (running) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Customer: Register a Customer and Purchase Ticket");
            System.out.println("2. Vendor: Add Tickets to Pool");
            System.out.println("3. Check Ticket Availability");
            System.out.println("4. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerAndPurchaseTicket(scanner);
                    break;
                case "2":
                    addTickets(scanner);
                    break;
                case "3":
                    checkTicketAvailability();
                    break;
                case "4":
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private void registerAndPurchaseTicket(Scanner scanner) {
        System.out.println("Enter customer details.");

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        // Register customer
        Customer customer = new Customer();
        customer.setName(name);
        customer.setAge(age);
        customer.setPhoneNumber(phoneNumber);
        customer.setEmail(email);
        Customer savedCustomer = customerService.saveCustomer(customer);

        // Attempt to purchase a ticket
        String purchaseResult = ticketService.consumeTicket();
        System.out.println("Customer " + savedCustomer.getName() + " registered. " + purchaseResult);
    }

    private void addTickets(Scanner scanner) {
        System.out.print("Enter Vendor ID: ");
        String vendorId = scanner.nextLine();

        System.out.print("Enter the number of tickets to add: ");
        int ticketCount = Integer.parseInt(scanner.nextLine());

        try {
            vendorService.produceTickets(vendorId, ticketCount);
            System.out.println("Added " + ticketCount + " tickets to the pool for vendor: " + vendorId);
        } catch (Exception e) {
            System.out.println("Failed to add tickets: " + e.getMessage());
        }
    }

    private void checkTicketAvailability() {
        int availableTickets = ticketPool.getAvailableTickets();
        System.out.println("Available tickets: " + availableTickets);
    }
}
