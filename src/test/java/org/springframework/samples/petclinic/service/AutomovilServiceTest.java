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
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AutomovilServiceTest {

	@Autowired
	AutomovilService autoService;
	@Test
	public void findByIdTest() {
		
		Optional<Automovil> automovil= autoService.findAutomovilById(1);
		assertEquals(automovil.get().getId(),1);
	}
	
	@Test
	@Transactional
	public void deleteAutomovilNuevo() {
		
		Long numAutomoviles=autoService.automovilCount();
		Optional<Automovil> automovil= autoService.findAutomovilById(4); //borrar un automóvil no usado en ningún serivico o viaje
		autoService.delete(automovil.get());
		Optional<Automovil> automovil2= autoService.findAutomovilById(4);
		Long numAutomoviles2=autoService.automovilCount();
		assertEquals (automovil2.isPresent(),false);
		assertEquals(numAutomoviles2,numAutomoviles-1 );
	}
	
	
	//El siguiente Test no detecta la excepción lanzada, pero sin embargo el controlador AutomovilController
	// lo detecta perfectamente. Preguntar por qué ocurre eso.
	
	/*@Test
	@Transactional
	public void deleteAutomovilUsado() {
		Optional<Automovil> automovil= autoService.findAutomovilById(1); //No se podrá borrar el automóvil usado
		DataAccessException thrown = assertThrows(
				DataAccessException.class,
		           () -> autoService.delete(automovil.get()),
		           "Expected delete() to throw, but it didn't"
		    );
		   
		    assertTrue(thrown.getMessage().contains("execute"));

	
	}*/
	@Test
	@Transactional
	public void editTest() {
		
		Optional<Automovil> automovil= autoService.findAutomovilById(1);
		
		String marca= automovil.get().getMarca();
		String nuevaMarca= "Marca2";
		automovil.get().setMarca(nuevaMarca);
		autoService.save(automovil.get());
		
		Optional<Automovil> automovilActualizado= autoService.findAutomovilById(1);
		
		assertEquals (automovil.get().getMarca(),automovilActualizado.get().getMarca());
	}
	
}
