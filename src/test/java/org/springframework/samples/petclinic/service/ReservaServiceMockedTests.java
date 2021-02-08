package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;





import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.samples.petclinic.service.exceptions.AutomovilPlazasInsuficientesException;
import org.springframework.samples.petclinic.service.exceptions.CancelacionViajeAntelacionException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.ExisteViajeEnEsteHorarioException;
import org.springframework.samples.petclinic.service.exceptions.FechaLlegadaAnteriorSalidaException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
import org.springframework.samples.petclinic.service.exceptions.HoraSalidaSinAntelacionException;
import org.springframework.samples.petclinic.service.exceptions.ParadaYaAceptadaRechazadaException;
import org.springframework.samples.petclinic.service.exceptions.ReservaYaRechazada;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;
import static org.mockito.Mockito.*;

import aj.org.objectweb.asm.ClassTooLargeException;


@ExtendWith(MockitoExtension.class)
class ReservaServiceMockedTests {

 
    
    @Mock
    private ReservaRepository reservaRepository;
    
    @Mock
    private TrayectoService trayectoService;
   
    @Mock
    private EstadoReservaService estadoService;
    
    @Mock
    private ClienteService clienteService;
    
    @Mock
    private RutaService rutaService;
    
    @Mock
    private TarifaService tarifaService;
    
    @Mock
    private TrabajadorService trabajadorService;
    @Mock
    private AuthoritiesService authoService;
    
    @Spy
    private UtilService utilService= new UtilService(); //Es necesario utilizar sus métodos reales, por ello es Spy
  
    protected ReservaService reservaService;

    @BeforeEach
    void setup() {
    	
    	reservaService= new ReservaService(reservaRepository,trayectoService,estadoService,clienteService,rutaService,tarifaService,trabajadorService,authoService,utilService);
   
    }
    
    @Test
    @Transactional
    void calcularPrecioDistanciaTest() {
    	//ARRANGE
    	Double kmTotal=1057.4;
    	Double precioPorKm=0.41;
    	Double precioTotalDistancia= kmTotal*precioPorKm; //433.534
		Double precioTotalRedondeado=433.53; //Aproximado a 2 decimales
		//ACT & ASSERT
    	assertEquals(precioTotalRedondeado,reservaService.calcularPrecioDistancia(kmTotal, precioPorKm));
    }
   
   
    
    
    
    @Test
    @Transactional
    @DisplayName("Calcular la fecha de llegada de un viaje dada la fecha/hora de salida y la duración del viaje")
    void calcularFechaHoraLlegadaTest() {
    	//ARRANGE
    	Date horaSalida= new Date(); //Salida: 5:00
    	horaSalida.setHours(5);
    	horaSalida.setMinutes(0);
    	Double horasEstimadasCliente=1.2; //Duración del viaje: 1 hora y 12 minutos
    	//Hora de Llegada esperada 6:12
    	int horaLlegada= 6;
    	int minutosLlegada=12; //0.2*60=12 minutos
    	
    	//ACT
    	Date dateCalculada=reservaService.calcularFechaYHoraLlegada(horaSalida, horaSalida, horasEstimadasCliente);
		//ASSERT
    	
    	//comprobamos si el método ha hecho bien la conversión de 0,2 a 12 minutos para llamar a addFecha
    	assertEquals(horaSalida.getDate(),dateCalculada.getDate());
    	assertEquals(horaLlegada,dateCalculada.getHours());
    	assertEquals(minutosLlegada,dateCalculada.getMinutes());
    }
    
    @Test
    @Transactional
    @DisplayName("fecha salida anterior al día actual")
    void fechaSalidaAnteriorActualTest() {

    	//ARRANGE
    	Date today= new Date();
    	Date fechaSalida1= utilService.addFecha(today,Calendar.DATE,-1); //Será la fecha actual restándole 1 día
    	//método testeado en UtilServiceMockedTests
	    
    	//ACT and ASSERT
    	assertThrows(FechaSalidaAnteriorActualException.class,()->reservaService.fechaSalidaAnteriorActual(fechaSalida1, fechaSalida1));
    }
    
    @Test
    @Transactional
    @DisplayName("fecha de salida igual al día actual pero HORA DE SALIDA ANTERIOR a la hora actual")
    void horaSalidaAnteriorActualTest() {

    	//ARRANGE
    	Date today= utilService.addFecha(new Date(),Calendar.MINUTE,-4); //DÍA actual restándole 5 min, este método ya está testeado

    	Date horaSalida= utilService.addFecha(today,Calendar.MINUTE,-4); //HORA actual restándole 5 min, este método ya está testeado
	  
    	//ACT
    	assertThrows(FechaSalidaAnteriorActualException.class,()->reservaService.fechaSalidaAnteriorActual(today, horaSalida));
    }
    //Creamos una clase anidada para ciertos tests que necesiten utilizar ciertos atributos
    @Nested
    class calcularReservaTests {
    	Reserva reservaSinCalcular;
    	Ruta rutaCalculada;
    	Tarifa tarifaActiva;
    	Tarifa tarifaCopia;
    	int horaLLegadaEstimada;
		int minutoLLegadaEstimada;
		Double precioTotalAproximado;
		EstadoReserva estadoSolicitada;
		
