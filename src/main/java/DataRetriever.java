import java.sql.*;
import java.util.*;

public class DataRetriever {

    public Dish findDishById(int id) throws SQLException {
        DBConnection connection = new DBConnection();

        String query =
                "SELECT d.id AS dish_id, d.name AS dish_name, d.dish_type, " +
                        "i.id AS ingredient_id, i.name AS ingredient_name, i.price, i.category " +
                        "FROM dish d " +
                        "LEFT JOIN ingredient i ON d.id = i.id_dish " +
                        "WHERE d.id = ?";

        PreparedStatement statement = connection.getDBConnection().prepareStatement(query);
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        Dish dish = null;

        while (rs.next()) {
            if (dish == null) {
                dish = new Dish(
                        rs.getInt("dish_id"),
                        rs.getString("dish_name"),
                        DishTypeEnum.valueOf(rs.getString("dish_type")),
                        new ArrayList<>()
                );
            }

            if (rs.getInt("ingredient_id") != 0) {
                Ingredient ingredient = new Ingredient(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getDouble("price"),
                        CategoryEnum.valueOf(rs.getString("category")),
                        dish
                );
                dish.getIngredients().add(ingredient);
            }
        }

        if (dish == null) {
            throw new RuntimeException("Dish not found");
        }

        return dish;
    }

    public List<Ingredient> findIngredients(int page, int size) throws SQLException {
        DBConnection connection = new DBConnection();

        String query = "SELECT id, name, price, category FROM ingredient LIMIT ? OFFSET ?";
        PreparedStatement stmt = connection.getDBConnection().prepareStatement(query);
        stmt.setInt(1, size);
        stmt.setInt(2, (page - 1) * size);

        ResultSet rs = stmt.executeQuery();
        List<Ingredient> list = new ArrayList<>();

        while (rs.next()) {
            list.add(new Ingredient(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    CategoryEnum.valueOf(rs.getString("category")),
                    null
            ));
        }
        return list;
    }

    public List<Ingredient> createIngredients(List<Ingredient> ingredients) throws SQLException {
        DBConnection connection = new DBConnection();
        Connection conn = connection.getDBConnection();
        conn.setAutoCommit(false);

        try {
            for (Ingredient i : ingredients) {
                PreparedStatement check = conn.prepareStatement(
                        "SELECT id FROM ingredient WHERE name = ?"
                );
                check.setString(1, i.getName());
                ResultSet rs = check.executeQuery();

                if (rs.next()) {
                    throw new RuntimeException("Ingredient already exists");
                }

                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO ingredient(name, price, category) VALUES (?, ?, ?)"
                );
                insert.setString(1, i.getName());
                insert.setDouble(2, i.getPrice());
                insert.setObject(3, i.getCategory(), Types.OTHER);
                insert.executeUpdate();
            }

            conn.commit();
            return ingredients;

        } catch (Exception e) {
            conn.rollback();
            throw e;
        }
    }

    public List<Dish> findDishByIngredientName(String ingredientName) throws SQLException {
        DBConnection connection = new DBConnection();

        String query =
                "SELECT DISTINCT d.id, d.name, d.dish_type " +
                        "FROM dish d JOIN ingredient i ON d.id = i.id_dish " +
                        "WHERE LOWER(i.name) LIKE LOWER(?)";

        PreparedStatement stmt = connection.getDBConnection().prepareStatement(query);
        stmt.setString(1, "%" + ingredientName + "%");

        ResultSet rs = stmt.executeQuery();
        List<Dish> dishes = new ArrayList<>();

        while (rs.next()) {
            dishes.add(new Dish(
                    rs.getInt("id"),
                    rs.getString("name"),
                    DishTypeEnum.valueOf(rs.getString("dish_type")),
                    new ArrayList<>()
            ));
        }

        return dishes;
    }

    public Dish saveDish(Dish dish) throws SQLException {
        DBConnection connection = new DBConnection();
        Connection conn = connection.getDBConnection();
        conn.setAutoCommit(false);

        try {
            PreparedStatement check = conn.prepareStatement(
                    "SELECT id FROM dish WHERE id = ?"
            );
            check.setInt(1, dish.getId());
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                PreparedStatement update = conn.prepareStatement(
                        "UPDATE dish SET name = ?, dish_type = ? WHERE id = ?"
                );
                update.setString(1, dish.getName());
                update.setObject(2, dish.getDishType(), Types.OTHER);
                update.setInt(3, dish.getId());
                update.executeUpdate();

                PreparedStatement delete = conn.prepareStatement(
                        "DELETE FROM ingredient WHERE id_dish = ?"
                );
                delete.setInt(1, dish.getId());
                delete.executeUpdate();
            } else {
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO dish(id, name, dish_type) VALUES (?, ?, ?)"
                );
                insert.setInt(1, dish.getId());
                insert.setString(2, dish.getName());
                insert.setObject(3, dish.getDishType(), Types.OTHER);
                insert.executeUpdate();
            }

            for (Ingredient i : dish.getIngredients()) {
                PreparedStatement link = conn.prepareStatement(
                        "UPDATE ingredient SET id_dish = ? WHERE id = ?"
                );
                link.setInt(1, dish.getId());
                link.setInt(2, i.getId());
                link.executeUpdate();
            }

            conn.commit();
            return dish;

        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
    }
}
