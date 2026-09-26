package com.example.simpleejemplo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText campoDato = findViewById(R.id.campoDato);
        TextView etiMensaje = findViewById(R.id.etiMensaje);
        Button botonOk = findViewById(R.id.botonOk);

        botonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dato = campoDato.getText().toString();
                etiMensaje.setText("Mensaje: " + dato);
            }
        });

        Button botonConversor = findViewById(R.id.botonConversor);
        Button botonCalculadora = findViewById(R.id.botonCalculadora);

        botonConversor.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ConversorMoneda.class)));

        botonCalculadora.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CalculadoraCredito.class)));
    }
}