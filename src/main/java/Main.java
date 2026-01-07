import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        DataRetriever dr = new DataRetriever();

        // a)
        System.out.println(dr.findDishById(1));

        // b)
        try {
            dr.findDishById(999);
        } catch (RuntimeException e) {
            System.out.println("Dish not found OK");
        }

        // c)
        System.out.println(dr.findIngredients(2, 2));

        // d)
        System.out.println(dr.findIngredients(3, 5));

        // e)
        System.out.println(dr.findDishByIngredientName("oeur"));

        // i)
        List<Ingredient> list = List.of(
                new Ingredient("Fromage", 1200.0d, CategoryEnum.DAIRY),
                new Ingredient("Oignon", 500.0d, CategoryEnum.VEGETABLE)
        );
        dr.createIngredients(list);

        // k)
//        Dish soup = new Dish(10, "Soupe de légumes", DishTypeEnum.START, List.of(
//                new Ingredient(2)
//        ));
//        dr.saveDish(soup);
    }
}
