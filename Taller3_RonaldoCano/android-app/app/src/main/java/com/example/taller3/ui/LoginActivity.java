package com.example.taller3.ui;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail;
    private EditText campoPassword;
    private TextView etiEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        campoEmail = findViewById(R.id.campoEmail);
        campoPassword = findViewById(R.id.campoPassword);
        etiEstado = findViewById(R.id.etiEstado);
        Button botonLogin = findViewById(R.id.botonLogin);
        Button botonIrRegistro = findViewById(R.id.botonIrRegistro);

        botonLogin.setOnClickListener(v -> intentarLogin());

        botonIrRegistro.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class)));
    }

    private void intentarLogin() {
        String email = campoEmail.getText().toString().trim();
        String password = campoPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            etiEstado.setText("Completa email y contraseña");
            return;
        }

        etiEstado.setText("Conectando...");

        Map<String, String> parametros = new HashMap<>();
        parametros.put("accion", "login");
        parametros.put("email", email);
        parametros.put("psw", password);

        ServicioHttp.enviarPeticion(parametros, new ServicioHttp.RespuestaCallback() {
            @Override
            public void onExito(String respuestaJson) {
                procesarRespuestaLogin(respuestaJson);
            }

            @Override
            public void onError(String mensajeError) {
                etiEstado.setText("Error de conexión: " + mensajeError);
            }
        });
    }

    private void procesarRespuestaLogin(String respuestaJson) {
        try {
            JSONObject json = new JSONObject(respuestaJson);

            // El PHP original devuelve la fila completa (email, password, nombre)
            // cuando el login es correcto, y {"mensaje": "..."} cuando falla.
            if (json.has("email")) {
                startActivity(new Intent(LoginActivity.this, ListaUsuariosActivity.class));
                finish();
            } else {
                String mensaje = json.optString("mensaje", "Acceso denegado");
                etiEstado.setText(mensaje);
            }
        } catch (Exception e) {
            etiEstado.setText("Respuesta inesperada del servidor: " + respuestaJson);
        }
    }
}
