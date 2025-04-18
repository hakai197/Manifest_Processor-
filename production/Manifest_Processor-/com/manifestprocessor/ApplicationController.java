package com.techelevator.custom;

import com.techelevator.custom.dao.*;
import com.techelevator.custom.exception.DaoException;
import com.techelevator.custom.model.Customer;
import com.techelevator.custom.model.Shipper;
import com.techelevator.custom.model.Trailer;
import com.techelevator.util.BasicConsole;

import java.util.List;

/**
 * ApplicationController orchestrates all of its operations through a series of menus. It relies
 * on other classes for the details of interacting with the user and with the database.
 */
public class ApplicationController {

    // The view manages all the user interaction, inputs and outputs.
    private final ApplicationView view;

    // The controller expects these DAO's to be "injected" into via its constructor
    private final JdbcCustomerDao customerDao;
    private final JdbcTrailerDao trailerDao;
    private final UnloaderDao unloaderDao;
    private final JdbcShipperDao shipperDao;
    private final ManifestProcessor manifestProcessor;


    public ApplicationController(BasicConsole console, JdbcCustomerDao customerDao,
                                 JdbcTrailerDao trailerDao, UnloaderDao unloaderDao,
                                 JdbcShipperDao shipperDao) {
        view = new ApplicationView(console);
        this.customerDao = customerDao;
        this.trailerDao = trailerDao;
        this.unloaderDao = unloaderDao;
        this.shipperDao = shipperDao;
        this.manifestProcessor = new ManifestProcessor(customerDao, trailerDao, unloaderDao);
    }

    /**
     * The run() method starts the program flow by running the main menu.
     */
    public void run() {
        displayMainMenu();
    }

    /**
     * A loop which displays the main program menu, and responds to the user's selection
     */
    private void displayMainMenu() {
        // Menu options
        final String SUB_MENU_1 = "Assign Door to Trailer";
        final String SUB_MENU_2 = "Release Door";
        final String SUB_MENU_3 = "View Manifest";
        final String SUB_MENU_4 = "Customer Management";
        final String SUB_MENU_5 = "Shipper Management";
        final String SUB_MENU_6 = "Trailer Management";
        final String EXIT = "Exit the program";
        final String[] MENU_OPTIONS = {SUB_MENU_1, SUB_MENU_2, SUB_MENU_3,
                SUB_MENU_4, SUB_MENU_5, SUB_MENU_6, EXIT};

        boolean finished = false;

        while (!finished) {
            String selection = view.getMenuSelection("Door Management System", MENU_OPTIONS);
            switch (selection) {
                case SUB_MENU_1:
                    displayTrailerAssignmentMenu();
                    break;
                case SUB_MENU_2:
                    releaseDoor();
                    break;
                case SUB_MENU_3:
                    viewManifest();
                    break;
                case SUB_MENU_4:
                    displayCustomerManagementMenu();
                    break;
                case SUB_MENU_5:
                    displayShipperManagementMenu();
                    break;
                case SUB_MENU_6:
                    displayTrailerManagementMenu();
                    break;
                case EXIT:
                    finished = true;
                    view.printMessage("Exiting program. Goodbye!");
                    break;
            }
        }
    }


    private void displayTrailerAssignmentMenu() {
        // Menu options
        final String OPTION_1 = "Assign door to Trailer";
        final String OPTION_2 = "Check Headload Status";
        final String OPTION_3 = "Check Available Doors";
        final String DONE = "Main menu";
        final String[] MENU_OPTIONS = {OPTION_1, OPTION_2, OPTION_3, DONE};

        boolean finished = false;

        // The menu loop
        while (!finished) {
            String selection = view.getMenuSelection("Trailer Assignment Menu", MENU_OPTIONS);
            try {
                switch (selection) {
                    case OPTION_1:
                        assignDoorToTrailer();
                        break;
                    case OPTION_2:
                        checkHeadloadStatus();
                        break;
                    case OPTION_3:
                        checkAvailableDoors();
                        break;
                    case DONE:
                        // Set finished to true so the loop exits.
                        finished = true;
                        break;
                }
            } catch (DaoException e) {
                view.printErrorMessage("DAO error - " + e.getMessage());
            }
        }
    }


