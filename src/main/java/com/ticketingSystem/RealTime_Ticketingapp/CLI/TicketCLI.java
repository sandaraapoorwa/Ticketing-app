package com.ticketingSystem.RealTime_Ticketingapp.CLI;

import com.google.gson.Gson;
import com.ticketingSystem.RealTime_Ticketingapp.SystemConfiguration.SystemConfig;
import com.ticketingSystem.RealTime_Ticketingapp.Ticket.Customer;
import com.ticketingSystem.RealTime_Ticketingapp.Ticket.TicketSystem;
import com.ticketingSystem.RealTime_Ticketingapp.Ticket.Vendor;

import java.io.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class TicketCLI {

    private static final List<Thread> vendorThreads = new ArrayList<>();
    private static final List<Thread> customerThreads = new ArrayList<>();
    private static TicketSystem ticketSystem;
    private static SystemConfig systemConfig = new SystemConfig();
    private static boolean systemRunning = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize or update the configuration
        promptConfigurationUpdate();

        // Initialize the ticket system
        ticketSystem = new TicketSystem(systemConfig.getMaxTicketCapacity());

        // Allow dynamic addition of vendors and customers
        int numVendors = promptInt(scanner, "Enter the number of vendors: ", 1, Integer.MAX_VALUE);
        int numCustomers = promptInt(scanner, "Enter the number of customers: ", 1, Integer.MAX_VALUE);

        initializeVendors(numVendors);
        initializeCustomers(numCustomers);

        // Display commands
        System.out.println("""
                \nCommands:
                1. start - Start the system
                2. stop - Stop the system
                3. status - Show ticket status
                4. config - Show current configuration
                5. exit - Exit CLI
                """);

        // Main command loop
        while (true) {
            System.out.print("\nEnter command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    startSystem();
                    break;
                case "stop":
                    stopSystem();
                    break;
                case "status":
                    showStatus();
                    break;
                case "config":
                    showConfiguration();
                    break;
                case "exit":
                    stopSystem();
                    System.out.println("Exiting CLI. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        }
    }

    private static void startSystem() {
        if (systemRunning) {
            System.out.println("System is already running.");
            return;
        }

        for (Thread vendorThread : vendorThreads) {
            if (!vendorThread.isAlive()) {
                vendorThread.start();
            }
        }
        for (Thread customerThread : customerThreads) {
            if (!customerThread.isAlive()) {
                customerThread.start();
            }
        }
        systemRunning = true;
        System.out.println("System started.");
    }

    private static void stopSystem() {
        if (!systemRunning) {
            System.out.println("System is not running.");
            return;
        }

        ticketSystem.stop();
        vendorThreads.clear();
        customerThreads.clear();
        systemRunning = false;
        System.out.println("System stopped and threads cleared.");
    }

    private static void showStatus() {
        System.out.println("Tickets in pool: " + ticketSystem.getTicketCount());
        System.out.println("Vendor threads: " + vendorThreads.size());
        System.out.println("Customer threads: " + customerThreads.size());
    }

    private static void showConfiguration() {
        System.out.println("Current Configuration:");
        System.out.println(systemConfig);
    }

    private static void initializeVendors(int numVendors) {
        for (int i = 1; i <= numVendors; i++) {
            Vendor vendor = new Vendor(ticketSystem, i, systemConfig.getTicketReleaseRate(), 0);
            Thread vendorThread = new Thread(vendor, "VendorThread-" + i);
            vendorThreads.add(vendorThread);
        }
    }

    private static void initializeCustomers(int numCustomers) {
        for (int i = 1; i <= numCustomers; i++) {
            Customer customer = new Customer(i, ticketSystem, systemConfig.getCustomerRetrievalRate());
            Thread customerThread = new Thread(customer, "CustomerThread-" + i);
            customerThreads.add(customerThread);
        }
    }

    private static void promptConfigurationUpdate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to load the existing configuration? (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(input)) {
            loadConfiguration();
        } else {
            System.out.println("Starting fresh configuration setup.");
            configureSystem();
            saveConfiguration();
        }
    }

    private static void loadConfiguration() {
        try (Reader reader = new FileReader("config.json")) {
            Gson gson = new Gson();
            systemConfig = gson.fromJson(reader, SystemConfig.class);
            System.out.println("Configuration loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Configuration file not found. Starting fresh setup.");
            configureSystem();
        } catch (IOException e) {
            System.out.println("Error loading configuration. Starting fresh setup.");
            configureSystem();
        }
    }

    private static void saveConfiguration() {
        try (Writer writer = new FileWriter("config.json")) {
            Gson gson = new Gson();
            gson.toJson(systemConfig, writer);
            System.out.println("Configuration saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving configuration.");
        }
    }

    private static void configureSystem() {
        Scanner scanner = new Scanner(System.in);

        systemConfig.setTotalTickets(promptInt(scanner, "Enter total tickets (positive integer): ", 1, Integer.MAX_VALUE));
        systemConfig.setTicketReleaseRate(promptInt(scanner, "Enter ticket release rate in milliseconds (positive integer): ", 1, Integer.MAX_VALUE));
        systemConfig.setCustomerRetrievalRate(promptInt(scanner, "Enter customer retrieval rate in milliseconds (positive integer): ", 1, Integer.MAX_VALUE));
        systemConfig.setMaxTicketCapacity(promptInt(scanner, "Enter max ticket capacity (greater than 0): ", 1, systemConfig.getTotalTickets()));
    }

    private static int promptInt(Scanner scanner, String message, int min, int max) {
        while (true) {
            System.out.print(message);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                if (value >= min && value <= max) {
                    scanner.nextLine(); // Clear input buffer
                    return value;
                }
            } else {
                scanner.nextLine(); // Clear invalid input
            }
            System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
        }
    }
}
