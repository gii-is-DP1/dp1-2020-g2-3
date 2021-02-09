package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.User;
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
		//ARRANGE Y ACT
		Cliente cliente=clienteService.findClienteById(1);
		//ASSERT
		assertEquals(cliente.getId(),1);
	}
	
	@Test
	public void findByNombreTest() {
		//ARRANGE
		Cliente cliente=clienteService.findClienteById(1);
		String nombre = cliente.getNombre();
		
		//ACT
		Collection<Cliente> c = clienteService.findClienteByNombre(nombre);
		
		//ASSERT
		for(Cliente res: c) {
			assertEquals(cliente.getNombre(), res.getNombre());
		}
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
		
		//ACT
		Reserva reserva1 = reservaService.findResById(1);

		//ASSERT
		assertEquals(reserva1.getId(), 1);
		assertNotEquals(reserva1.getId(), 2);
		assertTrue(reserva1.getEstadoReserva().getName().equals("Completada"));
		
		
		
	}
	
	@Test
	@Transactional
	public void findByUsernameTest() {
		
		//ACT
		Cliente cliente =clienteService.findClienteByUsername("pepe33");
		Cliente cliente2 =clienteService.findClienteByUsername("manuel84");
		
		//ASSERT
		assertTrue(cliente.getUser().getUsername().equals("pepe33"));
		assertNotEquals(cliente.getUser().getUsername(), "manuel33");
		assertFalse(cliente2.getUser().getUsername().equals(cliente.getUser().getUsername()));
	}
	
	@Test
	@Transactional
	public void findByNombreTest1() {
		//ACT
		Collection <Cliente> clientes = clienteService.findClienteByNombre("Perez");
		//ASSERT
		assertEquals(clientes.size(), 2);
	}
	
	@Test
	public void findAllTest() {
		//ACT
		Iterable <Cliente> clientes = clienteService.findAll();
		Set <Cliente> res = new HashSet<>();
		for(Cliente c: clientes) {
			res.add(c);
		}
		
		//ASSERT
		assertEquals(res.size(), 3);
	}
	
	@Test
	public void findIdByUsernameTest() {
		//ACT
		Integer id = clienteService.findIdByUsername("manuel84");
		//ASSERT
		assertEquals(id ,4);
	}
}
