package com.zemoso.demo.service;

import com.zemoso.demo.dao.CustomerRepository;
import com.zemoso.demo.entity.Customer;
import com.zemoso.demo.exception.NoResultFoundException;
import com.zemoso.demo.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {
    @Mock
    private CustomerRepository mockCustomerRepository;

    @InjectMocks
    private CustomerServiceImpl mockCustomerServiceImpl;

    @Test
    public void testGetCustomers(){
        List<Customer> emptyCustomerList = new ArrayList<>();
        List<Customer> customerListWithData = new ArrayList<>();
        customerListWithData.add(new Customer(1, "Ram", "Anand", "sample@gmail.com"));
        customerListWithData.add(new Customer(2, "Sita", "Ram", "abc@gmail.com"));

        when(mockCustomerRepository.findAll())
                .thenReturn(emptyCustomerList)
                .thenReturn(customerListWithData);

        List<Customer> customers = mockCustomerServiceImpl.getCustomers();
        assertEquals(emptyCustomerList, customers);

        customers = mockCustomerServiceImpl.getCustomers();
        assertEquals(customerListWithData, customers);
    }

    @Test
    public void testGetCustomer(){
        Customer existingCustomer = new Customer(1, "Ram", "Anand", "sample@gmail.com");
        int negativeCustomerId = -3;
        int nonExistingCustomerId = 100;
        int existingCustomerId = existingCustomer.getId();

        when(mockCustomerRepository.findById(anyInt()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(existingCustomer));

        Throwable exception = assertThrows(ValidationException.class, ()->
                mockCustomerServiceImpl.getCustomer(negativeCustomerId));
        assertEquals(exception.getMessage(), "Customer id cannot be negative", "Negative customer id test case failed");

        Customer resultantCustomer = mockCustomerServiceImpl.getCustomer(nonExistingCustomerId);
        assertNull(resultantCustomer, "Non-existing customer id case failed");

        resultantCustomer = mockCustomerServiceImpl.getCustomer(existingCustomerId);
        assertEquals(resultantCustomer, existingCustomer, "Valid customer id case failed");
    }

    @Test
    public void testAddCustomer(){
        Customer customerWithValidData = new Customer("Ram", "Anand", "sample@gmail.com");
        Customer customerWithValidDataAfterInsert = new Customer(1, "Ram", "Anand", "sample@gmail.com");
        Customer customerWithInvalidData = new Customer(100, "Robert", "Mark", "xyz@gmail.com");

        ValidationException customerNullException = new ValidationException("New customer cannot be null");
        ValidationException newCustomerWithIdException = new ValidationException("New customer id should not be populated");
        when(mockCustomerRepository.save(nullable(Customer.class)))
                .thenReturn(customerWithValidDataAfterInsert);

        Throwable exception = assertThrows(ValidationException.class, () -> mockCustomerServiceImpl.addCustomer(null));
        assertEquals(customerNullException.getMessage(), exception.getMessage(),"Null customer case failed");

        exception = assertThrows(ValidationException.class, () -> mockCustomerServiceImpl.addCustomer(customerWithInvalidData));
        assertEquals(newCustomerWithIdException.getMessage(), exception.getMessage(), "Invalid customer data case failed");

        mockCustomerServiceImpl.addCustomer(customerWithValidData);
        verify(mockCustomerRepository, times(1)).save(nullable(Customer.class));
    }

    @Test
    public void testUpdateCustomer(){
        Customer customerWithValidData = new Customer(1,"Ram", "Anand", "abc@gmail.com");
        Customer customerWithInvalidData = new Customer(0, "Robert", "Mark", "xyz@gmail.com");
        Customer customerNotInDB = new Customer(100, "Robert", "Mark", "xyz@gmail.com");

        ValidationException customerNullException = new ValidationException("Customer cannot be null");
        ValidationException customerWithoutKey = new ValidationException("Customer key is absent");
        NoResultFoundException customerNotFoundException =
                new NoResultFoundException("Customer with id " + customerNotInDB.getId() + " does not exists");
        when(mockCustomerRepository.existsById(anyInt()))
                .thenReturn(false)
                .thenReturn(true);
        when(mockCustomerRepository.save(nullable(Customer.class)))
                .thenReturn(customerWithValidData);

        Throwable exception = assertThrows(ValidationException.class, () -> mockCustomerServiceImpl.updateCustomer(null));
        assertEquals(customerNullException.getMessage(), exception.getMessage(),"Null customer case failed");

        exception = assertThrows(ValidationException.class, () -> mockCustomerServiceImpl.updateCustomer(customerWithInvalidData));
        assertEquals(customerWithoutKey.getMessage(), exception.getMessage(), "Customer key absent case failed");

        exception = assertThrows(NoResultFoundException.class, () -> mockCustomerServiceImpl.updateCustomer(customerNotInDB));
        assertEquals(customerNotFoundException.getMessage(), exception.getMessage(), "Customer not in db case failed");

        mockCustomerServiceImpl.updateCustomer(customerWithValidData);
        verify(mockCustomerRepository, times(1)).save(nullable(Customer.class));
    }


    @Test
    public void testDeleteCustomer(){
        int nonExistingCustomerId = 100;
        int existingCustomerId = 1;

        when(mockCustomerRepository.existsById(anyInt()))
                .thenReturn(false)
                .thenReturn(true);
        doNothing().when(mockCustomerRepository).deleteById(anyInt());
        NoResultFoundException customerNotFoundException =
                new NoResultFoundException("Customer with id " + nonExistingCustomerId + " does not exists");

        Throwable exception = assertThrows(NoResultFoundException.class, ()->
                mockCustomerServiceImpl.deleteCustomer(nonExistingCustomerId));
        assertEquals(exception.getMessage(), customerNotFoundException.getMessage(), "Customer not found test failed");

        mockCustomerServiceImpl.deleteCustomer(existingCustomerId);
        verify(mockCustomerRepository,times(1)).deleteById(anyInt());
    }

    @Test
    public void testSearchCustomerByName(){
        List<Customer> emptyCustomerList = new ArrayList<>();
        List<Customer> customerListWithData = new ArrayList<>();
        customerListWithData.add(new Customer(1, "Ram", "Anand", "sample@gmail.com"));
        customerListWithData.add(new Customer(2, "Sita", "Ram", "abc@gmail.com"));
        String searchStringWithData="Ram";
        String searchStringWithoutData="Hectare";
        when(mockCustomerRepository.findByFirstNameContainsOrLastNameContainsAllIgnoreCase(anyString(),anyString()))
                .thenReturn(emptyCustomerList)
                .thenReturn(customerListWithData);

        List<Customer> customers = mockCustomerServiceImpl.searchCustomerByName("");
        assertTrue(customers.isEmpty(), "Blank search string case failed");

        customers = mockCustomerServiceImpl.searchCustomerByName(null);
        assertTrue(customers.isEmpty(), "Null search string case failed");

        customers = mockCustomerServiceImpl.searchCustomerByName(searchStringWithoutData);
        assertTrue(customers.isEmpty(), "No matching data test failed");

        customers = mockCustomerServiceImpl.searchCustomerByName(searchStringWithData);
        assertTrue(!customers.isEmpty() && customers.size() == customerListWithData.size(),
                "No matching data test failed");
    }
}
