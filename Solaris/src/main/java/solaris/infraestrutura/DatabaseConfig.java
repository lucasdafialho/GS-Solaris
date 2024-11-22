package solaris.infraestrutura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConfig {
    private static String USER = "RM555466";
    private static String PASSWORD = "071105";
    private static String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCl";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}