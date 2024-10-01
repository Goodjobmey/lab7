import java.sql.*;

public class Test {
    public static final String DATABASE_HELIOS_URL = "jdbc:postgresql://pg/studs";

    public static Statement statmt;
    public static Connection connection;
    public static ResultSet resSet;

    public static void test(){
        try {
            connection = DriverManager.getConnection(Test.DATABASE_HELIOS_URL);
            System.out.println("Успешное подключение к базе данных PostgreSQL!");
            statmt = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
}
