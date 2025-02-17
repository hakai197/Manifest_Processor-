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
        customerBills.clear();
        try (Scanner scanner = new Scanner(new File("data/CustomerBills.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] bill = line.split("\\|");
                customerBills.add(bill);
            }
        } catch (IOException e) {
            System.err.println("Error reading customer bills dataset: " + e.getMessage());
        }
    }

    public void displayTrailerContents(String trailerNumber) {
        boolean found = false;
        for (String[] bill : customerBills) {
            if (bill[0].equals(trailerNumber)) {
                System.out.println("Trailer Number: " + bill[0]);
                System.out.println("Order Number: " + bill[1]);
                System.out.println("Customer Name: " + bill[2]);
                System.out.println("Customer Address: " + bill[3]);
                System.out.println("Handling Units: " + bill[4]);
                System.out.println("Weight: " + bill[5]);
                System.out.println("Delivery Door Assigned: " + bill[6]);
                System.out.println();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No bills found for trailer number: " + trailerNumber);
        }
    }
}

