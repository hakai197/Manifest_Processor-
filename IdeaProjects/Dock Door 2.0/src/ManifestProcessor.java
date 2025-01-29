import java.util.ArrayList;
import java.util.List;

public class ManifestProcessor {
    private static List<Integer> columbusDoors = new ArrayList<>();
    private static List<Integer> cincinnatiDoors = new ArrayList<>();
    private static List<Integer> daytonDoors = new ArrayList<>();
    private static List<Integer>building1Doors = new ArrayList<>();
    private static List<Integer>building2Doors = new ArrayList<>();
    private static List<Integer>building3Doors = new ArrayList<>();
    private static List<Integer>building4Doors = new ArrayList<>();
    private static List<Integer>building5Doors = new ArrayList<>();

    public static void main(String[] args) {
        initializeColumbusDoors();
        initializeCincinnatiDoors();
        initializeDaytonDoors();
        initializeBuilding1Doors();
        initializeBuilding2Doors();
        initializeBuilding3Doors();
        initializeBuilding4Doors();
        initializeBuilding5Doors();
        importManifest();
        startProcess();
    }

    public static void initializeColumbusDoors() {
        for (int i = 247; i <= 265; i++) {
            columbusDoors.add(i);
        }
        for (int i = 340; i <= 356; i++) {
            columbusDoors.add(i);
        }
    }

    public static void initializeCincinnatiDoors() {
        for (int i = 41; i <= 48; i++) {
            cincinnatiDoors.add(i);
        }
        for (int i = 51; i <= 82; i++) {
            cincinnatiDoors.add(i);
        }
        for (int i = 173; i <= 179; i++) {
            cincinnatiDoors.add(i);
        }
        for (int i = 422; i <= 425; i++) {
            cincinnatiDoors.add(i);
        }
    }

    public static void initializeDaytonDoors() {
        for (int i = 207; i <= 229; i++) {
            daytonDoors.add(i);
        }
        for (int i = 374; i <= 398; i++) {
            daytonDoors.add(i);
        }
    }
    public static void initializeBuilding1Doors() {
        for (int i = 6; i <= 240; i++) {
            columbusDoors.add(i);
        }
        for (int i = 426; i <= 468; i++) {
            columbusDoors.add(i);
        }
    }
    public static void initializeBuilding2Doors() {
        for (int i =83; i <= 115; i++) {
            columbusDoors.add(i);
        }
        for (int i = 135; i <= 172; i++) {
            columbusDoors.add(i);
        }
    }
    public static void initializeBuilding3Doors() {
        for (int i = 190; i <= 205; i++) {
            columbusDoors.add(i);
        }
        for (int i = 399; i <= 412; i++) {
            columbusDoors.add(i);
        }
    }
    public static void initializeBuilding4Doors() {
        for (int i = 230; i <= 246; i++) {
            columbusDoors.add(i);
        }
        for (int i = 357; i <= 373; i++) {
            columbusDoors.add(i);
        }
    }public static void initializeBuilding5Doors() {
        for (int i = 266; i <= 299; i++) {
            columbusDoors.add(i);
        }
        for (int i = 304; i <= 332; i++) {
            columbusDoors.add(i);
        }
    }

// How do I import a text or image file?
    // Should I import the class at this point?
    public static void importManifest() {
        System.out.println("Manifest Imported");
    }

    public static void startProcess() {
        scanManifest();
        int doorNumber = determineDoorNumber();
        int openDoorNumber = lookForOpenDoor(doorNumber);
        assignDoor(openDoorNumber);
    }
    // An order looks like this.
    // Pro Number         Customer Address              Shipper Address               HU          Door assigned
    // 999333454           124 Main Street               456 Method Way                2                344



    public static void scanManifest() {
        System.out.println("Manifest Scanned");
    }
    // Does this need an import from above?
    public static int determineDoorNumber() {
        // Should this be its own class?
        int billsPros = countBillsPros();
        int hus = countHUs();
        int headLoad = determineHeadLoad();
        if (hasHeadLoad()) {
            headLoad = 0;
        }
    // Does this work?  A bill pro is one order.  HUs are handling units.  1 skid = 1 HU.
        int totalLoad = billsPros * hus;

        if (majorityGoesToSpecificBuilding()) {
            return assignDoorBasedOnBuilding();
        } else {
            return findFirstOpenDoor();
        }
    }

    public static int assignDoorBasedOnBuilding() {
        if (goesToBuilding(1)) {
            return findOpenDoor(cincinnatiDoors);
        } else if (goesToBuilding(2)) {
            return findOpenDoor(cincinnatiDoors);
        } else if (goesToBuilding(3)) {
            return findOpenDoor(daytonDoors);
        } else if (goesToBuilding(4)) {
            return findOpenDoor(daytonDoors);
        } else if (goesToBuilding(5)) {
            return findOpenDoor(columbusDoors);
        }
        return -1; // No building found

    }

    public static int countBillsPros() {
        return 10;
    }

    public static int countHUs() {
        return 5;
    }

    public static int determineHeadLoad() {
        if (hasBillOf8OrMoreHUs()) {
            if (hasBillOver10kWeight()) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    public static boolean hasHeadLoad() {
        return true;
    }

    public static boolean majorityGoesToSpecificBuilding() {
        return true;
    }

    public static boolean goesToBuilding(int buildingNumber) {
        return true;
    }

    public static int lookForOpenDoor(int doorNumber) {
        return doorNumber;
    }

    public static void assignDoor(int doorNumber) {
        System.out.println("Door Assigned: " + doorNumber);
    }

    public static boolean hasBillOf8OrMoreHUs() {
        return true;
    }

    public static boolean hasBillOver10kWeight() {
        return true;
    }

    public static int findFirstOpenDoor() {
        return 1;
    }

    public static int findOpenDoor(List<Integer> doors) {
        for (int door : doors) {
            if (isDoorOpen(door)) {
                return door;
            }
        }
        return -1; // No open door found


    }

    public static boolean isDoorOpen(int door) {

        return true;
    }
}
