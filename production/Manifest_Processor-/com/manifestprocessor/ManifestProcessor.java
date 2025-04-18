package com.techelevator.custom;

import com.techelevator.custom.dao.CustomerDao;
import com.techelevator.custom.dao.TrailerDao;
import com.techelevator.custom.dao.UnloaderDao;
import com.techelevator.custom.model.Customer;
import com.techelevator.custom.model.Unloader;
import com.techelevator.util.BasicConsole;

import java.util.*;
import java.util.stream.Collectors;

public class ManifestProcessor {

    private static final int MIN_DOOR_NUMBER = 1;
    private static final int MAX_DOOR_NUMBER = 50;

    private final Set<Integer> availableDoors = new HashSet<>();
    private final Map<Integer, String> assignedDoors = new HashMap<>();
    private final Map<Integer, String> trailerAssignments = new HashMap<>();

    private final CustomerDao customerDao;
    private final TrailerDao trailerDao;
    private final UnloaderDao unloaderDao;

    public ManifestProcessor(CustomerDao customerDao, TrailerDao trailerDao, UnloaderDao unloaderDao) {
        this.customerDao = customerDao;
        this.trailerDao = trailerDao;
        this.unloaderDao = unloaderDao;
        initializeDoors();
    }

    private void initializeDoors() {
        for (int i = MIN_DOOR_NUMBER; i <= MAX_DOOR_NUMBER; i++) {
            availableDoors.add(i);
        }
    }

    public void assignDoorToTrailer(BasicConsole view, String trailerNumber) {
        try {
            trailerNumber = validateTrailerNumber(trailerNumber);

            if (isTrailerAlreadyAssigned(trailerNumber)) {
                view.printErrorMessage("Trailer " + trailerNumber + " is already assigned to a door.");
                return;
            }

            checkHeadLoadStatus(view, trailerNumber);
            int doorNumber = getValidDoorNumber(view, trailerNumber);
            String employeeName = getValidUnloaderName(view);

            completeAssignment(view, trailerNumber, doorNumber, employeeName);
        } catch (IllegalArgumentException e) {
            view.printErrorMessage(e.getMessage());
        }
    }

    private String validateTrailerNumber(String trailerNumber) {
        trailerNumber = trailerNumber.trim();
        if (trailerNumber.isEmpty()) {
            throw new IllegalArgumentException("Trailer number cannot be empty.");
        }
        return trailerNumber;
    }

    private boolean isTrailerAlreadyAssigned(String trailerNumber) {
        return trailerAssignments.containsValue(trailerNumber);
    }

    private void checkHeadLoadStatus(BasicConsole view, String trailerNumber) {
        List<Customer> orders = customerDao.getCustomersByTrailer(trailerNumber);
        boolean isHeadLoad = orders.stream()
                .anyMatch(order -> order.getHandlingUnit() >= 8 || order.getWeight() >= 10000);

        view.printMessage(isHeadLoad ?
                "This trailer qualifies as a HEAD LOAD." :
                "This trailer does NOT qualify as a head load.");
    }

    private int getValidDoorNumber(BasicConsole view, String trailerNumber) {
        Integer suggestedDoor = customerDao.getSuggestedDoor(trailerNumber);
        int doorNumber = promptForDoorNumber(view, suggestedDoor);

        while (!isDoorAvailable(doorNumber)) {
            view.printErrorMessage("Door " + doorNumber + " is invalid or already assigned.");
            doorNumber = promptForDoorNumber(view, null);
        }
        return doorNumber;
    }

    private int promptForDoorNumber(BasicConsole view, Integer suggestedDoor) {
        if (suggestedDoor != null && availableDoors.contains(suggestedDoor)) {
            boolean accept = view.promptForYesNo("Suggested door is " + suggestedDoor + ". Use it?");
            if (accept) {
                return suggestedDoor;
            }
        }
        return view.promptForInteger("Enter door number (" + MIN_DOOR_NUMBER + "-" + MAX_DOOR_NUMBER + ")");
    }

    private boolean isDoorAvailable(int doorNumber) {
        return doorNumber >= MIN_DOOR_NUMBER &&
                doorNumber <= MAX_DOOR_NUMBER &&
                availableDoors.contains(doorNumber);
    }

    private String getValidUnloaderName(BasicConsole view) {
        List<Unloader> unloaders = unloaderDao.getAllUnloaders();
        List<String> unloaderNames = unloaders.stream()
                .map(Unloader::getName)
                .collect(Collectors.toList());

        String employeeName = view.promptForString("Enter employee name");
        while (!isValidUnloader(unloaderNames, employeeName)) {
            view.printErrorMessage("Employee not found. Available unloaders: " +
                    String.join(", ", unloaderNames));
            employeeName = view.promptForString("Re-enter employee name");
        }
        return employeeName;
    }

    private boolean isValidUnloader(List<String> unloaderNames, String name) {
        return unloaderNames.stream()
                .anyMatch(u -> u.equalsIgnoreCase(name));
    }

    private void completeAssignment(BasicConsole view, String trailerNumber, int doorNumber, String employeeName) {
        availableDoors.remove(doorNumber);
        assignedDoors.put(doorNumber, employeeName);
        trailerAssignments.put(doorNumber, trailerNumber);

        view.printMessage(String.format(
                "Door %d assigned to trailer %s with employee %s",
                doorNumber, trailerNumber, employeeName));
    }

    public boolean releaseDoor(int doorNumber) {
        if (assignedDoors.containsKey(doorNumber)) {
            assignedDoors.remove(doorNumber);
            trailerAssignments.remove(doorNumber);
            availableDoors.add(doorNumber);
            return true;
        }
        return false;
    }


    public boolean isHeadLoad(String trailerNumber) {
        List<Customer> orders = customerDao.getCustomersByTrailer(trailerNumber);
        for (Customer order : orders) {
            if (order.getHandlingUnit() >= 8 || order.getWeight() >= 10000) {
                return true;
            }
        }
        return false;
    }

    public String getAvailableDoors() {
        if (availableDoors.isEmpty()) {
            return "No doors currently available";
        }


        List<Integer> sortedDoors = new ArrayList<>(availableDoors);
        Collections.sort(sortedDoors);

        String result = "Available Doors (" + availableDoors.size() + "): ";
        String doorList = "";


        int rangeStart = sortedDoors.get(0);
        int previous = rangeStart;
        doorList += rangeStart;

        for (int i = 1; i < sortedDoors.size(); i++) {
            int current = sortedDoors.get(i);
            if (current == previous + 1) {
                previous = current;
            } else {

                if (rangeStart != previous) {
                    doorList += "-" + previous;
                }
                doorList += ", " + current;
                rangeStart = current;
                previous = current;
            }
        }


        if (rangeStart != previous) {
            doorList += "-" + previous;
        }

        return result + doorList;
    }
}