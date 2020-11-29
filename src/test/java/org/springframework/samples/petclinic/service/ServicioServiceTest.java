package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Servicio;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ServicioServiceTest {

	@Autowired
	ServicioService servicioService;
	@Test
	public void findByIdTest() {
		
		Optional<Servicio> servicio= servicioService.findServicioById(1);
		assertEquals(servicio.get().getId(),1);
	}
	
	
	
	
	
	
	@Test
	@Transactional
	public void deleteServicio() {
		
		Long numServicios=servicioService.servicioCount();
		Optional<Servicio> servicio= servicioService.findServicioById(2); //borrar un automóvil no usado en ningún serivico o viaje
		servicioService.delete(servicio.get());
		Optional<Servicio> servicio2= servicioService.findServicioById(2);
		Long numServicios2=servicioService.servicioCount();
		assertEquals (servicio2.isPresent(),false);
		assertEquals(numServicios2,numServicios-1 );
	}
	
	
	
	
	@Test
	@Transactional
	public void editTest() {
		
		Optional<Servicio> servicio= servicioService.findServicioById(1);
		
		
		Double nuevPrecio= 100.00;
		servicio.get().setPrecio(nuevPrecio);
		servicioService.save(servicio.get());
		
		Optional<Servicio> servicioActualizado= servicioService.findServicioById(1);
		
		assertEquals (servicio.get().getPrecio(),servicioActualizado.get().getPrecio());
	}
	
}
