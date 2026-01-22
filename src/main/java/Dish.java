import java.util.List;

public class Dish {
    private int id;
    private String name;
    private DishTypeEnum dishType;
    private List<DishIngredient> ingredients;
    private Double unitPrice;

    public Dish(int id, String name, DishTypeEnum dishType, List<DishIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.ingredients = ingredients;
    }

    public Dish(int id, String name, DishTypeEnum dishType, Double unitPrice, List<DishIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.unitPrice = unitPrice;
        this.ingredients = ingredients;
    }

    public Dish() {

    }

    public List<DishIngredient> getDishIngredients() {
        return ingredients;
    }

    public double getDishCost() {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0.0;
        }

        return ingredients.stream()
                .mapToDouble(i -> i.getQuantityRequired() * i.getIngredient().getPrice())
                .sum();
    }

    public Double getGrossMargin() {
        if (this.unitPrice == null) {
            throw new RuntimeException("Le prix de vente n'ayant pas encore de valeur, il est impossible de calculer la marge.");
        }
        return this.unitPrice - this.getDishCost();
    }
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
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

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<DishIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<DishIngredient> ingredients) {
        this.ingredients = ingredients;
    }


    //    public Double getDishCost() {
//        return ingredients.stream()
//                .mapToDouble(Ingredient::getPrice)
//                .sum();
//    }


    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", unitPrice=" + unitPrice +
                ", ingredients=" + ingredients +
                '}';
    }
}
