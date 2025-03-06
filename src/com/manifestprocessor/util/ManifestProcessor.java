package com.manifestprocessor.util;

import com.manifestprocessor.model.CustomerBill;
import com.manifestprocessor.model.Unloader;
import com.manifestprocessor.model.Timer;


import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ManifestProcessor implements DoorAssignment {
    private List<Integer> availableDoors;
    private Map<Integer, String> assignedDoors;
    private List<String> assignedEntries;
    private Unloader unloader;
    private CustomerBill customerBill;
    private Timer timer;

    public ManifestProcessor(String unloaderFilePath, String customerBillFilePath) throws IOException {
        this.availableDoors = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            availableDoors.add(i);
        }
        this.assignedDoors = new HashMap<>();
        this.assignedEntries = new ArrayList<>();
        this.unloader = new Unloader(unloaderFilePath);
        this.customerBill = new CustomerBill(customerBillFilePath);
        this.timer = new Timer(this, customerBill);

    }

    public Map<Integer, String> getAssignedDoors() {
        return assignedDoors;
    }

    @Override
    public void assignDoor(Scanner scanner, String trailerNumber, String employeeName, int doorNumber)
            throws IllegalArgumentException {
        trailerNumber = trailerNumber.trim();
        employeeName = employeeName.trim();


        while (trailerNumber.isEmpty()) {
            System.out.println("Invalid trailer number: Trailer number cannot be empty. Please re-enter the trailer number: ");
            trailerNumber = scanner.nextLine().trim();
        }


        while (employeeName.isEmpty()) {
            System.out.println("Invalid employee name: Employee name cannot be empty. Please re-enter the employee name: ");
            employeeName = scanner.nextLine().trim();
        }


        while (!availableDoors.contains(doorNumber) || assignedDoors.containsKey(doorNumber)) {
            System.out.println("Invalid door number: Door " + doorNumber + " is not available or already assigned. Please re-enter the door number: ");
            doorNumber = Integer.parseInt(scanner.nextLine().trim());
        }


        String finalEmployeeName = employeeName;
        while (assignedDoors.values().stream().anyMatch(name -> name.equals(finalEmployeeName))) {
            System.out.println("Employee " + employeeName + " is already assigned to another door. Please re-enter the employee name: ");
            employeeName = scanner.nextLine().trim();
        }


        String finalEmployeeName1 = employeeName;
        while (!unloader.getRecords().stream().anyMatch(record -> record[0].trim().equals(finalEmployeeName1))) {
            System.out.println("Employee not found: " + employeeName + " is not in the unloader records. Please re-enter the employee name: ");
            employeeName = scanner.nextLine().trim();
        }


        availableDoors.remove(Integer.valueOf(doorNumber));
        assignedDoors.put(doorNumber, employeeName);
        assignedEntries.add("Door " + doorNumber + " assigned to trailer " + trailerNumber + " with employee " +
                employeeName);
        System.out.println("Door " + doorNumber + " has been assigned to trailer " + trailerNumber + " with employee " +
                employeeName + ".");


        try {
            double estimateTimeHours = timer.estimateCompletionTime(trailerNumber, doorNumber);
            System.out.println("Estimated Completion Time: " + String.format("%.2f", estimateTimeHours) + " hours.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean releaseDoor(int doorNumber) throws IllegalArgumentException {
        if (!assignedDoors.containsKey(doorNumber)) {
            return false;
        }


        String employeeName = assignedDoors.remove(doorNumber);
        availableDoors.add(doorNumber);
        System.out.println("Door " + doorNumber + " and employee " + employeeName + " have been released.");
        return true;
    }

    @Override
    public void viewAssignedTrailerAndEmployees() {
        if (assignedDoors.isEmpty()) {
            System.out.println("No doors are currently assigned.");
        } else {
            System.out.println("Assigned Doors, Trailers, and Employees:");
            for (Map.Entry<Integer, String> entry : assignedDoors.entrySet()) {
                int doorNumber = entry.getKey();
                String employeeName = entry.getValue();
                System.out.println("Door Number: " + doorNumber + ", Employee: " + employeeName);
            }
        }
    }


    public void viewUnloaders() {
        unloader.viewUnloaders();
    }

    //Add to existing trailer or new trailer.  .trim to ensure it enters in correctly. Using append in the file writer
    // to make sure that it writes to the next line.  Love that "\n".  Want to update in with a better GUI.
    public void addCustomerBill(String trailerNumber, String orderNumber, String customerName, String customerAddress,
                                String handlingUnits, String weight, String deliveryDoorAssignment) {

        trailerNumber = trailerNumber.trim();
        orderNumber = orderNumber.trim();
        customerName = customerName.trim();
        customerAddress = customerAddress.trim();
        handlingUnits = handlingUnits.trim();
        weight = weight.trim();
        deliveryDoorAssignment = deliveryDoorAssignment.trim();


        String bill = trailerNumber + " | " + orderNumber + " | " + customerName + " | " + customerAddress + " | "
                + handlingUnits + " | " + weight + " | " + deliveryDoorAssignment;

        FileWriter writer = null;
        try {

            writer = new FileWriter("src/main/resources/CustomerBills.txt", true);
            writer.write(bill + "\n");
            writer.flush();
            System.out.println("Customer Bill added.");
        } catch (IOException e) {
            System.out.println("Error writing to file");
            customerBill.addRecord(bill);
        } finally {

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error closing the file writer");
                }
            }
        }
    }


    public String seeManifest(String trailerNumber) throws IOException {
        trailerNumber = trailerNumber.trim();


        return customerBill.getManifestForTrailer(trailerNumber);
    }

    // Main Method and options, really happy with the Go Back subChoice.  I'd like to change this as we get into HTML to
    // show more information if possible.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            ManifestProcessor processor = new ManifestProcessor("data/Employees.txt",
                    "data/CustomerBills.txt");

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Assign Door");
                System.out.println("2. Release Door");
                System.out.println("3. See Manifest");
                System.out.println("4. View Unloaders");
                System.out.println("5. Add Customer Bill");
                System.out.println("6. View Assigned Trailers and Employees");
                System.out.println("7. Exit");
                System.out.println("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    System.out.println("1. Assign Door");
                    System.out.println("0. Go Back");
                    System.out.println("Select Choice: ");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        System.out.println("Enter trailer number: ");
                        String trailerNumber = scanner.nextLine().trim();


                        List<String[]> headLoads = processor.customerBill.suggestHeadLoads();
                        List<String> headLoadOrders = new ArrayList<>();
                        for (String[] bill : headLoads) {
                            if (bill[0].equals(trailerNumber)) {
                                headLoadOrders.add(bill[1]);
                            }
                        }

                        if (!headLoadOrders.isEmpty()) {
                            System.out.println("This trailer qualifies as a HEAD LOAD (8+ handling units OR 10,000+ lbs).");
                            System.out.println("Orders with head load: " + String.join(", ", headLoadOrders));
                        } else {
                            System.out.println("This trailer does NOT qualify as a head load.");
                        }

                        // Display suggested door (if available)
                        Integer suggestedDoor = processor.customerBill.getSuggestedDoor(trailerNumber);
                        int doorNumber;
                        if (suggestedDoor != null && !processor.getAssignedDoors().containsKey(suggestedDoor)) {
                            System.out.println("Suggested door for trailer " + trailerNumber + ": Door " + suggestedDoor);
                            System.out.println("Do you want to assign this door? (yes/no): ");
                            String response = scanner.nextLine().trim().toLowerCase();
                            if (response.equals("yes")) {
                                doorNumber = suggestedDoor; // Use the suggested door
                            } else {
                                System.out.println("Enter door number: ");
                                doorNumber = Integer.parseInt(scanner.nextLine().trim());
                            }
                        } else {
                            System.out.println("No suggested door available for this trailer.");
                            System.out.println("Enter door number: ");
                            doorNumber = Integer.parseInt(scanner.nextLine().trim());
                        }


                        System.out.println("Enter employee name: ");
                        String employeeName = scanner.nextLine().trim();


                        processor.assignDoor(scanner, trailerNumber, employeeName, doorNumber);
                    }
                } else if (choice == 2) {
                    System.out.println("1. Release Door");
                    System.out.println("0. Go Back");
                    System.out.println("Select Choice: ");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        boolean doorReleased = false;
                        while (!doorReleased) {
                            System.out.println("Enter door number to release: ");
                            int doorNumber = scanner.nextInt();
                            scanner.nextLine();


                            doorReleased = processor.releaseDoor(doorNumber);

                            if (!doorReleased) {
                                System.out.println("Door " + doorNumber + " is not currently assigned. Please enter a valid door number.");
                            }
                        }
                    }
                } else if (choice == 3) {
                    System.out.println("1. See Manifest");
                    System.out.println("0. Go Back");
                    System.out.println("Select Choice: ");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        System.out.println("Enter trailer number: ");
                        String trailerNumber = scanner.nextLine().trim();
                        System.out.println(processor.seeManifest(trailerNumber));
                    }
                } else if (choice == 4) {
                    System.out.println("1. View Unloaders");
                    System.out.println("0. Go Back");
                    System.out.println("Select Choice: ");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        processor.viewUnloaders();
                    }
                } else if (choice == 5) {
                    System.out.println("1. Add Customer Bill");
                    System.out.println("0. Go Back");
                    System.out.println("Select Choice: ");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        System.out.println("Enter trailer number: ");
                        String trailerNumber = scanner.nextLine().trim();
                        System.out.println("Enter order number: ");
                        String orderNumber = scanner.nextLine().trim();
                        System.out.print("Enter customer name: ");
                        String customerName = scanner.nextLine().trim();
                        System.out.println("Enter customer address: ");
                        String customerAddress = scanner.nextLine().trim();
                        System.out.println("Enter handling units: ");
                        String handlingUnits = scanner.nextLine().trim();
                        System.out.println("Enter weight: ");
                        String weight = scanner.nextLine().trim();
                        System.out.println("Enter delivery door assignment: ");
                        String deliveryDoorAssignment = scanner.nextLine().trim();
                        processor.addCustomerBill(trailerNumber, orderNumber, customerName, customerAddress,
                                handlingUnits, weight, deliveryDoorAssignment);
                    }
                } else if (choice == 6) {
                    System.out.println("1. View Assigned Trailers");
                    System.out.println("0. Go Back");
                    System.out.println("Select Choice: ");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        processor.viewAssignedTrailerAndEmployees();
                    }
                } else if (choice == 7) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing ManifestProcessor: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

