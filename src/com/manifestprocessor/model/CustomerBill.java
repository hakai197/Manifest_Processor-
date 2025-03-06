package com.manifestprocessor.model;

import java.io.IOException;
import java.util.*;

public class CustomerBill extends DatasetReader {
    public CustomerBill(String filePath) throws IOException {
        super(filePath);
    }

    @Override
    protected int getExpectedFieldCount() {
        return 7;
    }

    @Override
    public void processRecord(String[] record) {
        if (record.length < getExpectedFieldCount()) {
            System.out.println("Invalid record: " + Arrays.toString(record));
        }
    }

    public String getManifestForTrailer(String trailerNumber) {
        String results = "";
        boolean found = false;
        for (String[] bill : getRecords()) {
            if (bill[0].equals(trailerNumber)) {
                results += "Trailer Number: " + bill[0] + "\n";
                results += "Order Number: " + bill[1] + "\n";
                results += "Customer Name: " + bill[2] + "\n";
                results += "Customer Address: " + bill[3] + "\n";
                results += "Handling Units: " + bill[4] + "\n";
                results += "Weight: " + bill[5] + "\n";
                results += "Delivery Door Assigned: " + bill[6] + "\n\n";
                found = true;
            }
        }
        if (!found) {
            results += "No bills found for trailer number: " + trailerNumber + "\n";
        }
        return results;
    }

    public int getTotalHandlingUnitsPerTrailer(String trailerNumber) {
        int totalHandlingUnits = 0;
        for (String[] bill : getRecords()) {
            if (bill[0].equals(trailerNumber)) {
                totalHandlingUnits += Integer.parseInt(bill[4].trim());
            }
        }
        return totalHandlingUnits;
    }

    public int getDeliveryDoorAssignment(String[] bill) {
        String deliveryDoorAssignment = bill[6].trim();
        if (deliveryDoorAssignment.startsWith("Door")) {
            deliveryDoorAssignment = deliveryDoorAssignment.substring(4).trim();
        }
        return Integer.parseInt(deliveryDoorAssignment);
    }

    public Integer getSuggestedDoor(String trailerNumber) {
        int totalHandlingUnits = getTotalHandlingUnitsPerTrailer(trailerNumber);
        Map<Integer, Integer> doorToHandlingUnits = new HashMap<>();


        for (String[] bill : getRecords()) {
            if (bill[0].equals(trailerNumber)) {
                int door = getDeliveryDoorAssignment(bill);
                int handlingUnits = Integer.parseInt(bill[4].trim());
                doorToHandlingUnits.put(door, doorToHandlingUnits.getOrDefault(door, 0) + handlingUnits);
            }
        }


        for (Map.Entry<Integer, Integer> entry : doorToHandlingUnits.entrySet()) {
            int currentDoor = entry.getKey();
            int totalUnitsWithinRange = 0;

            for (int door = currentDoor - 10; door <= currentDoor + 10; door++) {
                if (doorToHandlingUnits.containsKey(door)) {
                    totalUnitsWithinRange += doorToHandlingUnits.get(door);
                }
            }

            if (totalUnitsWithinRange >= 0.25 * totalHandlingUnits) {
                return currentDoor; // Return the suggested door
            }
        }

        return null;
    }

    public List<String[]> suggestHeadLoads() {
        List<String[]> headLoads = new ArrayList<>();

        for (String[] bill : getRecords()) {

            if (bill.length < getExpectedFieldCount()) {
                System.out.println("Skipping invalid record: " + Arrays.toString(bill));
                continue;
            }

            try {
                int handlingUnits = Integer.parseInt(bill[4].trim());
                double weight = Double.parseDouble(bill[5].trim());


                if (handlingUnits >= 8 || weight >= 10000) {
                    headLoads.add(bill);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid handling units or weight in record: " + Arrays.toString(bill));
            }
        }

        return headLoads;
    }
}