package com.prueba.microservice2.repository.entity;

import com.prueba.microservice2.model.Cliente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numeroCuenta;

    private Long clienteId;

    private String tipoCuenta;
    private Double saldoInicial;
    private Double saldoDisponible;
    private String estado;


    @Transient
    private Cliente cliente;

    @Transient
    private List<Movimiento> movimientos;
}