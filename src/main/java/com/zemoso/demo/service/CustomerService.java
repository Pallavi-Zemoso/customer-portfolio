package com.zemoso.demo.service;

import com.zemoso.demo.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getCustomers();
    CustomerDTO getCustomer(int customerId);
    void addCustomer(CustomerDTO customerDTO);
    void updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(int customerId);
    List<CustomerDTO> searchCustomerByName(String searchString);
}
