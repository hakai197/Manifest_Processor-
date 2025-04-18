package com.techelevator.custom.dao;

import com.techelevator.custom.model.Shipper;

import java.util.List;

public interface ShipperDao {

    Shipper getShipperById(int id);

    List<Shipper> getShippers();

    Shipper createShipper(Shipper shipper);

    Shipper updateShipper(Shipper shipper);

    int deleteShipperById(int id);


    Shipper getByName(String name);

    void create(Shipper shipper);
}
