package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.RutaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;

import aj.org.objectweb.asm.ClassTooLargeException;


@ExtendWith(MockitoExtension.class)
class RutaServiceMockedTests {

    @Mock
    private RutaRepository rutaRepo;
    
    private Ruta ruta1;
    private Ruta ruta2;
    private Ruta ruta3;
    protected RutaService rutaService;

    @BeforeEach
    void setup() {
    	rutaService = new RutaService(rutaRepo);
    	// RUTA 1 y RUTA 2 CON LOS ATRIBUTOS BÁSICOS IDÉNTICOS PERO CON TRAYECTOS ASOCIADOS DIFERENTES
		//RUTA 1: Trayectos:  uno --> dos ---> uno (El cliente en este caso realiza uno --> dos)
    	ruta1= new Ruta(); 
		Double kmTotales=80.0;
		ruta1.setOrigenCliente("uno");
		ruta1.setDestinoCliente("dos");
		ruta1.setNumKmTotales(kmTotales);
		ruta1.setHorasEstimadasCliente(0.5); //El cliente no realiza dos--> uno (Trayecto de vuelta del taxista)
		ruta1.setHorasEstimadasTaxista(1.0);
		List<Trayecto> trayectos1= new ArrayList<Trayecto>();
		Trayecto trayecto1= Trayecto.nuevoTrayecto("uno", "dos", 40.0, 0.5);
		Trayecto trayecto2= Trayecto.nuevoTrayecto("dos", "uno", 40.0, 0.5);  //Trayecto de vuelta del taxista, no va el cliente
		
		trayectos1.add(trayecto1);
		trayectos1.add(trayecto2);
		ruta1.setTrayectos(trayectos1);
		
		//RUTA 2: Trayectos uno--> tres ---> dos ---> uno , El cliente realizará uno-->tres---> dos, y dos--> uno será el trayecto de vuelta del taxista

		ruta2= new Ruta();
		ruta2.setOrigenCliente("uno");
		ruta2.setDestinoCliente("dos");
		ruta2.setNumKmTotales(80.0);
		ruta2.setHorasEstimadasCliente(0.5);
		ruta2.setHorasEstimadasTaxista(1.0);
		List<Trayecto> trayectos2= new ArrayList<Trayecto>();
		Trayecto trayecto3= Trayecto.nuevoTrayecto("uno", "tres", 20.0, 0.25);
		Trayecto trayecto4= Trayecto.nuevoTrayecto("tres", "dos", 20.0, 0.25);
		
		
		trayectos2.add(trayecto3);
		trayectos2.add(trayecto4);
		trayectos2.add(trayecto2);
		ruta2.setTrayectos(trayectos2);
		
		
		//ruta3 tendrá el mismo número de trayectos que ruta2 pero serán diferentes
		
				ruta3= new Ruta();
				ruta3.setOrigenCliente("uno");
				ruta3.setDestinoCliente("dos");
				ruta3.setNumKmTotales(80.0);
				ruta3.setHorasEstimadasCliente(0.5);
				ruta3.setHorasEstimadasTaxista(1.0);
				List<Trayecto> trayectos3= new ArrayList<Trayecto>();
				Trayecto trayecto5= Trayecto.nuevoTrayecto("dos", "tres", 40.0, 0.5); 

				
				trayectos3.add(trayecto3);
				trayectos3.add(trayecto4);
				trayectos3.add(trayecto5);
				ruta3.setTrayectos(trayectos3);
    }

