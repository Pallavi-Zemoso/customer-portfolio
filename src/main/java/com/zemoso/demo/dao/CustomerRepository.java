package com.zemoso.demo.dao;

import java.util.List;

import com.zemoso.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	// add a method to sort by last name
	public List<Customer> findAllByOrderByLastNameAsc();
	
	// search by name
	public List<Customer> findByFirstNameContainsOrLastNameContainsAllIgnoreCase(String name, String lName);
}
