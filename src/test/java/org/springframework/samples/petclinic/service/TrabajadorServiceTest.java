package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TrabajadorServiceTest {

	@Autowired
	TrabajadorService trabService;
	
	@Test
	public void findByIdTest() {
		
		Trabajador trabajador= trabService.findById(1);
		assertEquals(trabajador.getId(),1);
	}

}
