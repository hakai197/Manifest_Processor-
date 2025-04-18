package com.techelevator.custom.dao;

import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.Customer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcCustomerDao implements CustomerDao {
        //Switch to mapper
    private static final String CUSTOMER_SELECT = "SELECT c.customer_id, c.order_number, c.name, c.address, " +
            "c.city, c.state, c.zip_code, c.door_number, c.trailer_id, c.handling_unit, c.weight FROM customer c ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcCustomerDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Customer getCustomerById(int id) {
        Customer customer = null;
        String sql = CUSTOMER_SELECT + "WHERE c.customer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                customer = mapRowToCustomer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return customer;
    }

    @Override
    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(CUSTOMER_SELECT);
            while (results.next()) {
                customers.add(mapRowToCustomer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return customers;
    }

    @Override
    public List<Customer> getCustomersByTrailerId(int trailerId) {
        List<Customer> customers = new ArrayList<>();
        String sql = CUSTOMER_SELECT + "WHERE c.trailer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, trailerId);
            while (results.next()) {
                customers.add(mapRowToCustomer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return customers;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        String sql = "INSERT INTO customer (order_number, name, address, city, " +
                "state, zip_code, door_number, trailer_id, handling_unit, weight) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING customer_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    customer.getOrderNumber(), customer.getName(), customer.getAddress(),
                    customer.getCity(), customer.getState(), customer.getZipCode(),
                    customer.getDoorNumber(), customer.getTrailerId(),
                    customer.getHandlingUnit(), customer.getWeight());
            if (newId != null) {
                return getCustomerById(newId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        String sql = "UPDATE customer SET order_number = ?, name = ?, address = ?, " +
                "city = ?, state = ?, zip_code = ?, door_number = ?, trailer_id = ?, " +
                "handling_unit = ?, weight = ? WHERE customer_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, customer.getOrderNumber(),
                    customer.getName(), customer.getAddress(), customer.getCity(),
                    customer.getState(), customer.getZipCode(), customer.getDoorNumber(),
                    customer.getTrailerId(), customer.getHandlingUnit(),
                    customer.getWeight(), customer.getCustomerId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            return getCustomerById(customer.getCustomerId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public int deleteCustomerById(int id) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public Customer deleteCustomerByName(String name) {
        return null;
    }

    @Override
    public Customer getByName(String name) {
        Customer customer = null;
        String sql = CUSTOMER_SELECT + "WHERE LOWER(c.name) = LOWER(?)";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);
            if (results.next()) {
                customer = mapRowToCustomer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return customer;
    }

    @Override
    public void create(Customer customer) {
        String sql = "INSERT INTO customer (order_number, name, address, city, " +
                "state, zip_code, door_number, trailer_id, handling_unit, weight) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                    customer.getOrderNumber(),
                    customer.getName(),
                    customer.getAddress(),
                    customer.getCity(),
                    customer.getState(),
                    customer.getZipCode(),
                    customer.getDoorNumber(),
                    customer.getTrailerId(),
                    customer.getHandlingUnit(),
                    customer.getWeight());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public List<Customer> getCustomersByTrailer(String trailerNumber) {
        List<Customer> customers = new ArrayList<>();
        String sql = CUSTOMER_SELECT + "WHERE c.trailer_id = (SELECT trailer_id FROM trailer WHERE trailer_number = ?)";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, trailerNumber);
            while (results.next()) {
                customers.add(mapRowToCustomer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return customers;
    }

    @Override
    public Integer getSuggestedDoor(String trailerNumber) {
        return null;
    }


    @Override
    public String getManifestForTrailer(String trailerNumber) {
        String sql = CUSTOMER_SELECT +
                "WHERE c.trailer_id = (SELECT trailer_id FROM trailer WHERE trailer_number = ?) " +
                "ORDER BY c.name";

        String header = String.format("%-30s %-10s %-10s %-15s\n",
                "Customer", "HU", "Weight", "Door");
        String divider = "------------------------------------------------------------\n";
        String manifest = header + divider;

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, trailerNumber);

            while (results.next()) {
                Customer customer = mapRowToCustomer(results);
                manifest += String.format("%-30s %-10d %-10d %-15s\n",
                        customer.getName(),
                        customer.getHandlingUnit(),
                        customer.getWeight(),
                        customer.getDoorNumber());
            }

            sql = "SELECT SUM(handling_unit) as total_hu, SUM(weight) as total_weight " +
                    "FROM customer WHERE trailer_id = (SELECT trailer_id FROM trailer WHERE trailer_number = ?)";
            results = jdbcTemplate.queryForRowSet(sql, trailerNumber);

            if (results.next()) {
                int totalHU = results.getInt("total_hu");
                int totalWeight = results.getInt("total_weight");
                manifest += "\n";
                manifest += String.format("%-30s %-10d %-10d\n",
                        "TOTALS:", totalHU, totalWeight);

                boolean isHeadLoad = (totalHU >= 8 || totalWeight >= 10000);
                manifest += "\nHEADLOAD STATUS: " + (isHeadLoad ? "YES" : "NO");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return manifest;
    }

    private Customer mapRowToCustomer(SqlRowSet results) {
        Customer customer = new Customer();
        customer.setCustomerId(results.getInt("customer_id"));
        customer.setOrderNumber(results.getString("order_number"));
        customer.setName(results.getString("name"));
        customer.setAddress(results.getString("address"));
        customer.setCity(results.getString("city"));
        customer.setState(results.getString("state"));
        customer.setZipCode(results.getString("zip_code"));
        customer.setDoorNumber(results.getString("door_number"));
        customer.setTrailerId(results.getInt("trailer_id"));
        customer.setHandlingUnit(results.getInt("handling_unit"));
        customer.setWeight(results.getInt("weight"));
        return customer;
    }
}


