
-- BASE DE DATOS: baseFernandez para aplicacion MecanicSync


CREATE DATABASE IF NOT EXISTS baseFernandez CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE baseFernandez;

-- 1. Tabla Roles
CREATE TABLE IF NOT EXISTS roles (
  id_rol INT AUTO_INCREMENT PRIMARY KEY,
  rol VARCHAR(50) NOT NULL,
  descripcion VARCHAR(255)
);

-- 2. Tabla Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  id_rol INT NOT NULL,
  usuario VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- 3. Tabla Clientes
CREATE TABLE IF NOT EXISTS clientes (
  id_cliente INT AUTO_INCREMENT PRIMARY KEY,
  cliente VARCHAR(100) NOT NULL,
  telefono VARCHAR(20),
  email VARCHAR(100),
  direccion VARCHAR(255),
  fecha_alta DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabla Marcas
CREATE TABLE IF NOT EXISTS marcas (
  id_marca INT AUTO_INCREMENT PRIMARY KEY,
  marca VARCHAR(100) NOT NULL
  
);

-- 5. Tabla Modelos
CREATE TABLE IF NOT EXISTS modelos (
  id_modelo INT AUTO_INCREMENT PRIMARY KEY,
  id_marca INT NOT NULL,
  modelo VARCHAR(100) NOT NULL,
  tipo_motor VARCHAR(50),
  anio_inicio INT,
  FOREIGN KEY (id_marca) REFERENCES marcas(id_marca)
);

-- 6. Tabla Vehiculos
CREATE TABLE IF NOT EXISTS vehiculos (
  id_vehiculo INT AUTO_INCREMENT PRIMARY KEY,
  id_cliente INT NOT NULL,
  id_modelo INT NOT NULL,
  matricula VARCHAR(20) UNIQUE NOT NULL,
  anio_matriculacion INT,
  FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
  FOREIGN KEY (id_modelo) REFERENCES modelos(id_modelo)
);

-- 7. Tabla Tipos de Reparación
CREATE TABLE IF NOT EXISTS tipos_reparacion (
  id_tipo INT AUTO_INCREMENT PRIMARY KEY,
  reparacion VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255),
  horas_estimadas DECIMAL(5,2),
  precio_hora DECIMAL(10,2),
  precio_materiales DECIMAL(10,2)
);

-- 8. Tabla Reparaciones
CREATE TABLE IF NOT EXISTS reparaciones (
  id_reparacion INT AUTO_INCREMENT PRIMARY KEY,
  id_vehiculo INT NOT NULL,
  id_tipo INT NOT NULL,
  descripcion_adicional VARCHAR(255),
  estado VARCHAR(50),
  fecha_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
  fecha_fin DATETIME NULL,
  horas_reales DECIMAL(5,2),
  importe_total DECIMAL(10,2),
  FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id_vehiculo),
  FOREIGN KEY (id_tipo) REFERENCES tipos_reparacion(id_tipo)
);
-- 9. Insertar datos iniciales


-- Insertar roles
INSERT INTO roles (rol, descripcion) VALUES
('Administrador', 'Usuario con acceso total al sistema'),
('Mecanico', 'Usuario encargado de las reparaciones y mantenimiento');

-- Insertar usuarios
INSERT INTO usuarios (id_rol, usuario, email, password) VALUES
(1, 'Paco Fernandez', 'admin@mecanicsync.com', 'admin123'),
(2, 'Juan Gomez', 'juan@mecanicsync.com', 'juan123'),
(2, 'Carlos Gallego', 'carlos@mecanicsync.com', 'carlos123'),
(2, 'Luis Mendez', 'luis@mecanicsync.com', 'luis123');

-- Insertar clientes
INSERT INTO clientes (cliente, telefono, email, direccion) VALUES
('Pedro Gómez', '600123456', 'pedro.gomez@email.com', 'Calle Mayor 10, Madrid'),
('Laura Martínez', '611234567', 'laura.martinez@email.com', 'Avenida de Andalucía 45, Sevilla'),
('Antonio Ruiz', '622345678', 'antonio.ruiz@email.com', 'Calle Valencia 22, Valencia'),
('María López', '633456789', 'maria.lopez@email.com', 'Calle del Sol 8, Málaga'),
('Javier Fernández', '644567890', 'javier.fernandez@email.com', 'Calle Luna 33, Barcelona'),
('Sergio Navarro', '655112233', 'sergio.navarro@email.com', 'Calle Alcalá 120, Madrid'),
('Elena Torres', '644998877', 'elena.torres@email.com', 'Avenida Libertad 89, Bilbao'),
('Ruben Castillo', '699223344', 'ruben.castillo@email.com', 'Paseo del Prado 55, Madrid'),
('Patricia Díaz', '688334455', 'patricia.diaz@email.com', 'Calle Serrano 14, Madrid'),
('Oscar Hernández', '677445566', 'oscar.hernandez@email.com', 'Avenida del Mar 77, Valencia');


-- Para cargar del cvs marcas y modelos primero creamos una tabla temporal
-- Crear tabla temporal
CREATE TEMPORARY TABLE tmp_vehiculos (
    marca VARCHAR(100),
    modelo VARCHAR(100),
    tipo_motor VARCHAR(50),
    anio_inicio INT
);

