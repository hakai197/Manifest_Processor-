import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBill {
    private String orderNumber;
    private String customerName;
    private String customerAddress;
    private int handlingUnits;
    private double weight;
    private String deliveryAssignment;

    public CustomerBill(String orderNumber, String customerName, String customerAddress, int handlingUnits, double weight, String deliveryAssignment) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer Name cannot be Blank");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be more than 0");
        }
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.handlingUnits = handlingUnits;
        this.weight = weight;
        this.deliveryAssignment = deliveryAssignment;
    }

    @Override
    public String toString() {
        return String.format("Order: %s, Customer: %s, Address: %s, Units: %d, Weight: %.2f, Assignment: %s",
                orderNumber, customerName, customerAddress, handlingUnits, weight, deliveryAssignment);
    }

    public double getWeight() {
        return weight;
    }

    // Static method to read CustomerBill data from multiple files
    public static List<CustomerBill> readFromFiles(List<String> files) throws IOException {
        List<CustomerBill> bills = new ArrayList<>();
        for (String file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\hakai\\Desktop\\CustomerBills\\CustomerBill1.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length != 6) {
                        throw new IOException("Invalid file format. Expected 6 fields per line.");
                    }
                    CustomerBill bill = getCustomerBill(data);
                    bills.add(bill);
                }
            }
        }
        return bills;
    }

    private static CustomerBill getCustomerBill(String[] data) {
        String orderNumber = data[0].trim();
        String customerName = data[1].trim();
        String customerAddress = data[2].trim();
        int handlingUnits = Integer.parseInt(data[3].trim());
        double weight = Double.parseDouble(data[4].trim());
        String deliveryAssignment = data[5].trim();

        CustomerBill bill = new CustomerBill(orderNumber, customerName, customerAddress, handlingUnits, weight, deliveryAssignment);
        return bill;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void writeToFile(String s) {
    }
}
