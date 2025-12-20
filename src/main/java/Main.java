import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataRetriever dataRetriever = new DataRetriever();

//        System.out.println(dataRetriever.findDishById(1));
//        System.out.println("------------------------------");

        List<Ingredient> myIngredients = new ArrayList<>();

        myIngredients.add(new Ingredient( "Pepper",300.0d, CategoryEnum.VEGETABLE));
        myIngredients.add(new Ingredient( "Carrot",200.0d, CategoryEnum.VEGETABLE));

        System.out.println(dataRetriever.createIngredients(myIngredients));
        System.out.println(dataRetriever.findIngredients(0, 10));
    }
}
