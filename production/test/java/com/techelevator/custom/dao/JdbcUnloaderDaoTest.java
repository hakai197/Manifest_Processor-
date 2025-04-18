package com.techelevator.custom.dao;

import com.techelevator.custom.model.Unloader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUnloaderDaoTest extends BaseDaoTest {

    private JdbcUnloaderDao sut;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUnloaderDao(dataSource);
    }

    @Test
    void getUnloaderById_returns_correct_unloader() {
        Unloader unloader = sut.getUnloaderById(1);
        assertNotNull(unloader);
        assertEquals("Erik Lehnsherr", unloader.getName());
    }

    @Test
    void getUnloaders_returns_all_unloaders() {
        List<Unloader> unloaders = sut.getUnloaders();
        assertEquals(1, unloaders.size());
    }

    @Test
    void createUnloader_creates_and_returns_unloader() {
        Unloader newUnloader = new Unloader();
        newUnloader.setName("Test Unloader");
        newUnloader.setShift("1st Shift");
        newUnloader.setEmployeeNumber("TEST123");

        Unloader createdUnloader = sut.createUnloader(newUnloader);
        assertNotNull(createdUnloader.getEmployeeId());

        Unloader retrievedUnloader = sut.getUnloaderById(createdUnloader.getEmployeeId());
        assertEquals("Test Unloader", retrievedUnloader.getName());
    }

    @Test
    void updateUnloader_updates_unloader() {
        Unloader unloader = sut.getUnloaderById(1);
        unloader.setName("Updated Name");

        Unloader updatedUnloader = sut.updateUnloader(unloader);
        assertEquals("Updated Name", updatedUnloader.getName());

        Unloader retrievedUnloader = sut.getUnloaderById(1);
        assertEquals("Updated Name", retrievedUnloader.getName());
    }

    @Test
    void deleteUnloaderById_deletes_unloader() {
        int rowsAffected = sut.deleteUnloaderById(1);
        assertEquals(1, rowsAffected);

        Unloader deletedUnloader = sut.getUnloaderById(1);
        assertNull(deletedUnloader);
    }

    @Test
    void getAllUnloaders_returns_all_unloaders_sorted_by_name() {
        jdbcTemplate.update("INSERT INTO unloader (name, shift, employee_number) VALUES ('Adam Smith', '1st Shift', '12345')");

        List<Unloader> unloaders = sut.getAllUnloaders();
        assertEquals(2, unloaders.size());
        assertEquals("Adam Smith", unloaders.get(0).getName());
    }
}