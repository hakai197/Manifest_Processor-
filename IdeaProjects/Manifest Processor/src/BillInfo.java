public class BillInfo {
    private String orderNumber;
    private String customerName;
    private String customerAddress;
    private String handlingUnits;
    private String weight;
    private String deliveryDoorAssignment;

    public BillInfo(String orderNumber, String customerName, String customerAddress, String handlingUnits, String weight, String deliveryDoorAssignment) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.handlingUnits = handlingUnits;
        this.weight = weight;
        this.deliveryDoorAssignment = deliveryDoorAssignment;
    }

    @Override
    public String toString() {
        return "Order Number: " + orderNumber + ", Customer Name: " + customerName + ", Customer Address: " + customerAddress +
                ", Handling Units: " + handlingUnits + ", Weight: " + weight + ", Delivery Door Assignment: " + deliveryDoorAssignment;
    }
}
