package com.co.Rudas.valeria.orquestador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GiraffeEnigmaOrchestrator {

    private final WebClient webClient;

    @Autowired
    public GiraffeEnigmaOrchestrator(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build(); // Construimos el WebClient
    }

    public Mono<String> solveEnigma() {
        // Llamamos a los tres microservicios con el cuerpo de solicitud correcto
        Mono<String> stepOne = getStep("http://localhost:8080/getStep",
                "{\"data\": [{\"header\": {\"id\": \"12345\", \"type\": \"TestGiraffeRefrigerator\"}, " +
                        "\"enigma\": \"How to put giraffe into a refrigerator?\"}]}");

        Mono<String> stepTwo = getStep("http://localhost:8081/getStep",
                "{\"data\": [{\"header\": {\"id\": \"12345\", \"type\": \"TestGiraffeRefrigerator\"}, " +
                        "\"enigma\": \"How to put giraffe into a refrigerator?\"}]}");

        Mono<String> stepThree = getStep("http://localhost:8082/getStep",
                "{\"data\": [{\"header\": {\"id\": \"12345\", \"type\": \"TestGiraffeRefrigerator\"}, " +
                        "\"enigma\": \"How to put giraffe into a refrigerator?\"}]}");

        // Combinamos las respuestas de los tres microservicios
        return Mono.zip(stepOne, stepTwo, stepThree)
                .map(tuple -> String.format(
                        "{\"data\": [{\"header\": {\"id\": \"12345\", \"type\": \"TestGiraffeRefrigerator\"}, " +
                                "\"answer\": \"Step1: %s - Step2: %s - Step3: %s\"}]}",
                        tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    private Mono<String> getStep(String url, String requestBody) {
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody) // Asegúrate de que el requestBody sea válido
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), response -> {
                    // Manejo de errores HTTP
                    return response.bodyToMono(String.class).flatMap(errorBody -> {
                        System.out.println("Error response from " + url + ": " + errorBody);
                        return Mono.error(new RuntimeException("Failed with status code: " + response.statusCode()));
                    });
                })
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    e.printStackTrace(); // Muestra el error exacto
                    return Mono.just("Error calling " + url + ": " + e.getMessage());
                });
    }
}