    	@BeforeEach
    	public void setup() {
    		//ARRANGE GENERAL (Necesario explicarlo bien)
        	
			//Simularemos una reserva con una ruta que tenga la misma estructura con la que viene desde el formulario (sin calcular muchos de sus datos)
		    	//Origen: Zahinos
		    	//Trayecto Intermedio1= {origen=Jerez de los Caballeros, destino= null}
		    	//Destino: Badajoz
		    
			reservaSinCalcular= this.arrangeReservaSinCalcularFormulario();
		    	//Al calcular la reserva, su ruta se formará con los trayectos:
		    	//Trayecto 1: {origen= Zahinos ; destino= Jerez de los Caballeros}
		    	//Trayecto 2: {origen= Jerez de los Caballeros ;  destino= Badajoz}
		    	//Trayecto 3: {origen= Badajoz ; Zahinos} ,	  (TRAYECTO DE VUELTA DEL TAXISTA A SU LOCALIDAD ZAHINOS)

			rutaCalculada= this.arrangeRutaCalculada();
			tarifaActiva= this.arrangeTarifaActiva();
			
			tarifaCopia= arrangeTarifaActiva(); 
        	tarifaCopia.setActivado(false);
        	tarifaCopia.setOriginal(false);

			Double precioTotal=rutaCalculada.getNumKmTotales()*tarifaActiva.getPrecioPorKm(); //166.2*0.5=83.1
			precioTotalAproximado=83.1;
		    	//horasEstimadasCliente de la rutaCalculada= 1.28 horas
		    	//hora de salida= 4:00
		    	//Sumarle 1.28 horas a las 4:00 horas
			horaLLegadaEstimada=5;
			minutoLLegadaEstimada=16; //0.28*60= 16.8 minutos,
			estadoSolicitada= this.arrangeEstadoReserva();
    		
    	}
    	//MÉTODOS AUXILIARES PARA EL ARRANGE GENERAL 
    	public Ruta arrangeRutaFormularioSinCalcular() {

    		   	//Simularemos la ruta con la misma estructura con la que viene desde el formulario (sin calcular muchos de sus datos)
    		   	//Origen: Zahinos
    		   	//Trayecto Intermedio1= {origen=Jerez de los Caballeros, destino= null}
    		   	//Destino: Badajoz
    		   		List<String> paradasIntermedias= new ArrayList<String>();
    		   		paradasIntermedias.add("Jerez de los Caballeros");
    		   		Ruta rutaFormularioSinCalcular= TrayectoServiceMockedTests.nuevaRutaFormulario("Zahinos", "Badajoz", new ArrayList<String>()); 
    		   		return rutaFormularioSinCalcular;
    		   }
    	public Ruta arrangeRutaCalculada() {
    		    	
    		    	//Al calcular la reserva del método anterior, se completará la ruta  con los siguientes trayectos:
    		    	//Trayecto 1: {origen= Zahinos ; destino= Jerez de los Caballeros}
    		    	//Trayecto 2: {origen= Jerez de los Caballeros ;  destino= Badajoz}
    		    	//Trayecto 3: {origen= Badajoz ; Zahinos} ,	  (TRAYECTO DE VUELTA DEL TAXISTA A SU LOCALIDAD ZAHINOS)
    		    	Ruta rutaCalculada= new Ruta();
    		    	
    		    	rutaCalculada.setOrigenCliente("Zahinos");
    		    	rutaCalculada.setDestinoCliente("Badajoz");
    		    	
    		    	List<Trayecto> trayectosRutaCalculada= new ArrayList<Trayecto>();
    		     	Trayecto trayecto1= Trayecto.nuevoTrayecto("Zahinos", "Jerez de los Caballeros", 19.1, 0.28);
    		    	Trayecto trayecto2= Trayecto.nuevoTrayecto("Jerez de los Caballeros", "Badajoz", 74.8, 1.0);
    		    	Trayecto trayecto3= Trayecto.nuevoTrayecto("Badajoz", "Zahinos", 72.3, 1.08);
    		    	trayectosRutaCalculada.add(trayecto1);
    		    	trayectosRutaCalculada.add(trayecto2);
    		    	trayectosRutaCalculada.add(trayecto3);
    		    	
    		    	rutaCalculada.setTrayectos(trayectosRutaCalculada);
    		    	rutaCalculada.setNumKmTotales(166.2);
    		    	rutaCalculada.setHorasEstimadasCliente(1.28);
    		    	rutaCalculada.setHorasEstimadasTaxista(2.36);
    		    	return rutaCalculada;
    		    }
    		    
    	public Reserva arrangeReservaSinCalcularFormulario() {
    		//Crear nueva reserva asociando la ruta sin calcular
    		    	Ruta rutaFormularioSinCalcular=this.arrangeRutaFormularioSinCalcular();
    		    	Reserva reservaSinCalcular= Reserva.newReservaSinCalcular(new Date(), new Date(), 2, "Maleta grande");
    		    	reservaSinCalcular.setRuta(rutaFormularioSinCalcular);
    		    	Date fechaSalida= new Date(); //Por defecto la fecha actual
    		    	//fechaSalida= utilService.addFecha(fechaSalida, Calendar.DATE, 2); //Para que no salte ninguna excepción, fecha salida= hoy + 2 días
    		    	Date horaSalida= new Date();
    		    	horaSalida.setHours(4);
    		    	horaSalida.setMinutes(0);
    		    	horaSalida.setDate(fechaSalida.getDate()); //hora de salida por defecto la actual
    		    	reservaSinCalcular.setFechaSalida(fechaSalida);
    		    	reservaSinCalcular.setHoraSalida(horaSalida);
    		    	reservaSinCalcular.setPlazas_Ocupadas(3);
    		    	return reservaSinCalcular;
    		    }
    		    
