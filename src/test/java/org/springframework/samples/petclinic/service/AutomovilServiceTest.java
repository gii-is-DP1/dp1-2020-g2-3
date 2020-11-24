package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
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
	public void deleteTest() {
		
		Long numAutomoviles=autoService.automovilCount();
		Optional<Automovil> automovil= autoService.findAutomovilById(1);
		autoService.delete(automovil.get());
		Optional<Automovil> automovil2= autoService.findAutomovilById(1);
		Long numAutomoviles2=autoService.automovilCount();
		assertEquals (automovil2.isPresent(),false);
		assertEquals(numAutomoviles2,numAutomoviles-1 );
	}
}
