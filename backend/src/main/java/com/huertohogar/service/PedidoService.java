package com.huertohogar.service;

import com.huertohogar.model.Pedido;
import com.huertohogar.model.PedidoDetalle;
import com.huertohogar.model.Product;
import com.huertohogar.model.User;
import com.huertohogar.repository.PedidoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UserService userService;
    private final ProductService productService;

    // ============================================================
    //                     CREAR PEDIDO
    // ============================================================
    public Pedido crearPedido(Integer usuarioId, String direccion, List<PedidoDetalle> items) {

        User cliente = userService.obtenerPorId(usuarioId);

        Pedido pedido = new Pedido();
        pedido.setUsuario(cliente);
        pedido.setDireccion_entrega(direccion);
        pedido.setEstado("PENDIENTE");

        List<PedidoDetalle> detalles = new ArrayList<>();
        int total = 0;

        for (PedidoDetalle item : items) {

            Product prod = productService.obtenerPorId(item.getProducto().getId());

            // Descontar stock
            productService.descontarStock(prod.getId(), item.getCantidad());

            PedidoDetalle det = new PedidoDetalle();
            det.setPedido(pedido);  // 🔥 NECESARIO para que se guarde
            det.setProducto(prod);
            det.setCantidad(item.getCantidad());
            det.setPrecio_unitario(prod.getPrecio());

            detalles.add(det);

            total += prod.getPrecio() * item.getCantidad();
        }

        pedido.setDetalles(detalles);
        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    // ============================================================
    //       PEDIDOS POR USUARIO
    // ============================================================
    public List<Pedido> pedidosPorUsuario(Integer usuarioId) {
        User cliente = userService.obtenerPorId(usuarioId);
        return pedidoRepository.findByUsuario(cliente);
    }

    // ============================================================
    //       LISTAR TODOS (ADMIN)
    // ============================================================
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    // ============================================================
    //       CAMBIAR ESTADO (ADMIN)
    // ============================================================
    public Pedido cambiarEstado(Integer idPedido, String nuevoEstado) {

        Pedido p = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        p.setEstado(nuevoEstado);
        return pedidoRepository.save(p);
    }

    // ============================================================
    //       CANCELAR PEDIDO (CLIENTE)
    // ============================================================
    public Pedido cancelarPedido(Integer idPedido, Integer usuarioId) {

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No puedes cancelar el pedido de otro usuario");
        }

        if (!pedido.getEstado().equalsIgnoreCase("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden cancelar pedidos pendientes");
        }

        pedido.setEstado("CANCELADO");
        return pedidoRepository.save(pedido);
    }

    // ============================================================
    //       ELIMINAR PEDIDO (CLIENTE)
    // ============================================================
    public void eliminarPedidoCliente(Integer idPedido, Integer usuarioId) {

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No puedes eliminar un pedido que no es tuyo");
        }

        if (pedido.getEstado().equalsIgnoreCase("PENDIENTE")) {
            throw new RuntimeException("No se puede eliminar un pedido pendiente");
        }

        pedidoRepository.delete(pedido);
    }

    // ============================================================
    //       ELIMINAR PEDIDO (ADMIN)
    // ============================================================
    public void eliminarPedidoAdmin(Integer idPedido) {

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedidoRepository.delete(pedido);
    }
}
