public class DishIngredient {
    private int id;
    private Dish Dish;
    private Ingredient Ingredient;
    private double quantityRequired;
    private UnitType unit;

    public DishIngredient(int id, Dish idDish, Ingredient ingredient, double quantityRequired, UnitType unit) {
        this.id = id;
        this.Dish = idDish;
        this.Ingredient = ingredient;
        this.quantityRequired = quantityRequired;
        this.unit = unit;
    }

    public DishIngredient(Dish dish, Ingredient ingredient, double quantity, UnitType unit) {
    }

    public double getQuantityRequired() {
        return quantityRequired;
    }

    public Dish getDish() {
        return Dish;
    }

    public void setDish(Dish dish) {
        Dish = dish;
    }

    public Ingredient getIngredient() {
        return Ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        Ingredient = ingredient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setQuantityRequired(double quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }

    public int getIdIngredient() {
        return this.Ingredient.getId();
    }
}