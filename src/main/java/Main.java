import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataRetriever dataRetriever = new DataRetriever();

        System.out.println(dataRetriever.findDishById(1));
        System.out.println("------------------------------");
        System.out.println(dataRetriever.findIngredients(1, 3));
    }
}
