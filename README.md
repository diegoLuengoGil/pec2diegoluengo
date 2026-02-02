# Gestor de Inventario y Ventas (Versión JPA/Hibernate)

## Resumen General
Esta proyecto es una aplicación de consola en Java refactorizada para utilizar **JPA (Java Persistence API)** con **Hibernate** como proveedor de persistencia. Permite administrar el inventario de productos, gestionar clientes y registrar ventas de manera transaccional y robusta, cumpliendo con los estándares de la arquitectura MVC y patrones de diseño Repository/Service.

## Características Principales
*   **Arquitectura en Capas**: MVC (Model-View-Controller) + capas de Servicio y Repositorio.
*   **Persistencia ORM**: Uso de Hibernate 6 para el mapeo objecto-relacional, eliminando SQL nativo en el código Java.
*   **Transaccionalidad ACID**: Las ventas se realizan en una única transacción atómica; si falla el stock o el saldo, se revierte toda la operación (rollback).
*   **Validación de Esquema**: Configurado para validar estrictamente que las clases Java coincidan con el esquema de base de datos (`hibernate.hbm2ddl.auto=validate`).

## Autor
**Diego Luengo Gil**

## Estructura del Proyecto

### Árbol de Directorios
```
src/main/java/com/inventario
├── Main.java                 # Punto de entrada
├── controller                # Controladores (Orquestación)
│   ├── GlobalController.java
│   ├── ClienteController.java
│   ├── ProductoController.java
│   └── VentaController.java
├── model                     # Entidades JPA (Base de Datos)
│   ├── Cliente.java
│   ├── Producto.java
│   ├── Venta.java
│   └── DetalleVenta.java
├── repository                # Acceso a Datos (DAO pattern con JPA)
│   ├── ClienteRepository.java
│   ├── ProductoRepository.java
│   └── VentaRepository.java
├── service                   # Lógica de Negocio
│   ├── ClienteService.java
│   ├── ProductoService.java
│   └── VentaService.java
├── view                      # Vistas (Interfaz de Consola)
│   ├── MainView.java
│   ├── ClienteView.java
│   ├── ProductoView.java
│   └── VentaView.java
└── util
    ├── JPAUtil.java          # Factoría de EntityManager (Singleton)
    └── Util.java             # Herramientas de entrada de datos
```

### Descripción de Componentes Clave

*   **JPAUtil**: Gestiona el `EntityManagerFactory`. Se inicializa estáticamente y debe cerrarse al finalizar la aplicación.
*   **VentaRepository**: Contiene el método `realizarVentaCompleta`, que ejecuta la lógica de negocio compleja (verificar stock, saldo, actualizar ambos y registrar venta) dentro de una transacción `EntityTransaction`.
*   **persistence.xml**: Archivo de configuración estándar de JPA ubicado en `src/main/resources/META-INF/`.

## Instrucciones de Ejecución

### 1. Requisitos Previos
*   Java Development Kit (JDK) 17 o superior.
*   Servidor MySQL en local (puerto 3306).

### 2. Configuración de Base de Datos
1.  Abre el archivo `Script.sql` ubicado en la raíz del proyecto.
2.  Ejecuta todo el contenido en tu gestor de base de datos (MySQL Workbench) para crear la base de datos `tienda` y las tablas con los tipos correctos (`DOUBLE` para dinero/precio).
    > **Importante:** El script usa tipos `DOUBLE` para coincidir con el tipo `double` de Java y pasar la validación de Hibernate.

### 3. Configuración de Conexión (JPA)
Verifica el archivo `src/main/resources/META-INF/persistence.xml`.
Por defecto está configurado para:
*   URL: `jdbc:mysql://localhost:3306/tienda`
*   User: `root`
*   Password: `root` (o vacía, o la que tú tengas).

**Si tu contraseña de root es diferente**, edita la línea:
```xml
<property name="javax.persistence.jdbc.password" value="TU_CONTRASEÑA" />
```

### 4. Ejecutar
Compila y ejecuta la clase `com.inventario.Main`.
Al iniciar, Hibernate validará el esquema y mostrará el menú principal si todo es correcto.