    @Test
    @Transactional
    @DisplayName("Obtener una ruta de la BD (existente) idéntica a la dada como parámetro (sin id) , con los mismos trayectos asociados ")
    void findRutaByRutaTest1() {
    	
    	//ARRANGE
    	
		Collection<Ruta> rutasPosibles= new ArrayList<Ruta>();
		//ruta3 tendrá el mismo número de trayectos que ruta2 pero serán diferentes
    	//ruta1 y ruta2 son  2 rutas con los  ATRIBUTOS BÁSICOS IDÉNTICOS pero con TRAYECTOS ASOCIADOS DIFERENTES
		rutasPosibles.add(ruta3);
		rutasPosibles.add(ruta1);
		rutasPosibles.add(ruta2);
		
		
		when(rutaRepo.findRutasByAttributes(ruta1.getOrigenCliente(), ruta1.getDestinoCliente(), ruta1.getNumKmTotales(), 
				ruta1.getHorasEstimadasCliente(), ruta1.getHorasEstimadasTaxista())).thenReturn(rutasPosibles);
		
		//ACT
		Optional<Ruta> rutaEncontrada1= rutaService.findRutaByRuta(ruta1);
		Optional<Ruta> rutaEncontrada2= rutaService.findRutaByRuta(ruta2);
		//Este método obtiene las rutas de la 'BD' con los mismos atributos básicos que la ruta dada como parámetro
		// y luego compara los trayectos para quedarse con aquella
		//ruta que tenga los mismos trayectos que la ruta pasada como parámetro 
		
		//ARRANGE
		assertEquals(ruta1,rutaEncontrada1.get());
		assertEquals(ruta1.getTrayectos().size(), rutaEncontrada1.get().getTrayectos().size());
		
		assertEquals(ruta2,rutaEncontrada2.get());
		assertEquals(ruta2.getTrayectos().size(), rutaEncontrada2.get().getTrayectos().size());
    }
    
    @Test
    @Transactional
    @DisplayName("Obtener un Optional.ofNullable al intentar buscar en la BD una ruta idéntica (que no existirá) a la dada como parámetro (sin id) , con los mismos trayectos asociados ")
    void findRutaByRutaTest2() {
    	//Lo probaremos para que findRutasByAttributes devuelva una lista vacía de rutas posibles, y también en el caso de que devuelva null
    	
    	//ARRANGE 1: List empty en findRutasByAttributes
		Collection<Ruta> rutasPosiblesVacia= new ArrayList<Ruta>();
		when(rutaRepo.findRutasByAttributes(ruta1.getOrigenCliente(), ruta1.getDestinoCliente(), ruta1.getNumKmTotales(), 
				ruta1.getHorasEstimadasCliente(), ruta1.getHorasEstimadasTaxista())).thenReturn(rutasPosiblesVacia);
		
		//ACT 1
		Optional<Ruta> rutaEncontrada1= rutaService.findRutaByRuta(ruta1);
		
		//ARRANGE 2: findRutasByAttributes como null
		
		when(rutaRepo.findRutasByAttributes(ruta1.getOrigenCliente(), ruta1.getDestinoCliente(), ruta1.getNumKmTotales(), 
				ruta1.getHorasEstimadasCliente(), ruta1.getHorasEstimadasTaxista())).thenReturn(null);
		//ACT 2
		Optional<Ruta> rutaEncontrada2= rutaService.findRutaByRuta(ruta1);

		//ASSERT 1 y 2
		assertEquals(rutaEncontrada1, Optional.empty());
		assertEquals(rutaEncontrada2, Optional.empty());
    }
   
    @Test
    @Transactional
    @DisplayName("Dada una ruta, obtener el número de HORAS estimadas de un cliente sin decimales(0,5 horas= 0 HORAS y 30 minutos...)" )
    void calcularHorasRutaClienteTest() {
    	//ARRANGE
    	ruta1.setHorasEstimadasCliente(0.5); // 0.5 horas= 0 horas y 30 minutos
    	
    	//ACT
    	
    	int numeroDeHoras=rutaService.calcularHorasRutaCliente(ruta1);
    	
    	//ASSERT
    	assertEquals(numeroDeHoras,0);
    }
    
    @Test
    @Transactional
    @DisplayName("Dada una ruta, obtener el número de MINUTOS estimados de un cliente sin decimales(0,5 horas= 0 horas y 30 MINUTOS...)" )
    void calcularMinutosRutaClienteTest() {
    	//ARRANGE
    	ruta1.setHorasEstimadasCliente(0.5); // 0.5 horas= 0 horas y 30 minutos
    	//ACT
    	int numeroMinutos=rutaService.calcularMinutosRutaCliente(ruta1);
    	//ASSERT
    	assertEquals(numeroMinutos,30);
    }
    
