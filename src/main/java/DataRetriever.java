import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Ingredient> findIngredients(int page, int size) throws SQLException {
        DBConnection connection = new DBConnection();

        String query = "SELECT i.id AS ingredient_id, i.name AS ingredient_name, i.price AS ingredient_price, i.category AS ingredient_category"
                +" FROM ingredient i " +
                " LIMIT ? OFFSET ?";
        PreparedStatement statement = connection.getDBConnection().prepareStatement(query);
        statement.setInt(1, size);
        statement.setInt(2, page * size);
        ResultSet resultSet = statement.executeQuery();
        List<Ingredient> ingredients = new ArrayList<>();
        while (resultSet.next()){
            Ingredient ingredient = new Ingredient(resultSet.getInt("ingredient_id"), resultSet.getString("ingredient_name"), resultSet.getDouble("ingredient_price"), CategoryEnum.valueOf(resultSet.getString("ingredient_category")), null);
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    public List<Ingredient> createIngredients(List<Ingredient> newIngredient) throws SQLException {
        DBConnection connection = new DBConnection();

        String query = "INSERT INTO INGREDIENT (name, price, category) VALUES (?, ?, ?);";

        Connection activeConnection = connection.getDBConnection();

        activeConnection.setAutoCommit(false);
        try(PreparedStatement statement = activeConnection.prepareStatement(query)){
            for (Ingredient ingredient : newIngredient) {
                statement.setString(1, ingredient.getName());
                statement.setDouble(2, ingredient.getPrice());
                statement.setObject(3, ingredient.getCategory(), Types.OTHER);
                statement.addBatch();
            }
            statement.executeBatch();
            activeConnection.commit();
        } catch (SQLException e) {
            activeConnection.rollback();
            throw new RuntimeException(e);
        }

        return newIngredient;
    }
    
    public Dish saveDish(Dish dishToSave) throws SQLException {
        DBConnection connection = new DBConnection();
        Connection activeConnection = connection.getDBConnection();

        String query = "INSERT INTO Dish VALUES (?, ?, ?);";

        try (PreparedStatement statement = activeConnection.prepareStatement(query)) {
            statement.setInt(1, dishToSave.getId());
            statement.setString(2, dishToSave.getName());
            statement.setObject(3, dishToSave.getDishType(), Types.OTHER);

            statement.executeUpdate(query);
        } catch (SQLException e){
            String updtateQuery = "UPDATE Dish SET name = ?, price = ? WHERE id = ?;";
            try (PreparedStatement statement = activeConnection.prepareStatement(updtateQuery)) {
                statement.setString(1, dishToSave.getName());
                statement.setDouble(2, dishToSave.getDishPrice());
                statement.setInt(3, dishToSave.getId());

                statement.executeUpdate(updtateQuery);
            }catch (SQLException sqlException){
                throw new RuntimeException(sqlException);
            }
        }

        return dishToSave;
    }

    public List<Dish> findDishByIngredientName(String ingredientName) throws SQLException {
        DBConnection connection = new DBConnection();
        Connection activeConnection = connection.getDBConnection();

        String query =
                "SELECT DISTINCT d.id AS dish_id, d.name AS dish_name, d.dish_type AS dish_type " +
                        "FROM dish d " +
                        "JOIN ingredient i ON d.id = i.id_dish " +
                        "WHERE LOWER(i.name) LIKE LOWER(?)";

        PreparedStatement statement = activeConnection.prepareStatement(query);
        statement.setString(1, "%" + ingredientName + "%");

        ResultSet resultSet = statement.executeQuery();

        List<Dish> dishes = new ArrayList<>();

        while (resultSet.next()) {
            Dish dish = new Dish(
                    resultSet.getInt("dish_id"),
                    resultSet.getString("dish_name"),
                    DishTypeEnum.valueOf(resultSet.getString("dish_type")),
                    new ArrayList<>()
            );
            dishes.add(dish);
        }

        return dishes;
    }

}
