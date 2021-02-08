package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat; 
import static org.junit.Assert.assertThat;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ReservaServiceTests {

	@Autowired
	ReservaService reservaService;
	
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
		
		
	}

