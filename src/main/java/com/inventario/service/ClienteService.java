package com.inventario.service;

import java.util.List;

import com.inventario.model.Cliente;
import com.inventario.repository.ClienteRepository;

/**
 * Clase que representa el servicio de clientes
 */
public class ClienteService {

    /**
     * Repositorio de clientes
     */
    private final ClienteRepository clienteRepository;

    /**
     * Constructor
     * 
     * @param clienteRepository el repositorio de clientes
     */
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Lista todos los clientes
     * 
     * @return la lista de clientes
     */
    public List<Cliente> listarClientes() {
        return clienteRepository.obtenerClientes();
    }

    /**
     * Busca un cliente por su ID
     * 
     * @param id el ID del cliente
     * @return el cliente encontrado
     */
    public Cliente buscarClientePorId(int id) {
        Cliente cliente = null;
        List<Cliente> clientes = clienteRepository.buscarPorCampo("id_cliente", id);
        if (!clientes.isEmpty()) {
            cliente = clientes.get(0);
        }
        return cliente;
    }

    /**
     * Inserta un cliente
     * 
     * @param cliente el cliente a insertar
     */
    public void insertarCliente(Cliente cliente) {
        clienteRepository.insertarCliente(cliente);
    }

    /**
     * Actualiza un cliente
     * 
     * @param id    el ID del cliente
     * @param campo el campo a actualizar
     * @param valor el valor a actualizar
     * @return true si se actualizó el cliente, false en caso contrario
     */
    public boolean actualizarCliente(int id, String campo, Object valor) {
        return clienteRepository.actualizarClienteCampo(id, campo, valor);
    }

    /**
     * Elimina un cliente
     * 
     * @param id el ID del cliente
     * @return true si se eliminó el cliente, false en caso contrario
     */
    public boolean eliminarCliente(int id) {
        return clienteRepository.eliminarCliente(id);
    }

    /**
     * Agrega dinero a un cliente
     * 
     * @param id       el ID del cliente
     * @param cantidad la cantidad de dinero a agregar
     * @return true si se agregó el dinero, false en caso contrario
     */
    public boolean agregarDinero(int id, double cantidad) {
        return clienteRepository.agregarDinero(id, cantidad);
    }

    /**
     * Resta dinero a un cliente
     * 
     * @param id       el ID del cliente
     * @param cantidad la cantidad de dinero a restar
     * @return true si se restó el dinero, false en caso contrario
     */
    public boolean restarDinero(int id, double cantidad) {
        return clienteRepository.restarDinero(id, cantidad);
    }
}