    	public Tarifa arrangeTarifaActiva() {
    		    	Tarifa tarifaActiva= new Tarifa();
    		    	tarifaActiva.setPrecioPorKm(0.5);
    		    	tarifaActiva.setOriginal(true);
    		    	tarifaActiva.setActivado(true);
    		    	tarifaActiva.setPorcentajeIvaRepercutido(10);
    		    	return tarifaActiva;
    		    	
    		    }
    	
    	public EstadoReserva arrangeEstadoReserva() {
	    	//Retorna un estado del tipo "Solicitada"
    		EstadoReserva estadoSolicitada= new EstadoReserva();
    		estadoSolicitada.setName("Solicitada");
    		return estadoSolicitada;
	    	
	    }
    
    	@Test
        @Transactional
        @DisplayName("Calcular datos (km,ruta,horaLlegada,precio...) de una reserva para mostrarla ANTES DE CONFIRMARLA")
        void calcularNuevaReservaTest() throws DuplicatedParadaException,FechaSalidaAnteriorActualException,HoraSalidaSinAntelacionException {
    		 			//Para que no salte ninguna excepción, fecha salida de la reserva= hoy + 2 días
    		
        	Date fechaSalida=utilService.addFecha(reservaSinCalcular.getFechaSalida(), Calendar.DATE, 2);
        	reservaSinCalcular.setFechaSalida(fechaSalida);
        	
        	when(tarifaService.findTarifaActiva()).thenReturn(tarifaActiva);
        	when(trayectoService.calcularYAsignarTrayectos(reservaSinCalcular.getRuta())).thenReturn(rutaCalculada);
        	
    				   //simularemos que la tarifa que va a tomar como referencia la reserva (original=true), presenta una de tipo copia 
    				   //(original=false) que se asociará a ella (Explicado en el documento de diseño), en lugar de crear una nueva entidad
        
        	when(tarifaService.findCopyOfTarifa(tarifaActiva)).thenReturn(Optional.ofNullable(tarifaCopia));
        	    	
        	//ACT
        	Reserva reservaCalculada= reservaService.calcularReservaAPartirDeRuta(reservaSinCalcular, false, true);
       
        	//ASSERT (Para que el método se considere válido todo esto tiene que funcionar...)
        	
        	//Comprobamos que los DATOS NUEVOS  de la reserva
        	
        	assertEquals(reservaCalculada.getPrecioTotal(),precioTotalAproximado);
        	assertEquals(reservaCalculada.getFechaLlegada().getDate(),reservaSinCalcular.getFechaSalida().getDate()); //Se llega el mismo día de salida
        	assertEquals(reservaCalculada.getFechaLlegada().getHours(),horaLLegadaEstimada);
        	assertEquals(reservaCalculada.getFechaLlegada().getMinutes(),minutoLLegadaEstimada);
        	assertEquals(reservaCalculada.getNumKmTotales(),rutaCalculada.getNumKmTotales());
        	assertEquals(reservaCalculada.getRuta().getTrayectos().size(),rutaCalculada.getTrayectos().size());
        	assertEquals(reservaCalculada.getTarifa().getPrecioPorKm(),tarifaCopia.getPrecioPorKm());
        	assertEquals(reservaCalculada.getTarifa().getPrecioPorKm(),tarifaActiva.getPrecioPorKm());

        	//Dado que existía una tarifa de tipo copia, no se ha debido crear una nueva entidad. Comprobamos que no se ha llamado al siguiente metodo
        	verify(tarifaService,never()).nuevaTarifa(false, false, tarifaActiva.getPorcentajeIvaRepercutido(), tarifaActiva.getPrecioEsperaPorHora(), tarifaActiva.getPrecioPorKm());
        	verify(tarifaService,never()).save(tarifaActiva);
        	
        	
        	
        	//Comprobamos que los datos que vendrían de un "formulario" se han guardado en la reserva
        	assertEquals(reservaCalculada.getDescripcionEquipaje(),"Maleta grande");
        	assertEquals(reservaCalculada.getFechaSalida(),reservaSinCalcular.getFechaSalida());
        	assertEquals(reservaCalculada.getHoraSalida(),reservaSinCalcular.getHoraSalida());
        	assertEquals(reservaCalculada.getPlazas_Ocupadas(),reservaSinCalcular.getPlazas_Ocupadas());
        	
        	//Comprobamos que los campos asociados a cuando se CONFIRMA una reserva no han sido creados TODAVÍA:
        
        	assertNull(reservaCalculada.getHorasEspera());
        	assertNull(reservaCalculada.getEstadoReserva());
        }
    	
    	
    	 @Test
    	 @Transactional
    	 @DisplayName("Al calcular una nueva reserva ANTES DE CONFIRMARLA, crear una NUEVA entidad tarifa al no existir una igual que la tarifa activa"
    	    		+ " con original=false en la BD")
    	 void calcularNuevaReservaTest2() throws DuplicatedParadaException,FechaSalidaAnteriorActualException,HoraSalidaSinAntelacionException {
    		 //ARRANGE
    		Date fechaSalida=utilService.addFecha(reservaSinCalcular.getFechaSalida(), Calendar.DATE, 2);
         	reservaSinCalcular.setFechaSalida(fechaSalida);
         	
    	    when(tarifaService.findTarifaActiva()).thenReturn(tarifaActiva);
    	    when(trayectoService.calcularYAsignarTrayectos(reservaSinCalcular.getRuta())).thenReturn(rutaCalculada);
    	    
    	    //simularemos que la tarifa que va a tomar como referencia la reserva (original=true) NO TIENE UNA IDÉNTICA CON 
			// original=false, por lo que se creará una nueva entidad y se guardará

    	    when(tarifaService.findCopyOfTarifa(tarifaActiva)).thenReturn(Optional.ofNullable(null));
    	    when(tarifaService.nuevaTarifa(false, false, tarifaActiva.getPorcentajeIvaRepercutido(), tarifaActiva.getPrecioEsperaPorHora(), tarifaActiva.getPrecioPorKm())).thenReturn(tarifaCopia);
    	    	
    	    //ACT
    	    Reserva reservaCalculada= reservaService.calcularReservaAPartirDeRuta(reservaSinCalcular, false, true);
    	   
    	    //ASSERT
    	    	
    	    assertEquals(reservaCalculada.getTarifa().getPrecioPorKm(),tarifaCopia.getPrecioPorKm());
    	    assertEquals(reservaCalculada.getTarifa().getPrecioPorKm(),tarifaActiva.getPrecioPorKm());
    	    	
    	    	//SE HA CREADO Y GUARDADO LA TARIFA COMO UNA COPIA (original=false)
    	    verify(tarifaService,times(1)).nuevaTarifa(false, false, tarifaActiva.getPorcentajeIvaRepercutido(), tarifaActiva.getPrecioEsperaPorHora(), tarifaActiva.getPrecioPorKm());
    	    verify(tarifaService,times(1)).save(tarifaCopia);
    	    }
    	  @Test
    	  @Transactional
    	  @DisplayName("Calcular y CONFIRMAR una nueva reserva, asignándole una ruta de la BD en caso de que exista una igual a la solicitada")
    	  void calcularNuevaReservaTest3() throws DuplicatedParadaException,FechaSalidaAnteriorActualException,HoraSalidaSinAntelacionException {

    	    //ARRANGE  
    		  Date fechaSalida=utilService.addFecha(reservaSinCalcular.getFechaSalida(), Calendar.DATE, 2);
    		  reservaSinCalcular.setFechaSalida(fechaSalida);
           	
    	    when(tarifaService.findTarifaActiva()).thenReturn(tarifaActiva);
    	    when(trayectoService.calcularYAsignarTrayectos(reservaSinCalcular.getRuta())).thenReturn(rutaCalculada);
    	    when(tarifaService.findCopyOfTarifa(tarifaActiva)).thenReturn(Optional.ofNullable(tarifaCopia));
    	    when(rutaService.findRutaByRuta(rutaCalculada)).thenReturn(Optional.ofNullable(rutaCalculada));
    	    when(estadoService.findEstadoById(1)).thenReturn(Optional.ofNullable(estadoSolicitada));
    	   
    	    //ACT
    	    Reserva reservaCalculada= reservaService.calcularReservaAPartirDeRuta(reservaSinCalcular, true, true);
    	    
    	    //ASSERT
    	    
    	    //COMPROBACIONES ESPECÍFICAS DE QUE SE HA CONFIRMADO LA RESERVA
    	    assertEquals(reservaCalculada.getHorasEspera(),(Double)0.0);
    	    assertEquals(reservaCalculada.getEstadoReserva().getName(),"Solicitada");
    	    verify(rutaService,times(1)).findRutaByRuta(reservaSinCalcular.getRuta());
    	    
    	    //COMPROBACIONES REALIZADAS EN OTROS TESTS PERO NECESARIAS
    	    
    	    //Comprobaciones de que se ha calculado correctamente
    	    assertEquals(reservaCalculada.getPrecioTotal(),precioTotalAproximado);
        	assertEquals(reservaCalculada.getFechaLlegada().getDate(),reservaSinCalcular.getFechaSalida().getDate()); //Se llega el mismo día de salida
        	assertEquals(reservaCalculada.getFechaLlegada().getHours(),horaLLegadaEstimada);
        	assertEquals(reservaCalculada.getFechaLlegada().getMinutes(),minutoLLegadaEstimada);
        	assertEquals(reservaCalculada.getNumKmTotales(),rutaCalculada.getNumKmTotales());
        	assertEquals(reservaCalculada.getRuta().getTrayectos().size(),rutaCalculada.getTrayectos().size());
        	assertEquals(reservaCalculada.getTarifa().getPrecioPorKm(),tarifaCopia.getPrecioPorKm());
        	assertEquals(reservaCalculada.getTarifa().getPrecioPorKm(),tarifaActiva.getPrecioPorKm());

        	//Dado que existía una tarifa de tipo copia, no se ha debido crear una nueva entidad. Comprobamos que no se ha llamado al siguiente metodo
        	verify(tarifaService,never()).nuevaTarifa(false, false, tarifaActiva.getPorcentajeIvaRepercutido(), tarifaActiva.getPrecioEsperaPorHora(), tarifaActiva.getPrecioPorKm());
        	verify(tarifaService,never()).save(tarifaActiva);
        	
        	//Comprobamos que los datos que vendrían de un "formulario" se han guardado en la reserva
        	assertEquals(reservaCalculada.getDescripcionEquipaje(),"Maleta grande");
        	assertEquals(reservaCalculada.getFechaSalida(),reservaSinCalcular.getFechaSalida());
        	assertEquals(reservaCalculada.getHoraSalida(),reservaSinCalcular.getHoraSalida());
        	assertEquals(reservaCalculada.getPlazas_Ocupadas(),reservaSinCalcular.getPlazas_Ocupadas());
    	    }
    	  
