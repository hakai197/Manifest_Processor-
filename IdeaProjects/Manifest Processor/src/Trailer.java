public class Trailer {
    private String trailerNumber;
    private String dockDoorAssignment;
    private Manifest manifest;

    public Trailer(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public void assignDockDoor(String dockDoorAssignment) {
        this.dockDoorAssignment = dockDoorAssignment;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    @Override
    public String toString() {
        return String.format("Trailer: %s, Dock Door: %s\n%s", trailerNumber, dockDoorAssignment, manifest.toString());
    }

    public String getTrailerNumber() {
        return trailerNumber;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public String getDockDoor() {
        return dockDoorAssignment;
    }
}
