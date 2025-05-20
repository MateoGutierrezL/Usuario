<?php
// Asegurar que la sesión se inicia correctamente solo si no está activa
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}

// Verificar si el usuario ha iniciado sesión correctamente
if (!isset($_SESSION['usuario'])) {
    die('<div class="mensaje-error">❌ Error: No has iniciado sesión.</div>');
}

$correo_actual = $_SESSION['usuario']; // Obtener el usuario autenticado

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $nuevo_nombre = trim($_POST['nombre']);
    $nuevo_correo = trim($_POST['correo']);
    $nuevo_numero = trim($_POST['numero']); 
    $nueva_clave = !empty($_POST['clave']) ? password_hash($_POST['clave'], PASSWORD_DEFAULT) : null;

    try {
        // Conectar a la base de datos
        $db = new PDO('sqlite:../registro/usuarios.db');

        // Construir la consulta dinámicamente para actualizar solo los campos que se modifican
        $sql = "UPDATE usuarios SET nombre = :nombre, correo = :correo";
        if ($nueva_clave) {
            $sql .= ", contraseña = :clave";
        }
        $sql .= " WHERE correo = :correo_actual";

        $stmt = $db->prepare($sql);
        $stmt->bindParam(':nombre', $nuevo_nombre);
        $stmt->bindParam(':correo', $nuevo_correo);
        $stmt->bindParam(':numero', $nuevo_numero); // Corregido para coincidir con la consulta
        $stmt->bindParam(':correo_actual', $correo_actual);
        if ($nueva_clave) {
            $stmt->bindParam(':clave', $nueva_clave);
        }

        if ($stmt->execute()) {
            $_SESSION['usuario'] = $nuevo_correo; // Actualiza el correo en la sesión
            echo '<div class="mensaje-exito">✅ Datos actualizados correctamente.</div>';
        } else {
            echo '<div class="mensaje-error">❌ Error al actualizar los datos.</div>';
        }
    } catch (PDOException $e) {
        echo "<div class='mensaje-error'>❌ Error en la base de datos: " . htmlspecialchars($e->getMessage()) . "</div>";
    }
}
?>
