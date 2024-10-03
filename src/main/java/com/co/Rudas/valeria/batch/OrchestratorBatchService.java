package com.co.Rudas.valeria.batch;
import com.co.Rudas.valeria.orquestador.GiraffeEnigmaOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrchestratorBatchService {
    @Autowired
    private GiraffeEnigmaOrchestrator orchestrator;


    @Scheduled(fixedRate = 120000)
    public void executeBatch() {
        Mono<String> result = orchestrator.solveEnigma(); // Llama al orquestador
        result.subscribe(response -> System.out.println("Batch result: " + response));
    }
}
