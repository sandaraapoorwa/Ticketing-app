package com.ticketingSystem.RealTime_Ticketingapp.repository;

import com.ticketingSystem.RealTime_Ticketingapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<Customer,Integer> {
    Customer findByName(String name);
}
