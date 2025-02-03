import java.util.ArrayList;
import java.util.List;

public class Manifest {
    private List<CustomerBill> customerBills;

    public Manifest() {
        this.customerBills = new ArrayList<>();
    }

    public void addCustomerBill(CustomerBill customerBill) {
        this.customerBills.add(customerBill);
    }

    public List<CustomerBill> getCustomerBills() {
        return customerBills;
    }

    public void removeCustomerBill(CustomerBill customerBill) {
        this.customerBills.remove(customerBill);
    }

    public CustomerBill findCustomerBillByProNumber(String proNumber) {
        for (CustomerBill bill : customerBills) {
            if (bill.getProNumber().equals(proNumber)) {
                return bill;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Manifest:\n");
        for (CustomerBill bill : customerBills) {
            sb.append(bill.toString()).append("\n");
        }
        return sb.toString();
    }
}




