package com.techelevator.custom.dao;

import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.Trailer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcTrailerDao implements TrailerDao {
    //Switch to mapper.
    private final String TRAILER_SELECT = "SELECT t.trailer_id, t.trailer_number, t.trailer_type, t.shipper_id FROM trailer t ";
    private final JdbcTemplate jdbcTemplate;

    public JdbcTrailerDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Trailer getTrailerById(int id) {
        Trailer trailer = null;
        String sql = TRAILER_SELECT + "WHERE t.trailer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                trailer = mapRowToTrailer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return trailer;
    }

    @Override
    public List<Trailer> getTrailers() {
        List<Trailer> trailers = new ArrayList<>();
        String sql = TRAILER_SELECT;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                trailers.add(mapRowToTrailer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return trailers;
    }

    @Override
    public List<Trailer> getTrailersByShipperId(int shipperId) {
        List<Trailer> trailers = new ArrayList<>();
        String sql = TRAILER_SELECT + "WHERE t.shipper_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, shipperId);
            while (results.next()) {
                trailers.add(mapRowToTrailer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return trailers;
    }

    @Override
    public Trailer createTrailer(Trailer trailer) {
        String sql = "INSERT INTO trailer (trailer_number, trailer_type, shipper_id) " +
                "VALUES (?, ?, ?) RETURNING trailer_id";
        try {
            Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                    trailer.getTrailerNumber(), trailer.getTrailerType(),
                    trailer.getShipperId());
            if (newId != null) {
                return getTrailerById(newId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;
    }

    @Override
    public Trailer updateTrailer(Trailer trailer) {
        String sql = "UPDATE trailer SET trailer_number = ?, trailer_type = ?, " +
                "shipper_id = ? WHERE trailer_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, trailer.getTrailerNumber(),
                    trailer.getTrailerType(), trailer.getShipperId(),
                    trailer.getTrailerId());
            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            }
            return getTrailerById(trailer.getTrailerId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public int deleteTrailerById(int id) {
        String checkCustomersSql = "SELECT COUNT(*) FROM customer WHERE trailer_id = ?";
        String deleteSql = "DELETE FROM trailer WHERE trailer_id = ?";
        try {
            int customerCount = jdbcTemplate.queryForObject(checkCustomersSql, Integer.class, id);
            if (customerCount > 0) {
                throw new DaoException("Cannot delete trailer with assigned customers");
            }
            return jdbcTemplate.update(deleteSql, id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }}
    @Override
    public Trailer getTrailerByNumber(String trailerNumber) {
        Trailer trailer = null;
        String sql = TRAILER_SELECT + "WHERE t.trailer_number = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, trailerNumber);
            if (results.next()) {
                trailer = mapRowToTrailer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return trailer;
    }

    private Trailer mapRowToTrailer(SqlRowSet results) {
        Trailer trailer = new Trailer();
        trailer.setTrailerId(results.getInt("trailer_id"));
        trailer.setTrailerNumber(results.getString("trailer_number"));
        trailer.setTrailerType(results.getString("trailer_type"));
        trailer.setShipperId(results.getInt("shipper_id"));
        return trailer;
    }
}