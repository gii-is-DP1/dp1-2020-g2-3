package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;

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
    
    protected ReservaService reservaService;

    @BeforeEach
    void setup() {
    	
    	reservaService= new ReservaService(reservaRepository,trayectoService,estadoService,clienteService,rutaService);
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
    @DisplayName("Calcular la HORA de llegada dada la hora de salida y la duración del viaje")
    void calcularHoraLlegadaTest() {
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
    	assertEquals(horaLlegada,dateCalculada.getHours());
    	assertEquals(minutosLlegada,dateCalculada.getMinutes());
    }
    
    @Test
    @Transactional
    @DisplayName("Calcular la FECHA y HORA de llegada cuando el viaje finaliza el MISMO DÍA en el que se empezó")
    void calcularHoraYFechaLlegadaTest() {
    	//ARRANGE
    	Date fechaSalida= new Date();
    	Date horaSalida= new Date();
    	horaSalida.setHours(10); //Salida 10:00
    	horaSalida.setMinutes(0);
    	Double horasEstimadasCliente=1.2;
    	int horaLlegada=11;
    	int minutosLlegada=12; //Llegada esperada: 11:12 del mismo día
    	
    	//ACT
    	Date dateCalculada=reservaService.calcularFechaYHoraLlegada(fechaSalida, horaSalida, horasEstimadasCliente);
    	//ASSERT
    	assertEquals(fechaSalida.getDate(),dateCalculada.getDate()); //La fecha de llegada tendrá que ser el mismo día de salida
    	assertEquals(horaLlegada,dateCalculada.getHours());
    	assertEquals(minutosLlegada,dateCalculada.getMinutes());
    }
    
    @Test
    @Transactional
    @DisplayName("Calcular la FECHA y HORA de llegada cuando el viaje finaliza en el SIGUIENTE DÍA en el que se empezó")
    void calcularHoraYFechaLlegadaSiguienteDiaTest() {
    	//ARRANGE
    	Date fechaSalida= new Date(); //Salida: Día 10
    	fechaSalida.setDate(10);
    	Date horaSalida= new Date();
    	horaSalida.setHours(23); //Hora Salida: 23:00
    	horaSalida.setMinutes(0);
    	Double horasEstimadasCliente=1.5;
    	int horaLlegada=0;
    	int minutosLlegada=30; //Llegada esperada: 0:30 del SIGUIENTE DÍA, Día 11
    	
    	//ACT
    	Date dateCalculada=reservaService.calcularFechaYHoraLlegada(fechaSalida, horaSalida, horasEstimadasCliente);
    	//ASSERT
    	assertEquals(11,dateCalculada.getDate());
    	assertEquals(horaLlegada,dateCalculada.getHours());
    	assertEquals(minutosLlegada,dateCalculada.getMinutes());
    }
    
    
}
