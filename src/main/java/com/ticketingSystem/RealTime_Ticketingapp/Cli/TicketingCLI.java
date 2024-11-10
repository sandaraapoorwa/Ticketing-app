package com.ticketingSystem.RealTime_Ticketingapp.Cli;

import com.ticketingSystem.RealTime_Ticketingapp.entity.Customer;
import com.ticketingSystem.RealTime_Ticketingapp.service.CustomerService;
import com.ticketingSystem.RealTime_Ticketingapp.service.TicketService;
import com.ticketingSystem.RealTime_Ticketingapp.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Scanner;
@Component
public class TicketingCLI implements CommandLineRunner {

    private final TicketService ticketService;
    private final CustomerService customerService;
    private final VendorService vendorService;

    @Autowired
    public TicketingCLI(TicketService ticketService, CustomerService customerService, VendorService vendorService) {
        this.ticketService = ticketService;
        this.customerService = customerService;
        this.vendorService = vendorService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Ticketing CLI!");

        boolean running = true;

        while (running) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Register a Customer and Purchase Ticket");
            System.out.println("2. Add Ticket to Pool");
            System.out.println("3. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerAndPurchaseTicket(scanner);
                    break;
                case "2":
                    addTicket();
                    break;
                case "3":
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
        customer.setPhoneNumber(Integer.parseInt(phoneNumber));
        customer.setEmail(email);

        Customer savedCustomer = ticketService.saveCustomer(customer);

        // Attempt to purchase a ticket
        String purchaseResult = customerService.consumeTicket();

        System.out.println("Customer " + savedCustomer.getName() + " registered. " + purchaseResult);
    }

    private void addTicket() {
        vendorService.produceTicket();
        System.out.println("Ticket added to the pool.");
    }
}
