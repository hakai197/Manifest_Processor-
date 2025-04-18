package com.techelevator.custom.dao;

import com.techelevator.custom.model.Shipper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcShipperDaoTest extends BaseDaoTest {

    private JdbcShipperDao dao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcShipperDao(dataSource);
    }

    @Test
    void getShipperById_returns_correct_shipper() {
        Shipper shipper = dao.getShipperById(1);
        assertNotNull(shipper);
        assertEquals("R&L Carriers", shipper.getName());
    }

    @Test
    void getShippers_returns_all_shippers() {
        List<Shipper> shippers = dao.getShippers();
        assertTrue(shippers.size() >= 1);
    }

    @Test
    void createShipper_creates_and_returns_shipper() {
        Shipper newShipper = new Shipper();
        newShipper.setName("Test Shipper");
        newShipper.setAddress("123 Test St");
        newShipper.setCity("Testville");
        newShipper.setState("TS");
        newShipper.setZipCode("12345");

        Shipper createdShipper = dao.createShipper(newShipper);
        assertNotNull(createdShipper.getShipperId());

        Shipper retrievedShipper = dao.getShipperById(createdShipper.getShipperId());
        assertEquals("Test Shipper", retrievedShipper.getName());
    }

    @Test
    void updateShipper_updates_shipper() {
        Shipper shipper = dao.getShipperById(1);
        shipper.setName("Updated Name");

        Shipper updatedShipper = dao.updateShipper(shipper);
        assertEquals("Updated Name", updatedShipper.getName());

        Shipper retrievedShipper = dao.getShipperById(1);
        assertEquals("Updated Name", retrievedShipper.getName());
    }



    @Test
    void getByName_returns_correct_shipper() {
        Shipper shipper = dao.getByName("R&L Carriers");
        assertNotNull(shipper);
        assertEquals(1, shipper.getShipperId());
    }
}