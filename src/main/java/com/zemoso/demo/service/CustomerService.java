package com.zemoso.demo.service;

import com.zemoso.demo.entity.Customer;

import java.util.List;

public interface CustomerService {
    public List<Customer> getCustomers();
    public void addCustomer(Customer customer);
    public void updateCustomer(Customer customer);
    public Customer getCustomer(int customerId);
    public void deleteCustomer(int customerId);
    public List<Customer> searchCustomerByName(String searchString);
}
