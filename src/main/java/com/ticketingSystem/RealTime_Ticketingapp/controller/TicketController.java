package com.ticketingSystem.RealTime_Ticketingapp.controller;

import com.ticketingSystem.RealTime_Ticketingapp.entity.Customer;
import com.ticketingSystem.RealTime_Ticketingapp.service.TicketService;
import com.ticketingSystem.RealTime_Ticketingapp.service.CustomerService;
import com.ticketingSystem.RealTime_Ticketingapp.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {
    private final VendorService vendorService;
    private final TicketService ticketService;
    private final CustomerService customerService;

    @Autowired
    public TicketController(VendorService vendorService, TicketService ticketService, CustomerService customerService) {
        this.vendorService = vendorService;
        this.ticketService = ticketService;
        this.customerService = customerService;
    }

    // Ticket Endpoints
    @PostMapping("/add-ticket")
    public String addTicket(@RequestParam String vendorId, @RequestParam int ticketCount) {
        vendorService.produceTickets(vendorId, ticketCount);
        return "Added " + ticketCount + " tickets to the pool for vendor: " + vendorId;
    }

    @GetMapping("/purchase-ticket")
    public String purchaseTicket() {
        return ticketService.consumeTicket();
    }

    // Customer Endpoints
    @PostMapping("/addCustomer")
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerService.saveCustomer(customer);
    }

    @PostMapping("/addCustomers")
    public List<Customer> addCustomers(@RequestBody List<Customer> customers) {
        return customerService.saveCustomers(customers);
    }

    @GetMapping("/customers")
    public List<Customer> findAllCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/customerByID/{id}")
    public Customer findCustomerById(@PathVariable int id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/customer/{name}")
    public Customer findCustomerByName(@PathVariable String name) {
        return customerService.getCustomerByName(name);
    }

    @PutMapping("/updateCustomer")
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

    @DeleteMapping("/deleteCustomer/{id}")
    public String deleteCustomer(@PathVariable int id) {
        return customerService.deleteCustomer(id);
    }
}