    private void assignDoorToTrailer() throws DaoException {
        String trailerNumber = view.promptForString("Enter trailer number");
        manifestProcessor.assignDoorToTrailer((BasicConsole) view, trailerNumber);
    }


    private void checkHeadloadStatus() throws DaoException {
        String trailerNumber = view.promptForString("Enter trailer number to check headload status");
        boolean isHeadLoad = manifestProcessor.isHeadLoad(trailerNumber);
        view.printMessage("Trailer " + trailerNumber + " is " + (isHeadLoad ? "a HEAD LOAD." : "not a head load."));
    }


    private void checkAvailableDoors() throws DaoException {
        view.printMessage("Available doors: " + manifestProcessor.getAvailableDoors());
    }

    private void releaseDoor() {
        int doorNumber = view.promptForInt("Enter door number to release");
        try {
            boolean success = manifestProcessor.releaseDoor(doorNumber);
            if (success) {
                view.printMessage("Door " + doorNumber + " released successfully.");
            } else {
                view.printErrorMessage("That door is not currently assigned.");
            }
        } catch (DaoException e) {
            view.printErrorMessage("Error releasing door: " + e.getMessage());
        }
    }


    private void viewManifest() {
        String trailerNumber = view.promptForString("Enter trailer number");
        try {
            String manifest = customerDao.getManifestForTrailer(trailerNumber);
            view.printMessage(manifest);
        } catch (DaoException e) {
            view.printErrorMessage("Error retrieving manifest: " + e.getMessage());
        }
    }

    private void displayShipperManagementMenu() {
        // Menu options
        final String LIST_SHIPPERS = "List All Shippers";
        final String ADD_SHIPPER = "Add New Shipper";
        final String DELETE_SHIPPER = "Delete Shipper";
        final String DONE = "Return to Main Menu";
        final String[] MENU_OPTIONS = {LIST_SHIPPERS, ADD_SHIPPER, DELETE_SHIPPER, DONE};

        boolean finished = false;

        // The menu loop
        while (!finished) {
            String selection = view.getMenuSelection("Shipper Management", MENU_OPTIONS);
            try {
                switch (selection) {
                    case LIST_SHIPPERS:
                        listAllShippers();
                        break;
                    case ADD_SHIPPER:
                        addNewShipper();
                        break;
                    case DELETE_SHIPPER:
                        deleteShipper();
                        break;
                    case DONE:
                        // Set finished to true so the loop exits.
                        finished = true;
                        break;
                }
            } catch (DaoException e) {
                view.printErrorMessage("DAO error - " + e.getMessage());
            }
        }
    }
    private void listAllShippers() throws DaoException {
        List<Shipper> shippers = shipperDao.getShippers();
        if (shippers.isEmpty()) {
            view.printMessage("No shippers found in the database.");
        } else {

            view.printMessage(String.format(
                    "%-5s %-25s %-30s %-20s %-5s %-10s",
                    "ID", "Name", "Address", "City", "ST", "Zip"
            ));
            view.printMessage("========================================================================================================================================================");

            for (Shipper shipper : shippers) {
                String shipperLine = String.format(
                        "%-5d %-25s %-30s %-20s %-5s %-10s",
                        shipper.getShipperId(),
                        shipper.getName(),
                        shipper.getAddress(),
                        shipper.getCity(),
                        shipper.getState(),
                        shipper.getZipCode()
                );
                view.printMessage(shipperLine);
            }
        }
    }



