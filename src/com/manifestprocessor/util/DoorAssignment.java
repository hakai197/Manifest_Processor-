package com.manifestprocessor.util;

import java.util.Scanner;
//Door assignment Interface
public interface DoorAssignment {
    void assignDoor(Scanner scanner, String trailerNumber, String employeeName, int doorNumber);
    void releaseDoor(int doorNumber);
    void viewAssignedTrailerAndEmployees();
}