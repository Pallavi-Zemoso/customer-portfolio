package com.zemoso.demo.controller;

import com.zemoso.demo.entity.Customer;
import com.zemoso.demo.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CustomerControllerTest {

    //@Autowired
    //private MockMvc mockMvc;

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    public void setup(){
       // mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testGetCustomers(){
        List<Customer> emptyCustomerList = new ArrayList<>();
        List<Customer> customerListWithData = new ArrayList<>();
        customerListWithData.add(new Customer(1, "Ram", "Anand", "sample@gmail.com"));
        customerListWithData.add(new Customer(2, "Sita", "Ram", "abc@gmail.com"));

        when(customerService.getCustomers())
                .thenReturn(emptyCustomerList)
                .thenReturn(customerListWithData);

        List<Customer> customers = customerService.getCustomers();
        assertEquals(emptyCustomerList, customers);

        customers = customerService.getCustomers();
        assertEquals(customerListWithData, customers);
    }

    @Test
    public void testAddCustomer(){
        Customer customerWithValidData = new Customer("Ram", "Anand", "sample@gmail.com");
        Customer customerWithInvalidData = new Customer(1, "Ram", "Anand", "sample@gmail.com");

        ValidationException customerNullException = new ValidationException("New customer cannot be null");
        ValidationException newCustomerWithIdException = new ValidationException("New customer id should not be populated");
        doThrow(customerNullException)
                .doThrow(newCustomerWithIdException)
                .doNothing()
                .when(customerService).addCustomer(nullable(Customer.class));

        Throwable exception = assertThrows(ValidationException.class, () -> {
            customerService.addCustomer(null);
        });
        assertEquals(customerNullException, exception,"Null customer case failed");

        exception = assertThrows(ValidationException.class, () -> {
            customerService.addCustomer(customerWithInvalidData);
        });
        assertEquals(newCustomerWithIdException, exception, "Invalid customer  data case failed");

        exception = assertThrows(ValidationException.class, () -> {
            customerService.addCustomer(customerWithValidData);
        });
        assertEquals(newCustomerWithIdException, exception, "Invalid customer  data case failed");
    }
}
