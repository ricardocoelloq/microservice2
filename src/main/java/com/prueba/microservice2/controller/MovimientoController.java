package com.prueba.microservice2.controller;

import com.prueba.microservice2.repository.entity.Movimiento;
import com.prueba.microservice2.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public List<Movimiento> getAllMovimientos() {
        return movimientoService.getAllMovimientos();
    }

    @GetMapping("/{id}")
    public Movimiento getMovimientoById(@PathVariable Long id) {
        return movimientoService.getMovimientoById(id);
    }

    @PostMapping
    public Movimiento createMovimiento(@RequestBody Movimiento movimiento) {
        return movimientoService.createMovimiento(movimiento);
    }

    @PutMapping
    public Movimiento updateMovimiento(@RequestBody Movimiento movimiento) {
        return movimientoService.updateMovimiento(movimiento);
    }

    @DeleteMapping("/{id}")
    public void deleteMovimiento(@PathVariable Long id) {
        movimientoService.deleteMovimiento(id);
    }


    @GetMapping("/cuenta/{numeroCuenta}")
    public ResponseEntity<List<Movimiento>> getMovimientosByNumeroCuenta(@PathVariable String numeroCuenta) {
        List<Movimiento> movimientos = movimientoService.getMovimientosByNumeroCuenta(numeroCuenta);
        return ResponseEntity.ok(movimientos);
    }
}