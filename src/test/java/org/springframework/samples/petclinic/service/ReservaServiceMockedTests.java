package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
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

    protected ReservaService reservaService;

    @BeforeEach
    void setup() {
    	
    	reservaService= new ReservaService(reservaRepository,trayectoService,estadoService,clienteService,rutaService,tarifaService,trabajadorService,authoService);
    	
    }
    
    @Test
    @Transactional
    void calcularPrecioDistanciaTest() {
    	//ARRANGE
    	Double kmTotal=1057.4;
    	Double precioPorKm=0.41;
    	Double precioTotalDistancia= kmTotal*precioPorKm;
		Double precioTotalRedondeado=Math.round(precioTotalDistancia*100.0)/100.0;
		//ACT & ASSERT
    	assertEquals(precioTotalRedondeado,reservaService.calcularPrecioDistancia(kmTotal, precioPorKm));
    }
    

    
    
    @Test
    @Transactional
    @DisplayName("Sumar minutos a una fecha")
    void addFechaTest1() {
    	//ARRANGE
    	
    	// Hora base: 22:00
    		Date fechaBase= new Date();
    		fechaBase.setHours(22);
    		fechaBase.setMinutes(0); 
    	//FECHA 1
    		int minutosSumar1= 90;
    	// Hora esperada: 23:30
    		int horaCalculada1= 23;
    		int minutosCalculados1=30; 
    	//FECHA 2
    		int minutosSumar2=210;
    		//hora esperada: 2:00 DEL SIGUIENTE DÍA
    		int horaCalculada2=1;
    		int minutosCalculados2=30;
    	//ACT
    	Date fechaCalculada1= reservaService.addFecha(fechaBase, Calendar.MINUTE, minutosSumar1);
    	Date fechaCalculada2= reservaService.addFecha(fechaBase, Calendar.MINUTE, minutosSumar2);

    	
    	//ASSERT
    		//Fecha 1
    	assertEquals(horaCalculada1,fechaCalculada1.getHours());
    	assertEquals(minutosCalculados1,fechaCalculada1.getMinutes());
    	assertEquals(fechaBase.getDate(),fechaCalculada1.getDate());
    		//Fecha 2
    	assertEquals(horaCalculada2,fechaCalculada2.getHours());
    	assertEquals(minutosCalculados2,fechaCalculada2.getMinutes());
    	assertEquals(fechaBase.getDate()+1,fechaCalculada2.getDate()); //SIGUIENTE DÍA!!!
    	
    }
    
    @Test
    @Transactional
    @DisplayName("Restar minutos a una fecha")
    void subtractFechaTest() {
    	//ARRANGE
    	
    	// Hora base: 1:00
    		Date fechaBase= new Date();
    		fechaBase.setHours(1);
    		fechaBase.setMinutes(0); 
    	//FECHA 1
    		int minutosRestar1= -30;
    	// Hora esperada: 00:30
    		int horaCalculada1= 0;
    		int minutosCalculados1=30; 
    	//FECHA 2
    		int minutosRestar2=-90;
    		//hora esperada: 23:30 DEL DÍA ANTERIOR
    		int horaCalculada2=23;
    		int minutosCalculados2=30;
    	//ACT
    	Date fechaCalculada1= reservaService.addFecha(fechaBase, Calendar.MINUTE, minutosRestar1);
    	Date fechaCalculada2= reservaService.addFecha(fechaBase, Calendar.MINUTE, minutosRestar2);

    	
    	//ASSERT
    		//Fecha 1
    	assertEquals(horaCalculada1,fechaCalculada1.getHours());
    	assertEquals(minutosCalculados1,fechaCalculada1.getMinutes());
    	assertEquals(fechaBase.getDate(),fechaCalculada1.getDate());
    		//Fecha 2
    	
    	assertEquals(horaCalculada2,fechaCalculada2.getHours());
    	assertEquals(minutosCalculados2,fechaCalculada2.getMinutes());
    	assertEquals(fechaBase.getDate()-1,fechaCalculada2.getDate()); //DÍA ANTERIOR
    	
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
    	//Llegada esperada 6:12
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
    	Date fechaSalida1= reservaService.addFecha(today,Calendar.DATE,-1); //Será la fecha actual restándole 1 día
	    
    	//ACT
    	assertThrows(FechaSalidaAnteriorActualException.class,()->reservaService.fechaSalidaAnteriorActual(fechaSalida1, fechaSalida1));
    }
    @Test
    @Transactional
    @DisplayName("Restar 1 día a una fecha") //Necesario testearlo porque se utiliza en el test anterior fechaSalidaAnteriorActualTest()
    void sumarDiaFecha() {
    	Date today= new Date(); //Día 10
    	today.setDate(10);
    	Date diaAnterior= reservaService.addFecha(today,Calendar.DATE,-1); //Día 9
    	assertEquals(9, diaAnterior.getDate());
    }
    
    @Test
    @Transactional
    @DisplayName("fecha de salida igual al día actual pero HORA DE SALIDA ANTERIOR a la hora actual")
    void horaSalidaAnteriorActualTest() {

    	//ARRANGE
    	Date today= new Date();
    	Date horaSalida= reservaService.addFecha(today,Calendar.MINUTE,-5); //Hora actual restándole 5 min, este método ya está testeado
	    
    	//ACT
    	assertThrows(FechaSalidaAnteriorActualException.class,()->reservaService.fechaSalidaAnteriorActual(today, horaSalida));
    }
   /*
    @Test
    @Transactional
    @DisplayName("Calcular y guardar datos en una reserva para mostrarla ANTES DE CONFIRMARLA")
    void calcularNuevaReservaTest() throws DuplicatedParadaException,FechaSalidaAnteriorActualException {

    	//ARRANGE  
    	
    	//Aunque no utilizáramos los mocks habría que crear una reserva para probar el método, es igual de largo.
    	// así que al menos usando mocks nos ahorramos buscar en la BD
    	
    	Reserva reserva= arrangeReservaRuta();
    	//Para que se entienda el test debemos repetir una parte del código de arrangeReservaRuta();
    	Double numKmTotales=142.0;
    	reserva.setNumKmTotales(numKmTotales);
    	Tarifa tarifa= new Tarifa(); 
    	tarifa.setPrecioPorKm(0.5);
    	when(tarifaService.findTarifaActiva()).thenReturn(tarifa);
    	reserva.getRuta().setHorasEstimadasCliente(1.0);
    	reserva.getHoraSalida().setHours(4);
    	Double precioTotalEsperado=71.0; //142*0.5
    	int horaDeLlegadaEsperada=5;
    	
    	//ACT
    	Reserva reservaCalculada= reservaService.calcularNuevaReserva(reserva, false);
   
    	//ASSERT
    	
    	//Comprobamos que los DATOS NUEVOS  de la reserva se han calculado y GUARDADO
    	System.out.println(reservaCalculada.getPrecioTotal());
    	assertEquals(reservaCalculada.getPrecioTotal(),precioTotalEsperado);
    	assertEquals(reservaCalculada.getFechaLlegada().getHours(),horaDeLlegadaEsperada);
    	assertEquals(reservaCalculada.getNumKmTotales(),numKmTotales);
    	
    	//Comprobamos que los datos que vendrían de un "formulario" se han guardado en la reserva
    	assertEquals(reservaCalculada.getDescripcionEquipaje(),"Maleta grande");
    	assertEquals(reservaCalculada.getFechaSalida(),reserva.getFechaSalida());
    	assertEquals(reservaCalculada.getHoraSalida(),reserva.getHoraSalida());
    	assertEquals(reservaCalculada.getPlazas_Ocupadas(),reserva.getPlazas_Ocupadas());
    	
    	//Comprobamos que los campos asociados a cuando se CONFIRMA una reserva no han sido creados TODAVÍA:
    
    	assertNull(reservaCalculada.getHorasEspera());
    	assertNull(reservaCalculada.getEstadoReserva());
    	
    }
    */
    
    /*
    @Test
    @Transactional
    @DisplayName("Calcular y guardar datos de una reserva al CONFIRMARLA con una ruta existente en la BD")
    void confirmarNuevaReservaTest() throws DuplicatedParadaException,FechaSalidaAnteriorActualException {

    //La parte comprobada en calcularNuevaReservaTest() no se volverá a comprobar aquí, 
    	//ya que calcularYConfirmarReserva() llama también a calcularNuevaReserva()
    	
    	Reserva reserva= arrangeReservaRuta();
    	
    	Double horasEspera=0.0;
    	EstadoReserva estado= new EstadoReserva();
    	Integer idEstado=1;
    	estado.setId(idEstado);
    	Cliente cliente=  new Cliente();
    	User user= new User();
    	user.setUsername("usuarioPrueba");
    	cliente.setUser(user);
    	when(estadoService.findEstadoById(anyInt())).thenReturn(Optional.of(estado));
    	when(clienteService.findClienteByUsername(anyString())).thenReturn(cliente);
    	when(rutaService.findRutaByRuta(reserva.getRuta())).thenReturn(Optional.of(reserva.getRuta()));
    	//ACT & ASSERT
    	
    		//La ruta que ha calculado existirá en la base de datos y no hará un save(ruta) en la BD
    		Reserva reservaConfirmada= reservaService.calcularYConfirmarReserva(reserva,"usuarioPrueba");
    		verify(rutaService,never()).save(reserva.getRuta());
    		
    	
    		//No habrá una ruta igual en la base de datos a la que ha calculado y hará un save(ruta) de dicha ruta
    		when(rutaService.findRutaByRuta(reserva.getRuta())).thenReturn(Optional.ofNullable(null));
        	Reserva reservaConfirmada2= reservaService.calcularYConfirmarReserva(reserva,"usuarioPrueba");
    		verify(rutaService).save(reserva.getRuta());
    	
    	verify(rutaService,times(2)).findRutaByRuta(reserva.getRuta());
    	assertEquals(reservaConfirmada.getHorasEspera(),horasEspera);
    	assertEquals(reservaConfirmada.getEstadoReserva().getId(),idEstado);
    	assertEquals(reservaConfirmada.getCliente().getUser().getUsername(),"usuarioPrueba");

    	
    	
    }
    */
    public  Reserva arrangeReservaRuta() throws DuplicatedParadaException,FechaSalidaAnteriorActualException {
    	Ruta ruta= new Ruta(); //Zahinos--->Badajoz--->Zahinos
    	Double numKmTotales=142.0;
    	ruta.setNumKmTotales(numKmTotales);
    	ruta.setHorasEstimadasCliente(1.0);
    
    	
    	Integer plazasOcupadas=2;
    	Reserva reserva= Reserva.newReservaSinCalcular(new Date(), new Date(), 2, "Maleta grande");
    	reserva.setRuta(ruta);
    	Date fechaSalida= new Date();
    	fechaSalida= reservaService.addFecha(fechaSalida, Calendar.DATE, 1); //Para que no salte ninguna excepción
    	Date horaSalida= new Date();
    	horaSalida.setHours(4);
    	reserva.setFechaSalida(fechaSalida);
    	reserva.setHoraSalida(horaSalida);
    	Tarifa tarifa= new Tarifa();
    	tarifa.setPrecioPorKm(0.5);
    	
    	when(tarifaService.findTarifaActiva()).thenReturn(tarifa);
    	when(trayectoService.calcularYAsignarTrayectos(reserva.getRuta())).thenReturn(ruta);
    	
    	return reserva;
    }
    
   
    
}
