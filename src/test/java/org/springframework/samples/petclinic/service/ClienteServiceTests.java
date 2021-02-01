package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClienteServiceTests {

	@Autowired
	ClienteService clienteService;
	
	@Autowired
	ReservaService reservaService;
	
	@Test
	public void findByIdTest() {
		Cliente cliente=clienteService.findClienteById(1);
		assertEquals(cliente.getId(),1);
	}
	@Test
	@Transactional
	public void newClienteTest() {
		
		Cliente cliente = new Cliente();
		cliente.setId(8);
		cliente.setNombre("Pablo");
		cliente.setApellidos("Garcia");
		cliente.setEmail("pablo@gmail.com");
		cliente.setTelefono("678823472");
		cliente.setDni("43434343K");
	
		assertEquals(cliente.getId(), 8);
		assertNotEquals(cliente.getId(), 7);
		assertTrue(cliente.getNombre().equals("Pablo"));
		assertFalse(cliente.getApellidos().equals("Lopez"));
	}
	
	@Test
	@Transactional
	public void editClienteTest() {
		
		Cliente cliente = clienteService.findClienteById(1);
		
		String telefono = cliente.getTelefono();
		String nuevoTelefono="659874123";
		cliente.setTelefono(nuevoTelefono);
		clienteService.saveCliente(cliente);
		
		Cliente clienteActualizado=clienteService.findClienteById(1);
		assertEquals(telefono,clienteActualizado.getTelefono());
	}
	@Test
	@Transactional
	public void findReservaByIdTest() {	
		
		Collection<Reserva> reserva = reservaService.findReservasByClienteId(1);
		Collection<Reserva> reserva2 = reservaService.findReservasByClienteId(8);
		
		assertEquals(reserva.size(), 1);
		assertNotEquals(reserva2.size(), 1);
		assertNotNull(reserva);
	}
}
