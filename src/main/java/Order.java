import java.time.Instant;
import java.util.List;

public class Order {
    int id;
    String reference;
    Instant creationDatetime;
    List<DishOrder> dishOrders;

    public Order(int id, String reference, Instant creationDatetime, List<DishOrder> dishOrders) {
        this.id = id;
        this.reference = reference;
        this.creationDatetime = creationDatetime;
        this.dishOrders = dishOrders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public List<DishOrder> getDishOrders() {
        return dishOrders;
    }

    public void setDishOrders(List<DishOrder> dishOrders) {
        this.dishOrders = dishOrders;
    }


    public double getTotalAmountWithoutVAT() {
        double total = 0.0;
        for (DishOrder dishOrder : dishOrders) {
            total += dishOrder.getDish().getUnitPrice() * dishOrder.getQuantity();
        }
        return total;
    }

    public double getTotalAmountWithVAT(double vatRate) {
        return getTotalAmountWithoutVAT() * (1 + vatRate);
    }
}
