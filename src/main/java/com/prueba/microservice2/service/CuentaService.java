package com.prueba.microservice2.service;

import com.prueba.microservice2.client.ClienteFeignClient;
import com.prueba.microservice2.exception.CuentaExistenteException;
import com.prueba.microservice2.exception.CuentaNotFoundException;
import com.prueba.microservice2.model.Cliente;
import com.prueba.microservice2.repository.entity.Cuenta;
import com.prueba.microservice2.repository.CuentaRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    private WebClient webClient = WebClient.create();

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private RetryRegistry retryRegistry;

    @Autowired
    private ClienteFeignClient clienteFeignClient;


    public List<Cuenta> getAllCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        for (Cuenta cuenta : cuentas) {
            Mono<Cliente> clienteMono = getClienteByCuenta(cuenta);
            Cliente cliente = clienteMono.block();
            cuenta.setCliente(cliente);
        }
        return cuentas;
    }

    public Cuenta getCuentaById(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id).orElse(null);
        if (cuenta == null) {
            throw new CuentaNotFoundException("Cuenta no encontrada");
        }
        Mono<Cliente> clienteMono = getClienteByCuenta(cuenta);
        Cliente cliente = clienteMono.block();
        cuenta.setCliente(cliente);
        return cuenta;
    }

    public Cuenta getCuentaByNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta);
        if (cuenta == null) {
            throw new CuentaNotFoundException("Cuenta no encontrada");
        }
        Mono<Cliente> clienteMono = getClienteByCuenta(cuenta);
        Cliente cliente = clienteMono.block();
        cuenta.setCliente(cliente);
        return cuenta;
    }


    public List<Cuenta> getCuentasByClienteId(Long clienteId) {
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        for (Cuenta cuenta : cuentas) {
            Mono<Cliente> clienteMono = getClienteByCuenta(cuenta);
            Cliente cliente = clienteMono.block();
            cuenta.setCliente(cliente);
        }
        return cuentas;
    }

    public Cuenta createCuenta(Cuenta cuenta) {

        // Verificar si la cuenta ya existe
        Cuenta existingCuenta = cuentaRepository.findByNumeroCuenta(cuenta.getNumeroCuenta());
        if (existingCuenta != null) {
            throw new CuentaExistenteException("La cuenta ya existe");
        }

        cuenta.setSaldoDisponible(cuenta.getSaldoInicial());
        Cuenta savedCuenta = cuentaRepository.save(cuenta);

        // Asociar el cliente con la cuenta
        //Mono<Cliente> clienteMono = getClienteByCuenta(savedCuenta);
        Cliente cliente = clienteFeignClient.getClienteById(savedCuenta.getClienteId());
        savedCuenta.setCliente(cliente);

        return savedCuenta;
    }

    public Cuenta updateCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    public void deleteCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }




    public Mono<Cliente> getClienteByCuenta(Cuenta cuenta) {
        if (cuenta != null) {
            String clienteServiceUrl = "http://localhost:8091/clientes/" + cuenta.getClienteId();


            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("backendA");
            Retry retry = retryRegistry.retry("backendA");

            return webClient.get()
                    .uri(clienteServiceUrl)
                    .retrieve()
                    .bodyToMono(Cliente.class);
        }
        return Mono.empty();
    }



}