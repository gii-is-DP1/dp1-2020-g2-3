package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.AutomovilRepository;
import org.springframework.samples.petclinic.repository.ClienteRepository;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.repository.RutaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

	private ReservaRepository reservaRepo;
	private TrayectoService trayectoService;
	private EstadoReservaService estadoService;
	private ClienteService clienteService;
	private RutaService rutaService;
	@Autowired
	public ReservaService(ReservaRepository reservaRepo,TrayectoService trayectoService,EstadoReservaService estadoService,ClienteService clienteService,RutaService rutaService) {
		this.reservaRepo=reservaRepo;
		this.trayectoService=trayectoService;
		this.estadoService=estadoService;
		this.clienteService=clienteService;
		this.rutaService=rutaService;
	}
	
	@Transactional
	public Double calcularPrecio(Double kmTotal, Double precioPorKm) {
		//precioPorKm realmente habría que buscarlo de la tarifa que se encuentre activa en este momento.
		
		Double precioTotal=kmTotal*precioPorKm;	
		Double precioTotalRedondeado=Math.round(precioTotal*100.0)/100.0;
		return precioTotalRedondeado;								
		
	}
	@Transactional
	public Date calcularFechaYHoraLlegada(Date fechaSalida, Date horaSalida, Double horasEstimadasCliente) {
		int minutosSumar= (int)Math.round(horasEstimadasCliente*60); //Devolvemos un Date con la fecha y hora de llegada
		fechaSalida.setHours(horaSalida.getHours());
		fechaSalida.setMinutes(horaSalida.getMinutes());
		
		if(minutosSumar!=0) {
			 Calendar calendar = Calendar.getInstance();
		      calendar.setTime(fechaSalida); 
		      calendar.add(Calendar.MINUTE, minutosSumar); 
		      fechaSalida= calendar.getTime(); 
		     
		}
		return fechaSalida;
	}
	
	@Transactional
	public void fechaSalidaAnteriorActual(Date fechaSalida, Date horaSalida) throws FechaSalidaAnteriorActualException  {
		Date today= new Date();
		fechaSalida.setHours(horaSalida.getHours());
		fechaSalida.setMinutes(horaSalida.getMinutes());
	
		if(fechaSalida.compareTo(today)<0) { //Fecha anterior
			System.out.println("fecha de salida es anterior a la actual, se lanza excepción");
			throw new FechaSalidaAnteriorActualException();
		}else {
			System.out.println("la fecha de salida es posterior o igual a la actual, no se lanza excepción");
		}
		
	}
	
	
	@Transactional
	public Iterable<Reserva> findAll(){
		 return reservaRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Reserva> findReservaById(int id) throws DataAccessException {
		return reservaRepo.findById(id);
	}
	
	@Transactional //Este método calculará el precio, la fecha/hora estimada de llegada, los km totales,ruta... de la reserva.
	public Reserva calcularReserva(Reserva reserva,boolean confirmarReserva) throws FechaSalidaAnteriorActualException,DataAccessException,DuplicatedParadaException {
		
		//Si confirmarReserva= false solo se calcularán los campos estrictamente necesarios para mostrar la reserva en el precioReserva.jsp
		
		fechaSalidaAnteriorActual(reserva.getFechaSalida(), reserva.getHoraSalida()); //Comprobar fecha 
		Reserva reservaCalculada= new Reserva();
		reservaCalculada.setDescripcionEquipaje(reserva.getDescripcionEquipaje());
		reservaCalculada.setFechaSalida(reserva.getFechaSalida());
		reservaCalculada.setHoraSalida(reserva.getHoraSalida());
		reservaCalculada.setPlazas_Ocupadas(reserva.getPlazas_Ocupadas());
		Ruta nuevaRuta= trayectoService.calcularYAsignarTrayectos(reserva.getRuta());
		reservaCalculada.setRuta(nuevaRuta);
		reservaCalculada.setNumKmTotales(nuevaRuta.getNumKmTotales());
		Date fechaHoraLlegada=calcularFechaYHoraLlegada(reserva.getFechaSalida(), reserva.getHoraSalida(),nuevaRuta.getHorasEstimadasCliente());
		reservaCalculada.setFechaLlegada(fechaHoraLlegada);
		reservaCalculada.setHoraLlegada(fechaHoraLlegada);
		Double precioPorKm=0.41; //Esto habría que cambiarlo cuadno estén implementadas las tarifas
		Double precioTotal=calcularPrecio(nuevaRuta.getNumKmTotales(), precioPorKm);
		reservaCalculada.setPrecioTotal(precioTotal);
		if(confirmarReserva ) { //Solo cuando se confirma la reserva se añadirán los siguientes campos
			
			//Por motivos de seguridad, tras ver el precio de una reserva, se vuelven a calcular todos los campos de la reserva
			// en este método,por si alguien ha intentado modificar algo desde el jsp.
			//Antes de confirmar la reserva no se calculan estos campos, porque en la vista precioReserva.jsp no aparecen
			
			//si la ruta creada ya existe en la BD se asignará
			Optional<Ruta> rutaExistente= rutaService.findRutaByRuta(nuevaRuta);
			if(rutaExistente.isPresent()) {
				System.out.println("Ya existe una ruta similar! Se asignará desde la BD");
				nuevaRuta= rutaExistente.get();
				reserva.setRuta(nuevaRuta);
				System.out.println("Trayectos de la ruta existente: " + rutaExistente.get().getTrayectos());
				
			}else {
				
				System.out.println("No existe la ruta, se asignará una  una nueva a la reserva"); //Ya está asignada antes del if(confirmarReserva)
				rutaService.save(nuevaRuta); //Se guarda en la BD la nueva ruta
				System.out.println("ruta asignada!");
			}
			reservaCalculada.setHorasEspera(0.0);
			reservaCalculada.setEstadoReserva(estadoService.findEstadoById(1).get());
			reservaCalculada.setPrecioDistancia(precioTotal); //Cuando se crea el viaje las horas de espera son 0 y el precio total es el de los km recorridos
			reservaCalculada.setPrecioEspera(0.0);
			Double precioIvaRedondeado= (double)Math.round((0.1*precioTotal)*100)/100; //Esto se cambiará cuando estén implementadas las tarifas
			reservaCalculada.setPrecioIVA(precioIvaRedondeado);
			Double baseImponibleRedondeada= (double)Math.round((0.9*precioTotal)*100)/100;
			reservaCalculada.setBaseImponible(baseImponibleRedondeada); //Esto se cambiará cunado estén implementadas las tarifas
		}
		return reservaCalculada;
		
	}
	@Transactional 
	public void calcularYConfirmarReserva(Reserva reserva,String username)throws FechaSalidaAnteriorActualException,DataAccessException,DuplicatedParadaException{
		Reserva reservaCalculada= calcularReserva(reserva,true);
		Cliente cliente= clienteService.findClienteByUsername(username);
		reservaCalculada.setCliente(cliente);
		save(reservaCalculada);
	}
	
	@Transactional
	public void delete(Reserva reserva) throws DataAccessException  {
			reservaRepo.delete(reserva);
	}

	@Transactional
	public void save(Reserva reserva)  {
		
		reservaRepo.save(reserva);
	}
	
}

