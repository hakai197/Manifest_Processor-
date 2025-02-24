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

    public void setRecords(List<String[]> records) {
        this.records = records;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    private void readDataset() throws IOException {
        records.clear();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                records.add(scanner.nextLine().split(" \\| "));
            }
        }
    }

    public List<String[]> getRecords() {
        return records;
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

    public abstract void processRecord(String[] record);
}