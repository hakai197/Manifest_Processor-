import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerBill {
    private int trailerNumber;
    private String orderNumber;
    private String customerName;
    private String customerAddress;
    private int handlingUnits;
    private int weight;
    private String deliveryDoorAssignment;

    private List<String[]> customerBills; // Declare the list to store customer bills

    public CustomerBill() {
        this.trailerNumber = trailerNumber;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.handlingUnits = handlingUnits;
        this.weight = weight;
        this.deliveryDoorAssignment = deliveryDoorAssignment;
        this.customerBills = new ArrayList<>(); // Initialize the list
    }

    public int getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(int trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getHandlingUnits() {
        return handlingUnits;
    }

    public void setHandlingUnits(int handlingUnits) {
        this.handlingUnits = handlingUnits;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDeliveryDoorAssignment() {
        return deliveryDoorAssignment;
    }

    public void setDeliveryDoorAssignment(String deliveryDoorAssignment) {
        this.deliveryDoorAssignment = deliveryDoorAssignment;
    }

    public void readDataset(String filePath) {
        try (Scanner scanner = new Scanner(new File("data/CustomerBills.txt"))) { // Use the provided filePath
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] bill = line.split("\\|");
                customerBills.add(bill);
            }
        } catch (IOException e) {
            System.err.println("Error reading customer bills dataset: " + e.getMessage());
        }
    }

    public String displayTrailerContents(String trailerNumber) {
        String results = ""; // Use a simple string to build the result
        boolean found = false;
        for (String[] bill : customerBills) {
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
        return results; // Return the built string
    }
}