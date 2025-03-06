package com.manifestprocessor.model;

import java.io.IOException;
import java.util.List;

public class Unloader extends DatasetReader {
    public Unloader(String filePath) throws IOException {
        super(filePath);
    }

    @Override
    protected int getExpectedFieldCount() {
        return 3;
    }

    @Override
    public void processRecord(String[] record) {

        if (!isValidRecord(record)) {
            return;
        }

        String employeeName = record[0];
        String shift = record[1];
        String employeeNumber = record[2];

        System.out.printf("Processing Unloader: Name=%s, Shift=%s, Employee Number=%s%n",
                employeeName, shift, employeeNumber);
    }
    //      May use this method later
    public String getUnloaderInfo(String unloaderName) {
        String results = "";
        for (String[] unloader : getRecords()) {

            if (!isValidRecord(unloader)) {
                continue;
            }

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

            if (!isValidRecord(unloader)) {
                continue;
            }

            System.out.printf("Employee name: %s, Shift: %s, Employee Number: %s%n",
                    unloader[0], unloader[1], unloader[2]);
        }
    }
}