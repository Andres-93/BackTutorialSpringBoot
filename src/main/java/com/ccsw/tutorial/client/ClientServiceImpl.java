package com.ccsw.tutorial.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

@Service
public class ClientServiceImpl implements IClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public Client get(Long id) {

        return this.clientRepository.findById(id).orElse(null);
    }

    @Override
    public List<Client> findAll() {

        return (List<Client>) this.clientRepository.findAll();
    }

    @Override
    public void save(Long id, ClientDto dto) throws Exception {

        Client clienteBuscado = this.clientRepository.findByName(dto.getName());
        Client cliente;

        if (id == null) {
            cliente = new Client();

        } else {
            cliente = this.get(id);
        }

        if (clienteBuscado != null) {
            throw new Exception("Ese cliente ya existe");
        }
        cliente.setName(dto.getName());
        this.clientRepository.save(cliente);

    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.clientRepository.deleteById(id);

    }

}
