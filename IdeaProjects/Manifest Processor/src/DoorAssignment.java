import java.util.Scanner;

public interface DoorAssignment {
    void assignDoor(Scanner scanner, String trailerNumber, String employeeName, int doorNumber);
    void releaseDoor(int doorNumber);
    void viewAssignedTrailersAndEmployees();
}