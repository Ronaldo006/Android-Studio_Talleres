package com.example.simpleejemplo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class CalculadoraCredito extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        EditText campoValorCredito = findViewById(R.id.campoValorCredito);
        EditText campoNumeroCuotas = findViewById(R.id.campoNumeroCuotas);
        EditText campoInteres = findViewById(R.id.campoInteres);
        Button botonCalcular = findViewById(R.id.botonCalcular);
        TextView etiResultado = findViewById(R.id.etiResultadoCredito);

        botonCalcular.setOnClickListener(v -> {
            String textoValor = campoValorCredito.getText().toString().trim();
            String textoCuotas = campoNumeroCuotas.getText().toString().trim();
            String textoInteres = campoInteres.getText().toString().trim();

            if (textoValor.isEmpty() || textoCuotas.isEmpty() || textoInteres.isEmpty()) {
                Toast.makeText(this, "Completa los tres campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double valorCredito = Double.parseDouble(textoValor);
            int numeroCuotas = Integer.parseInt(textoCuotas);
            double interesMensual = Double.parseDouble(textoInteres) / 100.0;

            double interesTotal = valorCredito * interesMensual * numeroCuotas;
            double valorTotal = valorCredito + interesTotal;
            double valorCuota = valorTotal / numeroCuotas;

            String resultado = String.format(Locale.getDefault(),
                    "Valor por cuota: %.2f\nValor total del crédito: %.2f\nGanancia total: %.2f",
                    valorCuota, valorTotal, interesTotal);

            etiResultado.setText(resultado);
        });
    }
}
