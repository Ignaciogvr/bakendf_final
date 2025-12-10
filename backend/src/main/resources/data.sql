-- ======================================================
-- LIMPIAR TABLAS (EN ORDEN CORRECTO)
-- ======================================================
DELETE FROM pedido_detalle;
DELETE FROM pedidos;
DELETE FROM products;
DELETE FROM usuarios;
DELETE FROM roles;

-- ======================================================
-- CREAR ROLES
-- ======================================================
INSERT INTO roles (nombre) VALUES 
('ROLE_ADMIN'),
('ROLE_CLIENTE');

-- ======================================================
-- USUARIO ADMIN (password = 1234)
-- ======================================================
INSERT INTO usuarios (nombre, apellido, email, password, direccion, telefono, rol_id)
VALUES (
    'Admin',
    'Master',
    'admin@gmail.com',
    '$2a$10$7EqJtq98hPqEX7fNZaFWoOt29M0FG8sVtI6FATf3pC6kY4CJra0Fe',
    'Santiago Centro',
    '999999999',
    1
);

-- ======================================================
-- USUARIO CLIENTE (password = 123456)
-- ======================================================
INSERT INTO usuarios (nombre, apellido, email, password, direccion, telefono, rol_id)
VALUES (
    'Ignacio',
    'Nunez',
    'ignacio@correo.com',
    '$2a$10$J8IB5rEgKcxEhtccZXgjeOvBLTCSQ7vXO5FRDg3l3yX3J0bh2YLC6',
    'Av. Siempre Viva 123',
    '987654321',
    2
);

-- ======================================================
-- PRODUCTOS (RUTAS CORRECTAS)
-- ======================================================
INSERT INTO products (nombre, precio, unidad, img, categoria, stock, descripcion) VALUES
('Manzanas Fuji', 1200, 'kg', '/img/producto/manzanafuji.jpg', 'frutas', 150,
 'Manzanas Fuji crujientes y dulces.'),

('Naranjas Valencia', 1000, 'kg', '/img/producto/naranjasvalencia.jpg', 'frutas', 200,
 'Jugosas y ricas en vitamina C.'),

('Plátanos Cavendish', 800, 'kg', '/img/producto/platanos_cavendish.jpg', 'frutas', 250,
 'Plátanos maduros y dulces.'),

('Zanahorias Orgánicas', 900, 'kg', '/img/producto/zanahoriasorganicas.jpg', 'verduras', 100,
 'Zanahorias cultivadas sin pesticidas.'),

('Espinacas Frescas', 700, '500g', '/img/producto/espinacas_frescas.jpg', 'verduras', 80,
 'Espinacas frescas y nutritivas.'),

('Pimientos Tricolores', 1500, 'kg', '/img/producto/pimientos_tricolores.jpg', 'verduras', 120,
 'Pimientos en tres colores.'),

('Miel Orgánica', 5000, '500g', '/img/producto/miel.jpg', 'organicos', 50,
 'Miel pura y orgánica.'),

('Quinua Orgánica', 3200, 'kg', '/img/producto/quinua_organica.jpg', 'organicos', 70,
 'Grano andino rico en proteínas.'),

('Leche Entera', 1200, 'L', '/img/producto/leche_entera.jpg', 'lacteos', 100,
 'Leche entera fresca.');

-- ======================================================
-- PEDIDO DE EJEMPLO DEL USUARIO IGNACIO (ID = 2)
-- ======================================================
INSERT INTO pedidos (usuario_id, direccion_entrega, total, estado)
VALUES (2, 'Av. Siempre Viva 123', 3400, 'En preparación');

SET @pedido_id = LAST_INSERT_ID();

INSERT INTO pedido_detalle (pedido_id, producto_id, cantidad, precio_unitario)
VALUES 
(@pedido_id, 1, 1, 1200),
(@pedido_id, 4, 2, 700);
