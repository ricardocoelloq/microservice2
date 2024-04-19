package com.prueba.microservice2.service;

import com.prueba.microservice2.exception.SaldoInsuficienteException;
import com.prueba.microservice2.repository.CuentaRepository;
import com.prueba.microservice2.repository.entity.Cuenta;
import com.prueba.microservice2.repository.entity.Movimiento;
import com.prueba.microservice2.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaService cuentaService;

    public List<Movimiento> getAllMovimientos() {
        return movimientoRepository.findAll();
    }

    public Movimiento getMovimientoById(Long id) {
        return movimientoRepository.findById(id).orElse(null);
    }

    public Movimiento createMovimiento(Movimiento movimiento) {
        // Obtener la cuenta asociada con el movimiento
        Cuenta cuenta = cuentaService.getCuentaByNumeroCuenta(movimiento.getNumeroCuenta());

        // Verificar si el saldo es suficiente
        if (cuenta.getSaldoDisponible() + movimiento.getValor() < 0) {
            throw new SaldoInsuficienteException("Saldo no disponible");
        }

        // Actualizar el saldo de la cuenta basado en el valor del movimiento
        cuenta.setSaldoDisponible(cuenta.getSaldoDisponible() + movimiento.getValor());

        // Guardar la cuenta actualizada en la base de datos
        cuentaService.updateCuenta(cuenta);

        // Establecer la fecha actual en el movimiento
        movimiento.setFecha(new Date());

        // Establecer el saldo actualizado en el movimiento
        movimiento.setSaldo(cuenta.getSaldoDisponible());

        // Guardar el movimiento en la base de datos
        return movimientoRepository.save(movimiento);
    }

    public Movimiento updateMovimiento(Movimiento movimiento) {
        return movimientoRepository.save(movimiento);
    }

    public void deleteMovimiento(Long id) {
        movimientoRepository.deleteById(id);
    }


    public List<Movimiento> getMovimientosByNumeroCuenta(String numeroCuenta) {
        List<Movimiento> movimientos = movimientoRepository.findByNumeroCuenta(numeroCuenta);

        Cuenta cuenta = cuentaService.getCuentaByNumeroCuenta(numeroCuenta);
        for (Movimiento movimiento : movimientos) {
            setMovimientoFields(movimiento, cuenta);
        }

        return movimientos;
    }


    public List<Movimiento> getMovimientosByNumeroCuentaAndFechaBetween(String numeroCuenta, Date fechaInicio, Date fechaFin) {
        List<Movimiento> movimientos = movimientoRepository.findByNumeroCuentaAndFechaBetween(numeroCuenta, fechaInicio, fechaFin);

        Cuenta cuenta = cuentaService.getCuentaByNumeroCuenta(numeroCuenta);
        for (Movimiento movimiento : movimientos) {
            setMovimientoFields(movimiento, cuenta);
        }

        return movimientos;
    }


    public void setMovimientoFields(Movimiento movimiento, Cuenta cuenta) {
        movimiento.setNombreCliente(cuenta.getCliente().getNombre());
        movimiento.setTipoCuenta(cuenta.getTipoCuenta());
        movimiento.setEstadoCuenta(cuenta.getEstado());
    }
}