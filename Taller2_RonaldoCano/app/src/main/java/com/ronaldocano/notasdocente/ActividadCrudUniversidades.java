package com.ronaldocano.notasdocente;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ronaldocano.notasdocente.dao.DAOUniversidad;
import com.ronaldocano.notasdocente.entidades.Universidad;

/**
 * Actividad CRUD Universidades.
 *
 * Implementa los 4 módulos pedidos en las Actividades de Aprendizaje del Taller 2:
 *   1. Guardar   (INSERT)  -> agregarUniversidad
 *   2. Buscar    (SELECT)  -> consultarUnaUniversidad
 *   3. Eliminar  (DELETE)  -> borrarUniversidad   [módulo agregado por el Taller 2]
 *   4. Modificar (UPDATE)  -> editarUniversidad   [módulo agregado por el Taller 2]
 */
public class ActividadCrudUniversidades extends AppCompatActivity {

    private EditText campoId;
    private EditText campoNombre;
    private EditText campoWww;

    private Button botonGuardar;
    private Button botonBuscar;
    private Button botonEliminar;
    private Button botonModificar;
    private Button botonNuevo;

    private DAOUniversidad dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_universidades);

        campoId = findViewById(R.id.campoId);
        campoNombre = findViewById(R.id.campoNombre);
        campoWww = findViewById(R.id.campoWww);

        botonGuardar = findViewById(R.id.botonGuardar);
        botonBuscar = findViewById(R.id.botonBuscar);
        botonEliminar = findViewById(R.id.botonEliminar);
        botonModificar = findViewById(R.id.botonModificar);
        botonNuevo = findViewById(R.id.botonNuevo);

        try {
            dao = new DAOUniversidad(this);
        } catch (Exception error) {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }

        // ---------- MÓDULO 1: GUARDAR (INSERT) ----------
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Universidad institucion = new Universidad(
                            campoNombre.getText().toString(),
                            campoWww.getText().toString());
                    dao.agregarUniversidad(institucion);
                    Toast.makeText(ActividadCrudUniversidades.this,
                            "Universidad agregada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } catch (Exception error) {
                    Toast.makeText(ActividadCrudUniversidades.this,
                            error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // ---------- MÓDULO 2: BUSCAR (SELECT) ----------
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoPedirId("Buscar Universidad", new AccionSobreId() {
                    @Override
                    public void ejecutar(String id) {
                        try {
                            Universidad u = dao.consultarUnaUniversidad(id);
                            campoId.setText(String.valueOf(u.getId()));
                            campoNombre.setText(u.getNombre());
                            campoWww.setText(u.getWww());
                            Toast.makeText(ActividadCrudUniversidades.this,
                                    "Universidad encontrada", Toast.LENGTH_SHORT).show();
                        } catch (Exception error) {
                            limpiarCampos();
                            Toast.makeText(ActividadCrudUniversidades.this,
                                    error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // ---------- MÓDULO 3: ELIMINAR (DELETE) ----------
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoPedirId("Eliminar Universidad", new AccionSobreId() {
                    @Override
                    public void ejecutar(String id) {
                        try {
                            dao.borrarUniversidad(id);
                            Toast.makeText(ActividadCrudUniversidades.this,
                                    "Universidad eliminada satisfactoriamente", Toast.LENGTH_SHORT).show();
                            limpiarCampos();
                        } catch (Exception error) {
                            Toast.makeText(ActividadCrudUniversidades.this,
                                    error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // ---------- MÓDULO 4: MODIFICAR (UPDATE) ----------
        botonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (campoId.getText().toString().trim().isEmpty()) {
                        throw new Exception("Primero debe Buscar una Universidad para poder Modificarla");
                    }
                    Universidad institucion = new Universidad(
                            Integer.parseInt(campoId.getText().toString().trim()),
                            campoNombre.getText().toString(),
                            campoWww.getText().toString());
                    dao.editarUniversidad(institucion);
                    Toast.makeText(ActividadCrudUniversidades.this,
                            "Universidad modificada satisfactoriamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } catch (Exception error) {
                    Toast.makeText(ActividadCrudUniversidades.this,
                            error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        // ---------- Botón auxiliar: limpiar formulario para un nuevo registro ----------
        botonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
        });
    }

    /**
     * Cuadro de Dialogo Personalizado que pide un ID por teclado y luego
     * ejecuta la acción (Buscar o Eliminar) recibida como parámetro.
     */
    private void mostrarDialogoPedirId(String titulo, final AccionSobreId accion) {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Ingrese el ID de la Universidad");

        LinearLayout contenedor = new LinearLayout(this);
        contenedor.setOrientation(LinearLayout.VERTICAL);
        int margen = (int) (16 * getResources().getDisplayMetrics().density);
        contenedor.setPadding(margen, margen / 2, margen, margen / 2);
        contenedor.addView(input);

        new AlertDialog.Builder(this)
                .setTitle(titulo)
                .setView(contenedor)
                .setPositiveButton("OK", (dialog, which) -> {
                    String id = input.getText().toString().trim();
                    if (id.isEmpty()) {
                        Toast.makeText(this, "Debe ingresar un ID", Toast.LENGTH_SHORT).show();
                    } else {
                        accion.ejecutar(id);
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void limpiarCampos() {
        campoId.setText("");
        campoNombre.setText("");
        campoWww.setText("");
        campoNombre.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dao != null) {
            dao.cerrarConexion();
        }
    }

    /**
     * Interfaz funcional utilizada por mostrarDialogoPedirId para reutilizar
     * el mismo cuadro de diálogo tanto para Buscar como para Eliminar.
     */
    private interface AccionSobreId {
        void ejecutar(String id);
    }
}
