package org.springframework.samples.petclinic.service;


import static org.junit.Assert.assertEquals;



import java.util.Collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;

import org.springframework.samples.petclinic.model.Trabajador;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ReservaServiceTests {

	@Autowired
	ReservaService reservaService;
	@Autowired
	TrabajadorService trabajadorService;
	@Autowired
	ClienteService clienteService;
	@Autowired
	EstadoReservaService estadoService;
	
	@Test
	@Transactional
	@DisplayName("Obtener todas las reservas cuyo estado sea igual a SOLICITADA")
	void findPeticionesReservasTest() { //CUSTOM QUERY
		
		//ARRANGE
		Reserva reserva= reservaService.findReservaById(1).get(); //Metemos una reserva SOLICITADA en la BD
		EstadoReserva estado= reserva.getEstadoReserva();
		estado.setName("Solicitada");
		reserva.setEstadoReserva(estado);
		reservaService.save(reserva);
		
		int numeroSolicitudes1=0;
		//ACT
		Iterable<Reserva> solicitudesReserva1= reservaService.findPeticionesReserva();
		//ASSERT
		for(Reserva res: solicitudesReserva1) {  //Comprobamos que todas los estados de las reservas son solicitadas
			assertEquals(res.getEstadoReserva().getName(),"Solicitada");
			numeroSolicitudes1++;
		}
		
		//ARRANGE 2
		estado.setName("Aceptada"); //Quitamos una reserva solicitada de la BD
		reserva.setEstadoReserva(estado);
		reservaService.save(reserva);
		//ACT 2
		Iterable<Reserva> solicitudesReserva2= reservaService.findPeticionesReserva();
		int numeroSolicitudes2=0;
		
		//ASSERT 2
		for(Reserva res: solicitudesReserva2) {
			assertEquals(res.getEstadoReserva().getName(),"Solicitada");
			numeroSolicitudes2++;
		}
		
		assertEquals(numeroSolicitudes1-1,numeroSolicitudes2); //El segundo ACT trae 1 solicitud menos
		}
	
	@Test
	@Transactional
	@DisplayName("Obtener todas las reservas cuyo estado sea igual a COMPLETADA")
	void findPeticionesByEstadoReservaCompletadaTest() { //CUSTOM QUERY
			//ARRANGE
			Reserva reserva= reservaService.findReservaById(1).get(); //Metemos una reserva SOLICITADA en la BD
			EstadoReserva estado= reserva.getEstadoReserva();
			estado.setName("Completada");
			reserva.setEstadoReserva(estado);
			reservaService.save(reserva);
			
			//ACT
			Collection<Reserva> r = reservaService.findByEstadoReservaCompletada();
			
			//ASSERT
			for(Reserva res: r) {
				assertEquals(res.getEstadoReserva().getName(), "Completada");
			}
			assertEquals(true,r.contains(reserva));
	}
	
	@Test
	@Transactional
	@DisplayName("Obtener las reservas aceptadas por un trabajador")
	void findReservasAceptadasByTrabajadorId() {
		
		//ARRANGE
		Reserva reserva= reservaService.findReservaById(3).get(); //Metemos una reserva SOLICITADA en la BD
		EstadoReserva estado= reserva.getEstadoReserva();
		estado.setName("Aceptada");
		reserva.setEstadoReserva(estado);
		
		Trabajador trabajador = trabajadorService.findById(1);
		reserva.setTrabajador(trabajador);
		reservaService.save(reserva);
		int id = reserva.getTrabajador().getId();
		
		//ACT
		Collection<Reserva> r = reservaService.findReservasAceptadasByTrabajadorId(id);
		
		//ASSERT
		for(Reserva res: r) {
			assertEquals(res.getEstadoReserva().getName(), "Aceptada");
			assertEquals(Integer.valueOf(1),reserva.getTrabajador().getId());
		}
		assertEquals("Aceptada",reserva.getEstadoReserva().getName());
		assertEquals(Integer.valueOf(1),reserva.getTrabajador().getId());
		assertEquals(true,r.contains(reserva));
	}
		
		
}

