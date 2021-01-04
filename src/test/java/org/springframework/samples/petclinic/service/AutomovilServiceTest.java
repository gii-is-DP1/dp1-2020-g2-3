package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat; 
import static org.junit.Assert.assertThat;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
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
	
	@Test
	@Transactional
	public void createTest() {
		Optional<Automovil> automovil = autoService.findAutomovilById(100);
		assertEquals(automovil.isPresent(),false);
		

		Trabajador trabajador = new Trabajador();
//		automovil.;
//		visit.setDescription("test");
//		this.petService.saveVisit(visit);
//            try {
//                this.petService.savePet(pet7);
//            } catch (DuplicatedPetNameException ex) {
//                Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
//            }
		
		/*
		//Creacion de Set de automoviles
		Set<Automovil> automovilSet = new HashSet<Automovil>();
		
		//Creacion user para trabajador
		User usuario = new User();
		
		//Creación autoridad
		Set<Authorities> autoridadSet = new HashSet<Authorities>();
		
		Authorities autoridad = new Authorities();
		
		
		//Asignacion autoridad
		autoridad.setAuthority("taxista");
		autoridad.setId(100);
		autoridad.setUser(usuario);
		
		autoridadSet.add(autoridad);
		//Asignacion de atributos de usuario
		usuario.setAuthorities(autoridadSet);
		usuario.setEnabled(true);
		usuario.setPassword("Soyuntaxista123");
		usuario.setUsername("pepe45");
		
		
		
		
		//Creacion trabajador
		Trabajador trabajador1 = new Trabajador();
		
		//Asignacion de atributos de trabajador
		trabajador1.setApellidos("García Villalobo");
		trabajador1.setCorreoElectronico("pepegarcia@gmail.com");
		trabajador1.setDNI("54348592L");
		trabajador1.setId(100);
		trabajador1.setNombre("Pepe");
		trabajador1.setTelefono(632486795);
		trabajador1.setTipoTrabajador(2);
		trabajador1.setAutomoviles(automovilSet);
		
		//Creacion del automovil
		Automovil autoNuevo = new Automovil();
		//Asignacion de sus atributos
		autoNuevo.setId(100);
		autoNuevo.setKmRecorridos(4000.0);
		autoNuevo.setMarca("Toyota");
		autoNuevo.setModelo("Corolla");
		autoNuevo.setNumPlazas(4);
		autoNuevo.setTrabajador(trabajador1);
		
		*/
		
	}
	
}
