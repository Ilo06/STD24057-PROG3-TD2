import java.time.Instant;

public class Main {
    public static void main(String[] args) {


        DataRetriever dataRetriever = new DataRetriever();

        Instant t = Instant.now();
        Ingredient tomate = dataRetriever.findIngredientById(2);

        if (tomate.getStockValueAt(t).equals(dataRetriever.getStockValueAt(2, t))) {
            System.out.println("okay");
        } else {
            System.out.println("not okay");
            System.out.println(tomate.getStockValueAt(t));
            System.out.println(dataRetriever.getStockValueAt(2, t));
        }


        Dish dish = dataRetriever.findDishById(1);
        if (dish.getDishCost().equals(dataRetriever.getDishCost(1))) {
            System.out.println("okay");
        } else {
            System.out.println("not okay");
            System.out.println(dish.getDishCost());
            System.out.println(dataRetriever.getDishCost(3));


        }

        if (dish.getGrossMargin().equals(dataRetriever.getGrossMargin(1))){
            System.out.println("okay");
        }else  {
            System.out.println("not okay");
            System.out.println(dish.getGrossMargin());
            System.out.println(dataRetriever.getGrossMargin(1));
        }
    }
}
