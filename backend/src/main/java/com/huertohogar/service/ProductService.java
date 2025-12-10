package com.huertohogar.service;

import com.huertohogar.model.Product;
import com.huertohogar.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> listarTodos() {
        return productRepository.findAll();
    }

    public List<Product> listarPorCategoria(String categoria) {
        return productRepository.findByCategoria(categoria);
    }

    public Product obtenerPorId(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Product crearProducto(Product product) {
        return productRepository.save(product);
    }

    public Product editarProducto(Integer id, Product datos) {
        Product p = obtenerPorId(id);

        p.setNombre(datos.getNombre());
        p.setPrecio(datos.getPrecio());
        p.setUnidad(datos.getUnidad());
        p.setImg(datos.getImg());
        p.setCategoria(datos.getCategoria());
        p.setStock(datos.getStock());
        p.setDescripcion(datos.getDescripcion());

        return productRepository.save(p);
    }

    // RESTA STOCK cuando el cliente compra
    public void descontarStock(Integer idProducto, int cantidad) {
        Product p = obtenerPorId(idProducto);
        if (p.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente del producto: " + p.getNombre());
        }
        p.setStock(p.getStock() - cantidad);
        productRepository.save(p);
    }

    public void eliminarProducto(Integer id) {
        productRepository.deleteById(id);
    }
}