    	   @Test
    		@Transactional
    		@DisplayName("Exepción al solicitar calcular una NUEVA reserva con fecha/hora de salida anterior a la actual")
    		void calcularNuevaReservaTest4()  {
    			  //ARRANGE
    			  Date fechaSalida=utilService.addFecha(new Date(), Calendar.DATE, -1); //Día Anterior a hoy
    			  reservaSinCalcular.setFechaSalida(fechaSalida);
    			  //ACT & ASSERT
    			  assertThrows(FechaSalidaAnteriorActualException.class,()->reservaService.calcularReservaAPartirDeRuta(reservaSinCalcular, true, true));    			
    		  }
    	   
    	  @Test
     	  @Transactional
     	  @DisplayName("Crear una nueva ruta en la BD en caso de que la ruta solicitada de la nueva reserva no exista previamente")
     	  void asignarNuevaRutaOCrearlaTest() {
    		  //ARRANGE
    		  
    		  reservaSinCalcular.setRuta(rutaCalculada);
      	    when(rutaService.findRutaByRuta(rutaCalculada)).thenReturn(Optional.ofNullable(null));
      	    
    		  //ACT
    		  Reserva reservaRutaAsignada= reservaService.asignarRutaExistenteOCrearla(reservaSinCalcular);
    		  
    		  //ASSERT
      	    verify(rutaService,times(1)).findRutaByRuta(reservaSinCalcular.getRuta());
      	    verify(rutaService,times(1)).save(rutaCalculada);
     	    }
    	  
