package com.example.demo.controlador;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DemoControlador {

  @GetMapping("/factors")
  public ResponseEntity<String> getFactors(@RequestParam("value") int value) {
    List<Integer> factors = new ArrayList<>();
    for (int i = 1; i <= value / 2; i++) {
      if (value % i == 0) {
        factors.add(i);
      }
    }
    factors.add(value); // El número mismo también es un factor.
    return ResponseEntity.ok(
      "{\"operation\": \"factors\", \"input\": " +
      value +
      ", \"output\": \"" +
      String.join(",", factors.toString()) +
      "\"}"
    );
  }

  @GetMapping("/primes")
  public ResponseEntity<String> getPrimes(@RequestParam("value") int value) {
    List<Integer> primes = new ArrayList<>();
    for (int i = 2; i <= value; i++) {
      boolean isPrime = true;
      for (int j = 2; j <= Math.sqrt(i); j++) {
        if (i % j == 0) {
          isPrime = false;
          break;
        }
      }
      if (isPrime) {
        primes.add(i);
      }
    }
    return ResponseEntity.ok(
      "{\"operation\": \"primes\", \"input\": " +
      value +
      ", \"output\": \"" +
      String.join(",", primes.toString()) +
      "\"}"
    );
  }
}
