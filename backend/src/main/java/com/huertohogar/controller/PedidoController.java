package com.huertohogar.controller;

import com.huertohogar.model.Pedido;
import com.huertohogar.model.PedidoDetalle;
import com.huertohogar.model.Product;
import com.huertohogar.service.PedidoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // ============================================================
    //                 CREAR PEDIDO (CLIENTE)
    // ============================================================
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Map<String, Object> body) {

        Integer usuarioId = (Integer) body.get("usuarioId");
        String direccion = (String) body.get("direccion");

        List<PedidoDetalle> items = ((List<?>) body.get("items"))
                .stream()
                .map(obj -> {
                    Map<String, Object> item = (Map<String, Object>) obj;
                    PedidoDetalle det = new PedidoDetalle();

                    det.setCantidad((Integer) item.get("cantidad"));

                    Product p = new Product();
                    p.setId((Integer) item.get("productoId"));
                    det.setProducto(p);

                    return det;
                })
                .toList();

        return ResponseEntity.ok(pedidoService.crearPedido(usuarioId, direccion, items));
    }

    // ============================================================
    //               CREAR PEDIDO POR ADMIN
    // ============================================================
    @PostMapping("/admin/{usuarioId}")
    public ResponseEntity<?> crearPedidoAdmin(
            @PathVariable Integer usuarioId,
            @RequestBody Map<String, Object> body) {

        String direccion = (String) body.get("direccion");

        List<PedidoDetalle> items = ((List<?>) body.get("items"))
                .stream()
                .map(obj -> {
                    Map<String, Object> item = (Map<String, Object>) obj;
                    PedidoDetalle det = new PedidoDetalle();

                    det.setCantidad((Integer) item.get("cantidad"));

                    Product p = new Product();
                    p.setId((Integer) item.get("productoId"));
                    det.setProducto(p);

                    return det;
                })
                .toList();

        return ResponseEntity.ok(pedidoService.crearPedido(usuarioId, direccion, items));
    }

    // ============================================================
    //               HISTORIAL POR USUARIO
    // ============================================================
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> pedidosUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.pedidosPorUsuario(id));
    }

    // ============================================================
    //               LISTAR TODOS (ADMIN)
    // ============================================================
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    // ============================================================
    //               CAMBIAR ESTADO (ADMIN)
    // ============================================================
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok(pedidoService.cambiarEstado(id, body.get("estado")));
    }

    // ============================================================
    //               CANCELAR PEDIDO (CLIENTE)
    // ============================================================
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {

        Integer usuarioId = (Integer) body.get("usuarioId");

        return ResponseEntity.ok(pedidoService.cancelarPedido(id, usuarioId));
    }

    // ============================================================
    //               ELIMINAR PEDIDO (CLIENTE)
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPedidoCliente(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {

        Integer usuarioId = (Integer) body.get("usuarioId");

        pedidoService.eliminarPedidoCliente(id, usuarioId);

        return ResponseEntity.ok("Pedido eliminado (cliente)");
    }

    // ============================================================
    //               ELIMINAR PEDIDO (ADMIN)
    // ============================================================
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> eliminarPedidoAdmin(@PathVariable Integer id) {

        pedidoService.eliminarPedidoAdmin(id);

        return ResponseEntity.ok("Pedido eliminado por administrador");
    }
}
