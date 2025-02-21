import java.io.*;
import java.util.*;

public abstract class DatasetReader {
    private List<String[]> records;
    private String filePath;

    public DatasetReader(String filePath) {
        this.filePath = filePath;
        this.records = new ArrayList<>();
        readDataset();
    }

   private void readDataset() {
        records.clear();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                records.add(scanner.nextLine().split(" \\| "));
            }
        } catch (IOException e) {
            System.err.println("Error reading dataset from " + filePath + ": " + e.getMessage());
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
