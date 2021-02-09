package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

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
		//ARRANGE
		User user = new User();
		user.setUsername("Pablo33");
		user.setPassword("Pablo33");
		user.setEnabled(true);
		
		Cliente cliente = new Cliente();
		cliente.setId(1);
		cliente.setNombre("Lucas");
		cliente.setApellidos("Perez");
		cliente.setTelefono("608555103");
		cliente.setUser(user);
	
		
		Reserva reserva = new Reserva();
    	
    	Date horaSalida= new Date(); 
    	horaSalida.setHours(8);
    	horaSalida.setMinutes(0);
    	
		Date fechaSalida= new Date();
		fechaSalida.setDate(22);
		fechaSalida.setMonth(3);
		fechaSalida.setYear(2021);
		fechaSalida.setHours(horaSalida.getHours());
		fechaSalida.setMinutes(horaSalida.getMinutes());
    	
    	EstadoReserva estado = new EstadoReserva();
    	estado.setId(1);
    	estado.setName("Solicitada");
    	reserva.setId(1);
    	reserva.setEstadoReserva(estado);
    	reserva.setFechaSalida(fechaSalida);
    	reserva.setHoraSalida(horaSalida);
    	reserva.setPlazas_Ocupadas(3);
		reserva.setCliente(cliente);
		
		//ACT
		Reserva reserva1 = reservaService.findResById(1);
		reserva1.setEstadoReserva(estado);
		//ASSERT
		
		assertEquals(user.getUsername(), "Pablo33");
		assertEquals(reserva1.getId(), reserva.getId());
		assertNotEquals(reserva1.getId(), 2);
		assertTrue(reserva1.getEstadoReserva().getName().equals("Solicitada"));
		
		
		
	}
}
