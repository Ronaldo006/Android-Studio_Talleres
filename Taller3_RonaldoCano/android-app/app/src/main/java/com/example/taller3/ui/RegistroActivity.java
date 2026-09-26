package com.example.taller3.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taller3.R;
import com.example.taller3.red.ServicioHttp;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private EditText campoNombre;
    private EditText campoEmail;
    private EditText campoPassword;
    private TextView etiEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        campoNombre = findViewById(R.id.campoNombre);
        campoEmail = findViewById(R.id.campoEmail);
        campoPassword = findViewById(R.id.campoPassword);
        etiEstado = findViewById(R.id.etiEstado);
        Button botonRegistrar = findViewById(R.id.botonRegistrar);

        botonRegistrar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombre = campoNombre.getText().toString().trim();
        String email = campoEmail.getText().toString().trim();
        String password = campoPassword.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            etiEstado.setText("Completa los tres campos");
            return;
        }

        etiEstado.setText("Registrando...");

        Map<String, String> parametros = new HashMap<>();
        parametros.put("accion", "Agregar");
        parametros.put("email", email);
        parametros.put("psw", password);
        parametros.put("nombre", nombre);

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
        try {
            JSONObject json = new JSONObject(respuestaJson);
            String mensaje = json.optString("mensaje", "");

            // El PHP original responde "OK)" (con un typo) cuando el registro es exitoso
            if (mensaje.contains("OK")) {
                etiEstado.setText("Usuario registrado. Ya puedes iniciar sesión.");
                campoNombre.setText("");
                campoEmail.setText("");
                campoPassword.setText("");
            } else {
                etiEstado.setText(mensaje.isEmpty() ? "No se pudo registrar" : mensaje);
            }
        } catch (Exception e) {
            etiEstado.setText("Respuesta inesperada del servidor: " + respuestaJson);
        }
    }
}
