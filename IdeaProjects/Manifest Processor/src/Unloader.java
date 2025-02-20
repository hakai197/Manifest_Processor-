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
                unloaders.add(unloader); // Add the array to the list
            }
        } catch (IOException e) {
            System.out.println("Error reading Employee dataset: " + e.getMessage());
        }
    }

    public String getUnloaderInfo(String unloaderName) {
        String results = "";
        for (String[] unloader : unloaders) {
            if (unloader[0].equals(unloaderName)) {
                results = "Employee name: " + unloader[0] + "\n" +
                        "Shift: " + unloader[1] + "\n" +
                        "Employee Number: " + unloader[2] + "\n";
                break; // Exit loop once the unloader is found
            }
        }
        return results;
    }

    public void viewUnloaders() {
        if (unloaders.isEmpty()) {
            System.out.println("No unloaders available.");
        } else {
            System.out.println("Unloaders:");
            for (String[] unloader : unloaders) {
                System.out.println("Employee name: " + unloader[0] +
                        ", Shift: " + unloader[1] +
                        ", Employee Number: " + unloader[2]);
            }
        }
    }
}
