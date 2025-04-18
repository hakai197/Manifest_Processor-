package com.techelevator.util;

import com.techelevator.custom.ManifestProcessor;

import java.util.List;
//Need to update this to work with the new processes. Also import it to the main processor.
public class Timer {
//    private CustomerBill customerBill;
//
//
//    public Timer(ManifestProcessor manifestProcessor, CustomerBill customerBill) {
//        this.customerBill = customerBill;
//    }
//
//    public double estimateCompletionTime(String trailerNumber, int assignedDoor) {
//        int retrievalTime = 10; // 10 seconds to retrieve a skid off a truck
//        int totalTravelTime = 0;
//        int totalHandlingUnits = 0;
//
//        List<String[]> bills = customerBill.getRecords();
//        for (String[] bill : bills) {
//            if (bill[0].equals(trailerNumber)) {
//                int deliveryDoor = Integer.parseInt(bill[6].replaceAll("Door", "").trim());
//                int travelTime;
//
//                if (assignedDoor > deliveryDoor) {
//                    travelTime = assignedDoor - deliveryDoor;
//                } else {
//                    travelTime = deliveryDoor - assignedDoor;
//                }
//                travelTime *= 2; // round trip
//
//                totalTravelTime += travelTime;
//                //grab each handling unit per order.
//                totalHandlingUnits += Integer.parseInt(bill[4]);
//            }
//        }
//
//        int handlingTime = totalHandlingUnits;
//        int totalTimeInSeconds = retrievalTime + totalTravelTime + handlingTime;
//
//        // Convert seconds to hours
//        return totalTimeInSeconds / 3600.0;
//    }
}

