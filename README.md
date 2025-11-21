# Gestor de Inventario y Ventas

## Resumen General
Este proyecto es una aplicación de consola en Java diseñada para la gestión integral de una tienda. Permite administrar el inventario de productos, gestionar la cartera de clientes y realizar ventas con control transaccional. El sistema se conecta a una base de datos MySQL para la persistencia de datos, asegurando la integridad y consistencia de la información.

## Autor
**Diego Luengo Gil**

## Estructura del Proyecto

### Árbol de Directorios
```
src/main/java/com/inventario
├── Main.java
├── bbdd
│   ├── ConexionBBDD.java
│   └── GestionBBDD.java
├── clientes
│   ├── Cliente.java
│   ├── ClientesBBDD.java
│   └── GestionDeClientes.java
├── excepciones
│   └── DatoInvalidoException.java
├── productos
│   ├── GestionDeProductos.java
│   ├── Producto.java
│   └── ProductosBBDD.java
├── util
│   └── Util.java
└── ventas
    ├── DetalleVenta.java
    ├── GestionVentas.java
    ├── Venta.java
    └── VentasBBDD.java
```

### Descripción de Paquetes y Clases

#### Paquete Principal (`com.inventario`)
- **`Main.java`**: Punto de entrada. Inicia el menú principal y dirige el flujo hacia los diferentes módulos (productos, clientes, ventas).

#### Paquete `bbdd`
- **`ConexionBBDD.java`**: Gestiona la conexión JDBC leyendo la configuración desde `config.properties`.
- **`GestionBBDD.java`**: Utilidades genéricas para consultas y visualización de tablas.

#### Paquete `clientes`
- **`Cliente.java`**: Modelo de datos del cliente con validaciones.
- **`ClientesBBDD.java`**: Clase para operaciones CRUD en la tabla `cliente`.
- **`GestionDeClientes.java`**: Lógica de menús para clientes.

#### Paquete `productos`
- **`Producto.java`**: Modelo de datos del producto.
- **`ProductosBBDD.java`**: Clase para operaciones CRUD en la tabla `producto`.
- **`GestionDeProductos.java`**: Lógica de menús para productos.

#### Paquete `ventas`
- **`Venta.java` / `DetalleVenta.java`**: Modelos de datos para ventas y sus líneas.
- **`VentasBBDD.java`**: Clase con transacciones y llamadas a procedimientos almacenados.
- **`GestionVentas.java`**: Flujo de venta interactivo (carrito, confirmación, rollback).

#### Paquete `util`
- **`Util.java`**: Herramientas para entrada de datos robusta y validaciones.

#### Paquete `excepciones`
- **`DatoInvalidoException.java`**: Excepción personalizada para reglas de negocio.

## Instrucciones de Ejecución

### 1. Requisitos Previos
- Java Development Kit (JDK) 17 o superior.
- Servidor MySQL o MariaDB en ejecución.
- Maven.

### 2. Configuración de la Base de Datos
Antes de ejecutar la aplicación, es **CRUCIAL** configurar la conexión a la base de datos:

1.  Navega al archivo de configuración:
    `src/main/resources/config.properties`
2.  Abre el archivo y modifica los valores según tu configuración local de MySQL:
    ```properties
    db.url=jdbc:mysql://localhost:3306/tienda
    db.user=TU_USUARIO_MYSQL      <-- Cambia esto (ej. root)
    db.password=TU_CONTRASEÑA     <-- Cambia esto
    ```
    > **Nota:** Asegúrate de que el usuario tenga permisos para crear bases de datos y tablas, o ejecuta primero el script SQL manualmente.

### 3. Inicialización de la Base de Datos
El proyecto incluye un script SQL para crear la estructura necesaria.
-   Ejecuta el archivo `Script.sql` en tu gestor de base de datos (Workbench, DBeaver, consola) para crear la base de datos `tienda`, las tablas y los procedimientos almacenados.

### 4. Ejecutar la Aplicación
Compila y ejecuta la clase `Main.java` desde tu IDE favorito (VS Code, IntelliJ, Eclipse).
