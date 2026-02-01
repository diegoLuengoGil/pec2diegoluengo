package com.inventario.service;

import java.sql.SQLException;
import java.util.List;

import com.inventario.excepciones.DatoInvalidoException;
import com.inventario.model.Cliente;
import com.inventario.repository.ClienteRepository;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteRepository.obtenerClientes();
    }

    public Cliente buscarClientePorId(int id) throws SQLException, DatoInvalidoException {
        Cliente cliente = null;
        List<Cliente> clientes = clienteRepository.buscarPorCampo("id_cliente", id);
        if (!clientes.isEmpty()) {
            cliente = clientes.get(0);
        }
        return cliente;
    }

    public void insertarCliente(Cliente cliente) throws SQLException {
        clienteRepository.insertarCliente(cliente);
    }

    public boolean actualizarCliente(int id, String campo, Object valor) throws SQLException {
        return clienteRepository.actualizarClienteCampo(id, campo, valor);
    }

    public boolean eliminarCliente(int id) throws SQLException {
        return clienteRepository.eliminarCliente(id);
    }

    public boolean agregarDinero(int id, double cantidad) throws SQLException {
        return clienteRepository.agregarDinero(id, cantidad);
    }

    public boolean restarDinero(int id, double cantidad) throws SQLException {
        return clienteRepository.restarDinero(id, cantidad);
    }
}
