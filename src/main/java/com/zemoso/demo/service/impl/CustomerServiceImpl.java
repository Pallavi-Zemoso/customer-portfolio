package com.zemoso.demo.service.impl;

import com.zemoso.demo.dao.CustomerRepository;
import com.zemoso.demo.dto.CustomerDTO;
import com.zemoso.demo.entity.Customer;
import com.zemoso.demo.exception.NoResultFoundException;
import com.zemoso.demo.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerDAO;

    public CustomerServiceImpl(CustomerRepository customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    @Transactional
    public List<CustomerDTO> getCustomers(){
        List<Customer> customers = customerDAO.findAll();
        return customers.stream().map(this::convertToCustomerDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerDTO getCustomer(int customerId) {
        if(customerId<0){
            throw new ValidationException("Customer id cannot be negative");
        }
        Customer customer =  customerDAO.findById(customerId).orElse(null);
        return  convertToCustomerDTO(customer) ;
    }

    @Override
    @Transactional
    public void addCustomer(CustomerDTO customerDTO) {
        if(customerDTO == null){
            throw new ValidationException("New customer cannot be null");
        }
        if(customerDTO.getId() != 0){
            throw new ValidationException("New customer id should not be populated");
        }
        Customer customer = convertToCustomerEntity(customerDTO);
        customerDAO.save(customer);
    }

    @Override
    @Transactional
    public void updateCustomer(CustomerDTO customerDTO) {
        if(customerDTO == null){
            throw new ValidationException("Customer cannot be null");
        }
        if(customerDTO.getId() == 0){
            throw new ValidationException("Customer key is absent");
        }
        int customerId = customerDTO.getId();
        if( !customerDAO.existsById(customerId)){
            throw new NoResultFoundException("Customer with id " + customerId + " does not exists");
        }
        Customer customer = convertToCustomerEntity(customerDTO);
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
    public List<CustomerDTO> searchCustomerByName(String searchString) {
        if(searchString ==  null || searchString.isBlank()){
            return new ArrayList<>();
        }
        List<Customer> customers = customerDAO.findByFirstNameContainsOrLastNameContainsAllIgnoreCase(searchString, searchString);
        return customers.stream().map(this::convertToCustomerDTO).collect(Collectors.toList());
    }

    private CustomerDTO convertToCustomerDTO(Customer customer){
        if(customer == null){
            return null;
        }
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }

    private Customer convertToCustomerEntity(CustomerDTO customerDTO){
        if(customerDTO == null){
            return null;
        }
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        return customer;
    }
}
