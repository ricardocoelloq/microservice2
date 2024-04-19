package com.prueba.microservice2;

import com.prueba.microservice2.service.ReporteService;
import com.prueba.microservice2.repository.entity.Cuenta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReporteServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCuentaMovimientoReporteControllers() throws Exception {

        long clienteId = 1L;


        // Create a new account
        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        //.content("{\"numeroCuenta\": \"123456\", \"saldoInicial\": 1000.0, \"clienteId\": 1}"))
                        .content("{\"numeroCuenta\": \"123456\", \"saldoInicial\": 1000.0, \"clienteId\":" + clienteId + "}"))
                .andExpect(status().isOk())
                .andDo(print());

        // Register a movement DEPOSITO
        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroCuenta\": \"123456\", \"valor\": 200.0, \"tipo\": \"DEPOSITO\"}"))
                .andExpect(status().isOk())
                .andDo(print());

        // Register a movement RETIRO
        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroCuenta\": \"123456\", \"valor\": -100.0, \"tipo\": \"RETIRO\"}"))
                .andExpect(status().isOk())
                .andDo(print());

        // Generate a report
        mockMvc.perform(get("/reportes?clienteId="+clienteId+"&fechaInicio=2024-03-15&fechaFin=2024-03-17"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].saldoInicial", is(1000.0)))
                .andExpect(jsonPath("$[0].saldoDisponible", is(1100.0)))
                .andDo(print());
    }
}