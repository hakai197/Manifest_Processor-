import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerBill {
    private List<String[]> customerBills;

    public CustomerBill() {
        this.customerBills = new ArrayList<>();
    }

    public void readDataset(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] bill = line.split("\\|");
                for (int i = 0; i < bill.length; i++) {
                    bill[i] = bill[i].trim(); // Trim any whitespace around the data
                }
                customerBills.add(bill);
            }
        } catch (IOException e) {
            System.err.println("Error reading customer bills dataset: " + e.getMessage());
        }
    }

    public String getManifestForTrailer(String trailerNumber) {
        StringBuilder results = new StringBuilder();
        boolean found = false;
        for (String[] bill : customerBills) {
            if (bill[0].equals(trailerNumber)) {
                results.append("Trailer Number: ").append(bill[0]).append("\n")
                        .append("Order Number: ").append(bill[1]).append("\n")
                        .append("Customer Name: ").append(bill[2]).append("\n")
                        .append("Customer Address: ").append(bill[3]).append("\n")
                        .append("Handling Units: ").append(bill[4]).append("\n")
                        .append("Weight: ").append(bill[5]).append("\n")
                        .append("Delivery Door Assigned: ").append(bill[6]).append("\n\n");
                found = true;
            }
        }
        if (!found) {
            results.append("No bills found for trailer number: ").append(trailerNumber).append("\n");
        }
        return results.toString();
    }
}
