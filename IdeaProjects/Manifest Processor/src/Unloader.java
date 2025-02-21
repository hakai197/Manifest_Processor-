import java.io.*;
import java.util.*;

public class Unloader extends DatasetReader {
    public Unloader(String filePath) {
        super(filePath);
    }

    @Override
    public void processRecord(String[] record) {

        if (record.length < 3) {
            System.err.println("Invalid record: " + Arrays.toString(record));
            return;
        }

        String employeeName = record[0];
        String shift = record[1];
        String employeeNumber = record[2];


        System.out.printf("Processing Unloader: Name=%s, Shift=%s, Employee Number=%s%n",
                employeeName, shift, employeeNumber);
    }

    public String getUnloaderInfo(String unloaderName) {
        String results = "";
        for (String[] unloader : getRecords()) {
            if (unloader[0].equals(unloaderName)) {
                results += "Employee name: " + unloader[0] + "\n";
                results += "Shift: " + unloader[1] + "\n";
                results += "Employee Number: " + unloader[2] + "\n";
                break;
            }
        }
        return results;
    }

    public void viewUnloaders() {
        List<String[]> records = getRecords();
        if (records == null || records.isEmpty()) {
            System.out.println("No unloaders available.");
            return;
        }

        System.out.println("Unloaders:");
        for (String[] unloader : records) {
            if (unloader.length < 3) {
                System.out.println("Skipping invalid record: " + Arrays.toString(unloader));
                continue; // Prevent accessing out-of-bounds indices
            }

            System.out.printf("Employee name: %s, Shift: %s, Employee Number: %s%n",
                    unloader[0], unloader[1], unloader[2]);
        }
    }
}


