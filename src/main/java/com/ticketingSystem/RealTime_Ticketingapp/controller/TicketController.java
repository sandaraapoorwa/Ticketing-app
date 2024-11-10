package com.ticketingSystem.RealTime_Ticketingapp.controller;
import com.ticketingSystem.RealTime_Ticketingapp.entity.Customer;
import com.ticketingSystem.RealTime_Ticketingapp.service.CustomerService;
import com.ticketingSystem.RealTime_Ticketingapp.service.TicketService;
import com.ticketingSystem.RealTime_Ticketingapp.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {
    private final VendorService vendorService;
    private final CustomerService customerService;
    private final TicketService ticketService;

    @Autowired
    public TicketController(VendorService vendorService, CustomerService customerService, TicketService ticketService) {
        this.vendorService = vendorService;
        this.customerService = customerService;
        this.ticketService = ticketService;
    }
    // Ticket Endpoints
    @PostMapping("/add-ticket")
    public String addTicket() {
        vendorService.produceTicket();
        return "Ticket added to the pool.";
    }

    @GetMapping("/purchase-ticket")
    public String purchaseTicket() {
        return customerService.consumeTicket();
    }

    // Customer Endpoints
    @PostMapping("/addCustomer")
    public Customer addCustomer(@RequestBody Customer customer) {
        return ticketService.saveCustomer(customer);
    }

    @PostMapping("/addCustomers")
    public List<Customer> addCustomers(@RequestBody List<Customer> customers) {
        return ticketService.saveCustomers(customers);
    }

    @GetMapping("/customers")
    public List<Customer> findAllCustomers() {
        return ticketService.getCustomers();
    }

    @GetMapping("/customerByID/{id}")
    public Customer findCustomerById(@PathVariable int id) {
        return ticketService.getCustomerById(id);
    }

    @GetMapping("/customer/{name}")
    public Customer findCustomerByName(@PathVariable String name) {
        return ticketService.getCustomerByName(name);
    }

    @PutMapping("/updateCustomer")
    public Customer updateCustomer(@RequestBody Customer customer) {
        return ticketService.updateCustomer(customer);
    }

    @DeleteMapping("/deleteCustomer/{id}")
    public String deleteCustomer(@PathVariable int id) {
        return ticketService.deleteCustomer(id);
    }
}
