package com.ronaldocano.notasdocente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad Principal: es la pantalla de bienvenida/menú de la App.
 * Desde aquí se navega hacia el módulo CRUD de Universidades.
 */
public class ActividadPrincipal extends AppCompatActivity {

    private Button botonUniversidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        botonUniversidades = findViewById(R.id.botonUniversidades);

        botonUniversidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActividadPrincipal.this, ActividadCrudUniversidades.class);
                startActivity(intent);
            }
        });
    }
}
