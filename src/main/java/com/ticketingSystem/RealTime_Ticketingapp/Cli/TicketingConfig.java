package com.ticketingSystem.RealTime_Ticketingapp.Cli;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketingConfig {
    @Bean
    public int maxTicketCapacity() {
        return 10;  // Set the max ticket capacity here
    }
}
