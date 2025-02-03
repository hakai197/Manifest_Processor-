import java.util.ArrayList;
import java.util.List;

public class CustomerBill {

// Should this be a part of a Manifest Class?   
    private String proNumber;
    private String customerName;
    private String customerAddress;
    private String shipperName;
    private String shipperAddress;
    private int handlingUnits;
    private String dockDoorAssigned;
    private int weight;
    private String serviceDate;
    private List <String> tags;


    public CustomerBill(String proNumber, String customerName, String customerAddress,
                        String shipperName, String shipperAddress, int handlingUnits,int weight,
                        String serviceDate,
                        String dockDoorAssigned) {
        this.proNumber = proNumber;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.shipperName = shipperName;
        this.shipperAddress = shipperAddress;
        this.handlingUnits = handlingUnits;
        this.dockDoorAssigned = dockDoorAssigned;
        this.weight= weight;
        this.tags = new ArrayList<>();
        this.serviceDate = serviceDate;
    }


    public String getProNumber() {

        return proNumber;
    }

    public void setProNumber(String proNumber) {

        this.proNumber = proNumber;
    }

    public String getCustomerName() {

        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {

        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getShipperName() {

        return shipperName;
    }

    public void setShipperName(String shipperName) {

        this.shipperName = shipperName;
    }

    public String getShipperAddress() {

        return shipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {

        this.shipperAddress = shipperAddress;
    }

    public int getHandlingUnits() {

        return handlingUnits;
    }

    public void setHandlingUnits(int handlingUnits) {

        this.handlingUnits = handlingUnits;
    }

    public String getDockDoorAssigned() {

        return dockDoorAssigned;
    }

    public void setDockDoorAssigned(String dockDoorAssigned) {

        this.dockDoorAssigned = dockDoorAssigned;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String toString() {
        return "CustomerBill{" +
                "ProNumber='" + proNumber + '\'' +
                ", CustomerName='" + customerName + '\'' +
                ", CustomerAddress='" + customerAddress + '\'' +
                ", ShipperName='" + shipperName + '\'' +
                ", ShipperAddress=" + shipperAddress + '\'' + ", " +
                "HandlingUnits=" + handlingUnits + ", " +
                "Weight=" + weight + "lbs, " +
                "Service Date=" + serviceDate +
                "Tags=" + tags +
                "DockDoorAssigned='" + dockDoorAssigned + '\'' + '}';
    }
}