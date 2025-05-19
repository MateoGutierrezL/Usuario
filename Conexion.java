import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Conexion {
    private final String dbURL;

    public Conexion(String dbURL) {
        this.dbURL = dbURL;
        try {
            // Cargar el driver JDBC (esto se puede omitir en versiones recientes de Java)
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("No se encontró el driver JDBC de SQLite.");
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbURL);
    }

    public void closeConnection(Connection connection) {
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

    public void createTableIfNotExists() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "correo TEXT NOT NULL," +
                    "contraseña TEXT NOT NULL," +
                    "numero TEXT NOT NULL)";
            statement.executeUpdate(createTableSQL);
            System.out.println("Tabla 'usuarios' creada (si no existía).");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla.");
            e.printStackTrace();
        }
    }

    public void insertData(String nombre, String correo, String contraseña, String numero) {
        String insertSQL = "INSERT INTO usuarios (nombre, correo, contraseña, numero) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, correo);
            preparedStatement.setString(3, contraseña);
            preparedStatement.setString(4, numero);
            preparedStatement.executeUpdate();
            System.out.println("Datos insertados.");
        } catch (SQLException e) {
            System.err.println("Error al insertar datos.");
            e.printStackTrace();
        }
    }

    public ResultSet getAllUsuarios() {
        String selectSQL = "SELECT * FROM usuarios";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {
            System.out.println("\nDatos en la tabla 'usuarios':");
            return resultSet; // OJO: La conexión se cierra al salir del try-with-resources
            // Considera retornar una lista de objetos Usuario en lugar del ResultSet directamente
        } catch (SQLException e) {
            System.err.println("Error al consultar usuarios.");
            e.printStackTrace();
            return null;
        }
    }

    // Ejemplo de método para obtener un usuario por ID
    public ResultSet getUsuarioById(int id) {
        String selectSQL = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet; // Considera retornar un objeto Usuario en lugar del ResultSet directamente
        } catch (SQLException e) {
            System.err.println("Error al consultar usuario por ID.");
            e.printStackTrace();
            return null;
        }
    }
}

