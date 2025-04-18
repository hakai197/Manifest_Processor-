package com.techelevator.custom.dao;

import com.techelevator.custom.model.Trailer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTrailerDaoTest extends BaseDaoTest {

    private JdbcTrailerDao dao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTrailerDao(dataSource);
    }

    @Test
    void getTrailerById_returns_correct_trailer() {
        Trailer trailer = dao.getTrailerById(1);
        assertNotNull(trailer);
        assertEquals("OF4328", trailer.getTrailerNumber()); // Match test data
    }

    @Test
    void getTrailers_returns_all_trailers() {
        List<Trailer> trailers = dao.getTrailers();
        assertEquals(1, trailers.size()); // One trailer in test data
    }

    @Test
    void getTrailersByShipperId_returns_correct_trailers() {
        List<Trailer> trailers = dao.getTrailersByShipperId(1);
        assertFalse(trailers.isEmpty());
        assertEquals("OF4328", trailers.get(0).getTrailerNumber());
    }

    @Test
    void createTrailer_creates_and_returns_trailer() {
        Trailer newTrailer = new Trailer();
        newTrailer.setTrailerNumber("TEST123");
        newTrailer.setTrailerType("Test Type");
        newTrailer.setShipperId(1);

        Trailer createdTrailer = dao.createTrailer(newTrailer);
        assertNotNull(createdTrailer.getTrailerId());

        Trailer retrievedTrailer = dao.getTrailerById(createdTrailer.getTrailerId());
        assertEquals("TEST123", retrievedTrailer.getTrailerNumber());
    }

    @Test
    void updateTrailer_updates_trailer() {
        Trailer trailer = dao.getTrailerById(1);
        trailer.setTrailerNumber("UPDATED123");

        Trailer updatedTrailer = dao.updateTrailer(trailer);
        assertEquals("UPDATED123", updatedTrailer.getTrailerNumber());

        Trailer retrievedTrailer = dao.getTrailerById(1);
        assertEquals("UPDATED123", retrievedTrailer.getTrailerNumber());
    }

    @Test
    void deleteTrailerById_deletes_trailer() {
        jdbcTemplate.update("DELETE FROM customer WHERE trailer_id = 1"); // Adjust to match test data

        int rowsAffected = dao.deleteTrailerById(1);
        assertEquals(1, rowsAffected);

        Trailer deletedTrailer = dao.getTrailerById(1);
        assertNull(deletedTrailer);
    }

    @Test
    void getTrailerByNumber_returns_correct_trailer() {
        Trailer trailer = dao.getTrailerByNumber("OF4328"); // Match test data
        assertNotNull(trailer);
        assertEquals(1, trailer.getTrailerId());
    }
}