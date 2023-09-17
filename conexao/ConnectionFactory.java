package jdbc.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/universidade"; // porta do pc
        String username = "root";
        String password = "root";

        // Para se conectar a um banco de dados, você precisa utilizar um objeto que implemente a interface Connection.
        // Isso geralmente é feito através de um DriverManager que gerencia as conexões.
        return DriverManager.getConnection(url, username, password);

    }

}
