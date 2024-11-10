package com.ticketingSystem.RealTime_Ticketingapp.service;

import com.ticketingSystem.RealTime_Ticketingapp.entity.Customer;
// Assuming the repository is specific to customers
import com.ticketingSystem.RealTime_Ticketingapp.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private Repository repository;

    public Customer saveCustomer(Customer customer){
        return repository.save(customer);
    }

    public List<Customer> saveCustomers(List<Customer> customers){
        return repository.saveAll(customers);
    }

    public List<Customer> getCustomers(){
        return repository.findAll();
    }

    public Customer getCustomerById(int id){
        return repository.findById(id).orElse(null);
    }

    public Customer getCustomerByName(String name){
        return repository.findByName(name);
    }

    public String deleteCustomer(int id){
        repository.deleteById(id);
        return "Customer removed! ID: " + id;
    }

    public Customer updateCustomer(Customer customer){
        Customer existingCustomer = repository.findById(customer.getId()).orElse(null);
        if (existingCustomer != null) {
            existingCustomer.setName(customer.getName());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setAge(customer.getAge());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            return repository.save(existingCustomer);
        }
        return null; // throw an exception if the customer isn't found
    }

}
