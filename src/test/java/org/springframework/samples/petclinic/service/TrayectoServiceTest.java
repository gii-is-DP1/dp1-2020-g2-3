package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TrayectoServiceTest {
	 @Autowired
		protected TrayectoService trayectoService;
	 
	 	@Test
		public void findByOrigenAndDestinoTest() { //Vamos a probar una Custom Query
	 		//ARRANGE
	 		Trayecto nuevoTrayecto= new Trayecto();
	 		nuevoTrayecto.setOrigen("ciudad1");
	 		nuevoTrayecto.setDestino("ciudad2");
	 		nuevoTrayecto.setNumKmTotales(20.0);
	 		nuevoTrayecto.setHorasEstimadas(0.2);
	 		trayectoService.save(nuevoTrayecto); //método nativo de Spring
	 		
	 		//ACT
			Trayecto trayecto= trayectoService.findByOrigenAndDestino("ciudad1", "ciudad2"); 
			
			//ASSERT
			assertThat(trayecto.getOrigen().equals("ciudad1"));
			assertThat(trayecto.getOrigen().equals("ciudad2"));
			assertThat(trayecto.getNumKmTotales()==20.0);
			assertThat(trayecto.getHorasEstimadas()==0.2);
			
		}
	 	
	 	
	 	@Test
	 	public void findDistinctParadasTest() { //Vamos a probar una Custom Query
	 		//ARRANGE
	 		Iterable<Trayecto> trayectos= trayectoService.findAll(); //método nativo de Spring
	 		Set<String> diferentesParadas= new HashSet<String>();
	 		for(Trayecto t:trayectos) {
	 			if(!diferentesParadas.contains(t.getOrigen())) { //Añadimos las diferntes paradas que encontremos en el sistema
	 															// (Conjunto de los diferentes orígenes de los trayectos)
	 				diferentesParadas.add(t.getOrigen());
	 			}
	 		}
	 		//ACT
	 		Iterable<String> distinctParada= trayectoService.findDistinctParadas();
	 		
	 		//ASSERT
	 		Set<String> diferentesParadasQuery=new HashSet<String>();
	 		for(String parada:distinctParada) {
	 			assertThat(diferentesParadas.contains(parada)); //Comprobamos que las paradas traídas por la Custom Query
	 															//son las mismas que las calculadas en el Arrange
	 			diferentesParadasQuery.add(parada);
	 		}
	 		assertThat(diferentesParadas.size()==diferentesParadasQuery.size()); //Además de coincidir, trae el mismo número de paradas diferentes
	 	}
	
}