    @Test
    @Transactional
    @DisplayName("Dada una ruta construida, obtener únicamente sus trayectos intermedios")
    void obtenerTrayectosIntermediosTest() {
    	//Trayectos intermedios serán todos aquellos excepto: Localidad taxista (Zahinos) --> Origen cliente,
    															  //Origen cliente ---> parada 1
    															//Destino cliente ---> Localidad del taxista (Zahinos)
    	
    	
    	//ARRANGE

		Ruta ruta4= new Ruta();
		ruta4.setOrigenCliente("uno");
		ruta4.setDestinoCliente("dos");
		ruta4.setNumKmTotales(200.0);
		ruta4.setHorasEstimadasCliente(1.5);
		ruta4.setHorasEstimadasTaxista(2.5);
		List<Trayecto> trayectos4= new ArrayList<Trayecto>();
		//Trayectos de RUTA 4 
		Trayecto trayecto1= Trayecto.nuevoTrayecto("tres", "uno", 40.0, 0.5); 
		Trayecto trayecto2= Trayecto.nuevoTrayecto("uno", "cuatro", 40.0, 0.5); 
		Trayecto trayecto3= Trayecto.nuevoTrayecto("cuatro", "cinco", 40.0, 0.5); 
		Trayecto trayecto4= Trayecto.nuevoTrayecto("cinco", "dos", 40.0, 0.5); 
		Trayecto trayecto5= Trayecto.nuevoTrayecto("dos", "uno", 40.0, 0.5); 
		//Dado que origen=uno  y destino=dos, entonces tenemos los siguientes trayectos intermedios:
		// trayecto3,trayecto4


		trayectos4.add(trayecto1);
		trayectos4.add(trayecto2);
		trayectos4.add(trayecto3);
		trayectos4.add(trayecto4);
		trayectos4.add(trayecto5);

		ruta4.setTrayectos(trayectos4);
    	
       	//ACT
		List<Trayecto> trayectosIntermediosRuta4=rutaService.obtenerTrayectosIntermedios(ruta4);
    	//ASSERT
    	assertEquals(trayectosIntermediosRuta4.size(),2);
    	assertEquals(trayectosIntermediosRuta4.get(0),trayecto3);
    	assertEquals(trayectosIntermediosRuta4.get(1),trayecto4);
    }

    @Test
    @Transactional
    @DisplayName("Dada una ruta construida, obtener únicamente sus trayectos intermedios (Cuando la ruta no tenga trayectos intermedios)")
    void obtenerTrayectosIntermediosTest2() {
		//RUTA 1: Trayectos:  uno --> dos ---> uno
    	//origen y destino de la ruta: uno  y dos
    	//dos--->uno lo realiza el taxista. 
    	//NO TIENE TRAYECTOS INTERMEDIOS

   
    	//ACT
		List<Trayecto> trayectosIntermediosRuta2=rutaService.obtenerTrayectosIntermedios(ruta1);
    	//ASSERT
    	assertEquals(trayectosIntermediosRuta2.size(),0);

    }

    @Test
    @Transactional
    @DisplayName("Instanciar una ruta con kmTotales,horasEstimadas del cliente y taxista a 0, copiando el origen/Destino de la ruta que se le pase como parámetro")
    void inicializarRutaTest() {
    	//ARRANGE
    	Ruta nuevaRuta= new Ruta();
    	nuevaRuta.setOrigenCliente("Jerez de los Caballeros");
    	nuevaRuta.setDestinoCliente("Badajoz");
    	//ACT
    	Ruta rutaInicializada= rutaService.inicializarRuta(nuevaRuta);
    	//ASSERT
    	assertEquals(rutaInicializada.getHorasEstimadasCliente(),(Double)0.0);
    	assertEquals(rutaInicializada.getHorasEstimadasTaxista(),(Double)0.0);
    	assertEquals(rutaInicializada.getNumKmTotales(),(Double)0.0);
    	assertEquals(rutaInicializada.getOrigenCliente(),"Jerez de los Caballeros");
    	assertEquals(rutaInicializada.getDestinoCliente(), "Badajoz");

    }


}
