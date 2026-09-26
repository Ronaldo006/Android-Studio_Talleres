package com.ronaldocano.notasdocente.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Clase hija (subclase) de SQLiteOpenHelper encargada de crear, abrir,
 * cerrar y realizar operaciones CRUD genéricas sobre la BD SQLite.
 *
 * Capa MODELO del patrón MVC.
 */
public class ConexionBasedatos extends SQLiteOpenHelper {

    // Modos de conexión a la BD
    public static final int MODO_LECTURA = 1;
    public static final int MODO_ESCRITURA = 2;

    // Instancia que representa la conexión abierta con la BD
    private SQLiteDatabase conexion;

    // Constructor: recibe el contexto (actividad), el nombre y la versión de la BD
    public ConexionBasedatos(Context contexto, String nombre, int version) throws Exception {
        super(contexto, nombre, null, version);
    }

    // Se invoca automáticamente la primera vez que se crea la BD
    @Override
    public void onCreate(SQLiteDatabase bd) {
        // Separamos cada sentencia CREATE TABLE del backup por el delimitador ";"
        String[] sentencias = CodigoSQLBackup.sqlBackup.split(";");
        for (String sentencia : sentencias) {
            if (sentencia != null && !sentencia.trim().isEmpty()) {
                bd.execSQL(sentencia.trim());
            }
        }
    }

    // Se invoca automáticamente cuando la versión de la BD cambia
    @Override
    public void onUpgrade(SQLiteDatabase bd, int versionActual, int nuevaVersion) {
        bd.execSQL("DROP TABLE IF EXISTS Calificaciones");
        bd.execSQL("DROP TABLE IF EXISTS Asignaturas");
        bd.execSQL("DROP TABLE IF EXISTS Alumnos");
        bd.execSQL("DROP TABLE IF EXISTS Programas");
        bd.execSQL("DROP TABLE IF EXISTS Universidades");
        onCreate(bd);
    }

    /**
     * Abre la conexión con la BD en modo lectura o escritura.
     */
    public SQLiteDatabase conectar(int modo) throws Exception {
        try {
            if (modo == MODO_ESCRITURA) {
                conexion = getWritableDatabase();
            } else {
                conexion = getReadableDatabase();
            }
        } catch (Exception error) {
            throw new Exception("Error al conectar con la BD: " + error.getMessage());
        }
        return conexion;
    }

    /**
     * Ejecuta una sentencia INSERT sobre la tabla indicada.
     * Devuelve el ID del registro insertado, o -1 si falló.
     */
    public long insertar(String tabla, ContentValues columnasValor) throws Exception {
        try {
            return conexion.insert(tabla, null, columnasValor);
        } catch (Exception error) {
            throw new Exception("Error al insertar en " + tabla + ": " + error.getMessage());
        }
    }

    /**
     * Ejecuta una sentencia UPDATE sobre la tabla indicada.
     * Devuelve el número de filas afectadas.
     */
    public int actualizar(String tabla, ContentValues columnasValor,
                           String whereColumnasIgualValor, String[] valoresWhere) throws Exception {
        try {
            return conexion.update(tabla, columnasValor, whereColumnasIgualValor, valoresWhere);
        } catch (Exception error) {
            throw new Exception("Error al actualizar " + tabla + ": " + error.getMessage());
        }
    }

    /**
     * Ejecuta una sentencia DELETE sobre la tabla indicada.
     * Devuelve el número de filas eliminadas.
     */
    public int eliminar(String tabla, String whereColumnasIgualValor, String[] valoresWhere) throws Exception {
        try {
            return conexion.delete(tabla, whereColumnasIgualValor, valoresWhere);
        } catch (Exception error) {
            throw new Exception("Error al eliminar en " + tabla + ": " + error.getMessage());
        }
    }

    /**
     * Ejecuta una consulta SELECT y devuelve un Cursor con el resultado.
     */
    public Cursor consultar(String tabla, String[] columnas, String where, String[] whereArgs,
                             String groupBy, String having, String orderBy) throws Exception {
        try {
            return conexion.query(tabla, columnas, where, whereArgs, groupBy, having, orderBy);
        } catch (Exception error) {
            throw new Exception("Error al consultar " + tabla + ": " + error.getMessage());
        }
    }

    /**
     * Cierra la conexión con la BD si está abierta.
     */
    public void desconectar() {
        if (conexion != null && conexion.isOpen()) {
            conexion.close();
        }
    }
}
