package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Servicio;
import org.springframework.samples.petclinic.model.Taller;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ServicioServiceTest {

	@Autowired
	ServicioService servicioService;
	
	@Test
	public void findByIdTest() {
		
		//ARRANGE
		
		Date fecha1 = new Date(2019,9,9);
		
		Servicio servicio1 = new Servicio();
		servicio1.setDescripcion("picotazo del cristal");
		servicio1.setFecha(fecha1);
		servicio1.setPrecio(42.36);
		servicioService.save(servicio1);
				
		//ACT
		Integer id = servicio1.getId();
		Servicio servicio= servicioService.findServicioById(id);
		
		//ASSERT
		assertEquals(servicio,servicio1);
	}
	
	
	@Test
	@Transactional
	public void editTest() {
		//ARRANGE
		
		Servicio servicio= servicioService.findServicioById(1);
		
		
		Double nuevoPrecio= 100.00;
		servicio.setPrecio(nuevoPrecio);
		servicioService.save(servicio);
		
		//ACT
		Servicio servicioActualizado= servicioService.findServicioById(1);
		
		//ASSERT
		assertEquals (servicio.getPrecio(),servicioActualizado.getPrecio());
	}
	
}
