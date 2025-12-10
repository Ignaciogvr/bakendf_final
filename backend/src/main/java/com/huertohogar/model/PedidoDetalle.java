package com.huertohogar.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pedido_detalle")
@Data
public class PedidoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 🔥 Muchos detalles pertenecen a un solo pedido
    // JsonBackReference evita ciclos infinitos en el JSON
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // 🔥 Muchos detalles pueden apuntar al mismo producto
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Product producto;

    // 🔥 Cantidad comprada del producto
    private Integer cantidad;

    // 🔥 Precio unitario al momento de la compra
    private Integer precio_unitario;
}
