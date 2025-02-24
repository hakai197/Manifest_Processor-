package test;


import com.manifestprocessor.model.CustomerBill;
import com.manifestprocessor.model.Unloader;
import com.manifestprocessor.util.ManifestProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class ManifestProcessorTest {

    private static final String UNLOADER_FILE_PATH = "src/test/resources/Employee.txt";
    private static final String CUSTOMER_BILL_FILE_PATH = "src/test/resources/CustomerBills.txt";
    private ManifestProcessor processor;

    @BeforeEach
    void setUp() throws IOException {
        // Create test files with sample data
        TestFileUtils.createTestFile(UNLOADER_FILE_PATH, "John Doe | Day Shift | 12345\nJane Smith | Night Shift | 67890\n");
        TestFileUtils.createTestFile(CUSTOMER_BILL_FILE_PATH, "TR123 | ORD456 | John Doe | 123 Main St | 2 | 150.5 | Door 1\n");

        processor = new ManifestProcessor(UNLOADER_FILE_PATH, CUSTOMER_BILL_FILE_PATH);
    }

    @Test
    void testAssignDoor() {
        Scanner scanner = new Scanner(System.in);
        processor.assignDoor(scanner, "TR123", "John Doe", 1);
        assertTrue(processor.getAssignedDoors().containsKey(1), "Door 1 should be assigned");
        assertEquals("John Doe", processor.getAssignedDoors().get(1), "Door 1 should be assigned to John Doe");
    }

    @Test
    void testReleaseDoor() {
        Scanner scanner = new Scanner(System.in);
        processor.assignDoor(scanner, "TR123", "John Doe", 1);
        processor.releaseDoor(1);
        assertFalse(processor.getAssignedDoors().containsKey(1), "Door 1 should be released");
    }

    @Test
    void testSeeManifest() {
        String manifest = processor.seeManifest("TR123");
        assertTrue(manifest.contains("Trailer Number: TR123"), "Manifest should contain trailer number TR123");
    }

    @Test
    void testAddCustomerBill() {
        processor.addCustomerBill("TR456", "ORD789", "Jane Smith", "456 Elm St", "1", "75.0", "Door 2");
        String manifest = processor.seeManifest("TR456");
        assertTrue(manifest.contains("Trailer Number: TR456"), "Manifest should contain trailer number TR456");
    }

    @Test
    void testViewUnloaders() {
        processor.viewUnloaders(); // Output will be printed to the console
    }

    @Test
    void testViewAssignedTrailerAndEmployees() {
        Scanner scanner = new Scanner(System.in);
        processor.assignDoor(scanner, "TR123", "John Doe", 1);
        processor.viewAssignedTrailerAndEmployees(); // Output will be printed to the console
    }

    @Test
    void testInitializeWithInvalidFile() {
        assertThrows(IOException.class, () -> {
            new ManifestProcessor("invalid_file.txt", "invalid_file.txt");
        }, "Initialization with invalid file paths should throw IOException");
    }

    @AfterEach
    void tearDown() {
        // Clean up test files
        TestFileUtils.deleteTestFile(UNLOADER_FILE_PATH);
        TestFileUtils.deleteTestFile(CUSTOMER_BILL_FILE_PATH);
    }
}