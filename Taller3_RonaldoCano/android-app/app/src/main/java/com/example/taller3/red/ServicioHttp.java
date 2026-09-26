package com.example.taller3.red;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase responsable de gestionar las conexiones HTTP con la aplicación PHP.
 * <p>
 * Reemplaza al ApacheHttpClient + GSON que usaba la guía original — esas librerías
 * ya no existen en Android moderno (HttpClient se eliminó del SDK hace años).
 * Aquí usamos HttpURLConnection, que viene incluido en Android, y org.json
 * (también incluido) para leer las respuestas.
 * <p>
 * IMPORTANTE — direcciones a usar:
 * - Si pruebas en el EMULADOR de Android Studio: usa 10.0.2.2 (es la forma en que
 *   el emulador le dice "localhost de mi computador", ya que "localhost" dentro
 *   del emulador se refiere al propio emulador, no a tu PC).
 * - Si pruebas en un CELULAR FÍSICO conectado por USB o por la misma red WiFi
 *   que tu PC: usa la IP local de tu PC (ej. 192.168.1.X), nunca localhost ni 10.0.2.2.
 */
public class ServicioHttp {

    // Ajusta "crudphpjson" al nombre real de la carpeta de tu proyecto PHP dentro de www/
    private static final String BASE_URL = "http://10.0.2.2/crudphpjson/crud/operacion.php";

    private static final ExecutorService hiloDeRed = Executors.newSingleThreadExecutor();
    private static final Handler hiloPrincipal = new Handler(Looper.getMainLooper());

    public interface RespuestaCallback {
        void onExito(String respuestaJson);
        void onError(String mensajeError);
    }

    /**
     * Envía una petición GET al servicio PHP con los parámetros indicados,
     * ejecutándola en un hilo secundario y devolviendo el resultado en el hilo
     * principal (UI) a través del callback.
     */
    public static void enviarPeticion(Map<String, String> parametros, RespuestaCallback callback) {
        hiloDeRed.execute(() -> {
            HttpURLConnection conexion = null;
            try {
                String urlCompleta = construirUrl(parametros);
                URL url = new URL(urlCompleta);
                conexion = (HttpURLConnection) url.openConnection();
                conexion.setRequestMethod("GET");
                conexion.setConnectTimeout(8000);
                conexion.setReadTimeout(8000);

                int codigo = conexion.getResponseCode();
                InputStream stream = (codigo == HttpURLConnection.HTTP_OK)
                        ? conexion.getInputStream()
                        : conexion.getErrorStream();

                String respuesta = leerStream(stream);
                notificarExito(callback, respuesta);

            } catch (Exception e) {
                notificarError(callback, e.getMessage());
            } finally {
                if (conexion != null) {
                    conexion.disconnect();
                }
            }
        });
    }

    private static void notificarExito(RespuestaCallback callback, String respuesta) {
        hiloPrincipal.post(() -> callback.onExito(respuesta));
    }

    private static void notificarError(RespuestaCallback callback, String mensaje) {
        hiloPrincipal.post(() -> callback.onError(mensaje));
    }

    private static String construirUrl(Map<String, String> parametros) throws Exception {
        StringBuilder sb = new StringBuilder(BASE_URL).append("?");
        boolean primero = true;
        for (Map.Entry<String, String> entrada : parametros.entrySet()) {
            if (!primero) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entrada.getKey(), "UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(entrada.getValue(), "UTF-8"));
            primero = false;
        }
        return sb.toString();
    }

    private static String leerStream(InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }
        StringBuilder resultado = new StringBuilder();
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                resultado.append(linea);
            }
        }
        return resultado.toString();
    }
}