    private void addNewShipper() throws DaoException {
        Shipper newShipper = new Shipper();
        newShipper.setName(view.promptForString("Enter shipper name"));
        newShipper.setAddress(view.promptForString("Enter address"));
        newShipper.setCity(view.promptForString("Enter city"));
        newShipper.setState(view.promptForString("Enter state"));
        newShipper.setZipCode(view.promptForString("Enter zip code"));

        Shipper createdShipper = shipperDao.createShipper(newShipper);
        if (createdShipper != null) {
            view.printMessage("Successfully created shipper with ID: " + createdShipper.getShipperId());
        } else {
            view.printErrorMessage("Failed to create shipper");
        }
    }


    private void deleteShipper() throws DaoException {

        int shipperId = view.promptForInt("Enter shipper ID to delete");
        int rowsAffected = shipperDao.deleteShipperById(shipperId);

        if (rowsAffected > 0) {
            view.printMessage("Successfully deleted shipper with ID: " + shipperId);
        } else {
            view.printErrorMessage("No shipper found with ID: " + shipperId);
        }
    }
    private void displayCustomerManagementMenu() {
        final String ADD_CUSTOMER = "Add New Customer";
        final String UPDATE_CUSTOMER = "Update Customer";
        final String LIST_CUSTOMERS = "List All Customers";
        final String DELETE_CUSTOMER = "Delete Customer";
        final String DONE = "Return to Main Menu";
        final String[] MENU_OPTIONS = {ADD_CUSTOMER, UPDATE_CUSTOMER, LIST_CUSTOMERS, DELETE_CUSTOMER, DONE};

        boolean finished = false;

        while (!finished) {
            String selection = view.getMenuSelection("Customer Management", MENU_OPTIONS);
            try {
                switch (selection) {
                    case ADD_CUSTOMER:
                        addNewCustomer();
                        break;
                    case UPDATE_CUSTOMER:
                        updateCustomer();
                        break;
                    case LIST_CUSTOMERS:
                        listAllCustomers();
                        break;
                    case DELETE_CUSTOMER:
                        deleteCustomer();
                        break;
                    case DONE:
                        finished = true;
                        break;
                }
            } catch (DaoException e) {
                view.printErrorMessage("DAO error - " + e.getMessage());
            }
        }
    }
    private void addNewCustomer() throws DaoException {
        Customer newCustomer = new Customer();

        newCustomer.setName(view.promptForString("Enter customer name"));
        newCustomer.setAddress(view.promptForString("Enter address"));
        newCustomer.setCity(view.promptForString("Enter city"));
        newCustomer.setState(view.promptForString("Enter state"));
        newCustomer.setZipCode(view.promptForString("Enter zip code"));
        String doorNumber = view.promptForString("Enter door number");
        while (doorNumber.isEmpty()) {
            view.printErrorMessage("Door number is required.");
            doorNumber = view.promptForString("Enter door number");
        }
        newCustomer.setDoorNumber(doorNumber);

        String orderNumber = view.promptForString("Enter order number");
        while (orderNumber.isEmpty()) {
            view.printErrorMessage("Order number is required.");
            orderNumber = view.promptForString("Enter order number");
        }
        newCustomer.setOrderNumber(orderNumber);

        String huStr = view.promptForString("Enter handling unit count");
        while (huStr.isEmpty()) {
            view.printErrorMessage("Handling unit count is required.");
            huStr = view.promptForString("Enter handling unit count");
        }
        newCustomer.setHandlingUnit(Integer.parseInt(huStr));

        String weightStr = view.promptForString("Enter weight");
        while (weightStr.isEmpty()) {
            view.printErrorMessage("Weight is required.");
            weightStr = view.promptForString("Enter weight");
        }
        newCustomer.setWeight(Integer.parseInt(weightStr));

        String trailerIdStr = view.promptForString("Enter trailer ID");
        while (trailerIdStr.isEmpty()) {
            view.printErrorMessage("Trailer ID is required.");
            trailerIdStr = view.promptForString("Enter trailer ID");
        }

        try {
            newCustomer.setTrailerId(Integer.parseInt(trailerIdStr));
        } catch (NumberFormatException e) {
            view.printErrorMessage("Invalid trailer ID. Must be a number.");
            return;
        }

        try {
            Customer createdCustomer = customerDao.createCustomer(newCustomer);
            if (createdCustomer != null) {
                view.printMessage("Successfully created customer with ID: " + createdCustomer.getCustomerId());
            } else {
                view.printErrorMessage("Failed to create customer");
            }
        } catch (DaoException e) {
            view.printErrorMessage("***Error creating customer: " + e.getMessage());
        }
    }