    	  @Test
     	  @Transactional
     	  @DisplayName("No guardar en la BD una nueva ruta de una reserva en caso de que ya exista una en la BD")
     	  void asignarNuevaRutaOCrearlaTest2() {
    		  //ARRANGE
    		  
    		  reservaSinCalcular.setRuta(rutaCalculada);
      	    when(rutaService.findRutaByRuta(rutaCalculada)).thenReturn(Optional.ofNullable(rutaCalculada));
      	    
    		  //ACT
    		  Reserva reservaRutaAsignada= reservaService.asignarRutaExistenteOCrearla(reservaSinCalcular);
    		  
    		  //ASSERT
      	    verify(rutaService,times(1)).findRutaByRuta(reservaSinCalcular.getRuta());
      	    verify(rutaService,never()).save(rutaCalculada);
     	    }
    	  
    	  @Test
     	  @Transactional
     	  @DisplayName("Editar una reserva existente y guardarla en la BD")
     	  void guardarReservaEditadaTest() throws DuplicatedParadaException,FechaLlegadaAnteriorSalidaException {
    		  //ARRANGE
    		Reserva reservaBDPrevia= reservaSinCalcular;
    		Date fechaLlegadaFutura= utilService.addFecha(new Date(),Calendar.DATE, 2); //Fecha llegada: 2 días más que hoy
    		reservaBDPrevia.setFechaLlegada(fechaLlegadaFutura); //llegada dentro de 2 días
    		reservaBDPrevia.setHoraLlegada(new Date()); //hora actual llegada
    		reservaBDPrevia.setRuta(rutaCalculada);
    		reservaBDPrevia.setEstadoReserva(estadoSolicitada);
    		reservaBDPrevia.setTarifa(tarifaCopia);
    		reservaBDPrevia.setDescripcionEquipaje("Descripción antigua");
    		
    		reservaSinCalcular.setDescripcionEquipaje("Descripción nueva");
    		
      	    when(trayectoService.calcularYAsignarTrayectos(reservaSinCalcular.getRuta())).thenReturn(rutaCalculada);
      	    when(rutaService.findRutaByRuta(rutaCalculada)).thenReturn(Optional.ofNullable(rutaCalculada));
    		
      	    //ACT
      	    Reserva reservaBD= reservaService.guardarReservaEditada(reservaSinCalcular, reservaBDPrevia);

      	    //ASSERT
      	    assertEquals(reservaBD.getDescripcionEquipaje(),"Descripción nueva");
      	    
     	    }
    	  
