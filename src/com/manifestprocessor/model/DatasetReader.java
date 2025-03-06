package com.manifestprocessor.model;

import java.io.*;
import java.util.*;

public abstract class DatasetReader {
    private List<String[]> records;
    private String filePath;

    public DatasetReader(String filePath) throws IOException {
        this.filePath = filePath;
        this.records = new ArrayList<>();
        readDataset();
    }

    public List<String[]> getRecords() {
        return records;
    }


    private void readDataset() throws IOException {
        records.clear();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] fields = line.split("\\|");
                String[] record = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    record[i] = fields[i].trim();
                }

                if (isValidRecord(record)) {
                    records.add(record);
                }
            }
        }
    }

    public void addRecord(String record) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(record);
            records.add(record.split(" \\| "));
            System.out.println("Record added: " + record);
        } catch (IOException e) {
            System.err.println("Error writing to dataset: " + e.getMessage());
        }
    }

    protected boolean isValidRecord(String[] record) {
        return record.length == getExpectedFieldCount();
    }

    protected abstract int getExpectedFieldCount();

    public abstract void processRecord(String[] record);
}
