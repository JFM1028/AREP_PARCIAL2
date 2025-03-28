package com.edu.escuelaing.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyController {

  private static String[] serviceUrls = new String[] {
    System.getenv("SERVICE_URL_1"), // Instancia del servicio 1
    System.getenv("SERVICE_URL_2"), // Instancia del servicio 2
  };

  private static AtomicInteger currentServiceIndex = new AtomicInteger(0);

  // Método para hacer Round Robin entre los servicios
  private static String getServiceUrl() {
    int index = currentServiceIndex.getAndUpdate(i ->
      (i + 1) % serviceUrls.length
    );
    return serviceUrls[index];
  }

  // Método para hacer la solicitud GET a un servicio
  private static String makeHttpRequest(String serviceUrl) throws IOException {
    URL url = new URL(serviceUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) { // Verifica si la conexión fue exitosa
      BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream())
      );
      String inputLine;
      StringBuilder response = new StringBuilder();

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();

      return response.toString(); // Retorna la respuesta del servicio
    } else {
      return "{\"error\": \"Error en la solicitud al servicio\"}";
    }
  }

  // Endpoints para el proxy
  @GetMapping("/factors")
  public String getFactors(@RequestParam("value") int value)
    throws IOException {
    String serviceUrl = getServiceUrl() + "/factors?value=" + value;
    return makeHttpRequest(serviceUrl);
  }

  @GetMapping("/primes")
  public String getPrimes(@RequestParam("value") int value) throws IOException {
    String serviceUrl = getServiceUrl() + "/primes?value=" + value;
    return makeHttpRequest(serviceUrl);
  }
}
