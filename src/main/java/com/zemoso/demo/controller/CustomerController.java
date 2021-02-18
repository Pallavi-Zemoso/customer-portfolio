package com.zemoso.demo.controller;

import com.zemoso.demo.entity.Customer;
import com.zemoso.demo.service.CustomerService;
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
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String getCustomers(Model model){
        List<Customer> customers = customerService.getCustomers();
        model.addAttribute("customerList", customers);
        return "/customer/list-customers";
    }

    @GetMapping("/show-add-form")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String showAddForm(Model model){
        if(!model.containsAttribute("customer")) {
            model.addAttribute("customer", new Customer());
        }
        return "/customer/customer-form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String addCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes){
        if(customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        if(!validateCustomer(customer, bindingResult, redirectAttributes)) {
            return "redirect:/customer/showAddForm";
        }
        customerService.addCustomer(customer);
        System.out.println("Adding customer");
        return "redirect:/customer";
    }

    @GetMapping("/{customerId}/show-update-form")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String showUpdateForm(@PathVariable("customerId") int customerId, Model model){
        if(!model.containsAttribute("customer")) {
            Customer customer = customerService.getCustomer(customerId);
            if(customer == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No Customer with id " + customerId);
            }
            model.addAttribute("customer", customer);
        }
        model.addAttribute("todo", "/update");
        return "/customer/customer-form";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String updateCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes){
        if(customer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        if(!validateCustomer(customer, bindingResult, redirectAttributes)) {
            redirectAttributes.addAttribute("todo", "/update");
            String redirectUrl = "/customer/" + customer.getId() + "/showUpdateForm";
            System.out.println(redirectUrl);
            return "redirect:"+redirectUrl;
        }
        System.out.println("Update Customer");
        customerService.updateCustomer(customer);
        return "redirect:/customer";
    }

    private boolean validateCustomer(Customer customer, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if( bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("customer", customer);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.customer", bindingResult);
            return false;
        }
        return true;
    }

    @GetMapping("/{customerId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable("customerId") int customerId){
        customerService.deleteCustomer(customerId);
        return "redirect:/customer";
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public String searchCustomerByName(@RequestParam("searchString") String searchString,
                                  @RequestParam("formAction") String action,
                                  Model model) {
        if(action.equals("Clear")){
            return "redirect:/customer";
        }
        List<Customer> customers = customerService.searchCustomerByName(searchString);
        System.out.println("Search result count: " + customers.size());
        model.addAttribute("customerList", customers);
        model.addAttribute("searchString", searchString);
        return "/customer/list-customers";
    }

}
