CREATE DATABASE IF NOT EXISTS crudphpjson;
USE crudphpjson;

CREATE TABLE IF NOT EXISTS Usuarios (
    email    VARCHAR(100) PRIMARY KEY,
    password VARCHAR(100) NOT NULL,
    nombre   VARCHAR(150) NOT NULL
);
