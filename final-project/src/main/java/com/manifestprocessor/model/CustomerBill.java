package com.manifestprocessor.model;

import java.io.IOException;
import java.util.Arrays;

public class CustomerBill extends DatasetReader {
    public CustomerBill(String filePath) throws IOException {
        super(filePath);
    }

    @Override
    public void processRecord(String[] record) {
        if (record.length < 7) {
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
                totalHandlingUnits += Integer.parseInt(bill[4]);
            }
        }
        return totalHandlingUnits;
    }

    public int getDeliveryDoorAssignment(String trailerNumber) {
        for (String[] bill : getRecords()) {
            if (bill[0].equals(trailerNumber)) {
                String deliveryDoorAssignment = bill[6].trim(); // Get the delivery door assignment
                // Remove the "Door" prefix and trim any extra spaces
                if (deliveryDoorAssignment.startsWith("Door")) {
                    deliveryDoorAssignment = deliveryDoorAssignment.substring(4).trim();
                }
                return Integer.parseInt(deliveryDoorAssignment); // Convert to integer
            }
        }
        throw new IllegalArgumentException("Trailer number not found: " + trailerNumber);
    }
}

