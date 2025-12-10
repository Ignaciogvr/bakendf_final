package com.huertohogar.controller;

import com.huertohogar.model.Product;
import com.huertohogar.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // LISTAR TODOS
    @GetMapping
    public List<Product> listar() {
        return productService.listarTodos();
    }

    // LISTAR POR CATEGORÍA
    @GetMapping("/categoria/{cat}")
    public List<Product> listarPorCat(@PathVariable("cat") String categoria) {
        return productService.listarPorCategoria(categoria);
    }

    // DETALLE
    @GetMapping("/{id}")
    public Product detalle(@PathVariable Integer id) {
        return productService.obtenerPorId(id);
    }

    // CREAR (ADMIN)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Product p) {
        return ResponseEntity.ok(productService.crearProducto(p));
    }

    // EDITAR (ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @RequestBody Product p) {
        return ResponseEntity.ok(productService.editarProducto(id, p));
    }

    // ELIMINAR (ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        productService.eliminarProducto(id);
        return ResponseEntity.ok().build();
    }
}
