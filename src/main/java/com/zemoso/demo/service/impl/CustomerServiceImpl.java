package com.zemoso.demo.service.impl;

import com.zemoso.demo.dao.CustomerRepository;
import com.zemoso.demo.entity.Customer;
import com.zemoso.demo.exception.NoResultFoundException;
import com.zemoso.demo.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerDAO;

    public CustomerServiceImpl(CustomerRepository customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    @Transactional
    public List<Customer> getCustomers(){
        return customerDAO.findAll();
    }

    @Override
    @Transactional
    public Customer getCustomer(int customerId) {
        if(customerId<0){
            throw new ValidationException("Customer id cannot be negative");
        }
        return customerDAO.findById(customerId).orElse(null);
    }

    @Override
    @Transactional
    public void addCustomer(Customer customer) {
        if(customer == null){
            throw new ValidationException("New customer cannot be null");
        }
        if(customer.getId() != 0){
            throw new ValidationException("New customer id should not be populated");
        }
        customerDAO.save(customer);
    }

    @Override
    @Transactional
    public void updateCustomer(Customer customer) {
        if(customer == null){
            throw new ValidationException("Customer cannot be null");
        }
        if(customer.getId() == 0){
            throw new ValidationException("Customer key is absent");
        }
        int customerId = customer.getId();
        if( !customerDAO.existsById(customerId)){
            throw new NoResultFoundException("Customer with id " + customerId + " does not exists");
        }
        customerDAO.save(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(int customerId) {
        if( !customerDAO.existsById(customerId)){
            throw new NoResultFoundException("Customer with id " + customerId + " does not exists");
        }
        customerDAO.deleteById(customerId);
    }

    @Override
    @Transactional
    public List<Customer> searchCustomerByName(String searchString) {
        if(searchString ==  null || searchString.isBlank()){
            return new ArrayList<>();
        }
        return customerDAO.findByFirstNameContainsOrLastNameContainsAllIgnoreCase(searchString, searchString);
    }
}