    	  public User arrangeUser(String username, String Authority) {
    		  User user= new User();
  	  		user.setUsername(username);
  	  		user.setEnabled(true);
  	  		user.setPassword("123");
  	  		Authorities a1= new Authorities();
  	  		a1.setAuthority(Authority);
  	  		a1.setUser(user);
  	  		Set<Authorities> set= new HashSet<Authorities>();
  	  		set.add(a1);
  	  		user.setAuthorities(set);
  	  		return user;
    	  }
    	  public Cliente arrangeCliente() {
    		  Cliente cliente= new Cliente();
  	  		cliente.setDni("80097910L");
  	  		cliente.setApellidos("Castillo Salcedo");
  	  		cliente.setNombre("Lucas");
  	  		cliente.setEmail("vicwork44@gmail.com");
  	  		cliente.setId(40);
  	  		cliente.setTelefono("638542987");
  	  		cliente.setUser(this.arrangeUser("cliente", "cliente"));
  	  		return cliente;
    	  }
    	  	@Test
    		@Transactional
    		@DisplayName("CLIENTE y taxista confirman una NUEVA RESERVA y se guarda en la BD. Si lo hace el CLIENTE la reserva se asocia a él")
    		void calcularYConfirmarNuevaReservaClienteTest() throws FechaSalidaAnteriorActualException,DataAccessException,DuplicatedParadaException,HoraSalidaSinAntelacionException {
    	  	
    	  	//ARRANGE
    	  		
    	  		
    	  	Cliente cliente= this.arrangeCliente();
    	  	Set<String> authorities= new HashSet<String>();
    	  	authorities.add("cliente");
    	  	
    	  	Date fechaSalida=utilService.addFecha(reservaSinCalcular.getFechaSalida(), Calendar.DATE, 2);
      		 reservaSinCalcular.setFechaSalida(fechaSalida);
      		when(clienteService.findClienteByUsername("cliente")).thenReturn(cliente);
            when(authoService.findAuthoritiesByUsername("cliente")).thenReturn(authorities);
      	    when(tarifaService.findTarifaActiva()).thenReturn(tarifaActiva);
      	    when(trayectoService.calcularYAsignarTrayectos(reservaSinCalcular.getRuta())).thenReturn(rutaCalculada);
      	    when(tarifaService.findCopyOfTarifa(tarifaActiva)).thenReturn(Optional.ofNullable(tarifaCopia));
      	    when(rutaService.findRutaByRuta(rutaCalculada)).thenReturn(Optional.ofNullable(rutaCalculada));
      	    when(estadoService.findEstadoById(1)).thenReturn(Optional.ofNullable(estadoSolicitada));
      	    assertEquals(reservaSinCalcular.getCliente(), null); //La reserva al principio no tiene el cliente asociado
      	    													//Pero tras el ACT lo tendrá
      	    Reserva reservaSinCalcularTaxista= reservaSinCalcular;
            //Cuando el taxista confirma la reserva, el cliente viene seleccionado desde un formulario

      	  reservaSinCalcularTaxista.setCliente(cliente);
      	    //ACT
      	    Reserva reservaGuardadaCliente=reservaService.calcularYConfirmarNuevaReserva(reservaSinCalcular, "cliente");      	    
      	   //ASSERT
      	   
      	    assertEquals(reservaGuardadaCliente.getCliente().getUser().getUsername(), cliente.getUser().getUsername());
      	    assertEquals(reservaGuardadaCliente.getCliente().getDni(), cliente.getDni());
      	    verify(clienteService,times(1)).findClienteByUsername("cliente"); //solo lo ha llamado la primera reserva
      	    
      	    //ninguna de las 2 reservas tiene asociado ningún taxista o automóvil, eso ocurre al aceptar las reservas
      	    
    	}
    	  	public Automovil arrangeAutomovil() {
    	  		Automovil auto= new Automovil();
    	  		
    	  		auto.setKmRecorridos(123.0);
    	  		auto.setMarca("Toyota");
    	  		auto.setModelo("Verso");
    	  		auto.setNumPlazas(5);
    	  		return auto;
    	  	}
    	  	public Trabajador arrangeTrabajador() {
    	  		
    	  		Trabajador trab= new Trabajador();
    	  		trab.setApellidos("Castillo Ruiz");
    	  		trab.setDni("80065692P");
    	  		trab.setEmail("arcg@gmail.com");
    	  		trab.setNombre("Alfredo");
    	  		trab.setTelefono("635698145");
    	  		trab.setId(1);
    	  		User user= this.arrangeUser("trabajador", "taxista");
    	  		trab.setUser(user);
    	  		return trab;
    	  		
    	  	}
    	  	
    	    @Test
    	   	@Transactional
    	   	@DisplayName("Exepción tras intentar aceptar una reserva que ya ha sido aceptada/rechazada anteriormente")
    	   	void aceptarReservaSolicitadaTest()  {
    	   		  //ARRANGE
    	    	reservaSinCalcular.setRuta(rutaCalculada);
    	    	estadoSolicitada.setName("Aceptada");
    	    	reservaSinCalcular.setEstadoReserva(estadoSolicitada);
    	    	Automovil auto= this.arrangeAutomovil();
    	    	Trabajador trabajador= this.arrangeTrabajador();	
    	   		  //ACT & ASSERT
    	   		  assertThrows(ParadaYaAceptadaRechazadaException.class,()->reservaService.aceptarReserva(reservaSinCalcular, auto, trabajador.getUser().getUsername()));
    	   	  }
    	    
    	    @Test
    	   	@Transactional
    	   	@DisplayName("Aceptar una reserva asociando el trabajador que la acepta y el automóvil con el que realizará la reserva")
    	   	void aceptarReservaSolicitadaTest2() throws DataAccessException,ParadaYaAceptadaRechazadaException,AutomovilPlazasInsuficientesException,ExisteViajeEnEsteHorarioException   {
    	   		  //ARRANGE
    	    	reservaSinCalcular.setRuta(rutaCalculada);
    	    	reservaSinCalcular.setEstadoReserva(estadoSolicitada);
    	    	
    	    	Date fechaLlegada= utilService.addFecha(reservaSinCalcular.getFechaSalida(), Calendar.HOUR, 2);
    	    	Date horaLlegada= utilService.addFecha(reservaSinCalcular.getFechaSalida(), Calendar.HOUR, 2);
    	    	reservaSinCalcular.setFechaLlegada(fechaLlegada); //Necesitamos completar este campo de la reserva
    	    	reservaSinCalcular.setHoraLlegada(horaLlegada);
    	    	Automovil auto= this.arrangeAutomovil();
    	    	Trabajador trabajador= this.arrangeTrabajador();
    	    	EstadoReserva estadoAceptada= new EstadoReserva();
    	    	estadoAceptada.setName("Aceptada");
    	    	estadoAceptada.setId(2);
    	    	when(trabajadorService.findByUsername(trabajador.getUser().getUsername())).thenReturn(trabajador);
    	    	when(estadoService.findEstadoById(2)).thenReturn(Optional.ofNullable(estadoAceptada));
    	    	when(reservaRepository.findReservasAceptadasByTrabajadorId(trabajador.getId())).thenReturn(new ArrayList<Reserva>());
    	    	
    	   		  //ACT
    	    	Reserva reservaAceptada= reservaService.aceptarReserva(reservaSinCalcular, auto, trabajador.getUser().getUsername());
    	    	
    	    	//ASSERT
    	    	assertEquals(reservaAceptada.getAutomovil(), auto);
    	    	assertEquals(reservaAceptada.getTrabajador(),trabajador);
    	    	assertEquals(reservaAceptada.getEstadoReserva().getName(),"Aceptada");
    	   	  }
    	    @Test
    	   	@Transactional
    	   	@DisplayName("Exepción tras intentar aceptar una reserva con un automóvil que tenga un número de plazas menor al solicitado por la reserva")
    	   	void aceptarReservaSolicitadaTest3()  {
    	   		  //ARRANGE
    	    	reservaSinCalcular.setRuta(rutaCalculada);
    	    	reservaSinCalcular.setEstadoReserva(estadoSolicitada);
    	    	Automovil auto= this.arrangeAutomovil();
    	    	auto.setNumPlazas(2); //La reserva tiene un número de plazas ocupadas de 3
    	    	Trabajador trabajador= this.arrangeTrabajador();	
    	   		  //ACT & ASSERT
    	   		  assertThrows(AutomovilPlazasInsuficientesException.class,()->reservaService.aceptarReserva(reservaSinCalcular, auto, trabajador.getUser().getUsername()));
    	   	  }
    	    
