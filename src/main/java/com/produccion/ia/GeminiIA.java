
package com.produccion.ia;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.nio.charset.StandardCharsets; 

public class GeminiIA {

    // 🔑 MANTÉN LA CLAVE API AQUÍ
    private static final String API_KEY = "AIzaSyCit7uqaWonKKJmTp5KFkUHCu0q2Vh0ISA";
    
    // ✅ MODELO DE PREVIEW MÁS ROBUSTO PARA V1BETA Y CAPA GRATUITA
    private static final String MODEL = "gemini-2.5-flash-preview-09-2025"; 
    
    // ⚙️ ENDPOINT: USAMOS LA VERSIÓN V1BETA, que es la esperada para modelos 2.5/Flash
    private static final String ENDPOINT = 
        "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL + ":generateContent";

   public String responder(String prompt) {
        HttpsURLConnection connection = null;

        try {
            String safePrompt = prompt.replace("\"", "\\\"");
            String jsonInput = "{ \"contents\": [{\"parts\":[{\"text\":\"" + safePrompt + "\"}]}]}";

            URL url = new URL(ENDPOINT);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("X-goog-api-key", API_KEY);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                String errorResponse = readErrorStream(connection);
                return "❌ Error HTTP " + responseCode + " (" +
                        connection.getResponseMessage() + "). Detalle: " + errorResponse;
            }

            String raw = readResponseStream(connection);
            String parsedText = extractTextFromJson(raw);

            if (parsedText.startsWith("⚠️")) {
                return parsedText;
            }

            return parsedText;

        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ Error de Ejecución: " + e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String readErrorStream(HttpsURLConnection connection) {
        StringBuilder errorResponse = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                errorResponse.append(responseLine).append('\n');
            }
        } catch (Exception e) {
            return "No se pudo leer el mensaje de error del servidor.";
        }
        return errorResponse.toString();
    }

    private String readResponseStream(HttpsURLConnection connection) throws Exception {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine).append('\n');
            }
        }
        return response.toString();
    }

    /**
     * Extracción manual y segura del primer valor de "text" en el JSON.
     * Evita regex que cause retroceso y stack overflow.
     */
    private String extractTextFromJson(String json) {
        if (json == null) {
            return "⚠️ Error de Parseo: respuesta nula.";
        }

        // Detección simple de bloqueo por seguridad
        if (json.contains("\"finishReason\":\"SAFETY\"") || json.contains("\"finish_reason\":\"safety\"")) {
            return "⚠️ Respuesta bloqueada: El contenido no se pudo generar debido a políticas de seguridad.";
        }

        String key = "\"text\"";
        int keyPos = json.indexOf(key);
        while (keyPos != -1) {
            // mover al caracter después de "text"
            int colonPos = json.indexOf(':', keyPos + key.length());
            if (colonPos == -1) break;

            // buscar la comilla de inicio del valor (ignorar espacios)
            int i = colonPos + 1;
            while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
            if (i >= json.length() || json.charAt(i) != '"') {
                // no es un valor string aquí; busca siguiente "text"
                keyPos = json.indexOf(key, keyPos + 1);
                continue;
            }

            // Ahora i apunta a la comilla inicial '"'
            i++; // mover al primer caracter del contenido
            StringBuilder sb = new StringBuilder();
            boolean closed = false;
            while (i < json.length()) {
                char c = json.charAt(i++);
                if (c == '\\') {
                    // escape: tomar el siguiente carácter (si existe)
                    if (i < json.length()) {
                        char esc = json.charAt(i++);
                       
                        sb.append('\\').append(esc);
                        if (esc == 'u' && i + 3 < json.length()) {
                           
                            sb.append(json.substring(i, i + 4));
                            i += 4;
                        }
                    } else {
                        // mal formado, pero lo agregamos tal cual
                        sb.append('\\');
                    }
                } else if (c == '"') {
                    // comilla de cierre encontrada (no escapada)
                    closed = true;
                    break;
                } else {
                    sb.append(c);
                }
            }

            if (closed) {
                
                String unescaped = unescapeJsonString(sb.toString());
                return unescaped;
            } else {
                // si no se cerró bien, buscar la siguiente ocurrencia de "text"
                keyPos = json.indexOf(key, keyPos + 1);
            }
        }

        return "⚠️ Error de Parseo: La clave 'text' no fue encontrada. Respuesta completa: " + json;
    }

    private String unescapeJsonString(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length();) {
            char c = s.charAt(i++);
            if (c == '\\' && i < s.length()) {
                char next = s.charAt(i++);
                switch (next) {
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case '"': sb.append('\"'); break;
                    case '\\': sb.append('\\'); break;
                    case 'u':
                        if (i + 4 <= s.length()) {
                            String hex = s.substring(i, i + 4);
                            try {
                                int code = Integer.parseInt(hex, 16);
                                sb.append((char) code);
                                i += 4;
                            } catch (NumberFormatException e) {
                                sb.append("\\u").append(hex);
                                i += 4;
                            }
                        } else {
                            sb.append("\\u");
                        }
                        break;
                    default:
                        // secuencia desconocida, la incluimos tal cual (por ejemplo \/ )
                        sb.append(next);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}