package com.prueba.microservice2.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroCuenta;
    private Date fecha;
    private String tipoMovimiento;
    private Double valor;
    private Double saldo;

    @Transient
    private String nombreCliente;
    @Transient
    private String tipoCuenta;
    @Transient
    private String estadoCuenta;
}