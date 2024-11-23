package com.ticketingSystem.RealTime_Ticketingapp.CLI;

import com.google.gson.Gson;
import com.ticketingSystem.RealTime_Ticketingapp.SystemConfiguration.SystemConfig;
import com.ticketingSystem.RealTime_Ticketingapp.Ticket.Customer;
import com.ticketingSystem.RealTime_Ticketingapp.Ticket.TicketSystem;
import com.ticketingSystem.RealTime_Ticketingapp.Ticket.Vendor;
import java.io.*;
import java.util.Scanner;

public class TicketCLI {

    private static SystemConfig systemConfig = new SystemConfig(); // Holds the configuration

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Configuration prompt
        promptConfigurationUpdate();

        // Initialize the ticket system with configuration values
        TicketSystem ticketSystem = new TicketSystem(systemConfig.getMaxTicketCapacity());

        // Initialize Vendor and Customer threads
        Vendor vendor = new Vendor(ticketSystem, systemConfig.getTicketReleaseRate());
        Customer customer = new Customer(ticketSystem, systemConfig.getCustomerRetrievalRate());

        Thread vendorThread = new Thread(vendor, "VendorThread");
        Thread customerThread = new Thread(customer, "CustomerThread");

        System.out.println("\nCommands:\n1. start - Start the system\n2. stop - Stop the system\n3. status - Show ticket status\n4. exit - Exit CLI");

        // Main loop for accepting user commands
        while (true) {
            System.out.print("\nEnter command: ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    startSystem(vendorThread, customerThread);
                    break;
                case "stop":
                    stopSystem(ticketSystem);
                    break;
                case "status":
                    showStatus(ticketSystem);
                    break;
                case "exit":
                    stopSystem(ticketSystem);
                    System.out.println("Exiting CLI. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid command. Try again.");
            }
        }
    }

    // Handles the start of the system
    private static void startSystem(Thread vendorThread, Thread customerThread) {
        if (!vendorThread.isAlive() && !customerThread.isAlive()) {
            vendorThread.start();
            customerThread.start();
            System.out.println("System started.");
        } else {
            System.out.println("System is already running.");
        }
    }

    // Handles the stop of the system
    private static void stopSystem(TicketSystem ticketSystem) {
        ticketSystem.stop();
        System.out.println("System stopped.");
    }

    // Shows the current status of the ticket system
    private static void showStatus(TicketSystem ticketSystem) {
        System.out.println("Tickets in pool: " + ticketSystem.getTicketCount());
    }

    // Prompts the user for configuration input or loads the existing configuration
    private static void promptConfigurationUpdate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to load the existing configuration? (yes/no)");
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.equals("yes")) {
            loadConfiguration();
        } else {
            System.out.println("Starting fresh configuration setup.");
            configureSystem();
            saveConfiguration();
        }
    }

    // Loads configuration from a JSON file
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

    // Saves the current configuration to a JSON file
    private static void saveConfiguration() {
        try (Writer writer = new FileWriter("config.json")) {
            Gson gson = new Gson();
            gson.toJson(systemConfig, writer);
            System.out.println("Configuration saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving configuration.");
        }
    }

    // Configures the system using user input
    private static void configureSystem() {
        Scanner scanner = new Scanner(System.in);

        systemConfig.setTotalTickets(promptInt(scanner, "Enter total tickets (positive integer): ", 1, Integer.MAX_VALUE));
        systemConfig.setTicketReleaseRate(promptInt(scanner, "Enter ticket release rate in milliseconds (positive integer): ", 1, Integer.MAX_VALUE));
        systemConfig.setCustomerRetrievalRate(promptInt(scanner, "Enter customer retrieval rate in milliseconds (positive integer): ", 1, Integer.MAX_VALUE));
        systemConfig.setMaxTicketCapacity(promptInt(scanner, "Enter max ticket capacity (greater than 0): ", 1, systemConfig.getTotalTickets()));
    }

    // Prompts for an integer value with a defined range and ensures valid input
    private static int promptInt(Scanner scanner, String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Value must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
