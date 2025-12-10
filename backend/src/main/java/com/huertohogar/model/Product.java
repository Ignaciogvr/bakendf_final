package com.huertohogar.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    private Integer precio;

    private String unidad;

    private String img;

    private String categoria;

    private Integer stock;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
