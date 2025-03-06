package com.manifestprocessor.util;

import java.util.Scanner;
//Door assignment Interface
// Can I use the methods from the ManifestProcessor here or is it better just to use the override?
public interface DoorAssignment {
    void assignDoor(Scanner scanner, String trailerNumber, String employeeName, int doorNumber);
    boolean releaseDoor (int doorNumber);
    void viewAssignedTrailerAndEmployees();
}