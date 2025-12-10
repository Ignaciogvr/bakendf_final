package com.huertohogar.repository;

import com.huertohogar.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Buscar productos por categoría
    List<Product> findByCategoria(String categoria);

    // Buscar por nombre (opcional si quieres)
    List<Product> findByNombreContainingIgnoreCase(String nombre);
}
