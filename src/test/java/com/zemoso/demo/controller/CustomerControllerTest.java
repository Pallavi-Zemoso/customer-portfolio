package com.zemoso.demo.controller;


import com.zemoso.demo.constants.ConstantUtils;
import com.zemoso.demo.dto.CustomerDTO;
import com.zemoso.demo.exception.NoResultFoundException;
import com.zemoso.demo.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Slf4j
public class CustomerControllerTest {
    @MockBean
    @Qualifier("securityDataSource")
    private DataSource dataSource;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService mockCustomerService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetCustomers() throws Exception {
        List<CustomerDTO> customerListWithData = new ArrayList<>();
        CustomerDTO customerDTO1 = new CustomerDTO(1, "Ram", "Anand", "sample@gmail.com");
        CustomerDTO customerDTO2 = new CustomerDTO(2, "Sita", "Ram", "abc@gmail.com");
        customerListWithData.add(customerDTO1);
        customerListWithData.add(customerDTO2);

        when(mockCustomerService.getCustomers())
                .thenReturn(customerListWithData);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(view().name("/customer/list-customers"))
                .andExpect(model().attribute("customerDTOList", hasSize(2)))
                .andExpect(model().attribute("customerDTOList", hasItem(customerDTO1)));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowAddForm() throws Exception {
        mockMvc.perform(get("/customers/show-add-form"))
                .andExpect(status().isOk())
                .andExpect(view().name("/customer/customer-form"))
                .andExpect(model().attribute(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE,hasProperty("id", is(0))))
                .andExpect(model().attribute(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE,hasProperty("firstName", emptyOrNullString())))
                .andExpect(model().attribute(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE,hasProperty("lastName", emptyOrNullString())))
                .andExpect(model().attribute(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE,hasProperty("email", emptyOrNullString())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddCustomer() throws Exception {
        CustomerDTO invalidCustomerDTO = new CustomerDTO(1, "Ram", "", "sample@gmail.com");
        CustomerDTO validCustomerDTO = new CustomerDTO( "Sita", "Ram", "abc@gmail.com");

        doNothing().when(mockCustomerService).addCustomer(nullable(CustomerDTO.class));

        mockMvc.perform(post("/customers")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customers/show-add-form"));

        mockMvc.perform(post("/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE, invalidCustomerDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customers/show-add-form"));

        mockMvc.perform(post("/customers")
                .with(csrf())
                .flashAttr(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE, validCustomerDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customers"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testShowUpdateForm() throws Exception {
        CustomerDTO validCustomerDTOFromDB = new CustomerDTO(1, "Sita", "Ram", "abc@gmail.com");
        CustomerDTO validCustomerDTOFromMVC = new CustomerDTO(1, "Sita", "Rama", "abc@gmail.com");
        int customerId = 1;

        when(mockCustomerService.getCustomer(anyInt()))
                .thenReturn(null)
                .thenReturn(validCustomerDTOFromDB);
        //Entry to update form for first
        mockMvc.perform(get("/customers/{customerId}/show-update-form", customerId))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/customers/{customerId}/show-update-form", customerId))
                .andExpect(status().isOk())
                .andExpect(view().name("/customer/customer-form"))
                .andExpect(model().attribute("todo","/update"));

        //Redirect to update form
        mockMvc.perform(get("/customers/{customerId}/show-update-form", customerId)
                .requestAttr(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE,validCustomerDTOFromMVC))
                .andExpect(status().isOk())
                .andExpect(view().name("/customer/customer-form"))
                .andExpect(model().attribute("todo","/update"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateCustomer() throws Exception {
        CustomerDTO invalidCustomerDTO = new CustomerDTO( 1,"Ram", "", "sample@gmail.com");
        CustomerDTO validCustomerDTO = new CustomerDTO( 1,"Sita", "Ram", "abc@gmail.com");

        doNothing().when(mockCustomerService).updateCustomer(nullable(CustomerDTO.class));

        mockMvc.perform(post("/customers/update")
                .with(csrf()))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/customers/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE, invalidCustomerDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customers/"+ invalidCustomerDTO.getId() + "/show-update-form"));

        mockMvc.perform(post("/customers/update")
                .with(csrf())
                .flashAttr(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE, validCustomerDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customers"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteCustomer() throws Exception {
        int invalidCustomerId = 100;
        int validCustomerId = 1;
        NoResultFoundException noResultFoundException = new NoResultFoundException("Customer with id " + invalidCustomerId + " does not exists");

        doNothing().doThrow(noResultFoundException)
                .when(mockCustomerService).deleteCustomer(anyInt());

        mockMvc.perform(get("/customers/{customerId}/delete",validCustomerId))
                    .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customers"));

        mockMvc.perform(get("/customers/{customerId}/delete",invalidCustomerId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void testSearchCustomerByName() throws Exception {
        String searchStringWithData = "Ram";
        List<CustomerDTO> customerDTOListWithData = new ArrayList<>();
        CustomerDTO customerDTO1 = new CustomerDTO(1, "Ram", "Anand", "sample@gmail.com");
        CustomerDTO customerDTO2 = new CustomerDTO(2, "Sita", "Ram", "abc@gmail.com");
        customerDTOListWithData.add(customerDTO1);
        customerDTOListWithData.add(customerDTO2);

        when(mockCustomerService.searchCustomerByName(nullable(String.class)))
                .thenReturn(customerDTOListWithData);

        mockMvc.perform(get("/customers/search")
                .param("formAction","Clear"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customers"));

        mockMvc.perform(get("/customers/search")
                .param("formAction","Search")
                .param("searchString",searchStringWithData))
                .andExpect(status().isOk())
                .andExpect(view().name("/customer/list-customers"))
                .andExpect(model().attribute("customerDTOList", hasSize(2)))
                .andExpect(model().attribute("customerDTOList", hasItem(customerDTO1)));
    }
}
