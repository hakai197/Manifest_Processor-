package com.techelevator.custom.dao;

import com.techelevator.custom.model.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerById(int id);

    List<Customer> getCustomers();

    List<Customer> getCustomersByTrailerId(int trailerId);


    Customer createCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    int deleteCustomerById(int id);

   Customer deleteCustomerByName (String name);


    Customer getByName(String name);


    void create(Customer customer);

    List<Customer> getCustomersByTrailer(String trailerNumber);

    Integer getSuggestedDoor(String trailerNumber);

    String getManifestForTrailer(String trailerNumber);
}
