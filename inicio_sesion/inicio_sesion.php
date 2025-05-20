<?php
$correo = $_POST['correo'];
$postData = array_change_key_case($_POST, CASE_LOWER);
$clave = $postData['clave'] ?? null;

echo '<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión - Clear Counts</title>
    <link rel="stylesheet" href="inicio_sesion.css">
</head>
<body>';

try {
    // Ruta al archivo .db 
    $db = new PDO('sqlite:../registro/usuarios.db');

    // Preparar y ejecutar la consulta
    $stmt = $db->prepare("SELECT * FROM usuarios WHERE correo = :correo AND contraseña = :contraseña");
    $stmt->bindParam(':correo', $correo);
    $stmt->bindParam(':contraseña', $clave);
    $stmt->execute();

    $resultado = $stmt->fetch();

    if ($resultado) {
        header("Location: ../nosotros/nosotros.html");
        exit;
    } else {
        echo '<div class="center-container">
        <div class="mensaje-error">
            <strong>❌ Error:</strong> Correo o contraseña incorrectos.<br>
            Por favor, verifica tus datos e intenta de nuevo.
        </div>
      </div>';

    }

} catch (PDOException $e) {
    echo "❌ Error de conexión: " . $e->getMessage();
}
?>