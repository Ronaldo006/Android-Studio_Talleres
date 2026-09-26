package com.ronaldocano.notasdocente.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import com.ronaldocano.notasdocente.db.ConexionBasedatos;
import com.ronaldocano.notasdocente.entidades.Universidad;

/**
 * Clase DAO (Data Access Object) que realiza las operaciones de persistencia
 * o CRUD (Crear, Consultar, Actualizar, Eliminar) sobre la tabla Universidades.
 *
 * Capa MODELO del patrón MVC.
 */
public class DAOUniversidad {

    public static final String NOMBRE_BD = "notasdocentev1";
    public static final int VERSION_BD = 1;
    private static final String TABLA = "Universidades";

    private Activity actividad;
    private ConexionBasedatos bd;

    // Constructor vacío
    public DAOUniversidad() {
    }

    // Constructor con argumentos: abre la conexión en modo escritura
    public DAOUniversidad(Activity actividad) throws Exception {
        this.actividad = actividad;
        try {
            bd = new ConexionBasedatos(actividad, NOMBRE_BD, VERSION_BD);
            bd.conectar(ConexionBasedatos.MODO_ESCRITURA);
        } catch (Exception error) {
            throw new Exception("Error al conectar con la BD notasdocente: " + error.getMessage());
        }
    }

    /**
     * 5). Agrega (INSERT) una nueva Universidad a la BD.
     */
    public void agregarUniversidad(Universidad institucion) throws Exception {
        ContentValues args = new ContentValues();

        if (institucion.getNombre() != null && !institucion.getNombre().trim().isEmpty()) {
            args.put("nombre", institucion.getNombre().trim());
        } else {
            throw new Exception("Es obligatorio introducir el nombre de la Universidad");
        }

        if (institucion.getWww() != null && !institucion.getWww().trim().isEmpty()) {
            args.put("www", institucion.getWww().trim());
        } else {
            args.putNull("www");
        }

        try {
            bd.insertar(TABLA, args);
        } catch (Exception error) {
            throw new Exception("No se pudo agregar la Universidad: " + error.getMessage());
        }
    }

    /**
     * 6). Construye una instancia de Universidad a partir de la fila actual del Cursor.
     */
    public Universidad getUniversidad(Cursor resultado) {
        Universidad u = new Universidad();
        u.setId(resultado.getInt(resultado.getColumnIndexOrThrow("id")));
        u.setNombre(resultado.getString(resultado.getColumnIndexOrThrow("nombre")));
        u.setWww(resultado.getString(resultado.getColumnIndexOrThrow("www")));
        return u;
    }

    /**
     * 7). Consulta (SELECT) una única Universidad por su ID.
     */
    public Universidad consultarUnaUniversidad(String id) throws Exception {
        Universidad u = null;
        try {
            Cursor resultado = bd.consultar(TABLA, null, "id = ?", new String[]{id},
                    null, null, null);
            if (resultado != null && resultado.moveToFirst()) {
                u = getUniversidad(resultado);
                resultado.close();
            } else {
                throw new Exception("No se encontró ninguna Universidad con el ID " + id);
            }
        } catch (Exception error) {
            throw new Exception("Error al consultar la Universidad: " + error.getMessage());
        }
        return u;
    }

    /**
     * 8). Lista (SELECT *) todas las Universidades almacenadas en la BD.
     */
    public List<Universidad> listarTodasLasUniversidades() throws Exception {
        List<Universidad> lista = new ArrayList<>();
        try {
            Cursor resultado = bd.consultar(TABLA, null, null, null, null, null, "nombre ASC");
            if (resultado != null && resultado.moveToFirst()) {
                do {
                    lista.add(getUniversidad(resultado));
                } while (resultado.moveToNext());
                resultado.close();
            }
        } catch (Exception error) {
            throw new Exception("Error al listar las Universidades: " + error.getMessage());
        }
        return lista;
    }

    /**
     * 9). Elimina (DELETE) una Universidad de la BD según su ID.
     * Este método hace parte del módulo ELIMINAR que pide el Taller 2.
     */
    public void borrarUniversidad(String id) throws Exception {
        try {
            int filas = bd.eliminar(TABLA, "id = ?", new String[]{id});
            if (filas == 0) {
                throw new Exception("No existe ninguna Universidad con el ID " + id);
            }
        } catch (Exception error) {
            throw new Exception("Error al eliminar la Universidad: " + error.getMessage());
        }
    }

    /**
     * 9b). Edita/Actualiza (UPDATE) los datos de una Universidad existente.
     * Este método hace parte del módulo MODIFICAR que pide el Taller 2.
     */
    public void editarUniversidad(Universidad institucion) throws Exception {
        ContentValues args = new ContentValues();

        if (institucion.getNombre() != null && !institucion.getNombre().trim().isEmpty()) {
            args.put("nombre", institucion.getNombre().trim());
        } else {
            throw new Exception("Es obligatorio introducir el nombre de la Universidad");
        }

        if (institucion.getWww() != null && !institucion.getWww().trim().isEmpty()) {
            args.put("www", institucion.getWww().trim());
        } else {
            args.putNull("www");
        }

        try {
            int filas = bd.actualizar(TABLA, args, "id = ?",
                    new String[]{String.valueOf(institucion.getId())});
            if (filas == 0) {
                throw new Exception("No existe ninguna Universidad con el ID " + institucion.getId());
            }
        } catch (Exception error) {
            throw new Exception("Error al modificar la Universidad: " + error.getMessage());
        }
    }

    /**
     * 10). Calcula cuál sería el próximo ID disponible (informativo, ya que la
     * columna id es AUTOINCREMENT y SQLite lo asigna automáticamente).
     */
    public int proximoId() throws Exception {
        int proximo = 1;
        try {
            Cursor resultado = bd.consultar(TABLA, new String[]{"MAX(id) AS maximo"},
                    null, null, null, null, null);
            if (resultado != null && resultado.moveToFirst()) {
                proximo = resultado.getInt(resultado.getColumnIndexOrThrow("maximo")) + 1;
                resultado.close();
            }
        } catch (Exception error) {
            throw new Exception("Error al calcular el próximo ID: " + error.getMessage());
        }
        return proximo;
    }

    // Cierra la conexión a la BD (paso 7 explicado en la GUÍA)
    public void cerrarConexion() {
        if (bd != null) {
            bd.desconectar();
        }
    }
}
