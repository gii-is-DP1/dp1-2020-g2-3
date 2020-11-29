package org.springframework.samples.petclinic.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClienteServiceTest {

	@Autowired
	ClienteService clienteService;
	
	@Test
	public void findByIdTest() {
		Cliente cliente=clienteService.findClienteById(1);
		assertEquals(cliente.getId(),1);
	}
	
	@Test
	@Transactional
	public void editClienteTest() {
		
		Cliente cliente = clienteService.findClienteById(1);
		
		String telefono = cliente.getTelefono();
		String nuevoTelefono="655001259";
		cliente.setTelefono(nuevoTelefono);
		clienteService.saveCliente(cliente);
		
		Cliente clienteActualizado=clienteService.findClienteById(1);
		assertEquals(telefono,clienteActualizado.getTelefono());
	}
}
