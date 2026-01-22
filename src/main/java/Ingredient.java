import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Ingredient {
    private int id;
    private String name;
    private Double price;
    private CategoryEnum category;
    private Dish dish;
    private List<StockMovement> stockMovementList = new ArrayList<>();


    public StockValue getStockValueAt(Instant t){
        double currentQuantity = 0.0;

        for (StockMovement movement : stockMovementList) {
            if (movement.getCreationDatetime().isBefore(t) || movement.getCreationDatetime().equals(t)) {
                if (movement.getMovementType() == MovementTypeEnum.IN) {
                    currentQuantity += movement.getValue().getQuantity();
                } else if (movement.getMovementType() == MovementTypeEnum.OUT) {
                    currentQuantity -= movement.getValue().getQuantity();
                }
            }
        }

        return new StockValue((int) currentQuantity, UnitType.KG);
    }


    public Ingredient(int id, String name, Double price, CategoryEnum category, Dish dish) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.dish = dish;
    }

    public Ingredient(String name, Double price, CategoryEnum category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }


    public List<StockMovement> getStockMovementList() {
        return stockMovementList;
    }

    public void setStockMovementList(List<StockMovement> stockMovementList) {
        this.stockMovementList = stockMovementList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }


    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public String getDishName() {
        return dish == null ? null : dish.getName();
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", dish=" + getDishName() +
                '}';
    }
}