    	    @Test
    	   	@Transactional
    	   	@DisplayName("Exepción cuando un trabajador intenta aceptar una reserva que se solapa en horario con otra haya aceptado anteriormente")
    	   	void aceptarReservaSolicitadaTest4()  {
    	   		  //ARRANGE
    	    	reservaSinCalcular.setRuta(rutaCalculada);
    	    	reservaSinCalcular.setEstadoReserva(estadoSolicitada);
    	    	Date fechaLlegada= utilService.addFecha(reservaSinCalcular.getFechaSalida(), Calendar.HOUR, 2);
    	    	Date horaLlegada= utilService.addFecha(reservaSinCalcular.getFechaSalida(), Calendar.HOUR, 2);
    	    	reservaSinCalcular.setFechaLlegada(fechaLlegada);
    	    	reservaSinCalcular.setHoraLlegada(horaLlegada);
    	    	Automovil auto= this.arrangeAutomovil();
    	    	Trabajador trabajador= this.arrangeTrabajador();
    	    	List<Reserva> reservasAceptadasAnteriormente= new ArrayList<Reserva>();
    	    	reservasAceptadasAnteriormente.add(reservaSinCalcular); //La añadiremos la misma reserva que se intenta aceptar para que se solapen en horario
    	    	
    	    	when(trabajadorService.findByUsername(trabajador.getUser().getUsername())).thenReturn(trabajador);    	    	
    	    	when(reservaRepository.findReservasAceptadasByTrabajadorId(trabajador.getId())).thenReturn(reservasAceptadasAnteriormente);

    	   		  //ACT & ASSERT
    	   		  assertThrows(ExisteViajeEnEsteHorarioException.class,()->reservaService.aceptarReserva(reservaSinCalcular, auto, trabajador.getUser().getUsername()));
    	   	  }
    	    
    	    @Test
    	   	@Transactional
    	   	@DisplayName("Exepción tras intentar RECHAZAR una reserva que ya ha sido aceptada/rechazada anteriormente")
    	   	void rechazarReservaTest1()  {
    	   		  //ARRANGE
    	    	reservaSinCalcular.setRuta(rutaCalculada);
    	    	estadoSolicitada.setName("Rechazada");
    	    	reservaSinCalcular.setEstadoReserva(estadoSolicitada);
    	    	
    	   		  //ACT & ASSERT
    	   		  assertThrows(ParadaYaAceptadaRechazadaException.class,()->reservaService.rechazarReserva(reservaSinCalcular));
    	   	  }
    	    
