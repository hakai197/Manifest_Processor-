import java.util.*;

public class ManifestProcessor {
    private List<Integer> availableDoors;
    private Map<Integer, String> assignedDoors;
    private Unloader unloader;
    private CustomerBill customerBill;

    public ManifestProcessor(String unloaderFilePath, String customerBillFilePath) {
        this.availableDoors = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            availableDoors.add(i);
        }
        this.assignedDoors = new HashMap<>();
        this.unloader = new Unloader(unloaderFilePath);
        this.customerBill = new CustomerBill(customerBillFilePath);
    }

    public void assignDoor(String trailerNumber, String employeeName, int doorNumber) {
        if (!availableDoors.contains(doorNumber)) {
            System.out.println("Invalid door number.");
            return;
        }

        boolean employeeExists = unloader.getRecords().stream()
                .anyMatch(record -> record[0].equals(employeeName));

        if (!employeeExists) {
            System.out.println("Invalid employee name.");
            return;
        }

        availableDoors.remove(Integer.valueOf(doorNumber));
        assignedDoors.put(doorNumber, employeeName);

        System.out.println("Door " + doorNumber + " has been assigned to trailer " + trailerNumber + " with employee " + employeeName + ".");
    }

    public void releaseDoor(int doorNumber) {
        if (assignedDoors.containsKey(doorNumber)) {
            String employeeName = assignedDoors.remove(doorNumber);
            availableDoors.add(doorNumber);
            System.out.println("Door " + doorNumber + " and employee " + employeeName + " have been released.");
        } else {
            System.out.println("Door " + doorNumber + " is not currently assigned.");
        }
    }

    public void viewAssignedTrailersAndEmployees() {
        if (assignedDoors.isEmpty()) {
            System.out.println("No assigned doors.");
        } else {
            System.out.println("Assigned Trailers and Employees:");
            assignedDoors.forEach((door, employee) ->
                    System.out.println("Door Number: " + door + ", Employee: " + employee));
        }
    }

    public void viewUnloaders() {
        unloader.viewUnloaders();
    }

    public void addCustomerBill(String trailerNumber, String orderNumber, String customerName, String customerAddress, String handlingUnits, String weight, String deliveryDoorAssignment) {
        String bill = trailerNumber + " | " + orderNumber + " | " + customerName + " | " + customerAddress + " | " + handlingUnits + " | " + weight + " | " + deliveryDoorAssignment;
        customerBill.addRecord(bill);
        System.out.println("Customer Bill added.");
    }

    public String seeManifest(String trailerNumber) {
        return customerBill.getManifestForTrailer(trailerNumber);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ManifestProcessor processor = new ManifestProcessor("data/Employees.txt", "data/CustomerBills.txt");

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
                System.out.print("Enter trailer number: ");
                String trailerNumber = scanner.nextLine();
                System.out.print("Enter employee name: ");
                String employeeName = scanner.nextLine();
                System.out.print("Enter door number: ");
                int doorNumber = scanner.nextInt();
                scanner.nextLine();
                processor.assignDoor(trailerNumber, employeeName, doorNumber);
            } else if (choice == 2) {
                System.out.print("Enter door number to release: ");
                int doorNumber = scanner.nextInt();
                scanner.nextLine();
                processor.releaseDoor(doorNumber);
            } else if (choice == 3) {
                System.out.print("Enter trailer number: ");
                String trailerNumber = scanner.nextLine();
                System.out.println(processor.seeManifest(trailerNumber));
            } else if (choice == 4) {
                processor.viewUnloaders();
            } else if (choice == 5) {
                System.out.print("Enter trailer number: ");
                String trailerNumber = scanner.nextLine();
                System.out.print("Enter order number: ");
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
                processor.addCustomerBill(trailerNumber, orderNumber, customerName, customerAddress, handlingUnits, weight, deliveryDoorAssignment);
            } else if (choice == 6) {
                processor.viewAssignedTrailersAndEmployees();
            } else if (choice == 7) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }
}
