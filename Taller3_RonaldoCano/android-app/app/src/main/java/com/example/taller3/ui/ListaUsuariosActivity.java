package com.example.taller3.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller3.R;
import com.example.taller3.modelo.Usuario;
import com.example.taller3.red.ServicioHttp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaUsuariosActivity extends AppCompatActivity {

    private ListView listaUsuarios;
    private TextView etiEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_usuarios);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listaUsuarios = findViewById(R.id.listaUsuarios);
        etiEstado = findViewById(R.id.etiEstado);
        Button botonActualizar = findViewById(R.id.botonActualizar);

        botonActualizar.setOnClickListener(v -> cargarUsuarios());

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        etiEstado.setText("Cargando...");

        Map<String, String> parametros = new HashMap<>();
        parametros.put("accion", "listar");

        ServicioHttp.enviarPeticion(parametros, new ServicioHttp.RespuestaCallback() {
            @Override
            public void onExito(String respuestaJson) {
                procesarRespuesta(respuestaJson);
            }

            @Override
            public void onError(String mensajeError) {
                etiEstado.setText("Error de conexión: " + mensajeError);
            }
        });
    }

    private void procesarRespuesta(String respuestaJson) {
        List<Usuario> usuarios = new ArrayList<>();

        try {
            // Si hay usuarios, el PHP responde un arreglo JSON: [ {...}, {...}, ... ]
            JSONArray arreglo = new JSONArray(respuestaJson);
            for (int i = 0; i < arreglo.length(); i++) {
                JSONObject item = arreglo.getJSONObject(i);
                usuarios.add(new Usuario(
                        item.optString("email"),
                        item.optString("password"),
                        item.optString("nombre")
                ));
            }
            etiEstado.setText(usuarios.size() + " usuario(s) encontrado(s)");
        } catch (Exception e) {
            // Si no hay usuarios, el PHP responde un objeto: {"mensaje": "No hay Usuarios"}
            try {
                JSONObject json = new JSONObject(respuestaJson);
                etiEstado.setText(json.optString("mensaje", "No hay usuarios"));
            } catch (Exception e2) {
                etiEstado.setText("Respuesta inesperada del servidor");
            }
        }

        List<String> textos = new ArrayList<>();
        for (Usuario u : usuarios) {
            textos.add(u.toString());
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, textos);
        listaUsuarios.setAdapter(adaptador);
    }
}
