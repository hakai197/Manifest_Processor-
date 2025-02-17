import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManifestProcessor {
    private List<Integer> availableDoors;
    private List<String> customerBills;
    private List<String> unloaders;
    private CustomerBill customerBillProcessor;
    private List<AssignedDoor> assignedDoors;

    public ManifestProcessor() {
        this.availableDoors = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            availableDoors.add(i);
        }
        this.customerBills = new ArrayList<>();
        this.unloaders = new ArrayList<>();
        this.customerBillProcessor = new CustomerBill();
        this.assignedDoors = new ArrayList<>();
    }

    // Assign Door
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

        if (!availableDoors.contains(doorNumber)) {
            System.out.println("Invalid door number.");
            return;
        }

        availableDoors.remove(Integer.valueOf(doorNumber));
        unloaders.remove(employeeName);
        assignedDoors.add(new AssignedDoor(doorNumber, employeeName));

        System.out.println("Door " + doorNumber + " has been assigned to trailer " + trailerNumber + " with employee " + employeeName + ".");
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
    public void seeManifestInteractive(Scanner scanner) {
        System.out.print("Enter trailer number: ");
        String trailerNumber = scanner.nextLine();

        // Load customer bills from the specified file path
        customerBillProcessor.readDataset("data/CustomerBills.txt");

        // Display the contents of the specified trailer
        customerBillProcessor.displayTrailerContents(trailerNumber);
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

    // Add Unloader and update employees.txt
    public void addUnloader(String unloader) {
        try (FileWriter writer = new FileWriter("employees.txt", true)) {
            writer.write(unloader + System.lineSeparator());
            unloaders.add(unloader);
            System.out.println("Unloader added: " + unloader);
        } catch (IOException e) {
            System.err.println("Error writing to unloaders dataset: " + e.getMessage());
        }
    }

    // Add Customer Bill
    public void addCustomerBill(String trailerNumber, String orderNumber, String customerName, String customerAddress, String handlingUnits, String weight, String deliveryDoorAssignment) {
        String customerBill = String.join(", ", trailerNumber, orderNumber, customerName, customerAddress, handlingUnits, weight, deliveryDoorAssignment);
        customerBills.add(customerBill);
        System.out.println("Customer bill added: " + customerBill);
    }

    // Load and display customer bills by trailer number
    public void loadAndDisplayCustomerBillsInteractive(Scanner scanner) {
        System.out.print("Enter file path for customer bills dataset (e.g., CustomerBills.txt): ");
        String filePath = scanner.nextLine();
        customerBillProcessor.readDataset(filePath);

        System.out.print("Enter trailer number: ");
        String trailerNumber = scanner.nextLine();

        customerBillProcessor.displayTrailerContents(trailerNumber);
    }

    // Methods for user interactions
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
            System.out.println("5. Add Customer Bill");
            System.out.println("6. Add Unloader");
            System.out.println("7. Load and Display Customer Bills");
            System.out.println("8. View Assigned Trailers and Employees");
            System.out.println("9. Exit");
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
                processor.addCustomerBillInteractive(scanner);
            } else if (choice == 6) {
                processor.addUnloaderInteractive(scanner);
            } else if (choice == 7) {
                processor.loadAndDisplayCustomerBillsInteractive(scanner);
            } else if (choice == 8) {
                processor.viewAssignedTrailersAndEmployees();
            } else if (choice == 9) {
                running = false;
                System.out.println("Exiting...");
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}

class AssignedDoor {
    private int doorNumber;
    private String employeeName;

    public AssignedDoor(int doorNumber, String employeeName) {
        this.doorNumber = doorNumber;
        this.employeeName = employeeName;
    }

    public int getDoorNumber() {
        return doorNumber;
    }

    public String getEmployeeName() {
        return employeeName;
    }
}

