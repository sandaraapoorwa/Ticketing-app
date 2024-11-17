package com.ticketingSystem.RealTime_Ticketingapp.Cli;

import java.util.List;

public class InputValidator {

    // Validate command input
    public static String validateCommand(String input, List<String> validCommands) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Command cannot be empty.");
        }

        input = input.trim().toLowerCase(); // Trim spaces and convert to lowercase

        if (!validCommands.contains(input)) {
            throw new IllegalArgumentException("Invalid command. Allowed commands: " + String.join(", ", validCommands));
        }
        return input;  // Return the valid command
    }
}
