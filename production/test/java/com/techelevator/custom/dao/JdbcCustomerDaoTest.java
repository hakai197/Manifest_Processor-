package com.techelevator.custom.dao;

import com.techelevator.custom.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcCustomerDaoTest extends BaseDaoTest {

    private JdbcCustomerDao sut;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcCustomerDao(dataSource);
    }

    @Test
    void getCustomerById_returns_correct_customer() {
        Customer customer = sut.getCustomerById(1);
        assertNotNull(customer);
        assertEquals("Alec Holland", customer.getName());
        assertEquals("4887500387", customer.getOrderNumber());
    }

    @Test
    void getCustomers_returns_all_customers() {
        List<Customer> customers = sut.getCustomers();
        assertEquals(3, customers.size());
    }

    @Test
    void getCustomersByTrailerId_returns_correct_customers() {
        List<Customer> customers = sut.getCustomersByTrailerId(1);
        assertEquals(3, customers.size());
        assertEquals("Alec Holland", customers.get(0).getName());
    }

    @Test
    void createCustomer_creates_and_returns_customer() {
        Customer newCustomer = new Customer();
        newCustomer.setOrderNumber("NEW123");
        newCustomer.setName("Test Customer");
        newCustomer.setAddress("123 Test St");
        newCustomer.setCity("Testville");
        newCustomer.setState("TS");
        newCustomer.setZipCode("12345");
        newCustomer.setDoorNumber("Door 1");
        newCustomer.setTrailerId(1);
        newCustomer.setHandlingUnit(5);
        newCustomer.setWeight(1000);

        Customer createdCustomer = sut.createCustomer(newCustomer);
        assertNotNull(createdCustomer.getCustomerId());

        Customer retrievedCustomer = sut.getCustomerById(createdCustomer.getCustomerId());
        assertEquals("Test Customer", retrievedCustomer.getName());
        assertEquals("NEW123", retrievedCustomer.getOrderNumber());
    }

    @Test
    void updateCustomer_updates_customer() {
        Customer customer = sut.getCustomerById(1);
        customer.setName("Updated Name");

        Customer updatedCustomer = sut.updateCustomer(customer);
        assertEquals("Updated Name", updatedCustomer.getName());

        Customer retrievedCustomer = sut.getCustomerById(1);
        assertEquals("Updated Name", retrievedCustomer.getName());
    }

    @Test
    void deleteCustomerById_deletes_customer() {
        int rowsAffected = sut.deleteCustomerById(1);
        assertEquals(1, rowsAffected);

        Customer deletedCustomer = sut.getCustomerById(1);
        assertNull(deletedCustomer);
    }

    @Test
    void getByName_returns_correct_customer() {
        Customer customer = sut.getByName("Alec Holland");
        assertNotNull(customer);
        assertEquals(1, customer.getCustomerId());
    }

    @Test
    void getCustomersByTrailer_returns_customers_for_trailer_number() {
        List<Customer> customers = sut.getCustomersByTrailer("OF4328");
        assertFalse(customers.isEmpty());
        assertEquals("Alec Holland", customers.get(0).getName());
    }

    @Test
    void getManifestForTrailer_returns_formatted_manifest() {
        String manifest = sut.getManifestForTrailer("OF4328");
        assertNotNull(manifest);
        assertTrue(manifest.contains("Alec Holland"));
        assertTrue(manifest.contains("TOTALS:"));
    }
}