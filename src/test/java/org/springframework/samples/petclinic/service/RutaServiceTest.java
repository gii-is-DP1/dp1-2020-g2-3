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
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class RutaServiceTest {

	@Autowired
	RutaService rutaService;
	
	@Autowired
	TrayectoService trayectoService;
	

	@Test
	@Transactional
	@DisplayName("Encontrar todas las rutas con DIFERENTES TRAYECTOS que tengan los mismos atributos básicos (origen,destino,kmTotal...)")
	void findRutaByAttributesTest() {
		
		// ¡2 rutas pueden tener los mismos atributos básicos pero con diferentes trayectos intermedios!
		//ARRANGE
		
		//RUTA 1
		Ruta ruta1= new Ruta();  //uno --> dos ---> uno (El cliente en este caso realiza Zahinos --> Badajoz)
		Double kmTotales=80.0;
		ruta1.setOrigenCliente("uno");
		ruta1.setDestinoCliente("dos");
		ruta1.setNumKmTotales(kmTotales);
		ruta1.setHorasEstimadasCliente(0.5);
		ruta1.setHorasEstimadasTaxista(1.0);
		List<Trayecto> trayectos1= new ArrayList<Trayecto>();
		Trayecto trayecto1= Trayecto.nuevoTrayecto("uno", "dos", 40.0, 0.5);
		Trayecto trayecto2= Trayecto.nuevoTrayecto("dos", "uno", 40.0, 0.5);  //Trayecto de vuelta del taxista, no va el cliente
		trayectoService.save(trayecto1);
		trayectoService.save(trayecto2);
		
		trayectos1.add(trayecto1);
		trayectos1.add(trayecto2);
		ruta1.setTrayectos(trayectos1);
		
		//RUTA 2
		Ruta ruta2= new Ruta(); //uno--> tres ---> dos ---> uno
		ruta2.setOrigenCliente("uno");
		ruta2.setDestinoCliente("dos");
		ruta2.setNumKmTotales(80.0);
		ruta2.setHorasEstimadasCliente(0.5);
		ruta2.setHorasEstimadasTaxista(1.0);
		List<Trayecto> trayectos2= new ArrayList<Trayecto>();
		Trayecto trayecto3= Trayecto.nuevoTrayecto("uno", "tres", 20.0, 0.25);
		Trayecto trayecto4= Trayecto.nuevoTrayecto("tres", "dos", 20.0, 0.25);
		trayectoService.save(trayecto3);
		trayectoService.save(trayecto4);
	
		
		trayectos2.add(trayecto3);
		trayectos2.add(trayecto4);
		trayectos2.add(trayecto2); //útlimo trayecto  dos ---> uno, trayecto de vuelta, no va el taxista
		ruta2.setTrayectos(trayectos2);
		
		rutaService.save(ruta1);
		rutaService.save(ruta2);
		
		//ACT 
		//intentamos conseguir las 2 rutas creadas anteriormente
		Collection<Ruta> rutasEncontradas=rutaService.findRutasByAttributes("uno", "dos", 80.0, 0.5,1.0);
		
		//intentamos conseguir rutas con atributos que no existan
		Collection<Ruta> rutasInexistentes= rutaService.findRutasByAttributes("origenInexistente", "destinoInexistente", 80.0,0.5,1.0);
		
		//ASSERT
		assertEquals(rutasEncontradas.size(),2); //Hemos encontrado 2 rutas con esos atributos aunque tengan diferentes trayectos
		assertEquals(rutasInexistentes.size(),0); //La ruta inventada no existe
		
		for(Ruta r: rutasEncontradas) {
			
			assertEquals(r.getOrigenCliente(),"uno");
			assertEquals(r.getDestinoCliente(),"dos");
			assertEquals(r.getNumKmTotales(),80.0);
			assertEquals(r.getHorasEstimadasCliente(),0.5);	
			assertEquals(r.getHorasEstimadasTaxista(),1.0);	
		}
	}
}
