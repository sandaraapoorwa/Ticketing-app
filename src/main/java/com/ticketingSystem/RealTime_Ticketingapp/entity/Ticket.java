package com.ticketingSystem.RealTime_Ticketingapp.entity;

public class Ticket {
    private Long id;
    private boolean available;

    public Ticket(Long id, boolean available) {
        this.id = id;
        this.available = available;
    }
    public Long getId() {
        return id;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
