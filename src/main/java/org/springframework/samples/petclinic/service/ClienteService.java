package org.springframework.samples.petclinic.service;

import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}	
	
	
	@Transactional(readOnly = true)
	public Cliente findClienteById(int id) throws DataAccessException {
		return clienteRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Cliente> findClienteByNombre(String nombre) throws DataAccessException {
		return clienteRepository.findByNombre(nombre);
	}
	
	@Transactional
	public Iterable<Cliente> findAll(){
		return clienteRepository.findAll();
	}
	
	
	
	@Transactional
	public void saveCliente(Cliente cliente) throws DataAccessException {
		//creating owner
		clienteRepository.save(cliente);		
		//creating user
		userService.saveUser(cliente.getUser());
		//creating authorities
		authoritiesService.saveAuthorities(cliente.getUser().getUsername(), "cliente");
	}	
	
}
