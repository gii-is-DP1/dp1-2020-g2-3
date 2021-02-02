package org.springframework.samples.petclinic.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.AutomovilRepository;
import org.springframework.samples.petclinic.repository.ClienteRepository;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.repository.RutaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.samples.petclinic.service.exceptions.AutomovilPlazasInsuficientesException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.EstadoReservaFacturaException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
import org.springframework.samples.petclinic.service.exceptions.ParadaYaAceptadaRechazadaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {
	//Repositorio
	private ReservaRepository reservaRepo;
	//Servicios
	private TrayectoService trayectoService;
	private EstadoReservaService estadoService;
	private ClienteService clienteService;
	private RutaService rutaService;
	private TarifaService tarifaService;
	private TrabajadorService trabajadorService;
	
	@Autowired
	public ReservaService(ReservaRepository reservaRepo,TrayectoService trayectoService,EstadoReservaService estadoService,ClienteService clienteService,RutaService rutaService,TarifaService tarifaService,TrabajadorService trabajadorService) {
		this.reservaRepo=reservaRepo;
		this.trayectoService=trayectoService;
		this.estadoService=estadoService;
		this.clienteService=clienteService;
		this.rutaService=rutaService;
		this.tarifaService=tarifaService;
		this.trabajadorService=trabajadorService;
	}
	
	@Transactional
	public Double calcularPrecioDistancia(Double kmTotal, Double precioPorKm) {
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
			fechaSalida= this.addFecha(fechaSalida, Calendar.MINUTE, minutosSumar);
		}
		return fechaSalida;
	}
	
	@Transactional
	public Date addFecha(Date fechaBase, int tipoFecha, int cantidadSumar ) {
		   Calendar calendar = Calendar.getInstance();
		      calendar.setTime(fechaBase); 
		      calendar.add(tipoFecha, cantidadSumar);
		      return calendar.getTime();
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
	
	@Transactional(readOnly = true)
	public Optional<Reserva> findFacturaReservaById(int id) throws DataAccessException, EstadoReservaFacturaException {
	Reserva reserva = reservaRepo.findById(id).get();
		if(reserva.getEstadoReserva().getName().equals("Completada")) {
			System.out.println("La reserva ha sido completada, se muestra la factura");
		}else {
			throw new EstadoReservaFacturaException();
		}
		
		return reservaRepo.findById(id);
	}

	
	
	@Transactional //Este método calculará el precio, la fecha/hora estimada de llegada, los km totales,ruta... de la reserva después del primer formulario de su creación
	public Reserva calcularReservaAPartirDeRuta(Reserva reserva,boolean confirmarReserva,boolean nuevaReserva) throws FechaSalidaAnteriorActualException,DataAccessException,DuplicatedParadaException {
		
		//Si confirmarReserva= false solo se calcularán los campos estrictamente necesarios para mostrar la reserva en el formulario
		
		if(nuevaReserva) { //si el cliente calcula la reserva se comprueba la restricción de la fecha
			fechaSalidaAnteriorActual(reserva.getFechaSalida(), reserva.getHoraSalida()); //Comprobar fecha

		}

		Ruta nuevaRuta= trayectoService.calcularYAsignarTrayectos(reserva.getRuta());
		reserva.setRuta(nuevaRuta);
		reserva.setNumKmTotales(nuevaRuta.getNumKmTotales());
		Date fechaHoraLlegada=calcularFechaYHoraLlegada(reserva.getFechaSalida(), reserva.getHoraSalida(),nuevaRuta.getHorasEstimadasCliente());
		reserva.setFechaLlegada(fechaHoraLlegada);
		reserva.setHoraLlegada(fechaHoraLlegada);
		Double precioPorKm=tarifaService.findTarifaActiva().getPrecioPorKm();
		Double precioTotal=calcularPrecioDistancia(nuevaRuta.getNumKmTotales(), precioPorKm);
		reserva.setPrecioTotal(precioTotal);
		if(confirmarReserva ) { //Solo cuando se confirma la reserva se añadirán los siguientes campos
			
			//Por motivos de seguridad, tras ver el precio de una reserva, se vuelven a calcular todos los campos de la reserva
			// en este método,por si alguien ha intentado modificar algo desde el jsp.
			//Antes de confirmar la reserva no se calculan estos campos, porque en la vista precioReserva.jsp no aparecen
			
			//si la ruta creada ya existe en la BD se asignará
			reserva=asignarRutaExistenteOCrearla(reserva);
			
				reserva.setHorasEspera(0.0);
				reserva.setEstadoReserva(estadoService.findEstadoById(1).get());
			
		
		}
		return reserva;
		
	}
	
	@Transactional
	public Reserva asignarRutaExistenteOCrearla(Reserva reserva) {
		Ruta ruta= reserva.getRuta();
		Optional<Ruta> rutaExistente= rutaService.findRutaByRuta(ruta);
		
		if(rutaExistente.isPresent()) {
			System.out.println("Ya existe una ruta similar! No hace falta guardarla en la BD");
			ruta.setId(rutaExistente.get().getId());
			reserva.setRuta(ruta);
			
			
		}else {
			
			System.out.println("No existe la ruta, se asignará  una nueva a la reserva"); //Ya está asignada antes del if(confirmarReserva)
			System.out.println(ruta);
			rutaService.save(ruta); //Se guarda en la BD la nueva ruta
			System.out.println("ruta asignada!");
		} 
		return reserva;
	}
	/*@Transactional //Este método calculará el precio, la fecha/hora estimada de llegada, los km totales,ruta... de la reserva después del primer formulario de su creación
	public Reserva calcularNuevaReserva(Reserva reserva,boolean confirmarReserva) throws FechaSalidaAnteriorActualException,DataAccessException,DuplicatedParadaException {
		
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
		Double precioPorKm=tarifaService.findTarifaActiva().getPrecioPorKm();
		Double precioTotal=calcularPrecioDistancia(nuevaRuta.getNumKmTotales(), precioPorKm);
		reservaCalculada.setPrecioTotal(precioTotal);
		if(confirmarReserva ) { //Solo cuando se confirma la reserva se añadirán los siguientes campos
			
			//Por motivos de seguridad, tras ver el precio de una reserva, se vuelven a calcular todos los campos de la reserva
			// en este método,por si alguien ha intentado modificar algo desde el jsp.
			//Antes de confirmar la reserva no se calculan estos campos, porque en la vista precioReserva.jsp no aparecen
			
			//si la ruta creada ya existe en la BD se asignará
			Optional<Ruta> rutaExistente= rutaService.findRutaByRuta(nuevaRuta);
		
			if(rutaExistente.isPresent()) {
				System.out.println("Ya existe una ruta similar! No hace falta guardarla en la BD");
				nuevaRuta.setId(rutaExistente.get().getId());
				reserva.setRuta(nuevaRuta);
				System.out.println("Trayectos de la ruta existente: " + rutaExistente.get().getTrayectos());
				System.out.println("Ruta existente: " + rutaExistente.get());
				
			}else {
				
				System.out.println("No existe la ruta, se asignará una  una nueva a la reserva"); //Ya está asignada antes del if(confirmarReserva)
				
				rutaService.save(nuevaRuta); //Se guarda en la BD la nueva ruta
				System.out.println("ruta asignada!");
			}
			reservaCalculada.setHorasEspera(0.0);
			reservaCalculada.setEstadoReserva(estadoService.findEstadoById(1).get());
			
		}
		return reservaCalculada;
		
	}
	*/
	@Transactional
	public Reserva guardarReservaEditada(Reserva reservaEditada,Reserva reservaBD) throws DuplicatedParadaException {
	
		Ruta rutaConstruida= trayectoService.calcularYAsignarTrayectos(reservaEditada.getRuta());
		reservaEditada.setRuta(rutaConstruida);
		System.out.println("Guardar ruta editada: " + rutaConstruida);
		reservaEditada= asignarRutaExistenteOCrearla(reservaEditada);
		System.out.println(reservaEditada.getRuta());
		BeanUtils.copyProperties(reservaEditada, reservaBD, "id");
		save(reservaBD);
		return reservaBD;
	}
	@Transactional 
	public Reserva calcularYConfirmarNuevaReservaCliente(Reserva reserva,String username)throws FechaSalidaAnteriorActualException,DataAccessException,DuplicatedParadaException{
		Reserva reservaCalculada= calcularReservaAPartirDeRuta(reserva,true,true);
		Cliente cliente= clienteService.findClienteByUsername(username);
		reservaCalculada.setCliente(cliente);
		this.save(reservaCalculada);
		return reservaCalculada;
	}
	
	
	
	@Transactional
	public Iterable<Reserva> findPeticionesReserva() throws DataAccessException {
		 return reservaRepo.findPeticionesReserva();
		
	}

	@Transactional
	public void rechazarReserva(Reserva reserva) throws DataAccessException,ParadaYaAceptadaRechazadaException {
		
		if(!reserva.getEstadoReserva().getName().equals("Solicitada")) {
			throw new ParadaYaAceptadaRechazadaException();
		}else {
			
			EstadoReserva estadoReserva= estadoService.findEstadoById(3).get(); //Estado 3= Rechazada
			reserva.setEstadoReserva(estadoReserva);
			save(reserva);
			
		}
		
	}
	@Transactional
	public void aceptarReserva(Reserva reserva,Automovil auto,Principal p) throws DataAccessException,ParadaYaAceptadaRechazadaException,AutomovilPlazasInsuficientesException {
	
		if(!reserva.getEstadoReserva().getName().equals("Solicitada")) {
			throw new ParadaYaAceptadaRechazadaException();
		}else {
			System.out.println("P GET NAME: " + p.getName());
			Trabajador trabajador= trabajadorService.findByUsername(p.getName());
			
			if(auto.getNumPlazas()-1<reserva.getPlazas_Ocupadas()){
				throw new AutomovilPlazasInsuficientesException();
			} //Añadir con else if las comprobaciones de las RN-02 Automóvil en uso  y RN-04- Rechazar viajes en un mismo horario
			else {
				
				
				EstadoReserva estadoReserva=estadoService.findEstadoById(2).get(); //Estado 2= Aceptada
				reserva.setTrabajador(trabajador);
				reserva.setAutomovil(auto);
				reserva.setEstadoReserva(estadoReserva);
				save(reserva);
			}
		
		}
		
		
	}
	@Transactional
	public Collection<Reserva> findReservasByClienteId(int id) throws DataAccessException {
	return reservaRepo.findReservasByClienteId(id);
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
