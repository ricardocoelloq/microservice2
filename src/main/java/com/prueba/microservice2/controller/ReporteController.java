package com.prueba.microservice2.controller;

import com.prueba.microservice2.repository.entity.Cuenta;
import com.prueba.microservice2.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<Cuenta>> getEstadoDeCuentas(@RequestParam("fechaInicio") @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaInicio,
                                                          @RequestParam("fechaFin") @DateTimeFormat(pattern="yyyy-MM-dd") Date fechaFin,
                                                          @RequestParam("clienteId") Long clienteId) {
        List<Cuenta> cuentas = reporteService.getEstadoDeCuentas(fechaInicio, fechaFin, clienteId);
        return ResponseEntity.ok(cuentas);
    }
}