    private void listAllCustomers() throws DaoException {
        List<Customer> customers = customerDao.getCustomers();
        if (customers.isEmpty()) {
            view.printMessage("No customers found.");
        } else {

            view.printMessage(String.format(
                    "%-5s %-15s %-20s %-30s %-15s %-5s %-10s %-8s %-10s %-15s %-10s",
                    "ID", "Order#", "Name", "Address", "City", "ST", "Zip", "Door", "Trailer", "Handling Unit", "Weight"
            ));
            view.printMessage("========================================================================================================================================================================================================6");

            for (Customer customer : customers) {
                String customerLine = String.format(
                        "%-5d %-15s %-20s %-30s %-15s %-5s %-10s %-8s %-10d %-15d %-10d",
                        customer.getCustomerId(),
                        customer.getOrderNumber(),
                        customer.getName(),
                        customer.getAddress(),
                        customer.getCity(),
                        customer.getState(),
                        customer.getZipCode(),
                        customer.getDoorNumber(),
                        customer.getTrailerId(),
                        customer.getHandlingUnit(),
                        customer.getWeight()
                );
                view.printMessage(customerLine);
            }
        }
    }



    private void updateCustomer() {

        String customerIdStr = view.promptForString("Enter customer ID to update");
        int customerId;
        try {
            customerId = Integer.parseInt(customerIdStr);
        } catch (NumberFormatException e) {
            view.printErrorMessage("Invalid customer ID format. Please enter a number.");
            return;
        }


        Customer existingCustomer = customerDao.getCustomerById(customerId);
        if (existingCustomer == null) {
            view.printErrorMessage("Customer not found with ID: " + customerId);
            return;
        }


        String newName = view.promptForString("Enter new customer name (current: " + existingCustomer.getName() + ")");
        String newAddress = view.promptForString("Enter new address (current: " + existingCustomer.getAddress() + ")");
        String newCity = view.promptForString("Enter new city (current: " + existingCustomer.getCity() + ")");
        String newState = view.promptForString("Enter new state (current: " + existingCustomer.getState() + ")");
        String newZipCode = view.promptForString("Enter new zip code (current: " + existingCustomer.getZipCode() + ")");
        String newDoorNumber = view.promptForString("Enter new door number (current: " + existingCustomer.getDoorNumber() + ")");
        String newOrderNumber = view.promptForString("Enter new order number (current: " + existingCustomer.getOrderNumber() + ")");

        String handlingUnitStr = view.promptForString("Enter new handling unit count (current: " + existingCustomer.getHandlingUnit() + ")");
        String weightStr = view.promptForString("Enter new weight (current: " + existingCustomer.getWeight() + ")");

        int newHandlingUnit = existingCustomer.getHandlingUnit();
        int newWeight = existingCustomer.getWeight();

        try {
            newHandlingUnit = Integer.parseInt(handlingUnitStr);
            newWeight = Integer.parseInt(weightStr);
        } catch (NumberFormatException e) {
            view.printErrorMessage("Invalid number format for handling unit or weight. Using existing values.");
        }


        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerId(customerId);
        updatedCustomer.setName(newName.isEmpty() ? existingCustomer.getName() : newName);
        updatedCustomer.setAddress(newAddress.isEmpty() ? existingCustomer.getAddress() : newAddress);
        updatedCustomer.setCity(newCity.isEmpty() ? existingCustomer.getCity() : newCity);
        updatedCustomer.setState(newState.isEmpty() ? existingCustomer.getState() : newState);
        updatedCustomer.setZipCode(newZipCode.isEmpty() ? existingCustomer.getZipCode() : newZipCode);
        updatedCustomer.setDoorNumber(newDoorNumber.isEmpty() ? existingCustomer.getDoorNumber() : newDoorNumber);
        updatedCustomer.setOrderNumber(newOrderNumber.isEmpty() ? existingCustomer.getOrderNumber() : newOrderNumber);
        updatedCustomer.setTrailerId(existingCustomer.getTrailerId());
        updatedCustomer.setHandlingUnit(newHandlingUnit);
        updatedCustomer.setWeight(newWeight);

        try {
            Customer result = customerDao.updateCustomer(updatedCustomer);
            if (result != null) {
                view.printMessage("Customer updated successfully:\n" +
                        "Name: " + result.getName() + "\n" +
                        "Address: " + result.getAddress() + "\n" +
                        "City: " + result.getCity() + "\n" +
                        "State: " + result.getState() + "\n" +
                        "Zip: " + result.getZipCode() + "\n" +
                        "Door: " + result.getDoorNumber() + "\n" +
                        "Order: " + result.getOrderNumber() + "\n" +
                        "HU: " + result.getHandlingUnit() + "\n" +
                        "Weight: " + result.getWeight());
            } else {
                view.printErrorMessage("Failed to update customer");
            }
        } catch (DaoException e) {
            view.printErrorMessage("Error updating customer: " + e.getMessage());
        }
    }
    private void deleteCustomer() throws DaoException {
        int customerId = view.promptForInt("Enter customer ID to delete");
        int rowsAffected = customerDao.deleteCustomerById(customerId);

        if (rowsAffected > 0) {
            view.printMessage("Successfully deleted customer with ID: " + customerId);
        } else {
            view.printErrorMessage("No customer found with ID: " + customerId);
        }
    }
    private void displayTrailerManagementMenu() {
        final String ADD_TRAILER = "Add New Trailer";
        final String UPDATE_TRAILER = "Update Trailer";
        final String LIST_TRAILERS = "List All Trailers";
        final String DELETE_TRAILER = "Delete Trailer";
        final String DONE = "Return to Main Menu";
        final String[] MENU_OPTIONS = {ADD_TRAILER, UPDATE_TRAILER, LIST_TRAILERS, DELETE_TRAILER, DONE};

        boolean finished = false;

        while (!finished) {
            String selection = view.getMenuSelection("Trailer Management", MENU_OPTIONS);
            try {
                switch (selection) {
                    case ADD_TRAILER:
                        addNewTrailer();
                        break;
                    case UPDATE_TRAILER:
                        updateTrailer();
                        break;
                    case LIST_TRAILERS:
                        listAllTrailers();
                        break;
                    case DELETE_TRAILER:
                        deleteTrailer();
                        break;
                    case DONE:
                        finished = true;
                        break;
                }
            } catch (DaoException e) {
                view.printErrorMessage("DAO error - " + e.getMessage());
            }
        }
    }
    private void addNewTrailer() throws DaoException {
        Trailer newTrailer = new Trailer();

        newTrailer.setTrailerNumber(view.promptForString("Enter trailer number"));
        newTrailer.setTrailerType(view.promptForString("Enter trailer type"));

        String shipperIdStr = view.promptForString("Enter shipper ID");
        while (shipperIdStr.isEmpty()) {
            view.printErrorMessage("Shipper ID is required.");
            shipperIdStr = view.promptForString("Enter shipper ID");
        }

        try {
            newTrailer.setShipperId(Integer.parseInt(shipperIdStr));
        } catch (NumberFormatException e) {
            view.printErrorMessage("Invalid shipper ID. Must be a number.");
            return;
        }

        try {
            Trailer createdTrailer = trailerDao.createTrailer(newTrailer);
            if (createdTrailer != null) {
                view.printMessage("Successfully created trailer with ID: " + createdTrailer.getTrailerId());
            } else {
                view.printErrorMessage("Failed to create trailer.");
            }
        } catch (DaoException e) {
            view.printErrorMessage("***Error creating trailer: " + e.getMessage());
        }
    }


