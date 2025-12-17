
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection getDBConnection() {
        String url = System.getenv("JDBC_URL");
        String user = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database");
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //JDBC_URL=jdbc:postgresql://localhost:5432/mini_dish_db; USERNAME=mini_dish_db_manager; PASSWORD='123456'
}

