import java.time.Instant;

public class Main {
    public static void main(String[] args) {


        DataRetriever dataRetriever = new DataRetriever();

        Instant t = Instant.now();
        Ingredient tomate = dataRetriever.findIngredientById(2);

        if (tomate.getStockValueAt(t).equals(dataRetriever.getStockValueAt(2, t))) {
            System.out.println("getStockValueAt : okay");
        } else {
            System.out.println("getStockValueAt : not okay");
            System.out.println(tomate.getStockValueAt(t));
            System.out.println(dataRetriever.getStockValueAt(2, t));
        }


        Dish dish = dataRetriever.findDishById(1);
        if (dish.getDishCost().equals(dataRetriever.getDishCost(1))) {
            System.out.println("getDishCost : okay");
        } else {
            System.out.println("getDishCost : not okay");
            System.out.println(dish.getDishCost());
            System.out.println(dataRetriever.getDishCost(1));


        }

        if (dish.getGrossMargin().equals(dataRetriever.getGrossMargin(1))){
            System.out.println("getGrossMargin : okay");
        }else  {
            System.out.println("getGrossMargin : not okay");
            System.out.println(dish.getGrossMargin());
            System.out.println(dataRetriever.getGrossMargin(1));
        }
    }
}
