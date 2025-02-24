package com.manifestprocessor.util;

import com.manifestprocessor.model.CustomerBill;
import com.manifestprocessor.model.Unloader;
import com.manifestprocessor.model.Timer;



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
        this.customerBill = new CustomerBill(customerBillFilePath); // Initialize CustomerBill
        this.timer = new Timer(this, customerBill); // Pass CustomerBill object to Timer
    }

    @Override
    public void assignDoor(Scanner scanner, String trailerNumber, String employeeName, int doorNumber) throws IllegalArgumentException {
        trailerNumber = trailerNumber.trim();
        employeeName = employeeName.trim();



        // Validate trailer number
        while (trailerNumber.isEmpty()) {
            System.out.print("Invalid trailer number: Trailer number cannot be empty. Please re-enter the trailer number: ");
            trailerNumber = scanner.nextLine().trim();
        }

        // Validate employee name
        while (employeeName.isEmpty()) {
            System.out.print("Invalid employee name: Employee name cannot be empty. Please re-enter the employee name: ");
            employeeName = scanner.nextLine().trim();
        }

        // Validate door number
        while (!availableDoors.contains(doorNumber)) {
            System.out.print("Invalid door number: Door " + doorNumber + " is not available. Please re-enter the door number: ");
            doorNumber = Integer.parseInt(scanner.nextLine().trim());
        }

        // Check if the trailer is already assigned to another door
        String finalEmployeeName = employeeName;
        while (assignedDoors.values().stream().anyMatch(name -> name.equals(finalEmployeeName))) {
            System.out.print("Employee " + employeeName + " is already assigned to another door. Please re-enter the employee name: ");
            employeeName = scanner.nextLine().trim();
        }

        // Check if the employee exists in the unloader records
        String finalEmployeeName1 = employeeName;
        while (!unloader.getRecords().stream().anyMatch(record -> record[0].trim().equals(finalEmployeeName1))) {
            System.out.print("Employee not found: " + employeeName + " is not in the unloader records. Please re-enter the employee name: ");
            employeeName = scanner.nextLine().trim();
        }

        // Check if the door is already assigned
        while (assignedDoors.containsKey(doorNumber)) {
            System.out.print("Door " + doorNumber + " is already assigned. Please re-enter the door number: ");
            doorNumber = Integer.parseInt(scanner.nextLine().trim());
        }

        // Assign the door
        availableDoors.remove(Integer.valueOf(doorNumber));
        assignedDoors.put(doorNumber, employeeName);
        assignedEntries.add("Door " + doorNumber + " assigned to trailer " + trailerNumber + " with employee " + employeeName);
        System.out.println("Door " + doorNumber + " has been assigned to trailer " + trailerNumber + " with employee " + employeeName + ".");

        // Estimate completion time
        try {
            double estimateTimeHours = timer.estimateCompletionTime(trailerNumber, doorNumber);
            System.out.println("Estimated Completion Time: " + String.format("%.2f", estimateTimeHours) + " hours.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void releaseDoor(int doorNumber) throws IllegalArgumentException {
        if (!assignedDoors.containsKey(doorNumber)) {
            throw new IllegalArgumentException("Door " + doorNumber + " is not currently assigned.");
        }

        // Release the door
        String employeeName = assignedDoors.remove(doorNumber);
        availableDoors.add(doorNumber);
        System.out.println("Door " + doorNumber + " and employee " + employeeName + " have been released.");
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

    public void addCustomerBill(String trailerNumber, String orderNumber, String customerName, String customerAddress, String handlingUnits, String weight, String deliveryDoorAssignment) {
        trailerNumber = trailerNumber.trim();
        orderNumber = orderNumber.trim();
        customerName = customerName.trim();
        customerAddress = customerAddress.trim();
        handlingUnits = handlingUnits.trim();
        weight = weight.trim();
        deliveryDoorAssignment = deliveryDoorAssignment.trim();

        String bill = trailerNumber + " | " + orderNumber + " | " + customerName + " | " + customerAddress + " | " + handlingUnits + " | " + weight + " | " + deliveryDoorAssignment;
        customerBill.addRecord(bill);
        System.out.println("Customer Bill added.");
    }

    public String seeManifest(String trailerNumber) {
        trailerNumber = trailerNumber.trim();
        return customerBill.getManifestForTrailer(trailerNumber);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            ManifestProcessor processor = new ManifestProcessor("src/main/resources/Employee.txt", "src/main/resources/CustomerBills.txt");

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Assign Door");
                System.out.println("2. Release Door");
                System.out.println("3. See Manifest");
                System.out.println("4. View Unloaders");
                System.out.println("5. Add Customer Bill");
                System.out.println("6. View Assigned Trailers and Employees");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    System.out.println("1.Assign Door");
                    System.out.println("0.Go Back");
                    System.out.println("Select Choice");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        System.out.print("Enter trailer number: ");
                        String trailerNumber = scanner.nextLine().trim();
                        System.out.print("Enter employee name: ");
                        String employeeName = scanner.nextLine().trim();
                        System.out.print("Enter door number: ");
                        int doorNumber = scanner.nextInt();
                        scanner.nextLine();
                        processor.assignDoor(scanner, trailerNumber, employeeName, doorNumber);
                    }
                } else if (choice == 2) {
                    System.out.println("1.Release Door");
                    System.out.println("0.Go Back");
                    System.out.println("Select Choice");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        System.out.print("Enter door number to release: ");
                        int doorNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        processor.releaseDoor(doorNumber);
                    }
                } else if (choice == 3) {
                    System.out.println("1.See Manifest");
                    System.out.println("0.Go Back");
                    System.out.println("Select Choice");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        System.out.print("Enter trailer number: ");
                        String trailerNumber = scanner.nextLine().trim();
                        System.out.println(processor.seeManifest(trailerNumber));
                    }
                } else if (choice == 4) {
                    System.out.println("1.View Unloaders");
                    System.out.println("0.Go Back");
                    System.out.println("Select Choice");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        processor.viewUnloaders();
                    }
                } else if (choice == 5) {
                    System.out.println("1.Add Customer Bill");
                    System.out.println("0.Go Back");
                    System.out.println("Select Choice");
                    int subChoice = Integer.parseInt(scanner.nextLine());
                    if (subChoice == 1) {
                        System.out.print("Enter trailer number: ");
                        String trailerNumber = scanner.nextLine().trim();
                        System.out.print("Enter order number: ");
                        String orderNumber = scanner.nextLine().trim();
                        System.out.print("Enter customer name: ");
                        String customerName = scanner.nextLine().trim();
                        System.out.print("Enter customer address: ");
                        String customerAddress = scanner.nextLine().trim();
                        System.out.print("Enter handling units: ");
                        String handlingUnits = scanner.nextLine().trim();
                        System.out.print("Enter weight: ");
                        String weight = scanner.nextLine().trim();
                        System.out.print("Enter delivery door assignment: ");
                        String deliveryDoorAssignment = scanner.nextLine().trim();
                        processor.addCustomerBill(trailerNumber, orderNumber, customerName, customerAddress, handlingUnits, weight, deliveryDoorAssignment);
                    }
                } else if (choice == 6) {
                    System.out.println("1.View Assigned Trailers");
                    System.out.println("0.Go Back");
                    System.out.println("Select Choice");
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

    public Map<Integer, String> getAssignedDoors() {
        return assignedDoors;
    }
}