package com.ticketingSystem.RealTime_Ticketingapp.controller;


import com.ticketingSystem.RealTime_Ticketingapp.entity.Customer;
import com.ticketingSystem.RealTime_Ticketingapp.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private TicketService service;

    @PostMapping("/addCustomer")

    public Customer addCustomer(@RequestBody Customer customer){
        return service.saveCustomer(customer);
    }
    @PostMapping("/addCustomers")
    public List<Customer> addCustomer(@RequestBody  List<Customer> customers) {
        return service.saveCustomers(customers);
    }
    @GetMapping("/customers")
    public List<Customer> findAllCustomers(){
        return service.getCustomers();
    }
    @GetMapping("/customerByID/{id}")
    public Customer findCustomerById(@PathVariable  int id){
        return service.getCustomerById(id);

    }
    @GetMapping("/customer/{name}")
    public Customer findCustomerByName(@PathVariable String name){
        return service.getCustomerByName(name);
    }
    @PostMapping("/update")
    public Customer updateCustomer(@RequestBody Customer customer){
        return service.updateCustomer(customer);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteCustomer(int id){
        return  service.deleteCustomer(id);
    }

}
