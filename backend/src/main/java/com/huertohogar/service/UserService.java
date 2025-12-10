package com.huertohogar.service;

import com.huertohogar.model.Role;
import com.huertohogar.model.User;
import com.huertohogar.repository.RoleRepository;
import com.huertohogar.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // =====================================================
    // BUSCAR USUARIO POR EMAIL (para login)
    // =====================================================
    public Optional<User> buscarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // =====================================================
    // REGISTRO CLIENTE
    // =====================================================
    public User registrarUsuario(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Role rolCliente = roleRepository.findByNombre("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("No se encontró el rol CLIENTE"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(rolCliente);

        return userRepository.save(user);
    }

    // =====================================================
    // REGISTRO ADMIN
    // =====================================================
    public User registrarAdmin(User admin) {

        if (userRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Role rolAdmin = roleRepository.findByNombre("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("No se encontró el rol ADMIN"));

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole(rolAdmin);

        return userRepository.save(admin);
    }

    // =====================================================
    // OBTENER USUARIO POR ID
    // =====================================================
    public User obtenerPorId(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // =====================================================
    // ACTUALIZAR DATOS DEL CLIENTE (los que se ven en Perfil)
    // =====================================================
    public User actualizarUsuario(Integer id, User datos) {

        User user = obtenerPorId(id);

        user.setNombre(datos.getNombre());
        user.setApellido(datos.getApellido());
        user.setDireccion(datos.getDireccion());
        user.setTelefono(datos.getTelefono());

        // Aquí NO se cambia la contraseña. Solo con endpoint propio.
        return userRepository.save(user);
    }

    // =====================================================
    // CAMBIAR CONTRASEÑA DEL CLIENTE
    // =====================================================
    public void cambiarPassword(Integer id, String passwordActual, String passwordNueva) {

        User user = obtenerPorId(id);

        if (!passwordEncoder.matches(passwordActual, user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        if (passwordNueva == null || passwordNueva.isBlank()) {
            throw new RuntimeException("La nueva contraseña no puede estar vacía");
        }

        user.setPassword(passwordEncoder.encode(passwordNueva));
        userRepository.save(user);
    }

    // =====================================================
    // ADMIN — EDITAR USUARIO COMPLETO
    // =====================================================
    public User adminEditarUsuario(Integer id, User datos) {

        User user = obtenerPorId(id);

        user.setNombre(datos.getNombre());
        user.setApellido(datos.getApellido());
        user.setEmail(datos.getEmail());
        user.setDireccion(datos.getDireccion());
        user.setTelefono(datos.getTelefono());

        // Cambiar rol si viene un rol nuevo
        if (datos.getRole() != null) {
            Role nuevoRol = roleRepository.findByNombre(datos.getRole().getNombre())
                    .orElseThrow(() -> new RuntimeException("Rol no existe"));
            user.setRole(nuevoRol);
        }

        return userRepository.save(user);
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================
    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    // =====================================================
    // ELIMINAR USUARIO (manejo FK pedidos)
    // =====================================================
    @Transactional
    public void eliminarUsuario(Integer id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("El usuario no existe");
        }

        try {
            userRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("No se puede eliminar: tiene pedidos asociados.");
        }
    }

    // =====================================================
    // CREAR ADMIN DEFAULT (solo 1 vez)
    // =====================================================
    public void crearAdminDefault() {

        if (userRepository.existsByEmail("admin@gmail.com")) return;

        Role rolAdmin = roleRepository.findByNombre("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROL ADMIN no encontrado"));

        User admin = new User();
        admin.setNombre("Admin");
        admin.setApellido("Master");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setDireccion("Santiago Centro");
        admin.setTelefono("999999999");
        admin.setRole(rolAdmin);

        userRepository.save(admin);
    }
}
