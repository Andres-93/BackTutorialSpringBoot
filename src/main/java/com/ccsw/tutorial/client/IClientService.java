package com.ccsw.tutorial.client;

import java.util.List;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

public interface IClientService {

    // Obtenemos cliente por ID
    Client get(Long id);

    // Obtenemos lista de todos los clientes
    List<Client> findAll();

    // Modificamos o guardamos un nuevo cliente
    void save(Long id, ClientDto dto) throws Exception;

    // Eliminamos un cliente
    void delete(Long id) throws Exception;

}
