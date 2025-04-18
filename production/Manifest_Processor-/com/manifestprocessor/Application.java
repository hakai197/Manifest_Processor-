package com.techelevator.custom;

import com.techelevator.custom.dao.*;
import com.techelevator.util.BasicConsole;
import com.techelevator.util.SystemInOutConsole;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Application is the class that launches your custom application by creating
 * the objects needed to interact with the user and file system and passing them to
 * the application's controller object.
 */

public class Application {
//Still a bit clunky but it works.
    public static void main(String[] args) {
        // Create the datasource used by all the DAOs
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/ManifestDB");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        // Controller expects the DAOs it needs to be "injected" in the constructor.
        // Create the DAOs needed by the controller.
        //*****************************************************************************************
        // TODO: When you implement a new DAO, create an instance of it here, replacing the NULL
        // TODO: For example  -  ProductDao productDao = new JdbcProductDao(datasource);
        //*****************************************************************************************
        CustomerDao customerDao = new JdbcCustomerDao(dataSource);
        TrailerDao trailerDao = new JdbcTrailerDao(dataSource);
        UnloaderDao unloaderDao = new JdbcUnloaderDao(dataSource);
        ShipperDao shipperDao = new JdbcShipperDao(dataSource);

        // Create the basic i/o mechanism (the console)
        SystemInOutConsole systemInOutConsole = new SystemInOutConsole();

        // The controller manages the program flow. Create a control and call its run() method to start the menu loop.
        // TODO: Pass your DAO instances into the ApplicationController constructor
        ApplicationController controller =
                new ApplicationController((BasicConsole) systemInOutConsole, (JdbcCustomerDao) customerDao, (JdbcTrailerDao) trailerDao,unloaderDao, (JdbcShipperDao) shipperDao);
        controller.run();
    }
}
