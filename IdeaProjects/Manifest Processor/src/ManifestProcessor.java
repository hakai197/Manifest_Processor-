import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManifestProcessor {
    private List<Integer> availableDoors;
    private List<String> customerBills;
    private List<String> unloaders;
    private List<AssignedDoor> assignedDoors;

    public ManifestProcessor() {
        this.availableDoors = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            availableDoors.add(i);
        }
        this.customerBills = new ArrayList<>();
        this.unloaders = new ArrayList<>();
        this.assignedDoors = new ArrayList<>();
    }

    // Method placeholder
    private static void readDataset(String s) {
    }

    // Assign Door
    public void assignDoor(Scanner scanner, String trailerNumber, String employeeName, int doorNumber) {
        if (!availableDoors.contains(doorNumber)) {
            System.out.println("Invalid door number.");
            return;
        }

        availableDoors.remove(Integer.valueOf(doorNumber));
        unloaders.remove(employeeName);
        assignedDoors.add(new AssignedDoor(doorNumber, employeeName));

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

        System.out.println("Available Employees:");
        viewUnloaders();

        System.out.print("Enter employee name: ");
        String employeeName = scanner.nextLine();

        System.out.println("Available Doors: " + availableDoors);
        System.out.print("Enter door number: ");
        int doorNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        assignDoor(scanner, trailerNumber, employeeName, doorNumber);
    }

    // Release Door
    public void releaseDoorInteractive(Scanner scanner) {
        System.out.print("Enter door number to release: ");
        int doorNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        AssignedDoor assignedDoor = null;
        for (AssignedDoor ad : assignedDoors) {
            if (ad.getDoorNumber() == doorNumber) {
                assignedDoor = ad;
                break;
            }
        }

        if (assignedDoor != null) {
            availableDoors.add(doorNumber);
            unloaders.add(assignedDoor.getEmployeeName());
            assignedDoors.remove(assignedDoor);
            System.out.println("Door " + doorNumber + " and employee " + assignedDoor.getEmployeeName() + " have been released.");
        } else {
            System.out.println("Door " + doorNumber + " is not currently assigned.");
        }
    }

    // See Manifest
    public static class seeManifestInteractive {
        public seeManifestInteractive(Scanner scanner) {
        }


            }

    // View Unloaders from Employees.txt
    public void viewUnloaders() {
        unloaders.clear();
        try (Scanner scanner = new Scanner(new File("data/Employees.txt"))) {
            while (scanner.hasNextLine()) {
                String unloader = scanner.nextLine();
                unloaders.add(unloader);
            }

            if (unloaders.isEmpty()) {
                System.out.println("No unloaders available.");
            } else {
                System.out.println("Unloaders: " + unloaders);
            }
        } catch (IOException e) {
            System.err.println("Error reading unloaders dataset: " + e.getMessage());
        }
    }

    // Add Unloader and update Employees.txt
    public void addUnloader(String unloader) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter("data/Employees.txt", true));
            writer.println(unloader);
            unloaders.add(unloader);
            System.out.println("Unloader added: " + unloader);
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
        String customerBill = String.join(", ", trailerNumber, orderNumber, customerName, customerAddress, handlingUnits, weight, deliveryDoorAssignment);
        customerBills.add(customerBill);
        System.out.println("Customer bill added: " + customerBill);
    }

    // Methods for user interactions
    public void seeManifest(Scanner scanner) {
        System.out.println("Enter Trailer Number: ");
        String trailerNumber = scanner.nextLine();

        CustomerBill customerBill = new CustomerBill();
        customerBill.readDataset("data/CustomerBills.txt");
        String trailerContents = customerBill.displayTrailerContents(trailerNumber);

        if (trailerContents.isEmpty()) {
            System.out.println("No bills found for trailer number: " + trailerNumber);
        }
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
        System.out.print("Enter unloader to add: ");
        String unloader = scanner.nextLine();
        addUnloader(unloader);
    }

    // New method: View Assigned Trailers and Employees
    public void viewAssignedTrailersAndEmployees() {
        if (assignedDoors.isEmpty()) {
            System.out.println("No assigned doors.");
            return;
        }

        System.out.println("Assigned Trailers and Employees:");
        for (AssignedDoor ad : assignedDoors) {
            System.out.println("Door Number: " + ad.getDoorNumber() + ", Employee: " + ad.getEmployeeName());
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
                new seeManifestInteractive(scanner);
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
