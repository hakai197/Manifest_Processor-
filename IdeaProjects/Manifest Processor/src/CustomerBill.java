import java.io.*;
import java.util.*;

public class CustomerBill extends DatasetReader {
    public CustomerBill(String filePath) {
        super(filePath);
    }

    @Override
    public void processRecord(String[] record) {
        // Processing logic for customer bills can be implemented here
    }

    public String getManifestForTrailer(String trailerNumber) {
        String results = "";
        boolean found = false;
        for (String[] bill : getRecords()) {
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