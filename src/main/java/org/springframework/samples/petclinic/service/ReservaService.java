package org.springframework.samples.petclinic.service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.AutomovilRepository;
import org.springframework.samples.petclinic.repository.ClienteRepository;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.repository.RutaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.samples.petclinic.service.exceptions.AutomovilPlazasInsuficientesException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.HoraSalidaSinAntelacionException;
import org.springframework.samples.petclinic.service.exceptions.FechaLlegadaAnteriorSalidaException;
import org.springframework.samples.petclinic.service.exceptions.EstadoReservaFacturaException;
import org.springframework.samples.petclinic.service.exceptions.ExisteViajeEnEsteHorarioException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
import org.springframework.samples.petclinic.service.exceptions.ParadaYaAceptadaRechazadaException;
import org.springframework.samples.petclinic.service.exceptions.ReservaYaRechazada;
import org.springframework.samples.petclinic.service.exceptions.ReservasSoliAceptException;
import org.springframework.samples.petclinic.service.exceptions.CancelacionViajeAntelacionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
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
	private AuthoritiesService authoService;
	private UtilService utilService;
	
	@Autowired
	public ReservaService(ReservaRepository reservaRepo,TrayectoService trayectoService,EstadoReservaService estadoService,ClienteService clienteService,RutaService rutaService,TarifaService tarifaService,TrabajadorService trabajadorService,AuthoritiesService authoService,UtilService utilService) {
		this.reservaRepo=reservaRepo;
		this.trayectoService=trayectoService;
		this.estadoService=estadoService;
		this.clienteService=clienteService;
		this.rutaService=rutaService;
		this.tarifaService=tarifaService;
		this.trabajadorService=trabajadorService;
		this.authoService=authoService;
		this.utilService=utilService;
	}
	
	@Transactional
	public Double calcularPrecioDistancia(Double kmTotal, Double precioPorKm) {
		//precioPorKm realmente habría que buscarlo de la tarifa que se encuentre activa en este momento.
		
		Double precioTotal=kmTotal*precioPorKm;	
		Double precioTotalRedondeado=Math.round(precioTotal*100.0)/100.0;
		return precioTotalRedondeado;								
		
	}
	@Transactional
	public Map<String,Double> calcularFactura(int id){

		Reserva reserva = reservaRepo.findById(id).get();
		Map<String, Double> res = new HashMap<String, Double>();
		res.put("IVA Repercutido", utilService.aproximarNumero(reserva.getTarifa().getPorcentajeIvaRepercutido() * 0.01 * reserva.getPrecioTotal()));
		res.put("Precio Distancia", utilService.aproximarNumero(reserva.getTarifa().getPrecioPorKm() * reserva.getNumKmTotales()));
		res.put("Precio Extra Espera", utilService.aproximarNumero(reserva.getHorasEspera() * reserva.getTarifa().getPrecioEsperaPorHora()));
        res.put("Base Imponible", utilService.aproximarNumero(reserva.getPrecioTotal() - (reserva.getTarifa().getPorcentajeIvaRepercutido() * 0.01 * reserva.getPrecioTotal())));
		return res;
		
		
	}
	
	@Transactional
	public Date calcularFechaYHoraLlegada(Date fechaSalida, Date horaSalida, Double horasEstimadasCliente) {
		int minutosSumar= (int)Math.round(horasEstimadasCliente*60); //Devolvemos un Date con la fecha y hora de llegada
		fechaSalida.setHours(horaSalida.getHours());
		fechaSalida.setMinutes(horaSalida.getMinutes());
		
		if(minutosSumar!=0) {
			fechaSalida= utilService.addFecha(fechaSalida, Calendar.MINUTE, minutosSumar);
		}
		return fechaSalida;
	}
	
	@Transactional
	public void fechaSalidaSinAntelacion(Date fechaSalida, Date horaSalida) throws HoraSalidaSinAntelacionException{
		Date today = new Date();
		fechaSalida.setHours(horaSalida.getHours());
		fechaSalida.setMinutes(horaSalida.getMinutes());
		fechaSalida = this.addFecha(fechaSalida, Calendar.MINUTE, -40);
		
		if(fechaSalida.compareTo(today)<0) {
			System.out.println("hora de salida con menos de 40 minutos de antelación, se lanza excepción");
			throw new HoraSalidaSinAntelacionException();
		}else {
			System.out.println("la hora de salida tiene al menos 40 minutos de antelación, no se lanza excepción");
		}
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
	public void existeViajeEnEsteHorario(Date horaSalida, Date horaLlegada, Date fechaSalida, int trabajadorId) throws ExisteViajeEnEsteHorarioException{
		Collection<Reserva> reservasTrabajador = reservaRepo.findReservasAceptadasByTrabajadorId(trabajadorId);
		
		for (Reserva r: reservasTrabajador) {
			Boolean cond1 = horaSalida.before(r.getHoraSalida()) && horaLlegada.before(r.getHoraSalida());
			Boolean cond2 = horaSalida.after(r.getHoraSalida()) && horaLlegada.after(r.getHoraLlegada());
			if((r.getFechaSalida().equals(fechaSalida)) && !(cond1 || cond2)){
				System.out.println("El taxista ya tiene un viaje aceptado en ese periodo de tiempo.");
				throw new ExisteViajeEnEsteHorarioException();
			}else {
				System.out.println("El taxista acepta el viaje.");
			}
		}
	}
	
	//Comprueba si un taxista ya tiene una reserva aceptada en el horario en el que esta intentando aceptar otra reserva.
	public boolean taxistaConViaje(int taxistaId, Reserva reserva) {
		Collection<Reserva> reservasTrabajador = reservaRepo.findReservasAceptadasByTrabajadorId(taxistaId);
		boolean res = false;
		for (Reserva r: reservasTrabajador) {
			Date horaSalida = reserva.getHoraSalida();
			Date horaLlegada = reserva.getHoraLlegada();
			Boolean cond1 = horaSalida.before(r.getHoraSalida()) && horaLlegada.before(r.getHoraSalida());
			Boolean cond2 = horaSalida.after(r.getHoraLlegada()) && horaLlegada.after(r.getHoraLlegada());
			if((r.getFechaSalida().equals(reserva.getFechaSalida())) && !(cond1 || cond2)){
				res = true;
			}
		}
		return res;
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
		Optional<Reserva> reserva = reservaRepo.findById(id);
			if(reserva.get().getEstadoReserva().getName().equals("Completada")) {
				log.info("La reserva ha sido completada, se muestra la factura");
				return reserva;
			}else {
				log.error("Sólo se mostrarán aquellas reservas que estén completadas.");
				throw new EstadoReservaFacturaException();
			}
		}


	
	
	@Transactional //Este método calculará el precio, la fecha/hora estimada de llegada, los km totales,ruta... de la reserva después del primer formulario de su creación
	public Reserva calcularReservaAPartirDeRuta(Reserva reserva,boolean confirmarReserva,boolean nuevaReserva) throws FechaSalidaAnteriorActualException,DataAccessException,DuplicatedParadaException,HoraSalidaSinAntelacionException {
		
		//Si confirmarReserva= false solo se calcularán los campos estrictamente necesarios para mostrar la reserva en el formulario
		
		if(nuevaReserva) { //si el cliente calcula la reserva se comprueba la restricción de la fecha
			fechaSalidaAnteriorActual(reserva.getFechaSalida(), reserva.getHoraSalida()); //Comprobar fecha
			fechaSalidaSinAntelacion(reserva.getFechaSalida(), reserva.getHoraSalida()); //Comprobar antelación
		}

		Ruta nuevaRuta= trayectoService.calcularYAsignarTrayectos(reserva.getRuta());
		reserva.setRuta(nuevaRuta);
		reserva.setNumKmTotales(nuevaRuta.getNumKmTotales());
		Date fechaHoraLlegada=calcularFechaYHoraLlegada(reserva.getFechaSalida(), reserva.getHoraSalida(),nuevaRuta.getHorasEstimadasCliente());
		reserva.setFechaLlegada(fechaHoraLlegada);
		reserva.setHoraLlegada(fechaHoraLlegada);
		Tarifa tarifa= tarifaService.findTarifaActiva();
		Optional<Tarifa> tarifaCopia= tarifaService.findCopyOfTarifa(tarifa);
		if(tarifaCopia.isPresent()) { //Si existe una tarifa copia se utiliza
			tarifa=tarifaCopia.get();
		}else { //Si no existe una tarifa copia igual, se crea
			tarifa= tarifaService.nuevaTarifa(false, false, tarifa.getPorcentajeIvaRepercutido(), tarifa.getPrecioEsperaPorHora(), tarifa.getPrecioPorKm());
			tarifaService.save(tarifa);
		}
		reserva.setTarifa(tarifa);
		Double precioPorKm= tarifa.getPrecioPorKm();
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
	public Reserva guardarReservaEditada(Reserva reservaEditada,Reserva reservaBD) throws DuplicatedParadaException,FechaLlegadaAnteriorSalidaException{
		
		if(reservaEditada.getFechaLlegada().compareTo(reservaEditada.getFechaSalida())<0) { //Si la fecha de llegada es anterior a la de salida se lanza excepción
			throw new FechaLlegadaAnteriorSalidaException();
		}else if(reservaEditada.getHoraLlegada().compareTo(reservaEditada.getHoraSalida())<0) {
			throw new FechaLlegadaAnteriorSalidaException();
		}
		Ruta rutaConstruida= trayectoService.calcularYAsignarTrayectos(reservaEditada.getRuta());
		reservaEditada.setRuta(rutaConstruida);
		System.out.println("Guardar ruta editada: " + rutaConstruida);
		reservaEditada= asignarRutaExistenteOCrearla(reservaEditada);
		System.out.println(reservaEditada.getRuta());
		reservaEditada.setTarifa(reservaBD.getTarifa());
		BeanUtils.copyProperties(reservaEditada, reservaBD, "id");
		save(reservaBD);
		return reservaBD;
	}
	@Transactional 
	public Reserva calcularYConfirmarNuevaReserva(Reserva reserva,String username)throws FechaSalidaAnteriorActualException,DataAccessException,DuplicatedParadaException,HoraSalidaSinAntelacionException{
		
		Reserva reservaCalculada= calcularReservaAPartirDeRuta(reserva,true,true);
		Set<String> authorities= authoService.findAuthoritiesByUsername(username);
		//Si la reserva la solicita un taxista, el cliente ya viene desde el formulario construido
		
		//Si la reserva la solicita un cliente desde su cuenta, se le asocia a él.
		
		if (!(authorities.contains("admin") || authorities.contains("taxista"))) { 
			Cliente cliente= clienteService.findClienteByUsername(username);
			reservaCalculada.setCliente(cliente);
		}
		this.save(reservaCalculada);
		return reservaCalculada;
	}
	
	@Transactional
	public Double calcularIngresos(Date fecha1, Date fecha2) {
		Collection<Reserva> reservas = reservaRepo.findByEstadoReservaCompletadaOAceptada();
		Double ingresos = 0.0;
		for(Reserva r:reservas) {
			if(r.getFechaSalida().after(fecha1) && r.getFechaSalida().before(fecha2)) {
				ingresos = ingresos + r.getPrecioTotal();
			}
		}
		return ingresos;
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
	public void aceptarReserva(Reserva reserva,Automovil auto,Principal p) throws DataAccessException,ParadaYaAceptadaRechazadaException,AutomovilPlazasInsuficientesException,ExisteViajeEnEsteHorarioException {
	
		if(!reserva.getEstadoReserva().getName().equals("Solicitada")) {
			throw new ParadaYaAceptadaRechazadaException();
		}else {
			System.out.println("P GET NAME: " + p.getName());
			Trabajador trabajador= trabajadorService.findByUsername(p.getName());
			
			if(auto.getNumPlazas()-1<reserva.getPlazas_Ocupadas()){
				throw new AutomovilPlazasInsuficientesException();
			}else if(taxistaConViaje(trabajador.getId(),reserva)){
				throw new ExisteViajeEnEsteHorarioException();			
			}else {//Añadir con else if las comprobaciones de las RN-02 Automóvil en uso
				
				
				EstadoReserva estadoReserva=estadoService.findEstadoById(2).get(); //Estado 2= Aceptada
				reserva.setTrabajador(trabajador);
				reserva.setAutomovil(auto);
				reserva.setEstadoReserva(estadoReserva);
				save(reserva);
			}
		
		}
		
		
	}
	
	@Transactional
	public void delete(Reserva reserva) throws DataAccessException  {
			reservaRepo.delete(reserva);
	}

	@Transactional
	public void save(Reserva reserva)  {
		
		reservaRepo.save(reserva);
	}

	@Transactional
	public Iterable<Reserva> findReservasByUsername(String username) throws DataAccessException {
		 return reservaRepo.findReservasByUsername(username);
		
	}
	
//	@Transactional
//	public void cancelarReserva(Reserva reserva) throws DataAccessException, CancelacionViajeAntelacionException, ReservasSoliAceptException {
//		
//		Date today = new Date();
//		Date fechaSalida = reserva.getFechaSalida();
//		Date horaSalida = reserva.getHoraSalida();
//		fechaSalida.setHours(horaSalida.getHours());
//		fechaSalida.setMinutes(horaSalida.getMinutes());
//		if(reserva.getEstadoReserva().getName().equals("Solicitada")) {
//			EstadoReserva estadoReserva= estadoService.findEstadoById(3).get(); 
//			reserva.setEstadoReserva(estadoReserva);
//			save(reserva);
//		}else if(reserva.getEstadoReserva().getName().equals("Aceptada")) {
//			if(fechaSalida.compareTo(today) < fechaSalida.getHours() + 24) {
//				throw new CancelacionViajeAntelacionException();
//			}else{
//				EstadoReserva estadoReserva= estadoService.findEstadoById(3).get(); 
//				reserva.setEstadoReserva(estadoReserva);
//				save(reserva);	
//			}
//		}else {
//			throw new ReservasSoliAceptException();
//		}
//	}
	
	
	
	@Transactional
    public void cancelarReserva(Reserva reserva) throws DataAccessException, ReservaYaRechazada, CancelacionViajeAntelacionException {
	
		Date today = new Date();

		Date fechaSalida= new Date();
		fechaSalida.setDate(reserva.getFechaSalida().getDate());
		fechaSalida.setMonth(reserva.getFechaSalida().getMonth());
		fechaSalida.setYear(reserva.getFechaSalida().getYear());
		fechaSalida.setHours(reserva.getHoraSalida().getHours());
		fechaSalida.setMinutes(reserva.getHoraSalida().getMinutes());
		Date fechaAux = utilService.addFecha(fechaSalida, Calendar.HOUR_OF_DAY, -24); //REstamos 24 horas
        if(!(reserva.getEstadoReserva().getName().equals("Solicitada") || reserva.getEstadoReserva().getName().equals("Aceptada"))) {
        	log.error("El estado de la reserva tiene que ser 'Solicitada' o 'Aceptada' para poder cancelarla");
        	throw new ReservaYaRechazada();
        }else if(today.compareTo(fechaAux)>0) {
        	log.error("La reserva tiene una fecha de salida con un intervalo de tiempo menor a 24 horas desde la actualidad o es anterior a la fecha actual, por lo que no puede ser cancelada");
        	throw new CancelacionViajeAntelacionException();
        }else {
        	
            reserva.setEstadoReserva(estadoService.findEstadoById(3).get());
            reservaRepo.save(reserva);
        }

    }
	
	@Transactional(readOnly = true)
	public Reserva findResById(int id) throws DataAccessException {
		return reservaRepo.findResById(id);
	}
	

	
}
