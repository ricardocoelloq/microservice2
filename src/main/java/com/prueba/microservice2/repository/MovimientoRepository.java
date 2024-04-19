package com.prueba.microservice2.repository;

import com.prueba.microservice2.repository.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByNumeroCuenta(String numeroCuenta);

    List<Movimiento> findByNumeroCuentaAndFechaBetween(String numeroCuenta, Date fechaInicio, Date fechaFin);

}
