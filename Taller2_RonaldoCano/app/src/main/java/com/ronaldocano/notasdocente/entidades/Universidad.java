package com.ronaldocano.notasdocente.entidades;

/**
 * Clase Entidad que mapea o abstrae la tabla "Universidades" de la BD.
 * Capa MODELO del patrón MVC.
 *
 * Autor: Ronaldo Cano
 * Taller 2 - Desarrollo de Apps - Android Studio
 */
public class Universidad {

    // Propiedades: deben coincidir con las columnas de la tabla Universidades
    private int id;
    private String nombre;
    private String www;

    // Constructor vacío (por defecto)
    public Universidad() {
    }

    // Constructor con argumentos, sin id (para cuando se va a AGREGAR una nueva)
    public Universidad(String nombre, String www) {
        this.nombre = nombre;
        this.www = www;
    }

    // Constructor con argumentos, con id (para cuando se va a MODIFICAR una existente)
    public Universidad(int id, String nombre, String www) {
        this.id = id;
        this.nombre = nombre;
        this.www = www;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    @Override
    public String toString() {
        return "Universidad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", www='" + www + '\'' +
                '}';
    }
}
