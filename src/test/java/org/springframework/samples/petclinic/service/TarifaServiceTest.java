package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TarifaServiceTest {
	
	@Autowired
	private TarifaService tariService;
	
	@Test
	public void findByOriginalTest() {
		//ARRANGE (configuracion de los datos del test)
		Tarifa nuevaTarifa= new Tarifa();
		nuevaTarifa.setPrecioPorKm(0.4);
		nuevaTarifa.setPrecioEsperaPorHora(2.0);
		nuevaTarifa.setPorcentajeIvaRepercutido(10);
		nuevaTarifa.setOriginal(true);
		nuevaTarifa.setActivado(true);
		tariService.save(nuevaTarifa); //lo añadimos a la BD
		
		Tarifa nuevaTarifa2= new Tarifa();
		nuevaTarifa2.setPrecioPorKm(0.666);
		nuevaTarifa2.setPrecioEsperaPorHora(6.0);
		nuevaTarifa2.setPorcentajeIvaRepercutido(20);
		nuevaTarifa2.setOriginal(false);
		nuevaTarifa2.setActivado(false);
		tariService.save(nuevaTarifa2); //lo añadimos a la BD la segunda tarifa con el original=false
		
	
		//ACT (ejecutamos el sujeto en el test)
		Iterable<Tarifa> tarifaOriginal = tariService.findByOriginal();
	
		//ASSERT (afirmamos que los resultados esperados son devueltos)
		for(Tarifa t:tarifaOriginal) {
			assertThat(t.getOriginal()==true);
		}
		
	}
	
	@Test
	public void numTarifasActivasTest() {
		//ARRANGE (configuracion de los datos del test)
		Tarifa nuevaTarifa= new Tarifa();
		nuevaTarifa.setPrecioPorKm(0.4);
		nuevaTarifa.setPrecioEsperaPorHora(2.0);
		nuevaTarifa.setPorcentajeIvaRepercutido(10);
		nuevaTarifa.setOriginal(true);
		nuevaTarifa.setActivado(false);
		tariService.save(nuevaTarifa); //lo añadimos a la BD
				
		Tarifa nuevaTarifa2= new Tarifa();
		nuevaTarifa2.setPrecioPorKm(0.666);
		nuevaTarifa2.setPrecioEsperaPorHora(6.0);
		nuevaTarifa2.setPorcentajeIvaRepercutido(20);
		nuevaTarifa2.setOriginal(false);
		nuevaTarifa2.setActivado(false);
		tariService.save(nuevaTarifa2); //lo añadimos a la BD
		
		Tarifa nuevaTarifa3= new Tarifa();
		nuevaTarifa3.setPrecioPorKm(0.3);
		nuevaTarifa3.setPrecioEsperaPorHora(1.0);
		nuevaTarifa3.setPorcentajeIvaRepercutido(15);
		nuevaTarifa3.setOriginal(true);
		nuevaTarifa3.setActivado(false);
		tariService.save(nuevaTarifa3); //lo añadimos a la BD
		
	
		//ACT (ejecutamos el sujeto en el test)
		Integer numeroTarifasActivas = tariService.numTarifasActivas();
	
		//ASSERT (afirmamos que los resultados esperados son devueltos)
		assertThat(numeroTarifasActivas==1);
		
	}
	
	@Test
	public void findCopyByTarifaTest() {
		//ARRANGE (configuracion de los datos del test)
		Tarifa nuevaTarifa= new Tarifa();
		nuevaTarifa.setPrecioPorKm(0.4);
		nuevaTarifa.setPrecioEsperaPorHora(2.0);
		nuevaTarifa.setPorcentajeIvaRepercutido(10);
		nuevaTarifa.setOriginal(true);
		nuevaTarifa.setActivado(true);
		tariService.save(nuevaTarifa); //lo añadimos a la BD y activado=true
				
		Tarifa nuevaTarifa2= new Tarifa();
		nuevaTarifa2.setPrecioPorKm(0.666);
		nuevaTarifa2.setPrecioEsperaPorHora(6.0);
		nuevaTarifa2.setPorcentajeIvaRepercutido(20);
		nuevaTarifa2.setOriginal(false);
		nuevaTarifa2.setActivado(false);
		tariService.save(nuevaTarifa2); //lo añadimos a la BD
		
		Tarifa nuevaTarifa3= new Tarifa();
		nuevaTarifa3.setPrecioPorKm(0.3);
		nuevaTarifa3.setPrecioEsperaPorHora(1.0);
		nuevaTarifa3.setPorcentajeIvaRepercutido(15);
		nuevaTarifa3.setOriginal(true);
		nuevaTarifa3.setActivado(false);
		tariService.save(nuevaTarifa3); //lo añadimos a la BD
		
	
		//ACT (ejecutamos el sujeto en el test)
		Integer numeroTarifasActivas = tariService.numTarifasActivas();
	
		//ASSERT (afirmamos que los resultados esperados son devueltos)
		assertThat(numeroTarifasActivas==2);
	}
	
	@Test
	public void numTarifasActivasTest2TarifasActivas() {
		//ARRANGE (configuracion de los datos del test)
		Tarifa nuevaTarifa= new Tarifa();
		nuevaTarifa.setPrecioPorKm(0.4);
		nuevaTarifa.setPrecioEsperaPorHora(2.0);
		nuevaTarifa.setPorcentajeIvaRepercutido(10);
		nuevaTarifa.setOriginal(true);
		nuevaTarifa.setActivado(true);
		tariService.save(nuevaTarifa); //lo añadimos a la BD y activado=true
				
		Tarifa nuevaTarifa2= new Tarifa();
		nuevaTarifa2.setPrecioPorKm(0.666);
		nuevaTarifa2.setPrecioEsperaPorHora(6.0);
		nuevaTarifa2.setPorcentajeIvaRepercutido(20);
		nuevaTarifa2.setOriginal(false);
		nuevaTarifa2.setActivado(false);
		tariService.save(nuevaTarifa2); //lo añadimos a la BD
		
		Tarifa nuevaTarifa3= new Tarifa();
		nuevaTarifa3.setPrecioPorKm(0.3);
		nuevaTarifa3.setPrecioEsperaPorHora(1.0);
		nuevaTarifa3.setPorcentajeIvaRepercutido(15);
		nuevaTarifa3.setOriginal(true);
		nuevaTarifa3.setActivado(false);
		tariService.save(nuevaTarifa3); //lo añadimos a la BD
		
	
		//ACT (ejecutamos el sujeto en el test)
		Integer numeroTarifasActivas = tariService.numTarifasActivas();
	
		//ASSERT (afirmamos que los resultados esperados son devueltos)
		assertThat(numeroTarifasActivas==2);
		
	}
	
	@Test
	public void findTarifaActivaTest() {
		//En este caso de test, no tendríamos que realizar el ARRANGE (configuracion de los datos del test)
		//Debido a que en nuestra base de datos ya tenemos una tarifa activa, así que solo
		//tenemos que hacer el ACT y el AASSERT
		
			
		//ACT (ejecutamos el sujeto en el test)
		Tarifa tarifaActiva = tariService.findTarifaActiva();
			
		//ASSERT (afirmamos que los resultados esperados son devueltos)
		assertThat(tarifaActiva.getActivado()==true);
		
	}

}