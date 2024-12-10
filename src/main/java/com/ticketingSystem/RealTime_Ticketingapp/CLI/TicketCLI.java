package com.ticketingSystem.RealTime_Ticketingapp.CLI;

import com.google.gson.Gson;
import com.ticketingSystem.RealTime_Ticketingapp.SystemConfiguration.SystemConfig;
import com.ticketingSystem.RealTime_Ticketingapp.Functions.Customer;
import com.ticketingSystem.RealTime_Ticketingapp.Functions.TicketPoolSystem;
import com.ticketingSystem.RealTime_Ticketingapp.Functions.Vendor;
import java.io.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class TicketCLI {
    private static final List<Thread> vendorThreads = new ArrayList<>();
    private static final List<Thread> customerThreads = new ArrayList<>();
    private static TicketPoolSystem ticketPoolSystem;
    private static SystemConfig systemConfig = new SystemConfig();
    private static boolean systemRunning = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize or update the configuration
        promptConfigurationUpdate();

        // Initialize the ticket system
        ticketPoolSystem = new TicketPoolSystem(systemConfig.getMaxTicketCapacity(), systemConfig.getTotalTickets());

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

        // Initialize vendors and customers using the configured numbers
        initializeVendors(systemConfig.getNumberOfVendors());
        initializeCustomers(systemConfig.getNumberOfCustomers());

        // Start vendor threads
        for (Thread vendorThread : vendorThreads) {
            vendorThread.start();
        }

        // Start customer threads
        for (Thread customerThread : customerThreads) {
            customerThread.start();
        }

        systemRunning = true;
        System.out.println("System started.");
    }

    private static void initializeVendors(int numVendors) {
        for (int i = 1; i <= numVendors; i++) {
            Vendor vendor = new Vendor(ticketPoolSystem, i, systemConfig.getTicketReleaseRate(), 0);
            Thread vendorThread = new Thread(vendor, "VendorThread-" + i);
            vendorThreads.add(vendorThread);
        }
    }

    private static void initializeCustomers(int numCustomers) {
        for (int i = 1; i <= numCustomers; i++) {
            Customer customer = new Customer(i, ticketPoolSystem, systemConfig.getCustomerRetrievalRate());
            Thread customerThread = new Thread(customer, "CustomerThread-" + i);
            customerThreads.add(customerThread);
        }
    }

    private static void stopSystem() {
        if (!systemRunning) {
            System.out.println("System is not running.");
            return;
        }

        // Stop all vendor and customer threads
        for (Thread vendorThread : vendorThreads) {
            vendorThread.interrupt();  // Interrupt vendor threads
        }
        for (Thread customerThread : customerThreads) {
            customerThread.interrupt();  // Interrupt customer threads
        }
        // Clear thread lists
        vendorThreads.clear();
        customerThreads.clear();
        // Stop the ticket pool system
        ticketPoolSystem.stop();
        systemRunning = false;
        System.out.println("System stopped and threads cleared.");
    }

    private static void showStatus() {
        System.out.println("Tickets in pool: " + ticketPoolSystem.getTicketCount());
        System.out.println("Vendor threads: " + vendorThreads.size());
        System.out.println("Customer threads: " + customerThreads.size());
    }

    private static void showConfiguration() {
        System.out.println("Current Configuration:");
        System.out.println(systemConfig);
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

        // Prompt for total tickets
        systemConfig.setTotalTickets(promptInt(scanner, "Enter total tickets (positive integer): ", 1, Integer.MAX_VALUE));

        // Prompt for ticket release rate
        systemConfig.setTicketReleaseRate(promptInt(scanner, "Enter ticket release rate in milliseconds (positive integer): ", 1, Integer.MAX_VALUE));

        // Prompt for customer retrieval rate
        systemConfig.setCustomerRetrievalRate(promptInt(scanner, "Enter customer retrieval rate in milliseconds (positive integer): ", 1, Integer.MAX_VALUE));

        // Prompt for max ticket capacity
        systemConfig.setMaxTicketCapacity(promptInt(scanner, "Enter max ticket capacity (less than or equal to total tickets): ", 1, systemConfig.getTotalTickets()));

        // New prompts for number of vendors and customers
        systemConfig.setNumberOfVendors(promptInt(scanner, "Enter number of vendors: ", 1, Integer.MAX_VALUE));
        systemConfig.setNumberOfCustomers(promptInt(scanner, "Enter number of customers: ", 1, Integer.MAX_VALUE));

        saveConfiguration();
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
