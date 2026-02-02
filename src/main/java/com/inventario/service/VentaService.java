package com.inventario.service;

import java.util.List;

import com.inventario.model.DetalleVenta;
import com.inventario.repository.VentaRepository;
import com.inventario.excepciones.SaldoInsuficienteException;

/**
 * Clase que representa el servicio de ventas
 */
public class VentaService {

    /**
     * Repositorio de ventas
     */
    private final VentaRepository ventaRepository;

    /**
     * Constructor
     * 
     * @param ventaRepository el repositorio de ventas
     */
    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    /**
     * Realiza una venta
     * 
     * @param idCliente el ID del cliente
     * @param detalles  la lista de detalles de la venta
     * @throws SaldoInsuficienteException si el cliente no tiene saldo suficiente
     */
    public void realizarVenta(int idCliente, List<DetalleVenta> detalles) throws SaldoInsuficienteException {
        ventaRepository.realizarVentaCompleta(idCliente, detalles);
    }

    /**
     * Lista el resumen de las ventas
     * 
     * @return la lista de resúmenes de las ventas
     */
    public List<String> listarResumenVentas() {
        return ventaRepository.obtenerResumenVentas();
    }

    /**
     * Lista los detalles de una venta
     * 
     * @param idVenta el ID de la venta
     * @return la lista de detalles de la venta
     */
    public List<String> listarDetallesVenta(int idVenta) {
        return ventaRepository.obtenerDetallesVenta(idVenta);
    }

    /**
     * Obtiene el total de una venta
     * 
     * @param idVenta el ID de la venta
     * @return el total de la venta
     */
    public double obtenerTotalVenta(int idVenta) {
        return ventaRepository.calcularTotalVenta(idVenta);
    }
}
