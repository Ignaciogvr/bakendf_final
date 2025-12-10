package com.huertohogar.controller;

import com.huertohogar.model.User;
import com.huertohogar.security.JwtUtil;
import com.huertohogar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    // ======================= LOGIN =======================

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String password = body.get("password");

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", user.getId(),
                "email", user.getEmail(),
                "nombre", user.getNombre(),
                "rol", user.getRole().getNombre()
        ));
    }

    // ======================= REGISTRO CLIENTE =======================

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registrarUsuario(user));
    }

    // ======================= CREAR ADMIN MANUAL =======================

    @PostMapping("/create-admin")
    public ResponseEntity<?> crearAdmin(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String password = body.get("password");
        String nombre = body.get("nombre");
        String apellido = body.get("apellido");

        if (userService.buscarPorEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("El admin ya existe");
        }

        User admin = new User();
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setNombre(nombre);
        admin.setApellido(apellido);
        admin.setDireccion("Santiago Centro");
        admin.setTelefono("999999999");

        return ResponseEntity.ok(userService.registrarAdmin(admin));
    }
}
