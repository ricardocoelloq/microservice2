package com.prueba.microservice2.service;

import com.prueba.microservice2.repository.CuentaRepository;
import com.prueba.microservice2.repository.MovimientoRepository;
import com.prueba.microservice2.repository.entity.Cuenta;
import com.prueba.microservice2.repository.entity.Movimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private MovimientoService movimientoService;


    public Cuenta getEstadoDeCuenta(Date fechaInicio, Date fechaFin, String numeroCuenta) {
        // Obtener todas las cuentas del cliente
        Cuenta cuenta = cuentaService.getCuentaByNumeroCuenta(numeroCuenta);

        // obtener los movimientos en el rango de fechas
        List<Movimiento> movimientos = movimientoService.getMovimientosByNumeroCuentaAndFechaBetween(cuenta.getNumeroCuenta(), fechaInicio, fechaFin);

        cuenta.setMovimientos(movimientos);
        return cuenta;
    }

    public List<Cuenta> getEstadoDeCuentas(Date fechaInicio, Date fechaFin, Long clienteId) {
        // Obtener todas las cuentas del cliente
        List<Cuenta> cuentas = cuentaService.getCuentasByClienteId(clienteId);

        // Para cada cuenta, obtener los movimientos en el rango de fechas
        for (Cuenta cuenta : cuentas) {
            List<Movimiento> movimientos = movimientoService.getMovimientosByNumeroCuentaAndFechaBetween(cuenta.getNumeroCuenta(), fechaInicio, fechaFin);
            cuenta.setMovimientos(movimientos);
        }

        return cuentas;
    }
}