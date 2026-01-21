public class DishIngredient {
    private int id;
    private int idDish;
    private int idIngredient;
    private double quantityRequired;
    private UnitType unit;

    public DishIngredient(int id, int idDish, int idIngredient, double quantityRequired, UnitType unit) {
        this.id = id;
        this.idDish = idDish;
        this.idIngredient = idIngredient;
        this.quantityRequired = quantityRequired;
        this.unit = unit;
    }

    public DishIngredient(Dish dish, Ingredient ingredient, double quantity, UnitType unit) {
    }

    public double getQuantityRequired() {
        return quantityRequired;
    }
    public int getIdIngredient() {
        return idIngredient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDish() {
        return idDish;
    }

    public void setIdDish(int idDish) {
        this.idDish = idDish;
    }

    public void setIdIngredient(int idIngredient) {
        this.idIngredient = idIngredient;
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
}