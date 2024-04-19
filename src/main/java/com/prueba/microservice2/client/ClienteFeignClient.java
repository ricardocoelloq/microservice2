package com.prueba.microservice2.client;

import com.prueba.microservice2.model.Cliente;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice1", url = "http://localhost:8091")
public interface ClienteFeignClient {
    @GetMapping("/clientes/{id}")
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallbackForGetCliente")
    @Retry(name = "backendA")
    Cliente getClienteById(@PathVariable("id") Long id);

    default Cliente fallbackForGetCliente(Long id, Throwable t) {
        Cliente cliente = new Cliente();
        cliente.setNombre("Cliente no disponible");
        return cliente;
    }
}