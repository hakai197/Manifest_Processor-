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
        String results = "";
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
        return results;
    }
}
