package com.huertohogar.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String direccion;
    private String telefono;

    // RELACIÓN CON ROLES
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Role role;
}