    	    @Test
    	   	@Transactional
    	   	@DisplayName("Rechazar una reserva solicitada por un cliente")
    	   	void rechazarReservaTest2() throws ParadaYaAceptadaRechazadaException {
    	   		  //ARRANGE
    	    	reservaSinCalcular.setRuta(rutaCalculada);
    	    	reservaSinCalcular.setEstadoReserva(estadoSolicitada);
    	    	EstadoReserva estadoRechazada= new EstadoReserva();
    	    	estadoRechazada.setName("Rechazada");
    	    	estadoRechazada.setId(3);
    	    	when(estadoService.findEstadoById(3)).thenReturn(Optional.ofNullable(estadoRechazada));

    	   		  //ACT
    	   		 Reserva reservaRechazada= reservaService.rechazarReserva(reservaSinCalcular);
    	   		 //ASSERT
    	   		 assertEquals(reservaRechazada.getEstadoReserva().getName(),"Rechazada");
    	   	  }
    }
   
    
    @Test
	@Transactional
	@DisplayName("Exepción tras editar una reserva con fecha de llegada anterior a la de salida")
	void calcularNuevaReservaTest4()  {
		  //ARRANGE
		  Reserva reserva= new Reserva();
		  Date today= new Date();
		  reserva.setFechaSalida(today);
		  reserva.setHoraSalida(today); //hora actual
		  Date ayer= utilService.addFecha(today, Calendar.DATE, -1);
		  reserva.setFechaLlegada(ayer); //día anterior
		  reserva.setHoraLlegada(today); //hora actual
		  //ACT & ASSERT
		  assertThrows(FechaLlegadaAnteriorSalidaException.class,()->reservaService.guardarReservaEditada(reserva, reserva));
	  }
    
  
    
    
    @Test
    @Transactional
	void calcularFactura(){
    	//ARRANGE
    	Integer porcentajeIvaRepercutido = 10;
    	Double precioKm = 0.12;
    	Double numKmTotales = 120.0;
    	Double precioEsperaPorHora = 4.5;
    	Double horasEspera = 0.0;
    	
    	Double precioTotal = precioKm * numKmTotales;
    	
    	Double ivaRepercutido = porcentajeIvaRepercutido * 0.01 * precioTotal;
		Double precioDistancia = precioKm * numKmTotales;
		Double precioExtraEspera = horasEspera * precioEsperaPorHora;
		Double baseImponible = precioTotal - ivaRepercutido;
		
		Map<String, Double> res = new HashMap<String, Double>();
		res.put("IVA Repercutido", utilService.aproximarNumero(ivaRepercutido));
		res.put("Precio Distancia", utilService.aproximarNumero(precioDistancia));
		res.put("Precio Extra Espera", utilService.aproximarNumero(precioExtraEspera));
        res.put("Base Imponible", utilService.aproximarNumero(baseImponible));
    
    /*
    	Reserva reserva1 = new Reserva();
    	Tarifa tarifa1 = new Tarifa();
    	
    	tarifa1.setPorcentajeIvaRepercutido(porcentajeIvaRepercutido);
    	tarifa1.setPrecioPorKm(precioKm);
    	tarifa1.setPrecioEsperaPorHora(precioEsperaPorHora);
    	
    	reserva1.setTarifa(tarifa1);
    	reserva1.setHorasEspera(horasEspera);
    	reserva1.setPrecioTotal(precioTotal);
    	*/
        
        Reserva reserva1 = reservaService.findReservaById(1).get();
    	when(reservaRepository.findById(any())).thenReturn(Optional.of(reserva1));

		//ACT & ASSERT
	assertNotEquals(res, reservaService.calcularFactura(1));
		
	}
	
		

//    @Test
//    @Transactional
//    @DisplayName("Cancelar una reserva con estado Solicitada o Aceptada")
//    void cancelarReservaSolicitadaAceptadaTest() {
//    //ARRANGE	
//    	Reserva reserva = new Reserva();
//    	reserva.getEstadoReserva().equals("Solicitada");
//    	
//    	Reserva reserva2 = new Reserva();
//    	reserva.getEstadoReserva().equals("Aceptada");
//    
//    //ACT
//    	reserva = reservaService.cancelarReserva(reserva);
//    }
    
    @Test
    @Transactional
    @DisplayName("Cancelar una reserva con estado Rechazada")
    void cancelarReservaRechazadaTest() {
      //ARRANGE	
    	
    	Reserva reserva = new Reserva();
    	
    	Date horaSalida= new Date(); 
    	horaSalida.setHours(8);
    	horaSalida.setMinutes(0);
    	
		Date fechaSalida= new Date();
		fechaSalida.setDate(22);
		fechaSalida.setMonth(3);
		fechaSalida.setYear(2021);
		fechaSalida.setHours(horaSalida.getHours());
		fechaSalida.setMinutes(horaSalida.getMinutes());
    	
    	EstadoReserva estado = new EstadoReserva();
    	estado.setId(3);
    	estado.setName("Rechazada");
    	reserva.setEstadoReserva(estado);
    	reserva.setFechaSalida(fechaSalida);
    	reserva.setHoraSalida(horaSalida);
    	reserva.setPlazas_Ocupadas(3);
    	
      //ASSERT
    	assertThrows(ReservaYaRechazada.class, ()->reservaService.cancelarReserva(reserva));
    	
    }
  
//    @Test
//    @Transactional
//    @DisplayName("Cancelar una reserva con fecha de salida con intervalo menor a 24 horas respecto a la actualidad")
//    void cancelarReservaMenorIntervaloTest() {
//    	//ARRANGE
//    	
//    	Ruta ruta= new Ruta(); 
//    	Double numKmTotales=142.0;
//    	ruta.setNumKmTotales(numKmTotales);
//    	ruta.setHorasEstimadasCliente(1.0);
//    	
//    	Reserva reserva = new Reserva();
//    	
//    	Date horaSalida= new Date(); 
//    	horaSalida.setHours(8);
//    	horaSalida.setMinutes(0);
//    	
//		Date fechaSalida= new Date();
//		fechaSalida.setDate(6);
//		fechaSalida.setMonth(2);
//		fechaSalida.setYear(2021);
//		fechaSalida.setHours(horaSalida.getHours());
//		fechaSalida.setMinutes(horaSalida.getMinutes());
//    	
//    	EstadoReserva estado = new EstadoReserva();
//    	estado.setId(2);
//    	estado.setName("Aceptada");
//    	reserva.setEstadoReserva(estado);
//    	reserva.setFechaSalida(fechaSalida);
//    	reserva.setHoraSalida(horaSalida);
//    	reserva.setPlazas_Ocupadas(3);
//    	reserva.setRuta(ruta);
//    	
//    	//ASSERT
//    	assertThrows(CancelacionViajeAntelacionException.class,()->reservaService.cancelarReserva(reserva));
//    }
    
//    @Test
//    @Transactional
//    @DisplayName("Cancelar una reserva con fecha de salida anterior a la fecha actual")
//    void cancelarReservaFechaSalidaAnteriorTest() {
//    }
}
