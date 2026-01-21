import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(int id) throws SQLException {
        try {
            DBConnection connection = new DBConnection();

            String query = "SELECT d.id AS dish_id, d.name AS dish_name, d.dish_type AS dish_type, d.unit_price, " +
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
                    Double price = resultSet.getObject("unit_price") != null ? resultSet.getDouble("unit_price") : null;

                    dish = new Dish(
                            resultSet.getInt("dish_id"),
                            resultSet.getString("dish_name"),
                            DishTypeEnum.valueOf(resultSet.getString("dish_type")),
                            price,
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
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Ingredient> findIngredients(int page, int size) throws SQLException {
        DBConnection connection = new DBConnection();

        String query =
                "SELECT id, name, price, category " +
                        "FROM ingredient " +
                        "LIMIT ? OFFSET ?";

        PreparedStatement statement = connection.getDBConnection().prepareStatement(query);
        statement.setInt(1, size);
        statement.setInt(2, (page - 1) * size);

        ResultSet resultSet = statement.executeQuery();

        List<Ingredient> ingredients = new ArrayList<>();

        while (resultSet.next()) {
            ingredients.add(new Ingredient(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    CategoryEnum.valueOf(resultSet.getString("category")),
                    null
            ));
        }

        return ingredients;
    }

    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) throws SQLException {
        DBConnection connection = new DBConnection();
        Connection activeConnection = connection.getDBConnection();

        String query = "INSERT INTO ingredient (name, price, category) VALUES (?, ?, ?)";

        activeConnection.setAutoCommit(false);

        try (PreparedStatement statement = activeConnection.prepareStatement(query)) {
            for (Ingredient ingredient : newIngredients) {
                statement.setString(1, ingredient.getName());
                statement.setDouble(2, ingredient.getPrice());
                statement.setObject(3, ingredient.getCategory(), Types.OTHER);
                statement.addBatch();
            }
            statement.executeBatch();
            activeConnection.commit();
        } catch (SQLException e) {
            activeConnection.rollback();
            throw e;
        }

        return newIngredients;
    }

    public Dish saveDish(Dish dishToSave) throws SQLException {
        DBConnection connection = new DBConnection();
        Connection activeConnection = connection.getDBConnection();

        String insertQuery = "INSERT INTO Dish (id, name, dish_type, unit_price) VALUES (?, ?, ?, ?);";

        try (PreparedStatement insertStmt = activeConnection.prepareStatement(insertQuery)) {
            insertStmt.setInt(1, dishToSave.getId());
            insertStmt.setString(2, dishToSave.getName());
            insertStmt.setObject(3, dishToSave.getDishType(), Types.OTHER);

            if (dishToSave.getUnitPrice() != null) {
                insertStmt.setDouble(4, dishToSave.getUnitPrice());
            } else {
                insertStmt.setNull(4, Types.NUMERIC);
            }

            insertStmt.executeUpdate();
        } catch (SQLException e) {
            String updateQuery = "UPDATE Dish SET name = ?, dish_type = ?, unit_price = ? WHERE id = ?;";
            try (PreparedStatement updateStmt = activeConnection.prepareStatement(updateQuery)) {
                updateStmt.setString(1, dishToSave.getName());
                updateStmt.setObject(2, dishToSave.getDishType(), Types.OTHER);

                if (dishToSave.getUnitPrice() != null) {
                    updateStmt.setDouble(3, dishToSave.getUnitPrice());
                } else {
                    updateStmt.setNull(3, Types.NUMERIC);
                }

                updateStmt.setInt(4, dishToSave.getId());
                updateStmt.executeUpdate();
            }
        }

        String removeIngredientsQuery = "UPDATE Ingredient SET id_dish = NULL WHERE id_dish = ?;";
        try (PreparedStatement removeStmt = activeConnection.prepareStatement(removeIngredientsQuery)) {
            removeStmt.setInt(1, dishToSave.getId());
            removeStmt.executeUpdate();
        }

        if (dishToSave.getIngredients() != null) {
            String assignIngredientQuery = "UPDATE Ingredient SET id_dish = ? WHERE id = ?;";
            for (Ingredient ingredient : dishToSave.getIngredients()) {
                try (PreparedStatement assignStmt = activeConnection.prepareStatement(assignIngredientQuery)) {
                    assignStmt.setInt(1, dishToSave.getId());
                    assignStmt.setInt(2, ingredient.getId());
                    assignStmt.executeUpdate();
                }
            }
        }

        return dishToSave;
    }


    public List<Dish> findDishByIngredientName(String ingredientName) throws SQLException {
        DBConnection connection = new DBConnection();

        String query =
                "SELECT DISTINCT d.id, d.name, d.dish_type " +
                        "FROM dish d " +
                        "JOIN ingredient i ON d.id = i.id_dish " +
                        "WHERE i.name ILIKE ?";

        PreparedStatement statement = connection.getDBConnection().prepareStatement(query);
        statement.setString(1, "%" + ingredientName + "%");

        ResultSet resultSet = statement.executeQuery();

        List<Dish> dishes = new ArrayList<>();

        while (resultSet.next()) {
            dishes.add(new Dish(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    DishTypeEnum.valueOf(resultSet.getString("dish_type")),
                    new ArrayList<>()
            ));
        }

        return dishes;
    }

    public List<Ingredient> findIngredientsByCriteria(
            String ingredientName,
            CategoryEnum category,
            String dishName,
            int page,
            int size
    ) throws SQLException {

        DBConnection connection = new DBConnection();

        StringBuilder query = new StringBuilder(
                "SELECT i.id, i.name, i.price, i.category, " +
                        "d.id AS dish_id, d.name AS dish_name, d.dish_type " +
                        "FROM ingredient i " +
                        "LEFT JOIN dish d ON i.id_dish = d.id " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (ingredientName != null) {
            query.append("AND LOWER(i.name) LIKE LOWER(?) ");
            params.add("%" + ingredientName + "%");
        }

        if (category != null) {
            query.append("AND i.category = ? ");
            params.add(category);
        }

        if (dishName != null) {
            query.append("AND LOWER(d.name) ILIKE LOWER(?) ");
            params.add("%" + dishName + "%");
        }

        query.append("LIMIT ? OFFSET ? ");
        params.add(size);
        params.add((page - 1) * size);

        PreparedStatement statement = connection.getDBConnection().prepareStatement(query.toString());

        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            if (param instanceof CategoryEnum) {
                statement.setObject(i + 1, param, Types.OTHER);
            } else {
                statement.setObject(i + 1, param);
            }
        }

        ResultSet resultSet = statement.executeQuery();

        List<Ingredient> ingredients = new ArrayList<>();

        while (resultSet.next()) {
            Dish dish = null;
            if (resultSet.getInt("dish_id") != 0) {
                dish = new Dish(
                        resultSet.getInt("dish_id"),
                        resultSet.getString("dish_name"),
                        DishTypeEnum.valueOf(resultSet.getString("dish_type")),
                        new ArrayList<>()
                );
            }

            ingredients.add(new Ingredient(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    CategoryEnum.valueOf(resultSet.getString("category")),
                    dish
            ));
        }

        return ingredients;
    }
}