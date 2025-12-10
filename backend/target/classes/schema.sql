-- ========================================
-- ELIMINAR TABLAS EN ORDEN CORRECTO
-- ========================================
DROP TABLE IF EXISTS pedido_detalle;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS roles;

-- ========================================
-- CREAR ROLES
-- ========================================
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- ========================================
-- CREAR USUARIOS
-- ========================================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    rol_id INT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- ========================================
-- CREAR PRODUCTOS
-- ========================================
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    precio INT NOT NULL,
    unidad VARCHAR(50) NOT NULL,
    img VARCHAR(255) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    stock INT NOT NULL,
    descripcion TEXT
);

-- ========================================
-- CREAR PEDIDOS
-- ========================================
CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    direccion_entrega VARCHAR(255) NOT NULL,
    total INT NOT NULL,
    estado VARCHAR(50) DEFAULT 'En preparación',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- ========================================
-- CREAR DETALLE DEL PEDIDO
-- ========================================
CREATE TABLE pedido_detalle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario INT NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (producto_id) REFERENCES products(id)
);
