import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataRetriever {
    public Dish findDishById(int id) throws SQLException {
        DBConnection connection = new DBConnection();

        String query = "SELECT d.id AS dish_id, d.name AS dish_name, d.dish_type AS dish_type, " +
                "i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, i.category AS ingredient_category " +
                "FROM dish d " +
                "JOIN ingredient i ON d.id = i.id_dish " +
                "WHERE d.id = ?";

        PreparedStatement statement = connection.getDBConnection().prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        Dish dish = null;

        while (resultSet.next()) {
            if (dish == null) {
                dish = new Dish(
                        resultSet.getInt("dish_id"),
                        resultSet.getString("dish_name"),
                        DishTypeEnum.valueOf(resultSet.getString("dish_type")),
                        new ArrayList<>()
                );
            }

            Ingredient ingredient = new Ingredient(
                    resultSet.getInt("ingredient_id"),
                    resultSet.getString("ingredient_name"),
                    resultSet.getDouble("ingredient_price"),
                    CategoryEnum.valueOf(resultSet.getString("ingredient_category")),
                    dish
            );

            dish.getIngredients().add(ingredient);
        }

        return dish;
    }
}
