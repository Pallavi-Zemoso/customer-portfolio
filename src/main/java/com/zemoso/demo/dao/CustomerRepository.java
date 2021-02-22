package com.zemoso.demo.dao;

import com.zemoso.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	// search by name
	List<Customer> findByFirstNameContainsOrLastNameContainsAllIgnoreCase(String name, String lName);
}
