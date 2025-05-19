<?php
// Conectar a la base de datos SQLite (se crea si no existe)
$db = new SQLite3('usuarios.db');

// Crear la tabla si no existe
$db->exec("CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    correo TEXT UNIQUE NOT NULL,
    contraseña TEXT NOT NULL,
    numero TEXT
)");

// Obtener datos del formulario
$nombre = $_POST['nombre'] ?? '';
$correo = $_POST['correo'] ?? '';
$contraseña = $_POST['contraseña'] ?? '';
$numero = $_POST['numero'] ?? '';

// Validación rápida
if (empty($nombre) || empty($correo) || empty($contraseña)) {
    die("Todos los campos son obligatorios.");
}

// Hashear la contraseña antes de guardarla
$hash = password_hash($contraseña, PASSWORD_DEFAULT);

// Insertar el usuario
$stmt = $db->prepare("INSERT INTO usuarios (nombre, correo, contraseña, numero) VALUES (:nombre, :correo, :contraseña, :numero)");
$stmt->bindValue(':nombre', $nombre, SQLITE3_TEXT);
$stmt->bindValue(':correo', $correo, SQLITE3_TEXT);
$stmt->bindValue(':contraseña', $hash, SQLITE3_TEXT);
$stmt->bindValue(':numero', $numero, SQLITE3_TEXT);

try {
    $stmt->execute();
    echo "<h1>¡Registro exitoso!</h1>";
} catch (Exception $e) {
    if (str_contains($e->getMessage(), 'UNIQUE')) {
        echo "Este correo ya está registrado.";
    } else {
        echo "Error: " . $e->getMessage();
    }
}
?>
