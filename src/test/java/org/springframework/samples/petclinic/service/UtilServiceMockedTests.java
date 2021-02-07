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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.samples.petclinic.service.exceptions.CancelacionViajeAntelacionException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
import org.springframework.samples.petclinic.service.exceptions.ReservaYaRechazada;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;
import static org.mockito.Mockito.*;

import aj.org.objectweb.asm.ClassTooLargeException;


@ExtendWith(MockitoExtension.class)
class UtilServiceMockedTests {

    protected UtilService utilService; //No utiliza ningún repositorio
    @BeforeEach
    void setup() {
    	
    	utilService= new UtilService();
    	
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
    	//hora esperada: 1:30 DEL SIGUIENTE DÍA
    		int horaCalculada2=1;
    		int minutosCalculados2=30;
    	//ACT
    	Date fechaCalculada1= utilService.addFecha(fechaBase, Calendar.MINUTE, minutosSumar1);
    	Date fechaCalculada2= utilService.addFecha(fechaBase, Calendar.MINUTE, minutosSumar2);

    	
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
    	Date fechaCalculada1= utilService.addFecha(fechaBase, Calendar.MINUTE, minutosRestar1);
    	Date fechaCalculada2= utilService.addFecha(fechaBase, Calendar.MINUTE, minutosRestar2);

    	
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
    @DisplayName("Restar 1 día a una fecha") 
    void sumarDiaFechaTest() {
    	//ARRANGE
    	Date dia10= new Date(); 
    	dia10.setDate(10); //Día 10
    	
    	//ACT
    	Date diaAnterior9= utilService.addFecha(dia10,Calendar.DATE,-1); //Día 9 al restarle 1 día
    	
    	//ASSERT
    	assertEquals(9, diaAnterior9.getDate());
    }
    
    @Test
    @Transactional
    @DisplayName("Aproximar un número a dos decimales")
    void aproximarNumerosTest() {
    	//ARRANGE
    	Double numeroMuchosDecimales1=7.12*3.14;//22.3568
    	Double numeroAproximado1=22.36;
    	
    	//ACT
    	Double numAproximadoPrueba1=utilService.aproximarNumero(numeroMuchosDecimales1);
    	
    	//ASSERT
    	assertEquals(numeroAproximado1,numAproximadoPrueba1);
    }
    
   
}
