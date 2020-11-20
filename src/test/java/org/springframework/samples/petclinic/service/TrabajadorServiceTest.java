package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TrabajadorServiceTest {

	@Autowired
	TrabajadorService trabService;
	
	@Test
	public void testCount() {
		
		long numeroTrabajadores= trabService.trabajdorCount();
		assertEquals(numeroTrabajadores,2);
		
	}
}
