package com.huertohogar.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 🔥 Un usuario puede tener muchos pedidos
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    // 🔥 Fecha en que se creó el pedido (se setea solo antes de persistir)
    private LocalDateTime fecha;

    // 🔥 Dirección donde se entregará el pedido
    private String direccion_entrega;

    // 🔥 Total en pesos del pedido
    private Integer total;

    // 🔥 Estado del pedido (se inicializa si no viene)
    private String estado;

    // 🔥 Relación 1:N con pedido_detalle
    // JsonManagedReference evita recursión infinita en la respuesta JSON
    @JsonManagedReference
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoDetalle> detalles;

    // 🔥 Antes de guardar el pedido, setea fecha y estado si no existen
    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
        if (estado == null) estado = "En preparación";
    }
}
