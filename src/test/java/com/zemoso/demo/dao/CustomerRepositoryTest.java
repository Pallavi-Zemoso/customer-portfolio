package com.zemoso.demo.dao;

import com.zemoso.demo.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class CustomerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindAll(){
        List<Customer> customers = customerRepository.findAll();
        assertEquals(customerRepository.count(), customers.size());
    }

    @Test
    public void testGetCustomer(){
        Customer customer = new Customer("Ram", "Anand", "sample@gmail.com");
        int customerId = (int) entityManager.persistAndGetId(customer);
        assertTrue(customerRepository.existsById(customerId), "Retrieve Customer by id test failed");
    }

    @Test
    public void testSaveCustomer(){
        Customer customer = new Customer("Ram", "Anand", "sample@gmail.com");
        Customer savedCustomer = entityManager.persistAndFlush(customer);
        assertTrue(savedCustomer.getId() !=0, "Customer save failed");

        String newEmail ="abcd@gmail.com";
        savedCustomer.setEmail(newEmail);
        assertEquals(newEmail, customerRepository.findById(savedCustomer.getId())
                .orElse(new Customer())
                .getEmail(), "Customer update test failed");
    }

    @Test
    public void testDeleteCustomer(){
        Customer customer = new Customer("Ram", "Anand", "sample@gmail.com");
        Customer savedCustomer = entityManager.persistAndFlush(customer);
        int customerId =savedCustomer.getId();
        assertTrue(customerRepository.existsById(customerId), "Customer save failed");

        customerRepository.deleteById(customerId);
        assertFalse(customerRepository.existsById(customerId), "Customer delete test failed");

    }

    @Test
    public void testSearchCustomerByName(){
        Customer customer1 = new Customer( "Ram", "Anand", "sample@gmail.com");
        Customer customer2 = new Customer( "Sita", "Ram", "abc@gmail.com");
        Customer customer3 = new Customer( "Heather", "Tiger", "rail@outlook.com");

        entityManager.persist(customer1);
        entityManager.persist(customer2);
        entityManager.persist(customer3);

        String searchString = "Ram";
        List<Customer> matchedCustomers = customerRepository
                .findByFirstNameContainsOrLastNameContainsAllIgnoreCase(searchString,searchString);
        assertTrue(matchedCustomers.contains(customer1) && matchedCustomers.contains(customer2), "Search by name case failed");
    }
}
