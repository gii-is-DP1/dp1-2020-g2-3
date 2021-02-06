package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.TipoTrabajador;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.FechaFinAnteriorInicioException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TrabajadorServiceTest {

	@Autowired
	TrabajadorService trabService;
	@Autowired
	TipoTrabajadorService tipotrabService;
	
	
	@Test
	public void findByIdTest() throws FechaFinAnteriorInicioException {
		//ARRANGE
		User  nuevoUser = new User();
		nuevoUser.setUsername("nuevo1");
		nuevoUser.setPassword("nuevo1");
		TipoTrabajador   nuevoTipoTrabajador = new TipoTrabajador();
		nuevoTipoTrabajador.setName("NuevoTipo");
		tipotrabService.save(nuevoTipoTrabajador);
		Contrato nuevoContrato = new Contrato();
		nuevoContrato.setFechaInicio(Date.valueOf(LocalDate.of(2015, 2, 15)));
		nuevoContrato.setFechaFin(Date.valueOf(LocalDate.of(2015, 4, 15)));
		nuevoContrato.setSalarioMensual(1200.00);
		Trabajador nuevoTrabajador= new Trabajador();
		nuevoTrabajador.setNombre("Paco");
		nuevoTrabajador.setApellidos("Torres");
		nuevoTrabajador.setDni("23422432Q");
		nuevoTrabajador.setTelefono("995778443");
		nuevoTrabajador.setTipoTrabajador(nuevoTipoTrabajador);
		nuevoTrabajador.setUser(nuevoUser);
		nuevoTrabajador.setContrato(nuevoContrato);
		trabService.save(nuevoTrabajador);
		Integer id = nuevoTrabajador.getId();
		
		//ACT
		Trabajador trabajador= trabService.findById(id);
		
		//ASSERT
		assertEquals(trabajador.getId(),id);
	}

	@Test
	public void findByUsernameTest() throws FechaFinAnteriorInicioException {
		//ARRANGE
		User  nuevoUser = new User();
		nuevoUser.setUsername("nuevo1");
		nuevoUser.setPassword("nuevo1");
		TipoTrabajador   nuevoTipoTrabajador = new TipoTrabajador();
		nuevoTipoTrabajador.setName("NuevoTipo");
		tipotrabService.save(nuevoTipoTrabajador);
		Contrato nuevoContrato = new Contrato();
		nuevoContrato.setFechaInicio(Date.valueOf(LocalDate.of(2015, 2, 15)));
		nuevoContrato.setFechaFin(Date.valueOf(LocalDate.of(2015, 4, 15)));
		nuevoContrato.setSalarioMensual(1200.00);
		Trabajador nuevoTrabajador= new Trabajador();
		nuevoTrabajador.setNombre("Paco");
		nuevoTrabajador.setApellidos("Torres");
		nuevoTrabajador.setDni("23422432Q");
		nuevoTrabajador.setTelefono("995778443");
		nuevoTrabajador.setTipoTrabajador(nuevoTipoTrabajador);
		nuevoTrabajador.setUser(nuevoUser);
		nuevoTrabajador.setContrato(nuevoContrato);
		trabService.save(nuevoTrabajador);
				
		//ACT
		Trabajador trabajador= trabService.findByUsername("nuevo1");
		
		//ASSERT
		assertEquals(trabajador.getUser().getUsername(),"nuevo1");
	}
}
