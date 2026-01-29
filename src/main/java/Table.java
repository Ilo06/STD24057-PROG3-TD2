import java.time.Instant;
import java.util.List;

public class Table {
    private int id;
    private int number;
    private List<Order> orders;

    public Table(int id, int number, List<Order> orders) {
        this.id = id;
        this.number = number;
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isAvailable(Instant t) {
        for (Order order : orders) {;
            if (order.getCreationDatetime().isBefore(t)) {
                return false;
            }
        }
        return true;
    }
}
