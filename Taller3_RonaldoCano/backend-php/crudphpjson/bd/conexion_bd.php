<?php
$bd = null;

function conectar(&$bd) {
    try {
        
        $bd = new mysqli("localhost", "root", "", "crudphpjson");
    } catch (Exception $error) {
        $msg = array("mensaje" => $error->getMessage());
        throw new Exception(json_encode($msg));
    }
}

function consultar($sql) {
    global $bd;
    $res = null;
    try {
        if ($bd == null) {
            conectar($bd);
        }
        $res = $bd->query($sql);
        return $res;
    } catch (Exception $error) {
        throw new Exception($error->getMessage());
    }
}
