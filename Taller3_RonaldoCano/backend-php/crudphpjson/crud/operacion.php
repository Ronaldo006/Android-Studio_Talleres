<?php
require_once '../bd/conexion_bd.php';

$accion = @$_REQUEST["accion"];

switch ($accion) {
    case "login":
        login();
        break;
    case "Agregar":
        guardar();
        break;
    case "editar":
        guardar();
        break;
    case "listar":
        listar();
        break;
    case "eliminar":
        eliminar();
        break;
    default:
        echo json_encode(array("mensaje" => "Acción no reconocida"));
        break;
}

function login() {
    $email = @$_REQUEST["email"];
    $pass = @$_REQUEST["psw"];
    try {
        $res = consultar("SELECT * FROM Usuarios WHERE email = '$email' AND password = '$pass'");

        if ($res != null && $res->num_rows > 0) {
            $json = json_encode($res->fetch_assoc());
            echo $json;
            $res->free();
        } else {
            echo json_encode(array("mensaje" => "Acceso denegado"));
        }
    } catch (Exception $error) {
        echo json_encode(array("mensaje" => $error->getMessage()));
    }
}

function guardar() {
    $email = @$_REQUEST["email"];
    $pass = @$_REQUEST["psw"];
    $nombre = @$_REQUEST["nombre"];
    try {
        $res = consultar("SELECT * FROM Usuarios WHERE email = '$email'");
        if ($res != null && $res->num_rows > 0) {
            consultar("UPDATE Usuarios SET password = '$pass', nombre = '$nombre' WHERE email = '$email'");
        } else {
            consultar("INSERT INTO Usuarios VALUES ('$email', '$pass', '$nombre')");
        }
        echo json_encode(array("mensaje" => "OK"));
    } catch (Exception $error) {
        echo json_encode(array("mensaje" => "Usuario no pudo ser guardado"));
    }
}

function listar() {
    try {
        $res = consultar("SELECT * FROM Usuarios");
        if ($res != null && $res->num_rows > 0) {
            $json = json_encode($res->fetch_all(MYSQLI_ASSOC));
            echo $json;
            $res->free();
        } else {
            echo json_encode(array("mensaje" => "No hay Usuarios"));
        }
    } catch (Exception $error) {
        echo json_encode(array("mensaje" => $error->getMessage()));
    }
}

function eliminar() {
    $email = @$_REQUEST["email"];
    try {
        $res = consultar("SELECT * FROM Usuarios WHERE email = '$email'");
        if ($res != null && $res->num_rows > 0) {
            consultar("DELETE FROM Usuarios WHERE email = '$email'");
            echo json_encode(array("mensaje" => "OK"));
        } else {
            echo json_encode(array("mensaje" => "Usuario no existe"));
        }
    } catch (Exception $error) {
        echo json_encode(array("mensaje" => $error->getMessage()));
    }
}
