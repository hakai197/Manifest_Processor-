import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Unloader {
    private List<String[]> unloaders;

    public Unloader() {
        this.unloaders = new ArrayList<>();
    }

    public List<String[]> getUnloaders() {
        return unloaders;
    }

    public void readDataset(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] unloader = line.split("\\|"); // Split the line into an array
                if (unloader.length == 3) { // Ensure there are 3 fields
                    unloaders.add(unloader); // Add the array to the list
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading dataset from file: " + filePath + " - " + e.getMessage());
        }
    }

    public String getUnloaderInfo(String unloaderName) {
        StringBuilder results = new StringBuilder();
        for (String[] unloader : unloaders) {
            if (unloader[0].equals(unloaderName)) {
                results.append("Employee name: ").append(unloader[0]).append("\n")
                        .append("Shift: ").append(unloader[1]).append("\n")
                        .append("Employee Number: ").append(unloader[2]).append("\n");
                break; // Exit loop once the unloader is found
            }
        }
        return results.toString();
    }

    public void viewUnloaders() {
        if (unloaders.isEmpty()) {
            System.out.println("No unloaders available.");
        } else {
            System.out.println("Unloaders:");
            for (String[] unloader : unloaders) {
                System.out.printf("Employee name: %s, Shift: %s, Employee Number: %s%n", unloader[0], unloader[1], unloader[2]);
            }
        }
    }

}
