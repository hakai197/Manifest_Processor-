package com.techelevator.custom.dao;

import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.Shipper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcShipperDao implements ShipperDao {
    //Switch to mapper
    private static final String SHIPPER_SELECT = "SELECT s.shipper_id, s.name, s.address, s.city, s.state, s.zip_code FROM shipper s ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcShipperDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Shipper getShipperById(int id) {
        Shipper shipper = null;
        String sql = SHIPPER_SELECT + "WHERE s.shipper_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                shipper = mapRowToShipper(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return shipper;
    }

    @Override
    public List<Shipper> getShippers() {
        List<Shipper> shippers = new ArrayList<>();
        String sql = SHIPPER_SELECT;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                shippers.add(mapRowToShipper(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return shippers;
    }

    @Override
    public Shipper createShipper(Shipper shipper) {
        String sql = "INSERT INTO shipper (name, address, city, state, zip_code) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING shipper_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    shipper.getName(), shipper.getAddress(), shipper.getCity(),
                    shipper.getState(), shipper.getZipCode());
            if (newId != null) {
                return getShipperById(newId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;
    }

    @Override
    public Shipper updateShipper(Shipper shipper) {
        String sql = "UPDATE shipper SET name = ?, address = ?, city = ?, " +
                "state = ?, zip_code = ? WHERE shipper_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, shipper.getName(),
                    shipper.getAddress(), shipper.getCity(), shipper.getState(),
                    shipper.getZipCode(), shipper.getShipperId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            return getShipperById(shipper.getShipperId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public int deleteShipperById(int id) {
        String nullifyShipperSql = "UPDATE trailer SET shipper_id = NULL WHERE shipper_id = ?";
        String deleteShipperSql = "DELETE FROM shipper WHERE shipper_id = ?";

        try {

            jdbcTemplate.update(nullifyShipperSql, id);


            return jdbcTemplate.update(deleteShipperSql, id);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public Shipper getByName(String name) {
        Shipper shipper = null;
        String sql = SHIPPER_SELECT + "WHERE LOWER(s.name) = LOWER(?)";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);
            if (results.next()) {
                shipper = mapRowToShipper(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return shipper;
    }

    @Override
    public void create(Shipper shipper) {
        String sql = "INSERT INTO shipper (name, address, city, state, zip_code) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                    shipper.getName(),
                    shipper.getAddress(),
                    shipper.getCity(),
                    shipper.getState(),
                    shipper.getZipCode());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Shipper mapRowToShipper(SqlRowSet results) {
        Shipper shipper = new Shipper();
        shipper.setShipperId(results.getInt("shipper_id"));
        shipper.setName(results.getString("name"));
        shipper.setAddress(results.getString("address"));
        shipper.setCity(results.getString("city"));
        shipper.setState(results.getString("state"));
        shipper.setZipCode(results.getString("zip_code"));
        return shipper;
    }
}