package com.prueba.microservice2.controller;

import com.prueba.microservice2.repository.entity.Cuenta;
import com.prueba.microservice2.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    public List<Cuenta> getAllCuentas() {
        return cuentaService.getAllCuentas();
    }

    @GetMapping("/{id}")
    public Cuenta getCuentaById(@PathVariable Long id) {
        return cuentaService.getCuentaById(id);
    }

    @PostMapping
    public Cuenta createCuenta(@RequestBody Cuenta cuenta) {
        return cuentaService.createCuenta(cuenta);
    }

    @PutMapping
    public Cuenta updateCuenta(@RequestBody Cuenta cuenta) {
        return cuentaService.updateCuenta(cuenta);
    }

    @DeleteMapping("/{id}")
    public void deleteCuenta(@PathVariable Long id) {
        cuentaService.deleteCuenta(id);
    }

    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<Cuenta> getCuentaByNumeroCuenta(@PathVariable String numeroCuenta) {
        Cuenta cuenta = cuentaService.getCuentaByNumeroCuenta(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }
}
