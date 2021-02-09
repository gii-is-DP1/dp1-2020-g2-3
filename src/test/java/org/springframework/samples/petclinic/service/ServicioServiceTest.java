package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.Collection;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Servicio;
import org.springframework.samples.petclinic.model.Taller;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.ServicioRepository;
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
	
	@Test
	@Transactional
	@DisplayName("Calcular los gastos asociados a un rango de fecha.")
	void calcularGastosTest() {
		
		//ARRANGE
		Date fecha = new Date();
		fecha.setDate(1);
		fecha.setMonth(1);
		fecha.setYear(2004);
		fecha.setHours(18);
		fecha.setMinutes(0);
		Servicio servicio = new Servicio();
		servicio.setFecha(fecha);
		servicio.setPrecio(100.0);
		servicio.setDescripcion("This is test");
		
		Date fecha1 = new Date();
		fecha1.setDate(1);
		fecha1.setMonth(1);
		fecha1.setYear(2003);
		fecha1.setHours(18);
		fecha1.setMinutes(0);
		
		Date fecha2 = new Date();
		fecha2.setDate(1);
		fecha2.setMonth(1);
		fecha2.setYear(2005);
		fecha2.setHours(18);
		fecha2.setMinutes(0);
		servicioService.save(servicio);
		
		//ACT
		Double gastos = servicioService.calcularGastos(fecha1, fecha2);
		
		//ASSERT
		System.out.println(fecha);
		System.out.println(fecha1);
		System.out.println(fecha2);
		assertEquals(Double.valueOf(100.0), gastos);
	}
	
	@Test
	@Transactional
	@DisplayName("Método findAllServicios del repositorio.")
	void findAllServiciosTest() {
		Date fecha = new Date();
		fecha.setDate(1);
		fecha.setMonth(1);
		fecha.setYear(2018);
		fecha.setHours(18);
		fecha.setMinutes(0);
		Servicio servicio = new Servicio();
		servicio.setFecha(fecha);
		servicio.setPrecio(100.0);
		servicio.setDescripcion("This is test");
		
		servicioService.save(servicio);
		
		Collection<Servicio> s = servicioService.findAllServicios();
		assertEquals(3,s.size());
		assertEquals(true,s.contains(servicio));
	}
}
