-- ======================================
-- Script: Creación de base de datos
-- ======================================

-- Crear base de datos
DROP DATABASE IF EXISTS tienda;
CREATE DATABASE IF NOT EXISTS tienda;
USE tienda;

-- ======================================
-- Tabla: Producto
-- ======================================
CREATE TABLE IF NOT EXISTS Producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL CHECK (stock >= 0)
);

-- ======================================
-- Tabla: Cliente
-- ======================================
CREATE TABLE IF NOT EXISTS Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    dinero int NOT NULL,
    CONSTRAINT chk_dinero_positivo CHECK (dinero >= 0)
);

-- ======================================
-- Tabla: Venta
-- ======================================
CREATE TABLE IF NOT EXISTS Venta (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente) ON DELETE CASCADE
);

-- ======================================
    -- Tabla: DetalleVenta
    -- ======================================
CREATE TABLE IF NOT EXISTS DetalleVenta (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES Venta(id_venta) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto) ON DELETE CASCADE
);


DELIMITER //
CREATE PROCEDURE RegistrarDetalleVenta( 
    IN p_idVenta INT,       
    IN p_idProducto INT,      
    IN p_cantidad INT,        
    IN p_precioUnitario DECIMAL(10, 2), 
    OUT p_estado INT          
)
BEGIN
    DECLARE stock_actual INT;
    
    
    SELECT stock INTO stock_actual 
    FROM producto 
    WHERE id_producto = p_idProducto;
    
    IF stock_actual IS NULL THEN
        SET p_estado = -2;
    ELSEIF stock_actual < p_cantidad THEN
        SET p_estado = -1;
    ELSE
        INSERT INTO DetalleVenta (id_venta, id_producto, cantidad, precio_unitario)
        VALUES (p_idVenta, p_idProducto, p_cantidad, p_precioUnitario);
        
        UPDATE producto 
        SET stock = stock - p_cantidad 
        WHERE id_producto = p_idProducto;
        
        SET p_estado = 1;
    END IF;
END //
DELIMITER ;

Delimiter //

CREATE FUNCTION CalcularTotalVenta(p_id_venta INT)
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE total DECIMAL(10,2);

    SELECT SUM(cantidad * precio_unitario) INTO total 
    FROM detalleVenta
    WHERE id_venta = p_id_venta;
    
    RETURN IFNULL(total, 0.0);
END //

DELIMITER ;

