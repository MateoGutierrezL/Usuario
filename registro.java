import java.sql.Connection;
import java.sql.SQLException;

public class registro {

    public static void main(String[] args) {

        Connection connection = null;

        String dbURL = "jdbc:sqlite:usuarios.db";

        Conexion dbConnector = new Conexion(dbURL);

        // Crear la tabla si no existe
        dbConnector.createTableIfNotExists();

        //Obtener la conexion
        try {
            connection = dbConnector.getConnection();
            System.out.println("Conexion establecida con la base de datos");

            // Insertar datos
            dbConnector.insertData("Luis", "mateo7112@gmail.com", "123454321", "3126320524");

        }catch (SQLException e) {
            System.err.println("Error al interactuar con la base de datos en el main.");
            e.printStackTrace();
        } finally {
            // 2. Cerrar la conexión llamando a closeConnection() y pasando el objeto Connection
            dbConnector.closeConnection(connection);
    }


    }
}
