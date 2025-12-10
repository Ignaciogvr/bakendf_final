package com.huertohogar.repository;

import com.huertohogar.model.Pedido;
import com.huertohogar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // Todos los pedidos de un usuario específico
    List<Pedido> findByUsuario(User usuario);

    // Filtrar por estado (opcional para el admin)
    List<Pedido> findByEstado(String estado);
}
