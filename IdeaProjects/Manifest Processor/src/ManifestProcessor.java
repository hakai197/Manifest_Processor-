import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ManifestProcessor {
    private List<Integer> availableDoors;
    private List<String> customerBills;
    private Map<Integer, String> assignedDoors;
    private Unloader unloader; // Use the Unloader class to manage unloaders
    private CustomerBill customerBill; //Use the Customer bill Class

    public ManifestProcessor() {
        this.availableDoors = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            availableDoors.add(i);
        }
        this.customerBills = new ArrayList<>();
        this.assignedDoors = new HashMap<>();
        this.unloader = new Unloader(); // Initialize the Unloader instance
        this.unloader.readDataset("data/Employees.txt");// Load unloaders from the file
        this.customerBill = new CustomerBill("data/CustomerBills.txt");
    }

    // Assign Door
    public void assignDoor(Scanner scanner, String trailerNumber, String employeeName, int doorNumber) {
        if (!availableDoors.contains(doorNumber)) {
            System.out.println("Invalid door number.");
            return;
        }

        // Check if the employee exists using the Unloader class
        boolean employeeExists = false;
        for (String[] unloaderData : unloader.getUnloaders()) {
            if (unloaderData[0].equals(employeeName)) {
                employeeExists = true;
                break;
            }
        }

        if (!employeeExists) {
            System.out.println("Invalid employee name.");
            return;
        }

        // Assign the door
        availableDoors.remove(Integer.valueOf(doorNumber));
        assignedDoors.put(doorNumber, employeeName);

        System.out.println("Door " + doorNumber + " has been assigned to trailer " + trailerNumber + " with employee " + employeeName + ".");
    }

    // Assign Door Interactive
    public void assignDoorInteractive(Scanner scanner) {
        if (availableDoors.isEmpty()) {
            System.out.println("No available doors.");
            return;
        }

        System.out.print("Enter trailer number: ");
        String trailerNumber = scanner.nextLine();

        // Display available employees using the Unloader class
        System.out.println("Available Employees:");
        unloader.viewUnloaders();

        System.out.print("Enter employee name: ");
        String employeeName = scanner.nextLine();

        System.out.println("Available Doors: " + availableDoors);
        System.out.print("Enter door number: ");
        int doorNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        assignDoor(scanner, trailerNumber, employeeName, doorNumber);
    }

    // Release Door
    public void releaseDoor(int doorNumber) {
        if (assignedDoors.containsKey(doorNumber)) {
            String employeeName = assignedDoors.get(doorNumber);
            availableDoors.add(doorNumber);
            assignedDoors.remove(doorNumber);
            System.out.println("Door " + doorNumber + " and employee " + employeeName + " have been released.");
        } else {
            System.out.println("Door " + doorNumber + " is not currently assigned.");
        }
    }

    // Release Door Interactive
    public void releaseDoorInteractive(Scanner scanner) {
        System.out.print("Enter door number to release: ");
        int doorNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        releaseDoor(doorNumber);
    }

    // View Unloaders from Employees.txt
    public void viewUnloaders() {
        List<String[]> unloaders = unloader.getUnloaders();

        if (unloaders.isEmpty()) {
            System.out.println("No unloaders available.");
        } else {
            System.out.println("Unloaders:");
            for (String[] unloaderData : unloaders) {
                // Format and display each unloader's information
                System.out.println("Employee name: " + unloaderData[0] +
                        ", Shift: " + unloaderData[1] +
                        ", Employee Number: " + unloaderData[2]);
            }
        }
    }

    // Add Unloader and update Employees.txt
    public void addUnloader(String unloaderData) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter("data/Employees.txt", true));
            writer.println(unloaderData);
            unloader.readDataset("data/Employees.txt"); // Reload unloaders to update the list
            System.out.println("Unloader added: " + unloaderData);
        } catch (IOException e) {
            System.err.println("Error writing to unloaders dataset: " + e.getMessage());
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    // Add Customer Bill
    public void addCustomerBill(String trailerNumber, String orderNumber, String customerName, String customerAddress, String handlingUnits, String weight, String deliveryDoorAssignment) {
        String[] newBill = {trailerNumber, orderNumber, customerName, customerAddress, handlingUnits, weight, deliveryDoorAssignment};
        customerBills.add(Arrays.toString(newBill));

        try (PrintWriter writer = new PrintWriter(new FileWriter("data/CustomerBills.txt", true))) {
            writer.println(String.join(" | ", newBill));
            System.out.println("Customer Bill added");
        } catch (IOException e) {
            System.err.println("Error writing customer bill to file: " + e.getMessage());
        }
    }

    public String seeManifest(String trailerNumber) {
        return customerBill.getManifestForTrailer(trailerNumber);
    }

    // See Manifest Interactive
    public String seeManifestInteractive(Scanner scanner) {
        System.out.print("Enter Trailer Number: ");
        String trailerNumber = scanner.nextLine();

        String manifest = seeManifest(trailerNumber);
        System.out.println(manifest);

        return manifest;
    }
    public void viewUnloadersInteractive() {
        viewUnloaders();
    }

    public void addCustomerBillInteractive(Scanner scanner) {
        System.out.print("Enter trailer number: ");
        String trailerNumber = scanner.nextLine();

        System.out.print("Enter 9-digit order number: ");
        String orderNumber = scanner.nextLine();

        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter customer address: ");
        String customerAddress = scanner.nextLine();

        System.out.print("Enter handling units: ");
        String handlingUnits = scanner.nextLine();

        System.out.print("Enter weight: ");
        String weight = scanner.nextLine();

        System.out.print("Enter delivery door assignment: ");
        String deliveryDoorAssignment = scanner.nextLine();

        addCustomerBill(trailerNumber, orderNumber, customerName, customerAddress, handlingUnits, weight, deliveryDoorAssignment);
    }

    public void addUnloaderInteractive(Scanner scanner) {
        System.out.print("Enter unloader data (format: Name|Shift|EmployeeNumber): ");
        String unloaderData = scanner.nextLine();
        addUnloader(unloaderData);
    }

    // View Assigned Trailers and Employees
    public void viewAssignedTrailersAndEmployees() {
        if (assignedDoors.isEmpty()) {
            System.out.println("No assigned doors.");
            return;
        }

        System.out.println("Assigned Trailers and Employees:");
        for (Map.Entry<Integer, String> entry : assignedDoors.entrySet()) {
            System.out.println("Door Number: " + entry.getKey() + ", Employee: " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        ManifestProcessor processor = new ManifestProcessor();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Assign Door");
            System.out.println("2. Release Door");
            System.out.println("3. See Manifest");
            System.out.println("4. View Unloaders");
            System.out.println("5. Add Unloader");
            System.out.println("6. Add Customer Bill");
            System.out.println("7. View Assigned Trailers and Employees");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                processor.assignDoorInteractive(scanner);
            } else if (choice == 2) {
                processor.releaseDoorInteractive(scanner);
            } else if (choice == 3) {
                processor.seeManifestInteractive(scanner);
            } else if (choice == 4) {
                processor.viewUnloadersInteractive();
            } else if (choice == 5) {
                processor.addUnloaderInteractive(scanner);
            } else if (choice == 6) {
                processor.addCustomerBillInteractive(scanner);
            } else if (choice == 7) {
                processor.viewAssignedTrailersAndEmployees();
            } else if (choice == 8) {
                running = false;
                System.out.println("Exiting...");
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}