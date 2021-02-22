package com.zemoso.demo.controller;

import com.zemoso.demo.constants.ConstantUtils;
import com.zemoso.demo.dto.CustomerDTO;
import com.zemoso.demo.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequestMapping(ConstantUtils.CUSTOMER_PATH)
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String getCustomers(Model model){
        List<CustomerDTO> customers = customerService.getCustomers();
        model.addAttribute("customerDTOList", customers);
        return "/customer/list-customers";
    }

    @GetMapping("/show-add-form")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String showAddForm(Model model){
        if(!model.containsAttribute(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE)) {
            model.addAttribute(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE, new CustomerDTO());
        }
        return "/customer/customer-form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String addCustomer(@Valid @ModelAttribute("customerDTO") CustomerDTO customerDTO, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes){
        if(customerDTO == null) {
            log.debug("Throwing error: CustomerDTO is null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        if(!isValidateCustomer(customerDTO, bindingResult, redirectAttributes)) {
            return ConstantUtils.REDIRECT_ACTION + ConstantUtils.CUSTOMER_PATH + ConstantUtils.CUSTOMER_ADD_FORM_PATH;
        }
        customerService.addCustomer(customerDTO);
        return ConstantUtils.REDIRECT_ACTION + ConstantUtils.CUSTOMER_PATH;
    }

    @GetMapping("/{customerId}/show-update-form")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String showUpdateForm(@PathVariable("customerId") int customerId, Model model){
        if(!model.containsAttribute(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE)) {
            CustomerDTO customerDTO = customerService.getCustomer(customerId);
            if(customerDTO == null){
                log.debug("Throwing error: Customer does not exists");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No Customer with id " + customerId);
            }
            model.addAttribute(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE, customerDTO);
        }
        model.addAttribute("todo", "/update");
        return "/customer/customer-form";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String updateCustomer(@Valid @ModelAttribute(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE) CustomerDTO customerDTO, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes){
        if(customerDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        if(!isValidateCustomer(customerDTO, bindingResult, redirectAttributes)) {
            redirectAttributes.addAttribute("todo", "/update");
            String redirectUrl = ConstantUtils.CUSTOMER_PATH +  "/" + customerDTO.getId() + ConstantUtils.CUSTOMER_UPDATE_FORM_PATH;
            return ConstantUtils.REDIRECT_ACTION + redirectUrl;
        }
        customerService.updateCustomer(customerDTO);
        return ConstantUtils.REDIRECT_ACTION + ConstantUtils.CUSTOMER_PATH;
    }

    private boolean isValidateCustomer(CustomerDTO customerDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if( bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute(ConstantUtils.CUSTOMER_MODEL_ATTRIBUTE, customerDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.customerDTO", bindingResult);
            return false;
        }
        return true;
    }

    @GetMapping("/{customerId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable("customerId") int customerId){
        customerService.deleteCustomer(customerId);
        return ConstantUtils.REDIRECT_ACTION + ConstantUtils.CUSTOMER_PATH;
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public String searchCustomerByName(@RequestParam("searchString") String searchString,
                                  @RequestParam("formAction") String action,
                                  Model model) {
        if(action.equals("Clear")){
            return ConstantUtils.REDIRECT_ACTION + ConstantUtils.CUSTOMER_PATH;
        }
        List<CustomerDTO> customers = customerService.searchCustomerByName(searchString);
        model.addAttribute("customerDTOList", customers);
        model.addAttribute("searchString", searchString);
        return "/customer/list-customers";
    }

}
