import java.util.ArrayList;
import java.util.List;

public class Manifest {
    private List<CustomerBill> customerBills;

    // Constructor
    public Manifest() {
        this.customerBills = new ArrayList<>();
    }

    // Method to add a CustomerBill
    public void addCustomerBill(CustomerBill customerBill) {
        this.customerBills.add(customerBill);
    }

    // Method to get all CustomerBills
    public List<CustomerBill> getCustomerBills() {
        return customerBills;
    }

    // Method to get a string representation of the Manifest
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Manifest:\n");
        for (CustomerBill cb : customerBills) {
            sb.append(cb.toString()).append("\n");
        }
        return sb.toString();
    }



}
