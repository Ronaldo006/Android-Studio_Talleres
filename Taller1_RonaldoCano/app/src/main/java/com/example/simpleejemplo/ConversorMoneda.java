package com.example.simpleejemplo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ConversorMoneda extends AppCompatActivity {

    // Tasas de cambio fijas de ejemplo (cuánto vale 1 unidad de esa moneda en USD)
    private final Map<Integer, Double> tasasEnUsd = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversor);

        tasasEnUsd.put(R.id.radioOrigenCOP, 1.0 / 4000.0);
        tasasEnUsd.put(R.id.radioOrigenUSD, 1.0);
        tasasEnUsd.put(R.id.radioOrigenEUR, 1.09);

        EditText campoCantidad = findViewById(R.id.campoCantidad);
        RadioGroup radioGroupOrigen = findViewById(R.id.radioGroupOrigen);
        RadioGroup radioGroupDestino = findViewById(R.id.radioGroupDestino);
        Button botonConvertir = findViewById(R.id.botonConvertir);
        TextView etiResultado = findViewById(R.id.etiResultadoConversion);

        botonConvertir.setOnClickListener(v -> {
            String textoCantidad = campoCantidad.getText().toString().trim();
            if (textoCantidad.isEmpty()) {
                Toast.makeText(this, "Ingresa una cantidad", Toast.LENGTH_SHORT).show();
                return;
            }

            double cantidad = Double.parseDouble(textoCantidad);

            int idOrigen = radioGroupOrigen.getCheckedRadioButtonId();
            int idDestino = mapDestinoAOrigen(radioGroupDestino.getCheckedRadioButtonId());

            double tasaOrigen = tasasEnUsd.get(idOrigen);
            double tasaDestino = tasasEnUsd.get(idDestino);

            double cantidadEnUsd = cantidad * tasaOrigen;
            double resultado = cantidadEnUsd / tasaDestino;

            etiResultado.setText(String.format("%.2f", resultado));
        });
    }

    // Los RadioButton de destino tienen ids distintos a los de origen;
    // los mapeamos a la misma clave usada en tasasEnUsd
    private int mapDestinoAOrigen(int idDestino) {
        if (idDestino == R.id.radioDestinoCOP) return R.id.radioOrigenCOP;
        if (idDestino == R.id.radioDestinoUSD) return R.id.radioOrigenUSD;
        return R.id.radioOrigenEUR;
    }
}
