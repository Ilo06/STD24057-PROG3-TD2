import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    public Dish findDishById(int id) throws SQLException {
        DBConnection connection = new DBConnection();

        String query =
                "SELECT d.id AS dish_id, d.name AS dish_name, d.dish_type, d.unit_price, " +
                        "i.id AS ingredient_id, i.name AS ingredient_name, i.price, i.category, " +
                        "di.quantity, di.unit " +
                        "FROM dish d " +
                        "JOIN dish_ingredient di ON d.id = di.dish_id " +
                        "JOIN ingredient i ON di.ingredient_id = i.id " +
                        "WHERE d.id = ?";

        PreparedStatement stmt = connection.getDBConnection().prepareStatement(query);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        Dish dish = null;

        while (rs.next()) {
            if (dish == null) {
                dish = new Dish(
                        rs.getInt("dish_id"),
                        rs.getString("dish_name"),
                        DishTypeEnum.valueOf(rs.getString("dish_type")),
                        rs.getObject("unit_price") != null ? rs.getDouble("unit_price") : null,
                        new ArrayList<>()
                );
            }

            Ingredient ingredient = new Ingredient(
                    rs.getInt("ingredient_id"),
                    rs.getString("ingredient_name"),
                    rs.getDouble("price"),
                    CategoryEnum.valueOf(rs.getString("category")),
                    null
            );

            DishIngredient di = new DishIngredient(
                    rs.getInt("id"),
                    dish,
                    ingredient,
                    rs.getDouble("quantity"),
                    UnitType.valueOf(rs.getString("unit"))
            );

            dish.getDishIngredients().add(di);
        }

        return dish;
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

    public Dish saveDish(Dish dish) throws SQLException {
        Connection conn = new DBConnection().getDBConnection();
        conn.setAutoCommit(false);

        try {
            PreparedStatement dishStmt = conn.prepareStatement(
                    "INSERT INTO dish (id, name, dish_type, unit_price) " +
                            "VALUES (?, ?, ?, ?) " +
                            "ON CONFLICT (id) DO UPDATE SET name=?, dish_type=?, unit_price=?"
            );

            dishStmt.setInt(1, dish.getId());
            dishStmt.setString(2, dish.getName());
            dishStmt.setObject(3, dish.getDishType(), Types.OTHER);
            dishStmt.setObject(4, dish.getUnitPrice());

            dishStmt.setString(5, dish.getName());
            dishStmt.setObject(6, dish.getDishType(), Types.OTHER);
            dishStmt.setObject(7, dish.getUnitPrice());

            dishStmt.executeUpdate();

            PreparedStatement deleteDI = conn.prepareStatement(
                    "DELETE FROM dish_ingredient WHERE dish_id = ?"
            );
            deleteDI.setInt(1, dish.getId());
            deleteDI.executeUpdate();

            PreparedStatement diStmt = conn.prepareStatement(
                    "INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity, unit) " +
                            "VALUES (?, ?, ?, ?)"
            );

            for (DishIngredient di : dish.getDishIngredients()) {
                diStmt.setInt(1, dish.getId());
                diStmt.setInt(2, di.getIdIngredient());
                diStmt.setDouble(3, di.getQuantityRequired());
                diStmt.setObject(4, di.getUnit(), Types.OTHER);
                diStmt.addBatch();
            }

            diStmt.executeBatch();
            conn.commit();
            return dish;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
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



    public Ingredient saveIngredient(Ingredient ingredient) throws SQLException {
        DBConnection connection = new DBConnection();
        Connection conn = connection.getDBConnection();
        conn.setAutoCommit(false); // Transaction start

        try {
            String ingQuery = "INSERT INTO ingredient (id, name, price, category) VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (id) DO UPDATE SET name=?, price=?, category=? " +
                    "RETURNING id";

            PreparedStatement ingStmt = conn.prepareStatement(ingQuery);

            ingStmt.setInt(1, ingredient.getId());
            ingStmt.setString(2, ingredient.getName());
            ingStmt.setDouble(3, ingredient.getPrice());
            ingStmt.setObject(4, ingredient.getCategory(), Types.OTHER);

            ingStmt.setString(5, ingredient.getName());
            ingStmt.setDouble(6, ingredient.getPrice());
            ingStmt.setObject(7, ingredient.getCategory(), Types.OTHER);

            ResultSet rs = ingStmt.executeQuery();
            if (rs.next()) {
                ingredient.setId(rs.getInt(1));
            }

            if (ingredient.getStockMovementList() != null && !ingredient.getStockMovementList().isEmpty()) {
                String movQuery = "INSERT INTO stock_movement (id, id_ingredient, quantity, type, unit, creation_datetime) " +
                        "VALUES (?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT (id) DO NOTHING"; // [cite: 51]

                PreparedStatement movStmt = conn.prepareStatement(movQuery);

                for (StockMovement movement : ingredient.getStockMovementList()) {
                    movStmt.setInt(1, movement.getId());
                    movStmt.setInt(2, ingredient.getId()); // Force link to this ingredient
                    movStmt.setDouble(3, movement.getValue().getQuantity());
                    movStmt.setObject(4, movement.getMovementType(), Types.OTHER);
                    movStmt.setObject(5, movement.getMovementType(), Types.OTHER);
                    movStmt.setTimestamp(6, java.sql.Timestamp.from(movement.getCreationDatetime()));

                    movStmt.addBatch();
                }
                movStmt.executeBatch();
            }

            conn.commit();
            return ingredient;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }


}