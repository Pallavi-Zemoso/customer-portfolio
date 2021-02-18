package com.zemoso.demo.dao.impl;

import com.zemoso.demo.entity.Customer;
import com.zemoso.demo.dao.CustomerDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

//@Repository
public class CustomerDAOImpl implements CustomerDAO {
    private static final Logger logger = LogManager.getLogger(CustomerDAOImpl.class);
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Customer> getCustomers() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("from Customer order by firstName,lastName", Customer.class);
        List<Customer> customers = query.getResultList();
        return customers;
    }

    @Override
    public void saveCustomer(Customer customer) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(customer);
    }

    @Override
    public Customer getCustomer(int customerId) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Customer.class, customerId);
    }

    @Override
    public void deleteCustomer(int customerId) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.createQuery(" delete from Customer where id = :customerId")
                .setParameter("customerId", customerId)
                .executeUpdate();
    }

    @Override
    public List<Customer> searchCustomerByName(String searchString) {
        Session currentSession = sessionFactory.getCurrentSession();

        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
        Root<Customer> customerRoot = criteriaQuery.from(Customer.class);
        Predicate firstNameMatched = builder.like(customerRoot.get("firstName"), "%" + searchString + "%");
        Predicate lastNameMatched = builder.like(customerRoot.get("lastName"), "%" + searchString + "%");
        criteriaQuery.where( builder.or(firstNameMatched, lastNameMatched));

        List<Customer> customers = currentSession.createQuery(criteriaQuery.select(customerRoot)).getResultList();

        /*
        Query theQuery =currentSession.createQuery("from Customer where firstName like :theName or lastName like :theName", Customer.class);
        theQuery.setParameter("theName",  searchString );
        List<Customer> customers = theQuery.getResultList();
        */
        return customers;
    }
}
