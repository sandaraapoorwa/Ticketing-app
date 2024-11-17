package com.ticketingSystem.RealTime_Ticketingapp.controller;

import com.ticketingSystem.RealTime_Ticketingapp.service.TicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Endpoint to release a ticket
    @PostMapping("/release/{ticketId}")
    public String releaseTicket(@PathVariable Long ticketId) {
        try {
            return ticketService.releaseTicket(ticketId);
        } catch (Exception e) {
            return "Error releasing ticket: " + e.getMessage();
        }
    }

    // Endpoint to purchase a ticket
    @PostMapping("/purchase")
    public String purchaseTicket() {
        try {
            return ticketService.purchaseTicket();
        } catch (Exception e) {
            return "Error purchasing ticket: " + e.getMessage();
        }
    }

    // Endpoint to get current system metrics
    @GetMapping("/metrics")
    public TicketMetrics getMetrics() {
        return new TicketMetrics(
                ticketService.getTotalTickets(),
                ticketService.getAvailableTickets(),
                ticketService.getTicketReleaseRate(),
                ticketService.getMaxTicketCapacity()
        );
    }

    // Custom class to hold metrics
    public static class TicketMetrics {
        private final int totalTickets;
        private final int availableTickets;
        private final double ticketReleaseRate;
        private final int maxTicketCapacity;

        public TicketMetrics(int totalTickets, int availableTickets, double ticketReleaseRate, int maxTicketCapacity) {
            this.totalTickets = totalTickets;
            this.availableTickets = availableTickets;
            this.ticketReleaseRate = ticketReleaseRate;
            this.maxTicketCapacity = maxTicketCapacity;
        }

        public int getTotalTickets() {
            return totalTickets;
        }

        public int getAvailableTickets() {
            return availableTickets;
        }

        public double getTicketReleaseRate() {
            return ticketReleaseRate;
        }

        public int getMaxTicketCapacity() {
            return maxTicketCapacity;
        }
    }
}
