package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ReservaServiceTest {

	@Autowired
	UtilService utilService;
	
	@Autowired
	ReservaService reservaService;
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	TarifaService tarifaService;
	
	@Autowired
	RutaService rutaService;
	
	@Autowired
	EstadoReservaService estadoReservaService;
	
	@Autowired
	UserService userService;
	
	  
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

   
    /*	Reserva reserva1 = new Reserva();
    	Cliente cliente1 = new Cliente();
    	//User user1 = new User();
    	//userService.saveUser(user1);
		
    	
    	//cliente1.setUser(user1);
		cliente1.setNombre("Pablo");
		cliente1.setApellidos("Garcia");
		cliente1.setEmail("pablo@gmail.com");
		cliente1.setTelefono("678823472");
		cliente1.setDni("43434343K");
		clienteService.saveCliente(cliente1);
    	
    	EstadoReserva estadoReserva1 = new EstadoReserva();
    	estadoReserva1.setName("Completada");
    	estadoReservaService.save(estadoReserva1);
    	
    	Tarifa tarifa1 = new Tarifa();
    	tarifa1.setPrecioPorKm(0.12);
    	tarifa1.setPrecioEsperaPorHora(4.5);
    	tarifa1.setPorcentajeIvaRepercutido(10);
    	//tarifaService.save(tarifa1);
    	
     	Date horaSalida= new Date(); 
    	horaSalida.setHours(8);
    	horaSalida.setMinutes(0);
    	
    	Date horaLlegada= new Date(); 
    	horaSalida.setHours(9);
    	horaSalida.setMinutes(0);
    	
    	Date fechaSalida= new Date();
		fechaSalida.setDate(6);
		fechaSalida.setMonth(2);
		fechaSalida.setYear(2021);
		fechaSalida.setHours(horaSalida.getHours());
		fechaSalida.setMinutes(horaSalida.getMinutes());
		
		Date fechaLlegada= new Date();
		fechaLlegada.setDate(6);
		fechaLlegada.setMonth(2);
		fechaLlegada.setYear(2021);
		fechaLlegada.setHours(horaLlegada.getHours());
		fechaLlegada.setMinutes(horaLlegada.getMinutes());
		
    	Ruta ruta1 = new Ruta();
    	ruta1.setOrigenCliente("Badajoz");
    	ruta1.setHorasEstimadasCliente(2.0);
    	ruta1.setDestinoCliente("Zahínos");
    	ruta1.setNumKmTotales(180.0);
    	ruta1.setHorasEstimadasTaxista(3.0);
    	//rutaService.save(ruta1);
    	
    	reserva1.setId(5);
    	reserva1.setCliente(cliente1);
    	reserva1.setRuta(ruta1);
    	reserva1.setFechaSalida(fechaSalida);
    	reserva1.setHoraSalida(horaSalida);
    	reserva1.setFechaLlegada(fechaLlegada);
    	reserva1.setHoraLlegada(horaLlegada);
    	reserva1.setHorasEspera(0.0);
    	reserva1.setPlazas_Ocupadas(2);
    	reserva1.setDescripcionEquipaje("pesado");
    	reserva1.setEstadoReserva(estadoReserva1);
    	reserva1.setPrecioTotal(34.0);
    	reserva1.setNumKmTotales(60.0);
    	reserva1.setTarifa(tarifa1);
    	reservaService.save(reserva1);

 */
          
      		//ACT & ASSERT
    	assertNotEquals(res, reservaService.calcularFactura(1));
	}
	
		
}