    private void updateTrailer() {

        String trailerNumber = view.promptForString("Enter trailer number to update");

        try {

            Trailer existingTrailer = trailerDao.getTrailerByNumber(trailerNumber);
            if (existingTrailer == null) {
                view.printErrorMessage("Trailer not found with number: " + trailerNumber);
                return;
            }


            String newTrailerNumber = view.promptForString(
                    "Enter new trailer number (current: " + existingTrailer.getTrailerNumber() + ")");
            String newTrailerType = view.promptForString(
                    "Enter new trailer type (current: " + existingTrailer.getTrailerType() + ")");
            String newShipperIdStr = view.promptForString(
                    "Enter new shipper ID (current: " + existingTrailer.getShipperId() + ")");


            Trailer updatedTrailer = new Trailer();
            updatedTrailer.setTrailerId(existingTrailer.getTrailerId());
            updatedTrailer.setTrailerNumber(
                    newTrailerNumber.isEmpty() ? existingTrailer.getTrailerNumber() : newTrailerNumber);
            updatedTrailer.setTrailerType(
                    newTrailerType.isEmpty() ? existingTrailer.getTrailerType() : newTrailerType);

            try {
                updatedTrailer.setShipperId(
                        newShipperIdStr.isEmpty() ? existingTrailer.getShipperId() : Integer.parseInt(newShipperIdStr));
            } catch (NumberFormatException e) {
                view.printErrorMessage("Invalid shipper ID format. Keeping current value.");
                updatedTrailer.setShipperId(existingTrailer.getShipperId());
            }


            Trailer result = trailerDao.updateTrailer(updatedTrailer);
            if (result != null) {
                view.printMessage("Trailer updated successfully:\n" +
                        "Number: " + result.getTrailerNumber() + "\n" +
                        "Type: " + result.getTrailerType() + "\n" +
                        "Shipper ID: " + result.getShipperId());
            } else {
                view.printErrorMessage("Failed to update trailer");
            }
        } catch (DaoException e) {
            view.printErrorMessage("Error updating trailer: " + e.getMessage());
        }
    }
    private void deleteTrailer() throws DaoException {
        String trailerNumber = view.promptForString("Enter trailer number to delete");
        Trailer trailerToDelete = trailerDao.getTrailerByNumber(trailerNumber);

        if (trailerToDelete == null) {
            view.printErrorMessage("No trailer found with number: " + trailerNumber);
            return;
        }

        int rowsAffected = trailerDao.deleteTrailerById(trailerToDelete.getTrailerId());
        if (rowsAffected > 0) {
            view.printMessage("Successfully deleted trailer: " + trailerNumber);
        } else {
            view.printErrorMessage("Failed to delete trailer: " + trailerNumber);
        }
    }
    private void listAllTrailers() throws DaoException {
        List<Trailer> trailers = trailerDao.getTrailers();
        if (trailers.isEmpty()) {
            view.printMessage("No trailers found.");
        } else {

            view.printMessage(String.format(
                    "%-10s %-20s %-20s",
                    "ID", "Trailer Number", "Trailer Type"
            ));
            view.printMessage("=====================================================");

            for (Trailer trailer : trailers) {
                String trailerLine = String.format(
                        "%-10d %-20s %-20s",
                        trailer.getTrailerId(),
                        trailer.getTrailerNumber(),
                        trailer.getTrailerType()
                );
                view.printMessage(trailerLine);
            }
        }
    }

}