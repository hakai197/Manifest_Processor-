public class Timer {
        public static void main(String[] args) {
            int totalDoors = 470;
            int doorsPerSide = 115;
            int doorsInMainHall = totalDoors - (doorsPerSide * 2);


            int secondsPerDoor = 10;


            int totalTimeInSeconds = totalDoors * secondsPerDoor;


            int hours = totalTimeInSeconds / 3600;
            int minutes = (totalTimeInSeconds % 3600) / 60;
            int seconds = totalTimeInSeconds % 60;
        }
    }


