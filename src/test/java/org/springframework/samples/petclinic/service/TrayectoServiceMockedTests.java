package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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
class TrayectoServiceMockedTests {

    @Mock
    private TrayectoRepository trayectoRepo;
    private RutaRepository rutaRepo; //No lo utilizamos pero es necesario para inicializar el rutaService de tipo Spy
    
    @Spy
    private RutaService rutaService= new RutaService(rutaRepo); //Solo se utilizará para un método que no implica ningún repositorio
    															//y que es necesario realizar, por lo tanto queremos usar el comportamiento
    															// original y no replicarlo (Spy)
    @Spy private UtilService utilService;
    protected TrayectoService trayectoService;

    @BeforeEach
    void setup() {
    	trayectoService = new TrayectoService(trayectoRepo,rutaService,utilService);

    }

  
    @Test
    @Transactional
    @DisplayName("calcular y asignar los diferentes trayectos de una ruta sin paradas intermedias cuyo origen y destino ES DIFERENTE a Zahinos")
    void calcularTrayectosRutaSinParadasTest1() throws DuplicatedParadaException {
   
    		//ARRANGE
    		when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Badajoz")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Badajoz",72.3,1.15));
    		when(trayectoRepo.findByOrigenAndDestino("Badajoz", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Badajoz","Jerez de los Caballeros",73.7,1.03));
    		when(trayectoRepo.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
    		
        	Ruta rutaFormulario=nuevaRutaFormulario("Badajoz","Jerez de los Caballeros",new ArrayList<String>());
        	
        
        	//ACT
    		Ruta rutaConTrayectosCalculados=trayectoService.calcularYAsignarTrayectos(rutaFormulario);
    	/*	Trayectos que se tienen que crear:
    	 
    		Zahinos --> Badajoz ;;;;  Badajoz ---> Jerez de los Caballeros ;;;;  Jerez de los Caballeros --> >Zahinos */
    	
    		
    		//ASSERT
    
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().size(),3);
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getOrigen(),"Zahinos");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getDestino(),"Badajoz");
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getOrigen(),"Badajoz");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getDestino(),"Jerez de los Caballeros");
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(2).getOrigen(),"Jerez de los Caballeros");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(2).getDestino(),"Zahinos");

    
    	
    }
    
    @Test
    @Transactional
    @DisplayName("calcular y asignar los diferentes trayectos de una ruta sin paradas intermedias CUYO ORIGEN ES ZAHINOS")
    void calcularTrayectosRutaSinParadasTest2() throws DuplicatedParadaException{
    			
		
    	
    		//ARRANGE
    		when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28));
    		when(trayectoRepo.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
    		
        	Ruta rutaFormulario=nuevaRutaFormulario("Zahinos","Jerez de los Caballeros",new ArrayList<String>());

        	//ACT
    		Ruta rutaConTrayectosCalculados=trayectoService.calcularYAsignarTrayectos(rutaFormulario);
    	/*	Trayectos que se tienen que crear:
    	 
    		Zahinos --> Jerez de los Caballeros ;;;;  Jerez de los Caballeros ---> Zahinos  */
    	
    		
    		//ASSERT
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().size(),2);
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getOrigen(),"Zahinos");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getDestino(),"Jerez de los Caballeros");
    		
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getOrigen(),"Zahinos");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getDestino(),"Jerez de los Caballeros");
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getOrigen(),"Jerez de los Caballeros");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getDestino(),"Zahinos");
    		
    }
    
    @Test
    @Transactional
    @DisplayName("calcular y asignar los diferentes trayectos de una ruta CON TRAYECTOS INTERMEDIOS=NULL CUYO ORIGEN ES ZAHINOS")
    void calcularTrayectosRutaSinParadasTest4() throws DuplicatedParadaException{
    			
		
    	
    		//ARRANGE
    		when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28));
    		when(trayectoRepo.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
    		
        	Ruta rutaTrayectosNull=nuevaRutaFormulario("Zahinos","Jerez de los Caballeros", new ArrayList<String>());
        	rutaTrayectosNull.setTrayectos(null); // comprobamos que el método también funciona con trayectos intermedios= null

        	//ACT
    		Ruta rutaNullConTrayectosCalculados=  trayectoService.calcularYAsignarTrayectos(rutaTrayectosNull);   		
    	/*	Trayectos que se tienen que crear:
    	 
    		Zahinos --> Jerez de los Caballeros ;;;;  Jerez de los Caballeros ---> Zahinos  */
    	
    		
    		//ASSERT
    		
    
    		
    		assertEquals(rutaNullConTrayectosCalculados.getTrayectos().size(),2);

        	
    		assertEquals(rutaNullConTrayectosCalculados.getTrayectos().get(0).getOrigen(),"Zahinos");
    		assertEquals(rutaNullConTrayectosCalculados.getTrayectos().get(0).getDestino(),"Jerez de los Caballeros");
    		
    		assertEquals(rutaNullConTrayectosCalculados.getTrayectos().get(0).getOrigen(),"Zahinos");
    		assertEquals(rutaNullConTrayectosCalculados.getTrayectos().get(0).getDestino(),"Jerez de los Caballeros");
        	
    		assertEquals(rutaNullConTrayectosCalculados.getTrayectos().get(1).getOrigen(),"Jerez de los Caballeros");
    		assertEquals(rutaNullConTrayectosCalculados.getTrayectos().get(1).getDestino(),"Zahinos");
    		
    }
    
    @Test
    @Transactional
    @DisplayName("calcular y asignar los diferentes trayectos de una ruta sin paradas intermedias CUYO DESTINO ES ZAHINOS")
    void calcularTrayectosRutaSinParadasTest3() throws DuplicatedParadaException {
    			
	
    		//ARRANGE
    		when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28));
    		when(trayectoRepo.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
        	
    		Ruta rutaFormulario=nuevaRutaFormulario("Jerez de los Caballeros","Zahinos",new ArrayList<String>());
        	

        	//ACT
    		Ruta rutaConTrayectosCalculados=trayectoService.calcularYAsignarTrayectos(rutaFormulario);
    		
    	/*	Trayectos que se tienen que crear:
    	 
    		Zahinos --> Jerez de los Caballeros ;;;;  Jerez de los Caballeros ---> Zahinos  */
    	
    		
    		//ASSERT
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().size(),2);
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getOrigen(),"Zahinos");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getDestino(),"Jerez de los Caballeros");
        	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getOrigen(),"Jerez de los Caballeros");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getDestino(),"Zahinos");

    	
    }
    
    @Test
    @Transactional
    @DisplayName("calcular los kilómetros totales de una ruta, incluyendo el número de horas QUE ESTÁ EL CLIENTE EN EL AUTOMÓVIL (no en toda la ruta...)")
    void calcularKmYHoras() throws DuplicatedParadaException {
    		
    		//ARRANGE
    		when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Badajoz")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Badajoz",72.3,1.15));
    		when(trayectoRepo.findByOrigenAndDestino("Badajoz", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Badajoz","Jerez de los Caballeros",73.7,1.03));
    		when(trayectoRepo.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
    		
    	//HAY QUE APROXIMAR A 2 DECIMALES PORQUE JAVA AL SUMAR PUEDE AÑADIR MÁS DECIMALES, por ello añadimos la suma manualmente en el test
    		

    		Double numKmTotalAproximado= 165.1; //72.3+73.7+19.1        	
    		//¡Las horas estimadas del cliente no tendrán en cuenta los trayectos que realice únicamente el taxista!
    		Double horasEstimadasClienteAproximadas=1.03;
    		Double horasEstimadasTaxistaAproximadas= 2.45; //1.15+1.03+0.27
        	Ruta rutaFormulario=nuevaRutaFormulario("Badajoz","Jerez de los Caballeros",new ArrayList<String>());
        	
        	//ACT
    		Ruta rutaConTrayectosCalculados=trayectoService.calcularYAsignarTrayectos(rutaFormulario);
    		

    		//ASSERT
    		assertEquals(rutaConTrayectosCalculados.getNumKmTotales(),numKmTotalAproximado);
    
    		assertEquals(rutaConTrayectosCalculados.getHorasEstimadasCliente(),horasEstimadasClienteAproximadas);
    		assertEquals(rutaConTrayectosCalculados.getHorasEstimadasTaxista(),horasEstimadasTaxistaAproximadas);
      
    }
    
    @Test
    @Transactional
    @DisplayName("Origen y destino del viaje iguales")
    public void origenIgualDestinoTest() {
    	//ARRANGE
    	Ruta rutaFormulario=nuevaRutaFormulario("Badajoz","Badajoz",new ArrayList<String>());
    	// ACT & ASSERT
       	assertThrows(DuplicatedParadaException.class,()->trayectoService.calcularYAsignarTrayectos(rutaFormulario));

    }
    
    
    @Test
    @Transactional
    @DisplayName("Origen del cliente igual a la primera parada intermedia")
    public void OrigenIgualPrimeraParadaTest() {
    	//ARRANGE

    	List<String> paradasIntermedias= new ArrayList<String>();
    	paradasIntermedias.add("Zahinos");
    	
    	Ruta rutaFormulario=nuevaRutaFormulario("Zahinos","Jerez de los Caballeros",paradasIntermedias);
    	// ACT & ASSERT
       	assertThrows(DuplicatedParadaException.class,()->trayectoService.calcularYAsignarTrayectos(rutaFormulario));

    }
    
    @Test
    @Transactional
    @DisplayName("Ultima parada intermedia igual al destino del cliente")
    public void UltimaParadaIgualDestinoTest() {
    	//ARRANGE
    	
		
    	when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28));
    	
		List<String> paradasIntermedias= new ArrayList<String>();
    	paradasIntermedias.add("Jerez de los Caballeros");
    	
    	Ruta rutaFormulario=nuevaRutaFormulario("Zahinos","Jerez de los Caballeros",paradasIntermedias);
    	
    	// ACT & ASSERT
       	assertThrows(DuplicatedParadaException.class,()->trayectoService.calcularYAsignarTrayectos(rutaFormulario));

    }
    
    @Test
    @Transactional
    @DisplayName("Dos paradas intermedias consecutivas iguales")
    public void ParadasIntermediasCosecutivasIgualesTest() {
    	//ARRANGE
		when(trayectoRepo.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
		when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28));

    	List<String> paradasIntermedias= new ArrayList<String>();
    	paradasIntermedias.add("Zahinos");
    	paradasIntermedias.add("Zahinos");
    	Ruta rutaFormulario=nuevaRutaFormulario("Jerez de los Caballeros","Badajoz",paradasIntermedias);
    	// ACT & ASSERT
       	assertThrows(DuplicatedParadaException.class,()->trayectoService.calcularYAsignarTrayectos(rutaFormulario));

    }
    
    
    @Test
    @Transactional
    @DisplayName("Calcular y asignar diferentes trayectos de una ruta con paradas intermedias")
    void calcularTrayectosIntermediosTest() throws DuplicatedParadaException {
   
    		//ARRANGE
    		when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Badajoz")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Badajoz",72.3,1.15));
    		when(trayectoRepo.findByOrigenAndDestino("Badajoz", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Badajoz","Jerez de los Caballeros",73.7,1.03));
    		when(trayectoRepo.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
        	when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28));

    		
    		List<String> paradasIntermedias= new ArrayList<String>();
        	paradasIntermedias.add("Jerez de los Caballeros");
        	paradasIntermedias.add("Zahinos");
    		
        	Ruta rutaFormulario=nuevaRutaFormulario("Badajoz","Jerez de los Caballeros",paradasIntermedias);
        	
        	//ACT
    		Ruta rutaConTrayectosCalculados=trayectoService.calcularYAsignarTrayectos(rutaFormulario);
    	/*	Trayectos que se tienen que crear:
    		Zahinos --> Badajoz ;;;;  Badajoz ---> Jerez de los Caballeros ;;;;  Jerez de los Caballeros --> Zahinos ;;;;; Zahinos --> Jerez de los Caballeros ;;;;;; Jerez de los Caballeros ----> Zahinos */
    		
    	
    		//ASSERT
    	
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().size(),5);
        	
    		//Zahínos---> Badajoz
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getOrigen(),"Zahinos");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(0).getDestino(),"Badajoz");
        	
    		//Badajoz ---> Jerez de los Caballeros
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getOrigen(),"Badajoz");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(1).getDestino(),"Jerez de los Caballeros");
        	
    		//Jerez de los Caballeros ---> Zahinos
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(2).getOrigen(),"Jerez de los Caballeros");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(2).getDestino(),"Zahinos");
    		
    		//Zahinos ---> Jerez de los Caballeros
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(3).getOrigen(),"Zahinos");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(3).getDestino(),"Jerez de los Caballeros");
    		
    		//Jerez de los Caballeros ---> Zahinos
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(4).getOrigen(),"Jerez de los Caballeros");
    		assertEquals(rutaConTrayectosCalculados.getTrayectos().get(4).getDestino(),"Zahinos");
    
    	
    }
    
    @Test
    @Transactional
    @DisplayName("calcular los kilómetros totales de una ruta CON TRAYECTOS INTERMEDIOS, incluyendo el número de horas QUE ESTÁ EL CLIENTE EN EL AUTOMÓVIL (no en toda la ruta)")
    void calcularKmYHorasTrayectosIntermediosTest() throws DuplicatedParadaException {
    		
    		
    	//ARRANGE
		when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Badajoz")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Badajoz",72.3,1.15));
		when(trayectoRepo.findByOrigenAndDestino("Badajoz", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Badajoz","Jerez de los Caballeros",73.7,1.03));
		when(trayectoRepo.findByOrigenAndDestino("Jerez de los Caballeros", "Zahinos")).thenReturn(Trayecto.nuevoTrayecto("Jerez de los Caballeros","Zahinos",19.1,0.27));
    	when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Jerez de los Caballeros")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28));
    	
		List<String> paradasIntermedias= new ArrayList<String>();
    	paradasIntermedias.add("Jerez de los Caballeros");
    	paradasIntermedias.add("Zahinos");
 
    	Ruta rutaFormulario=nuevaRutaFormulario("Badajoz","Jerez de los Caballeros",paradasIntermedias);
    	/*	Trayectos que se tienen que crear que realizará el taxista:
		Zahinos --> Badajoz ;;;;  Badajoz ---> Jerez de los Caballeros ;;;;  Jerez de los Caballeros --> Zahinos ;;;;; Zahinos --> Jerez de los Caballeros ;;;;;; Jerez de los Caballeros ----> Zahinos */
		
    
		Double numKmTotalAproximado=203.3; //72.3+73.7+19.1+19.1+19.1;
		 
		//Trayectos en los que estará el cliente: Badajoz---> Jerez de los Caballeros ---> Zahinos ----> Jerez de los Caballero
		Double horasEstimadasClienteAproximadas=1.58; //1.03 + 0.27 + 0.28
		Double horasEstimadasTaxistaAproximadas=3.0; //1.15+1.03+0.27+0.28+0.27;
	
    	//ACT
		Ruta rutaConTrayectosCalculados=trayectoService.calcularYAsignarTrayectos(rutaFormulario);

    	//ASSERT
		
    	assertEquals(rutaConTrayectosCalculados.getNumKmTotales(),numKmTotalAproximado);
    	assertEquals(rutaConTrayectosCalculados.getHorasEstimadasCliente(),horasEstimadasClienteAproximadas);
    	assertEquals(rutaConTrayectosCalculados.getHorasEstimadasTaxista(),horasEstimadasTaxistaAproximadas);
      
    }
    
    @Test
    @Transactional
    @DisplayName("Añadir un solo trayecto específico a un objeto de tipo ruta (sin formato formulario) y recalcular sus kmTotales, horasEstimadasCliente y horasEstimadasTaxista")
    void recalcularRutaAddTrayectoTest1() throws DuplicatedParadaException {
    	//Este método es uno auxuliar que utiliza el testeado anteriormente "calcularYAsignarTrayectos()"
    	//ARRANGE
    	Ruta nuevaRuta= new Ruta();
    		List<Trayecto> trayectosTotalesRuta= new ArrayList<Trayecto>();
    		Trayecto primerTrayecto= Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28);
    		trayectosTotalesRuta.add(primerTrayecto);
    		nuevaRuta.setTrayectos(trayectosTotalesRuta);
    	
    		nuevaRuta.setNumKmTotales(19.1);
    		nuevaRuta.setHorasEstimadasCliente(0.28);
    		nuevaRuta.setHorasEstimadasTaxista(0.28);
    		
		when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Badajoz")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Badajoz",72.3,1.15));

    	//ACT
    	Ruta rutaRecalculada=trayectoService.recalcularRutaAddTrayecto(nuevaRuta,"Zahinos", "Badajoz",true);
    	
    	//ASSERT
    	assertEquals(rutaRecalculada.getNumKmTotales(), (Double)91.4); //19.1+72.3
    	assertEquals(rutaRecalculada.getHorasEstimadasCliente(), (Double)1.43); //0.28 + 1.15
    	assertEquals(rutaRecalculada.getHorasEstimadasTaxista(), (Double)1.43); //0.28 + 1.15
    	
    }
    
    @Test
    @Transactional
    @DisplayName("Añadir un solo trayecto específico a un objeto de tipo ruta (sin tener formato del formulario) y recalcular sus kmTotales, horasEstimadasTaxista PERO NO LAS HORAS ESTIMADAS DEL CLIENTE")
    void recalcularRutaAddTrayectoTest2() throws DuplicatedParadaException {
    	//ARRANGE
    	Ruta nuevaRuta= new Ruta();
    		List<Trayecto> trayectosTotalesRuta= new ArrayList<Trayecto>();
    		Trayecto primerTrayecto= Trayecto.nuevoTrayecto("Zahinos","Jerez de los Caballeros",19.1,0.28);
    		trayectosTotalesRuta.add(primerTrayecto);
    		nuevaRuta.setTrayectos(trayectosTotalesRuta);
    	
    		nuevaRuta.setNumKmTotales(19.1);
    		nuevaRuta.setHorasEstimadasCliente(0.28);
    		nuevaRuta.setHorasEstimadasTaxista(0.28);
    		
		when(trayectoRepo.findByOrigenAndDestino("Zahinos", "Badajoz")).thenReturn(Trayecto.nuevoTrayecto("Zahinos","Badajoz",72.3,1.15));

    	//ACT
		
		//simularemos como que en el trayecto Zahinos --> Badajoz no se encuentra montado el cliente en el autmóvil
    	Ruta rutaRecalculada=trayectoService.recalcularRutaAddTrayecto(nuevaRuta,"Zahinos", "Badajoz",false);
    	
    	//ASSERT
    	assertEquals(rutaRecalculada.getNumKmTotales(), (Double)91.4); //19.1+72.3
    	assertEquals(rutaRecalculada.getHorasEstimadasCliente(), (Double)0.28); //0.28, ya que NO SUMAMOS EN ESTE CASO LAS HORAS ESTIMADAS DEL CLIENTE
    	assertEquals(rutaRecalculada.getHorasEstimadasTaxista(), (Double)1.43); //0.28 + 1.15
    	
    }
    
   
    
    
    /*
    	El siguiente método crea una RUTA  con los datos dados como parámetro, 
   		EN EL FORMATO EN EL QUE VENDRÍA DESDE EL FORMULARIO:
  	
  	 	List<Trayecto> solo los trayectos intermedios que se especifiquen desde el formulario, teniendo el formato:
    
   		trayecto1= {origen=parada1, destino=null}
   		trayecto2= {origen=parada2, destino=null}
    
    Con este formato se pasa la ruta como parámetro al método calcularYAsignarTrayectos y por ello lo tenemos 
    que simular para testearlo
   
    */
    static Ruta nuevaRutaFormulario(String origenCliente, String destinoCliente,List<String> paradasIntermedias) {
    	Ruta nuevaRuta= new Ruta();
 
    	List<Trayecto> trayectosRuta= new ArrayList<Trayecto>();
    	
    	nuevaRuta.setOrigenCliente(origenCliente);
    	nuevaRuta.setDestinoCliente(destinoCliente);
    	if(paradasIntermedias.size()>0) {
    		for (String parada: paradasIntermedias) {
        		Trayecto trayecto= new Trayecto();
        		trayecto.setOrigen(parada);
        		trayectosRuta.add(trayecto);
        	}
    	}
    	
    	nuevaRuta.setTrayectos(trayectosRuta);
    	return nuevaRuta;
    }
   
    
    /*
    static Stream<Ruta> diferentesRutasSinParadas1() {
    	
    	Ruta ruta1=nuevaRutaFormulario("Badajoz","Jerez de los Caballeros",new ArrayList<String>());
    	Ruta ruta2=nuevaRutaFormulario("Jerez de los Caballeros","Oliva de la Frontera",new ArrayList<String>());
    	Ruta ruta3=nuevaRutaFormulario("Oliva de la Frontera","Badajoz",new ArrayList<String>());
    
        return Stream.of(ruta1,ruta2,ruta3);
    }
    */

}
