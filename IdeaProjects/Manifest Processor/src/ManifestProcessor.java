import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class ManifestProcessor {
    private Map<String, Trailer> trailers = new HashMap<>();
    private Map<String, String> dockDoorAssignments = new HashMap<>();

    public void addTrailer(Trailer trailer) {
        trailers.put(trailer.getTrailerNumber().toLowerCase(), trailer);
    }

    public void assignDockDoorToTrailer(String trailerNumber) {
        Trailer trailer = trailers.get(trailerNumber.toLowerCase());
        if (trailer != null) {
            String dockDoor = findAvailableDockDoor(trailer);
            if (dockDoor != null) {
                trailer.assignDockDoor(dockDoor.toLowerCase());
                dockDoorAssignments.put(dockDoor.toLowerCase(), trailerNumber.toLowerCase());
                System.out.println("Dock door " + dockDoor.toLowerCase() + " assigned to trailer " + trailerNumber.toLowerCase());
            } else {
                System.out.println("No available dock door found for trailer " + trailerNumber.toLowerCase());
            }
        } else {
            System.out.println("Trailer not found.");
        }
    }

    public void releaseDockDoorFromTrailer(String trailerNumber) {
        Trailer trailer = trailers.get(trailerNumber.toLowerCase());
        if (trailer != null) {
            String dockDoor = trailer.getDockDoor();
            if (dockDoor != null) {
                trailer.assignDockDoor(null);
                dockDoorAssignments.remove(dockDoor.toLowerCase());
                System.out.println("Dock door released for trailer " + trailerNumber.toLowerCase());
            } else {
                System.out.println("Trailer " + trailerNumber.toLowerCase() + " is not assigned to any dock door.");
            }
        } else {
            System.out.println("Trailer not found.");
        }
    }

    public void showManifest(String trailerNumber) {
        Trailer trailer = trailers.get(trailerNumber.toLowerCase());
        if (trailer != null) {
            System.out.println(trailer.toString());
        } else {
            System.out.println("Trailer not found.");
        }
    }

    public void addCustomerBillToManifest(String trailerNumber, CustomerBill bill) {
        Trailer trailer = trailers.get(trailerNumber.toLowerCase());
        if (trailer != null) {
            trailer.getManifest().addCustomerBill(bill);
            System.out.println("Customer bill added to trailer " + trailerNumber.toLowerCase());
            bill.writeToFile("C:\\Users\\hakai\\Desktop\\CustomerBills\\" + trailerNumber.toLowerCase() + ".txt");
        } else {
            System.out.println("Trailer not found.");
        }
    }

    private String findAvailableDockDoor(Trailer trailer) {
        int[] buildingCount = new int[4]; // Assuming 4 buildings
        for (CustomerBill bill : trailer.getManifest().getCustomerBills()) {
            int building = getBuilding(bill.getCustomerAddress());
            buildingCount[building]++;
        }

        // Find the building with the highest count of customer bills
        int assignedBuilding = 0;
        for (int i = 1; i < buildingCount.length; i++) {
            if (buildingCount[i] > buildingCount[assignedBuilding]) {
                assignedBuilding = i;
            }
        }

        // Calculate the percentage of customer bills assigned to that building
        int totalBills = trailer.getManifest().getCustomerBills().size();
        if (buildingCount[assignedBuilding] * 100 / totalBills >= 25) {
            // Assign a dock door from the building with the highest count
            for (int i = assignedBuilding * 50; i < (assignedBuilding + 1) * 50; i++) {
                String dockDoor = "D" + i;
                if (!dockDoorAssignments.containsKey(dockDoor.toLowerCase())) {
                    return dockDoor;
                }
            }
        }

        // If no suitable dock door found, return any available dock door
        for (int i = 0; i < 200; i++) {
            String dockDoor = "D" + i;
            if (!dockDoorAssignments.containsKey(dockDoor.toLowerCase())) {
                return dockDoor;
            }
        }
        return null; // No available dock door found
    }

    private int getBuilding(String address) {
        // Dummy implementation, you can replace it with real logic based on address
        return (address.hashCode() % 4 + 4) % 4; // Returns 0, 1, 2, or 3
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Manifest Processor Menu ---");
            System.out.println("1. Assign Door");
            System.out.println("2. Release Door");
            System.out.println("3. See Manifest");
            System.out.println("4. Add Customer Bill");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            handleChoice(choice, scanner);
        }
    }

    private void handleChoice(int choice, Scanner scanner) {
        if (choice == 1) {
            handleAssignDoor(scanner);
        } else if (choice == 2) {
            handleReleaseDoor(scanner);
        } else if (choice == 3) {
            handleSeeManifest(scanner);
        } else if (choice == 4) {
            handleAddCustomerBill(scanner);
        } else if (choice == 5) {
            System.out.println("Exiting...");
            scanner.close();
            System.exit(0);
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    private void handleAssignDoor(Scanner scanner) {
        System.out.print("Enter trailer number: ");
        String trailerNumber = scanner.nextLine().toLowerCase();
        assignDockDoorToTrailer(trailerNumber);
    }

    private void handleReleaseDoor(Scanner scanner) {
        System.out.print("Enter trailer number: ");
        String trailerNumber = scanner.nextLine().toLowerCase();
        releaseDockDoorFromTrailer(trailerNumber);
    }

    private void handleSeeManifest(Scanner scanner) {
        System.out.print("Enter trailer number: ");
        String trailerNumber = scanner.nextLine().toLowerCase();
        showManifest(trailerNumber);
    }

    private void handleAddCustomerBill(Scanner scanner) {
        System.out.print("Enter trailer number: ");
        String trailerNumber = scanner.nextLine().toLowerCase();
        System.out.print("Enter order number: ");
        String orderNumber = scanner.nextLine();
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();
        System.out.print("Enter customer address: ");
        String customerAddress = scanner.nextLine();
        System.out.print("Enter handling units: ");
        int handlingUnits = scanner.nextInt();
        System.out.print("Enter weight: ");
        double weight = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter delivery assignment: ");
        String deliveryAssignment = scanner.nextLine();

        try {
            CustomerBill bill = new CustomerBill(orderNumber, customerName, customerAddress, handlingUnits, weight, deliveryAssignment);
            addCustomerBillToManifest(trailerNumber, bill);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ManifestProcessor processor = new ManifestProcessor();
        processor.menu();
    }
}
