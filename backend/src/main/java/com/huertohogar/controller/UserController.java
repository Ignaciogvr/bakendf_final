package com.huertohogar.controller;

import com.huertohogar.model.User;
import com.huertohogar.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // =========================================================
    // PERFIL DEL USUARIO
    // =========================================================
    @GetMapping("/perfil/{id}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.obtenerPorId(id));
    }

    // =========================================================
    // ACTUALIZAR DATOS DEL USUARIO CLIENTE
    // =========================================================
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody User datos) {
        return ResponseEntity.ok(userService.actualizarUsuario(id, datos));
    }

    // =========================================================
    // CAMBIAR CONTRASEÑA DEL CLIENTE
    // =========================================================
    @PutMapping("/cambiar-password/{id}")
    public ResponseEntity<?> cambiarPassword(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {

        try {
            userService.cambiarPassword(
                    id,
                    body.get("passwordActual"),
                    body.get("passwordNueva")
            );

            return ResponseEntity.ok("Contraseña cambiada correctamente");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // =========================================================
    // LISTAR TODOS LOS USUARIOS (ADMIN)
    // =========================================================
    @GetMapping("/admin/lista")
    public ResponseEntity<?> listarTodos() {
        return ResponseEntity.ok(userService.listarTodos());
    }

    // =========================================================
    // ADMIN — EDITAR USUARIO COMPLETO
    // =========================================================
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> adminEditar(@PathVariable Integer id, @RequestBody User datos) {
        return ResponseEntity.ok(userService.adminEditarUsuario(id, datos));
    }

    // =========================================================
    // ADMIN — ELIMINAR USUARIO
    // =========================================================
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {

        try {
            userService.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
