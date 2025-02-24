package test;



import com.manifestprocessor.model.Unloader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.FileWriter;
import java.io.IOException;

class UnloaderTest {

    private static final String TEST_FILE_PATH = "test_unloaders.txt";
    private Unloader unloader;

    @BeforeEach
    void setUp() throws IOException {
        // Create a test file with sample data
        try (FileWriter writer = new FileWriter(TEST_FILE_PATH)) {
            writer.write("John Doe | Day Shift | 12345\n");
            writer.write("Jane Smith | Night Shift | 67890\n");
            writer.write("Invalid Record\n"); // Invalid record for testing
        }
        unloader = new Unloader(TEST_FILE_PATH);
    }

    @Test
    void testProcessRecord() {
        // Verify that processRecord works correctly
        unloader.processRecord(new String[]{"John Doe", "Day Shift", "12345"});
        // Output will be printed to the console, so we can't assert it directly
    }

    @Test
    void testGetUnloaderInfo() {
        // Test retrieving unloader info for an existing unloader
        String expectedOutput = "Employee name: John Doe\n" +
                "Shift: Day Shift\n" +
                "Employee Number: 12345\n";
        String result = unloader.getUnloaderInfo("John Doe");
        assertEquals(expectedOutput, result, "Unloader info for John Doe should match");

        // Test retrieving unloader info for a non-existent unloader
        String nonExistentResult = unloader.getUnloaderInfo("Non Existent");
        assertEquals("", nonExistentResult, "Unloader info for non-existent unloader should be empty");
    }

    @Test
    void testViewUnloaders() {
        // Test viewing all unloaders
        unloader.viewUnloaders();
        // Output will be printed to the console, so we can't assert it directly
    }



    @AfterEach
    void tearDown() {
        // Clean up the test file after each test
        java.io.File file = new java.io.File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }
}