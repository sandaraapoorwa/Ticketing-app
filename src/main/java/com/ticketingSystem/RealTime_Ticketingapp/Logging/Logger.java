package com.ticketingSystem.RealTime_Ticketingapp.Logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    // Define the log file path
    private static final String LOG_FILE = "resources/logs.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Ensure log directory exists
    static {
        try {
            Path logDirectory = Paths.get("resources");
            if (!Files.exists(logDirectory)) {
                Files.createDirectories(logDirectory);
            }
        } catch (IOException e) {
            System.err.println("Failed to create log directory: " + e.getMessage());
        }
    }

    // Log a message
    public static void log(String message) {
        String timeStampedMessage = "[" + LocalDateTime.now().format(FORMATTER) + "] " + message;

        // Print the log to console
        System.out.println(timeStampedMessage);

        // Write the log to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(timeStampedMessage);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write log to file: " + e.getMessage());
        }
    }
}
