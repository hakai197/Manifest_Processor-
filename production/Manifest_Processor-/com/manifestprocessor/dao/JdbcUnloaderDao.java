package com.techelevator.custom.dao;

import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.Unloader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcUnloaderDao implements UnloaderDao {
    //Switch to mapper. 
    private static final String UNLOADER_SELECT = "SELECT u.employee_id, u.name, u.shift, u.employee_number FROM unloader u ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcUnloaderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Unloader getUnloaderById(int id) {
        Unloader unloader = null;
        String sql = UNLOADER_SELECT + "WHERE u.employee_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                unloader = mapRowToUnloader(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return unloader;
    }

    @Override
    public List<Unloader> getUnloaders() {
        List<Unloader> unloaders = new ArrayList<>();
        String sql = UNLOADER_SELECT;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                unloaders.add(mapRowToUnloader(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return unloaders;
    }

    @Override
    public Unloader createUnloader(Unloader unloader) {
        String sql = "INSERT INTO unloader (name, shift, employee_number) " +
                "VALUES (?, ?, ?) RETURNING employee_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    unloader.getName(), unloader.getShift(), unloader.getEmployeeNumber());
            if (newId != null) {
                return getUnloaderById(newId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;
    }

    @Override
    public Unloader updateUnloader(Unloader unloader) {
        String sql = "UPDATE unloader SET name = ?, shift = ?, employee_number = ? " +
                "WHERE employee_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, unloader.getName(),
                    unloader.getShift(), unloader.getEmployeeNumber(),
                    unloader.getEmployeeId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            return getUnloaderById(unloader.getEmployeeId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public int deleteUnloaderById(int id) {
        String sql = "DELETE FROM unloader WHERE employee_id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public List<Unloader> getAllUnloaders() {
        List<Unloader> unloaders = new ArrayList<>();
        String sql = UNLOADER_SELECT + "ORDER BY u.name";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                unloaders.add(mapRowToUnloader(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return unloaders;
    }

    private Unloader mapRowToUnloader(SqlRowSet results) {
        Unloader unloader = new Unloader();
        unloader.setEmployeeId(results.getInt("employee_id"));
        unloader.setName(results.getString("name"));
        unloader.setShift(results.getString("shift"));
        unloader.setEmployeeNumber(results.getString("employee_number"));
        return unloader;
    }
}

