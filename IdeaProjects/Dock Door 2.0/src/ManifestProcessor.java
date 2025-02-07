import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManifestProcessor {

    private static final List<Integer> cincinnatiDoors = new ArrayList<>();
    private static final List<Integer> daytonDoors = new ArrayList<>();
    private static final  List<Integer> columbusDoors = new ArrayList<>();
    private static final List<Integer> building1Doors = new ArrayList<>();
    private static final List<Integer> building2Doors = new ArrayList<>();
    private static final  List<Integer> building3Doors = new ArrayList<>();
    private static final List<Integer> building4Doors = new ArrayList<>();
    private static final List<Integer> building5Doors = new ArrayList<>();
    private static  final Set<Integer> assignedDoors = new HashSet<>();

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

    public static void initializeColumbusDoors() {
        for (int i = 247; i <= 265; i++) {
            columbusDoors.add(i);
        }
        for (int i = 340; i <= 356; i++) {
            columbusDoors.add(i);
        }
    }

    public static void initializeBuilding1Doors() {
        for (int i = 6; i <= 240; i++) {
            building1Doors.add(i);
        }
        for (int i = 426; i <= 468; i++) {
            building1Doors.add(i);
        }
    }

    public static void initializeBuilding2Doors() {
        for (int i = 83; i <= 115; i++) {
            building2Doors.add(i);
        }
        for (int i = 135; i <= 172; i++) {
            building2Doors.add(i);
        }
    }

    public static void initializeBuilding3Doors() {
        for (int i = 190; i <= 205; i++) {
            building3Doors.add(i);
        }
        for (int i = 399; i <= 412; i++) {
            building3Doors.add(i);
        }
    }

    public static void initializeBuilding4Doors() {
        for (int i = 230; i <= 246; i++) {
            building4Doors.add(i);
        }
        for (int i = 357; i <= 373; i++) {
            building4Doors.add(i);
        }
    }

    public static void initializeBuilding5Doors() {
        for (int i = 266; i <= 299; i++) {
            building5Doors.add(i);
        }
        for (int i = 304; i <= 332; i++) {
            building5Doors.add(i);
        }
    }

    public static void importManifest() {
        System.out.println("Manifest Imported");
    }

    public static void startProcess() {
        scanManifest();
        int doorNumber = determineDoorNumber();
        int openDoorNumber = lookForOpenDoor(doorNumber);
        assignDoor(openDoorNumber);
    }

    public static void scanManifest() {
        System.out.println("Manifest Scanned");
    }

    public static int determineDoorNumber() {
        int billsPros = countBillsPros();
        int hus = getTotalHUs();
        int headLoad = determineHeadLoad();
        if (hasHeadLoad()) {
            headLoad = 0;
        }
        int totalLoad = billsPros * hus;

        if (majorityGoesToSpecificBuilding(hus)) {
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
        return -1;
    }

    public static int countBillsPros() {
        return CustomerBill.manifest.size();
    }

    public static int getTotalHUs() {
        int totalHUs = 0;
        for (CustomerBill cb : CustomerBill.manifest.values()) {
            totalHUs += cb.getHandlingUnits();
        }
        return totalHUs;
    }

    public static int getTotalWeight() {
        int totalWeight = 0;
        for (CustomerBill cb : CustomerBill.manifest.values()) {
            totalWeight += cb.getWeight();
        }
        return totalWeight;
    }

    public static int determineHeadLoad() {
        if (hasBillOf8OrMoreHUs() || hasBillOver10kWeight()) {
            return 1;
        }
        return 0;
    }

    public static boolean hasHeadLoad() {
        return determineHeadLoad() == 1;
    }

    public static boolean hasBillOf8OrMoreHUs() {
        for (CustomerBill cb : CustomerBill.manifest.values()) {
            if (cb.getHandlingUnits() >= 8) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasBillOver10kWeight() {
        for (CustomerBill cb : CustomerBill.manifest.values()) {
            if (cb.getWeight() >= 10000) {
                return true;
            }
        }
        return false;
    }

    public static boolean majorityGoesToSpecificBuilding(int totalHUs) {
        int totalDoors = cincinnatiDoors.size() + daytonDoors.size() + columbusDoors.size() +
                building1Doors.size() + building2Doors.size() + building3Doors.size() +
                building4Doors.size() + building5Doors.size();

        double percentage = (double) totalHUs / totalDoors;
        return percentage >= 0.25;
    }

    public static boolean goesToBuilding(int buildingNumber) {
        return true;
    }

    public static int lookForOpenDoor(int doorNumber) {
        return doorNumber;
    }

    public static void assignDoor(int doorNumber) {
        if (isDoorAssigned(doorNumber)) {
            System.out.println("Door " + doorNumber + " is already assigned, looking for the next available door.");
            doorNumber = findFirstOpenDoor();
        }

        System.out.println("Door Assigned: " + doorNumber);
        assignedDoors.add(doorNumber);
    }

    public static int findFirstOpenDoor() {
        List<Integer> allDoors = new ArrayList<>();
        allDoors.addAll(cincinnatiDoors);
        allDoors.addAll(daytonDoors);
        allDoors.addAll(columbusDoors);
        allDoors.addAll(building1Doors);
        allDoors.addAll(building2Doors);
        allDoors.addAll(building3Doors);
        allDoors.addAll(building4Doors);
        allDoors.addAll(building5Doors);

        for (int door : allDoors) {
            if (!isDoorAssigned(door)) {
                return door;
            }
        }
        return -1;
    }

    public static int findOpenDoor(List<Integer> doors) {
        for (int door : doors) {
            if (!isDoorAssigned(door)) {
                return door;
            }
        }
        return -1;
    }

    public static boolean isDoorAssigned(int door) {
        return assignedDoors.contains(door);
    }
}
