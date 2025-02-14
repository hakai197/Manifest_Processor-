import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Manifest {
    private String trailerNumber;
    private List<CustomerBill> customerBills;

    public Manifest(String trailerNumber) {
        this.trailerNumber = trailerNumber;
        this.customerBills = new ArrayList<>();
    }

    public void addCustomerBill(CustomerBill bill) {
        customerBills.add(bill);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trailer Number: ").append(trailerNumber).append("\n");
        for (CustomerBill bill : customerBills) {
            sb.append(bill.toString()).append("\n");
        }
        return sb.toString();
    }

    public List<CustomerBill> getCustomerBills() {
        return customerBills;
    }

    // Method to write manifest details to a file using the specified format
    public void writeToFile() {
        String fileName = "C:\\Users\\hakai\\Desktop\\CustomerBills\\Trailer Number_" + trailerNumber + "_Manifest.txt"; // Updated file name format
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(this.toString()); // Write the manifest details to the file
            System.out.println("Manifest written to file: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