-- Cargar CSV en tabla temporal
LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/vehiculos_modelos_limpio.csv'
INTO TABLE tmp_vehiculos
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS;

-- Insertar marcas únicas en tabla marcas
-- Limitamos a las 50 primeras marcas y a sus modelos
INSERT INTO marcas (marca)
SELECT DISTINCT marca
FROM tmp_vehiculos
LIMIT 50;

INSERT INTO modelos (id_marca, modelo, tipo_motor, anio_inicio)
SELECT m.id_marca, t.modelo, t.tipo_motor, t.anio_inicio
FROM tmp_vehiculos t
JOIN marcas m ON t.marca = m.marca;

-- Borrar tabla temporal
DROP TEMPORARY TABLE tmp_vehiculos;

-- Insertar tipos_reparacion

INSERT INTO tipos_reparacion (reparacion, descripcion, horas_estimadas, precio_hora, precio_materiales) VALUES
('Cambio de aceite', 'Sustitución de aceite y filtro', 1.50, 30.00, 25.00),
('Revisión frenos', 'Comprobación y sustitución de pastillas si es necesario', 2.00, 30.00, 50.00),
('Alineación de ruedas', 'Alineación y equilibrado de ruedas', 1.00, 30.00, 10.00),
('Cambio de batería', 'Sustitución de batería por nueva', 0.75, 30.00, 80.00),
('Reparación motor', 'Reparación parcial del motor', 5.00, 30.00, 200.00);


-- Insertar vehiculos

INSERT INTO vehiculos (id_cliente, id_modelo, matricula, anio_matriculacion) VALUES
(1, 1, '1234ABC', 2018),
(2, 2, '5678DEF', 2020),
(3, 3, '9012GHI', 2017),
(4, 4, '3456JKL', 2019),
(5, 5, '7890MNO', 2021),
(6, 6, '1122XYZ', 2016),
(7, 7, '3344QWE', 2019),
(8, 8, '5566RTY', 2021),
(9, 9, '7788UIO', 2015),
(10, 10, '9900PAS', 2018);



-- Insertar reparaciones

INSERT INTO reparaciones (id_vehiculo, id_tipo, descripcion_adicional, estado, fecha_inicio, fecha_fin, horas_reales, importe_total) VALUES
(6, 1, 'Aceite 10W40 semisintético', 'Entregado', '2019-03-12 09:00:00', '2019-03-12 10:30:00', 1.50, (1.50*30.00)+25.00),
(7, 2, 'Pastillas delanteras', 'Entregado', '2020-07-20 11:00:00', '2020-07-20 13:00:00', 2.00, (2.00*30.00)+50.00),
(8, 3, 'Alineación completa', 'Entregado', '2020-09-05 15:00:00', '2020-09-05 16:00:00', 1.00, (1.00*30.00)+10.00),
(9, 4, 'Batería 70Ah nueva', 'Entregado', '2021-02-18 08:20:00', '2021-02-18 09:05:00', 0.75, (0.75*30.00)+80.00),
(10, 5, 'Cambio kit distribución', 'Entregado', '2022-06-10 10:00:00', '2022-06-10 16:00:00', 5.00, (5.00*30.00)+200.00),
(6, 2, 'Frenos traseros cambiados', 'Entregado', '2022-12-01 09:30:00', '2022-12-01 11:30:00', 2.00, (2.00*30.00)+50.00),
(7, 1, 'Cambio de aceite y revisión Entregado', 'Finalizada', '2023-03-14 08:45:00', '2023-03-14 10:15:00', 1.50, (1.50*30.00)+25.00),
(8, 4, 'Batería AGM reemplazada', 'Entregado', '2024-04-21 11:00:00', '2024-04-21 11:50:00', 0.75, (0.75*30.00)+80.00),
(10, 3, 'Alineación por vibración', 'Entregado', '2024-02-17 16:00:00', '2024-02-17 17:00:00', 1.00, (1.00*30.00)+10.00),
(1, 1, 'Aceite sintético 5W30', 'Entregado', '2025-11-01 10:00:00', '2025-11-01 11:30:00', 1.50, (1.50*30.00)+25.00),
(2, 2, 'Pastillas delanteras y traseras', 'En proceso', '2025-11-05 09:00:00', NULL, 1.80, (1.80*30.00)+50.00),
(3, 3, 'Alineación ruedas delanteras', 'Finalizada', '2025-11-03 14:00:00', '2025-11-03 15:00:00', 1.00, (1.00*30.00)+10.00),
(4, 4, 'Batería 60Ah reemplazada', 'Finalizada', '2025-11-07 08:30:00', '2025-11-07 09:15:00', 0.75, (0.75*30.00)+80.00),
(3, 5, 'Revisión culata y correas', 'Pendiente', '2025-11-10 11:00:00', NULL, 5.00, (5.00*30.00)+200.00),
(1, 2, 'Pastillas traseras nuevas', 'Finalizada', '2025-11-08 10:00:00', '2025-11-08 12:00:00', 2.00, (2.00*30.00)+50.00),
(5, 1, 'Cambio de aceite mineral', 'Entregado', '2025-11-12 09:00:00','2025-12-08 10:00:00', 1.20, (1.20*30.00)+25.00);


