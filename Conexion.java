import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Conexion {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // 1. Cargar el driver JDBC (esto se puede omitir en versiones recientes de Java)
            Class.forName("org.sqlite.JDBC");

            // 2. Establecer la conexión
            String dbURL = "jdbc:sqlite:usuarios.db"; // Ruta al archivo de tu base de datos
            connection = DriverManager.getConnection(dbURL);

            System.out.println("Conexión a la base de datos SQLite establecida.");

            // 3. Crear una tabla (si no existe)
            Statement statement = connection.createStatement();
            String createTableSQL = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "correo TEXT NOT NULL," +
                    "contraseña TEXT NOT NULL," +
                    "numero TEXT NOT NULL)";
            statement.executeUpdate(createTableSQL);
            System.out.println("Tabla 'usuarios' creada (si no existía).");

            // 4. Insertar datos
            String insertSQL = "INSERT INTO usuarios (nombre, correo, contraseña, numero) VALUES ('Mateo', 'mateo7112@gmail.com', '123454321', '3126320524')";
            statement.executeUpdate(insertSQL);
            System.out.println("Datos insertados.");

            // Otra forma de insertar datos usando PreparedStatement (más seguro contra inyección SQL)
            /*String insertPreparedSQL = "INSERT INTO usuarios (nombre, edad) VALUES (?, ?)";
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(insertPreparedSQL);
            preparedStatement.setString(1, "María");
            preparedStatement.setInt(2, 25);
            preparedStatement.executeUpdate();
            System.out.println("Más datos insertados.");
            preparedStatement.close();*/

            // 5. Consultar datos
            String selectSQL = "SELECT * FROM usuarios";
            ResultSet resultSet = statement.executeQuery(selectSQL);

            /*System.out.println("\nDatos en la tabla 'usuarios':");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                int edad = resultSet.getInt("edad");
                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Edad: " + edad);
            }*/

            // Cerrar recursos
            resultSet.close();
            statement.close();

        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el driver JDBC de SQLite.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar o interactuar con la base de datos.");
            e.printStackTrace();
        } finally {
            // 6. Cerrar la conexión
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Conexión a la base de datos cerrada.");
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión.");
                e.printStackTrace();
            }

        }
    }
}
