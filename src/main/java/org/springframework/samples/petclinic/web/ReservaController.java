package org.springframework.samples.petclinic.web;
import java.security.Principal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Servicio;
import org.springframework.samples.petclinic.model.Taller;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.AutomovilService;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.EstadoReservaService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.RutaService;
import org.springframework.samples.petclinic.service.TarifaService;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.samples.petclinic.service.TrayectoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.AutomovilPlazasInsuficientesException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.FechaLlegadaAnteriorSalidaException;
import org.springframework.samples.petclinic.service.exceptions.EstadoReservaFacturaException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
import org.springframework.samples.petclinic.service.exceptions.ParadaYaAceptadaRechazadaException;
import org.springframework.samples.petclinic.service.exceptions.HoraSalidaSinAntelacionException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

	private final  ReservaService reservaService;
	private final  TrayectoService trayectoService;
	private final RutaService rutaService;
	private final AuthoritiesService authoService;
	private final EstadoReservaService estadoReservaService;
	private final ClienteService clienteService;
	private final TarifaService tarifaService;
	private final AutomovilService autoService;
	private final TrabajadorService trabajadorService;
	
	
	@Autowired
	public ReservaController(ReservaService reservaService,TrayectoService trayectoService,RutaService rutaService,AuthoritiesService authoService,EstadoReservaService estadoReservaService,ClienteService clienteService, TarifaService tarifaService,AutomovilService autoService,TrabajadorService trabajadorService) {
		this.reservaService=reservaService;
		this.trayectoService=trayectoService;
		this.rutaService=rutaService;
		this.authoService=authoService;
		this.estadoReservaService=estadoReservaService;
		this.clienteService=clienteService;
		this.tarifaService=tarifaService;
		this.autoService=autoService;
		this.trabajadorService=trabajadorService;
	}
	
	@GetMapping(value = "/reservasList")
	public String listadoReservas(ModelMap modelMap) {
		String vista="reservas/reservasList";
		Iterable<Reserva> reservas= reservaService.findAll();
		modelMap.addAttribute("reservas", reservas);
		return vista;
	}
	
		
	
	@GetMapping("/new")
	public String newReserva(ModelMap modelMap) {
		Reserva nuevaReserva= new Reserva();
		Date today= new Date();
		
		//Mostramos una fecha de salida predeterminada del día de hoy, con 45 minutos más a los actuales en la hora de salida
		// ya que no se pueden realizar reservas con una antelación menor a 40 minutos de la fecha de salida
		
		today=reservaService.addFecha(today, Calendar.MINUTE, 45);
		nuevaReserva.setHoraSalida(today);
		nuevaReserva.setFechaSalida(today);
		modelMap.addAttribute("reserva",nuevaReserva);
		Iterable<String> paradas= trayectoService.findDistinctParadas();
		modelMap.addAttribute("paradas", paradas);
		modelMap.addAttribute("numCiudadesIntermedias", 0);
		modelMap.addAttribute("finBucle", 0);
		return "reservas/newReservaForm";
	}
	
	
	@PostMapping("/redirigirNewReservaForm")
	public String redirigirNewReservaForm(@Valid Reserva reserva,BindingResult binding,ModelMap modelMap,@RequestParam("action") String action,@RequestParam("numCiudadesIntermedias") Integer numCiudadesIntermedias,Principal p) {
		
		//necesitamos este método porque tenemos varios botones para un mismo formulario
		//Se decidirá entre añadir una nueva parada o calcular el precio, fecha/hora estimada de llegada de la reserva
		
		Iterable<String> paradas= trayectoService.findDistinctParadas();	
		if(action.equals("continuar")) {
			//El usuario quiere ver el precio y fecha estimada de llegada del viaje
			//Comprobamos el binding antes de pasar de página
			if(binding.hasErrors()) {
				modelMap.put("reserva",reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle", numCiudadesIntermedias-1);
				return "reservas/newReservaForm";
			}else {
				return calcularMostrarReserva(reserva, modelMap,paradas,numCiudadesIntermedias,"reservas/newReservaForm","reservas/precioReserva",true,false,p);
				
			}
			
		}else if(action.equals("addParada")) {
			//En esta parte no queremos mostrar los erroes del binding, porque el cliente todavía está editando el formulario
			//Por ello lo ponemos null
			modelMap.put("org.springframework.validation.BindingResult.reserva", null);
			return addParada(reserva,modelMap,numCiudadesIntermedias,paradas,"reservas/newReservaForm");
		}else {
			return "exception";
		}
	
	}
	
	
	@PostMapping("/redirigirPrecioReserva")
	public String redirigirPrecioReserva(@Valid Reserva reserva,BindingResult binding,ModelMap modelMap,@RequestParam("action") String action,@RequestParam("numCiudadesIntermedias") Integer numCiudadesIntermedias,Principal p) {
		
		//necesitamos este método porque tenemos varios botones para un mismo formulario
		// se decidirá entre volver al anterior formulario o solicitar la reserva
		
		Iterable<String> paradas= trayectoService.findDistinctParadas(); //se usará si hay errores de binding o si se pulsa en el botón "atrás"
		if (action.equals("confirmarReserva")) {
			
		//Comprobamos otra vez el binding por si se ha intentado modificar manualmente algún atributo desde el jsp
			if(binding.hasErrors()) {
				modelMap.put("reserva",reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle", numCiudadesIntermedias-1);
				return "reservas/newReservaForm";
			}else {
				
				return confirmarNuevaReserva(reserva,modelMap,paradas,numCiudadesIntermedias,p);
			}
		}else if(action.equals("atras")) {
			
			modelMap.put("reserva",reserva);
			modelMap.put("paradas", paradas);
			modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
			modelMap.put("finBucle", numCiudadesIntermedias-1);
			return "reservas/newReservaForm";
			
		}else {
			
			return "exception";
		}
	
	}
	
	
	public String addParada(Reserva reserva,ModelMap modelMap,@PathVariable("numCiudadesIntermedias") Integer numCiudadesIntermedias,Iterable<String> paradas,String formularioExito) {
		
	
		modelMap.put("reserva",reserva);
		modelMap.put("paradas", paradas);
		modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias+1);
		modelMap.put("finBucle", numCiudadesIntermedias);
		return formularioExito;
	}
	
	//No tiene url, viene desde /redirigir porque el mismo formulario tiene varios botones, y "redirigir" llama a un sitio u otro 
	// dependiendo del botón pulsado
	
	public String calcularMostrarReserva(Reserva reserva, ModelMap modelMap, Iterable<String> paradas,Integer numCiudadesIntermedias,String formularioError, String formularioExito,boolean nuevaReserva,boolean confirmarReserva,Principal p) {
		
		try {
			
			List<Trayecto> trayectosIntermedios= reserva.getRuta().getTrayectos(); //Trayectos intermedios, que serán los que tenga la ruta antigua que vino desde el formulario
		 
			if(trayectosIntermedios==null) {
				trayectosIntermedios= new ArrayList<Trayecto>();
			}
			
		
		    Reserva reservaCalculada= reservaService.calcularReservaAPartirDeRuta(reserva, confirmarReserva,nuevaReserva); //Reserva con precio,horaEstimada de llegada, km totales...
		   System.out.println("Reserva recalculada: " +  reservaCalculada.getNumKmTotales());
		    int horasRutaCliente=rutaService.calcularHorasRutaCliente(reserva.getRuta());
		    int minutosRutaCliente= rutaService.calcularMinutosRutaCliente(reserva.getRuta());
			return mostrarReservaCalculada(reservaCalculada,modelMap,formularioExito,trayectosIntermedios,horasRutaCliente,minutosRutaCliente,p);
			
		}catch(DuplicatedParadaException e){
		
		modelMap.put("reserva", reserva);
		modelMap.put("paradas", paradas);
		modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
		modelMap.put("finBucle", numCiudadesIntermedias-1);
		modelMap.addAttribute("error", "El origen y destino deben ser diferentes."
				+ " (Dos paradas consecutivas tampoco pueden ser iguales)");

		return formularioError;

		} catch(FechaSalidaAnteriorActualException e2) {
	
		modelMap.put("reserva", reserva);
		modelMap.put("paradas", paradas);
		modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
		modelMap.put("finBucle", numCiudadesIntermedias-1);
		modelMap.addAttribute("error", "La fecha y hora de salida no puede ser anterior al instante actual");

		return formularioError;	
		} catch(HoraSalidaSinAntelacionException e3) {
				modelMap.put("reserva", reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle", numCiudadesIntermedias-1);
				modelMap.addAttribute("error", "La fecha de salida debe realizarse con al menos 40 minutos de antelación");

				return formularioError;
			}
	}
	
	public String mostrarReservaCalculada(Reserva reserva, ModelMap modelMap,String formularioExito,List<Trayecto> trayectosIntermedios,int horasRutaCliente,int minutosRutaCliente,Principal p) {
				
		Set<String> authorities= authoService.findAuthoritiesByUsername(p.getName());
			
				if(formularioExito.equals("reservas/editReservaForm")) { //Si hemos solicitado EDITAR la ruta, aparecerán más campos en el formulario
					Iterable<EstadoReserva> estadosReserva= estadoReservaService.findAll();
					modelMap.put("estadosReserva", estadosReserva);
					
					Iterable<Cliente> clientes= clienteService.findAll();
					modelMap.put("clientes", clientes);
					
					Iterable<Trabajador> trabajadores= trabajadorService.findAll();
					modelMap.put("trabajadores", trabajadores);
					
					Iterable<Automovil> automoviles= autoService.findAll();
					modelMap.put("automoviles",automoviles);
					
				
				}else if(authorities.contains("admin") || authorities.contains("taxista")) { //Un trabajador está solicitando una NUEVA RESERVA
					//debe aparecer el campo "cliente" en el formulario
					Iterable<Cliente> clientes= clienteService.findAll();
					modelMap.put("clientes", clientes);
				}
				
				modelMap.put("reserva", reserva);
				modelMap.put("trayectosIntermedios", trayectosIntermedios);
				modelMap.put("horasRutaCliente", horasRutaCliente);
				modelMap.put("minutosRutaCliente", minutosRutaCliente);
				Integer numCiudadesIntermedias= trayectosIntermedios.size();
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle",numCiudadesIntermedias-1);				
				return formularioExito;
	}
	
	//Cambiar
	public String confirmarNuevaReserva(Reserva reserva,ModelMap modelMap,Iterable<String> paradas,Integer numCiudadesIntermedias,Principal p) {
		try {
				reservaService.calcularYConfirmarNuevaReserva(reserva,p.getName());
				modelMap.addAttribute("message", "¡Reserva solicitada con éxito!");
				return newReserva(modelMap);

		}catch(DuplicatedParadaException e) {
			modelMap.put("reserva", reserva);
			modelMap.put("paradas", paradas);
			modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
			modelMap.addAttribute("error", "El origen y destino deben ser diferentes."
					+ " (Dos paradas consecutivas tampoco pueden ser iguales)");

			return "reservas/newReservaForm";	

		}catch(FechaSalidaAnteriorActualException e2) {
			
			modelMap.put("reserva", reserva);
			modelMap.put("paradas", paradas);
			modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
			modelMap.put("finBucle", numCiudadesIntermedias-1);
			modelMap.addAttribute("error", "La fecha y hora de salida no puede ser anterior al instante actual");

			return "reservas/newReservaForm";	

		} catch(HoraSalidaSinAntelacionException e3) {
			modelMap.put("reserva", reserva);
			modelMap.put("paradas", paradas);
			modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
			modelMap.put("finBucle", numCiudadesIntermedias-1);
			modelMap.addAttribute("error", "La reserva debe realizarse con un mínimo de 1 hora de antelación");

			return "reservas/newReservaForm";	
		}
	}
	
	@GetMapping(value="/delete/{reservaId}")
	public String borrarReserva(@PathVariable("reservaId") int reservaId,ModelMap modelMap) {
		Optional<Reserva> reserva=reservaService.findReservaById(reservaId);
		if (reserva.isPresent()) {
			reservaService.delete(reserva.get()); 
			modelMap.addAttribute("message", "Reserva eliminada correctamente");
		}else {
			
			modelMap.addAttribute("message", "Reserva no encontrada!");
		}
		return listadoReservas(modelMap);
	}
	
	@GetMapping(value= "/edit/{reservaId}")
	public String editReserva(@PathVariable("reservaId") int reservaId,ModelMap modelMap,Principal p) {
		
		Optional<Reserva> reservaOptional=reservaService.findReservaById(reservaId);
		if(reservaOptional.isPresent()) {
			Reserva reserva= reservaOptional.get();
			List<Trayecto> trayectosIntermedios= rutaService.obtenerTrayectosIntermedios(reserva.getRuta()); //La reserva viene construida totalmente desde la base de datos, por ello tenemos que detectar cuáles de sus trayectos son los intermedios
			int horasRutaCliente=rutaService.calcularHorasRutaCliente(reserva.getRuta());
		    int minutosRutaCliente= rutaService.calcularMinutosRutaCliente(reserva.getRuta());
			return mostrarReservaCalculada(reserva, modelMap,"reservas/editReservaForm",trayectosIntermedios,horasRutaCliente,minutosRutaCliente,p);
		}else {
			modelMap.addAttribute("message","No se ha encontrado la reserva a editar");
			return listadoReservas(modelMap);
		}
	} 
	
	
	@PostMapping("/redirigirEditReservaForm")
	public String redirigirEditReservaForm(@Valid Reserva reserva,BindingResult binding,ModelMap modelMap,@RequestParam("action") String action,@RequestParam("numCiudadesIntermedias") Integer numCiudadesIntermedias,@RequestParam("horasRutaCliente") int horasRutaCliente,@RequestParam("minutosRutaCliente") int minutosRutaCliente,Principal p) {
		//necesitamos este método porque tenemos varios botones para un mismo formulario
		//Se decidirá entre añadir una nueva parada al jsp de la reserva o recalcular la reserva en base  a la ruta editada
		
		if(binding.hasErrors()) {
			
			List<Trayecto> trayectosIntermedios= reserva.getRuta().getTrayectos(); //Si la reserva viene de un formulario sus trayectos siempre serán los trayectos intermedios
			 if(trayectosIntermedios==null) {
				 trayectosIntermedios= new ArrayList<Trayecto>();
			 }
			return mostrarReservaCalculada(reserva,modelMap,"reservas/editReservaForm",trayectosIntermedios,horasRutaCliente,minutosRutaCliente,p);
			
		}else {
			if(action.equals("editarRuta")) {	
				
				
				Iterable<String> paradas= trayectoService.findDistinctParadas();
				modelMap.put("paradas", paradas);
				modelMap.put("reserva", reserva);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle",numCiudadesIntermedias-1);
				
				return "reservas/editRutaForm";
			
			}else if (action.equals("guardarReserva")) {
				Optional<Reserva> reservaBD= reservaService.findReservaById(reserva.getId());
				if(reservaBD.isPresent()) {
					try {
						reservaService.guardarReservaEditada(reserva, reservaBD.get());
						return listadoReservas(modelMap);
					}catch(DuplicatedParadaException e) {
						Iterable<String> paradas= trayectoService.findDistinctParadas();
						modelMap.put("paradas", paradas);
						modelMap.put("reserva", reserva);
						modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
						modelMap.put("finBucle",numCiudadesIntermedias-1);
						modelMap.addAttribute("error", "El origen y destino deben ser diferentes."
								+ " (Dos paradas consecutivas tampoco pueden ser iguales)");
						return "reservas/editRutaForm";
					}catch(FechaLlegadaAnteriorSalidaException e2) {

						List<Trayecto> trayectosIntermedios= reserva.getRuta().getTrayectos(); //Si la reserva viene de un formulario sus trayectos siempre serán los trayectos intermedios
						 if(trayectosIntermedios==null) {
							 trayectosIntermedios= new ArrayList<Trayecto>();
						 }
							modelMap.addAttribute("error", "La fecha de llegada no puede ser anterior a la de salida");

						return mostrarReservaCalculada(reserva,modelMap,"reservas/editReservaForm",trayectosIntermedios,horasRutaCliente,minutosRutaCliente,p);
						
					}
					
					
					
				}else {
					return "exception";
				}
				
			
			}else {
			
			return "exception";
			}
			
		}	
		
	
	}

	@PostMapping("/redirigirEditRutaForm")
	public String redirigirEditRutaForm(@Valid Reserva reserva,BindingResult binding,ModelMap modelMap,@RequestParam("action") String action,@RequestParam("numCiudadesIntermedias") Integer numCiudadesIntermedias,Principal p) {
		
		
		Iterable<String> paradas= trayectoService.findDistinctParadas();	
		if(action.equals("recalcularReserva")) {
			//Solo queremos comprobar el binding aquí
			if(binding.hasErrors()) {
				System.out.println("Errores de binding");
				modelMap.put("reserva",reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle", numCiudadesIntermedias-1);
				return "reservas/editRutaForm";
			}else {
				
				return calcularMostrarReserva(reserva, modelMap,paradas,numCiudadesIntermedias,"reservas/editRutaForm","reservas/editReservaForm",false,false,p);
			}
			
		}else if(action.equals("addParada")) {
			//En esta parte no queremos mostrar los erroes del binding, porque el cliente todavía está editando el formulario
			//Por ello lo ponemos null
			modelMap.put("org.springframework.validation.BindingResult.reserva", null);
			return addParada(reserva,modelMap,numCiudadesIntermedias,paradas,"reservas/editRutaForm"); //Este método cambiarlo para que no redirija al formulario de crear reserva
		}else {
			return "exception";
		}
	}
	
	
	@GetMapping(value = "/peticionesReservas")
	public String listadoPeticionesReservas(ModelMap modelMap) {
		String vista="reservas/peticionesReservas";
		Iterable<Reserva> reservas= reservaService.findPeticionesReserva();
		modelMap.addAttribute("reservas", reservas);
		return vista;
	}
	
	

	@GetMapping(value= "/aceptar/{reservaId}")
	public String aceptarReserva(@PathVariable("reservaId") int reservaId,ModelMap modelMap) {
		
		Optional<Reserva> reservaOptional= reservaService.findReservaById(reservaId);
		if(!reservaOptional.isPresent()) {
			modelMap.addAttribute("error", "Reserva no encontrada");
			return listadoPeticionesReservas(modelMap);
		}else {
			Iterable<Automovil> automoviles= autoService.findAll();
			modelMap.addAttribute("automoviles",automoviles);
			return "reservas/selectAutomovil";
			}
		}
	
	
	@PostMapping(value= "/aceptar/{reservaId}")
	public String aceptarReserva(@PathVariable("reservaId") int reservaId,ModelMap modelMap,@RequestParam("autoId") int autoId, Principal p) {
	
		Optional<Reserva> reservaOptional= reservaService.findReservaById(reservaId);
		if(!reservaOptional.isPresent()) {
			modelMap.addAttribute("error", "Reserva no encontrada");
			return listadoPeticionesReservas(modelMap);
		}else {
			Optional<Automovil> automovil= autoService.findAutomovilById(autoId);
			if(!automovil.isPresent()) {
				modelMap.addAttribute("error", "El automóvil que se ha intentado asignar no existe");
				return listadoPeticionesReservas(modelMap);
			}else {
				
				try {
					reservaService.aceptarReserva(reservaOptional.get(),automovil.get(),p);
					modelMap.addAttribute("message", "Reserva aceptada correctamente");
				}catch(ParadaYaAceptadaRechazadaException e) {
					modelMap.addAttribute("error", "La reserva que se intenta aceptar ya ha sido aceptada/rechazada anteriormente");
				}catch(AutomovilPlazasInsuficientesException e) {
					modelMap.addAttribute("error", "El automóvil que ha seleccionado no tiene suficientes plazas para realizar la reserva");
				}
				return listadoPeticionesReservas(modelMap);
			}
			
		}
		}
		
	
	@GetMapping(value= "/rechazar/{reservaId}")
	public String rechazarReserva(@PathVariable("reservaId") int reservaId,ModelMap modelMap) {
		
		Optional<Reserva> reservaOptional= reservaService.findReservaById(reservaId);
		if(!reservaOptional.isPresent()) {
			modelMap.addAttribute("error", "Reserva no encontrada");
			return listadoPeticionesReservas(modelMap);
		}else {
			try {
				reservaService.rechazarReserva(reservaOptional.get());
				modelMap.addAttribute("message", "Reserva rechazada correctamente");
			}catch(ParadaYaAceptadaRechazadaException e) {
				modelMap.addAttribute("error", "La reserva que se intenta rechazar ya ha sido aceptada/rechazada anteriormente");
			}
			return listadoPeticionesReservas(modelMap);
		}
	}


	@GetMapping("/reservaFactura/{reservaId}")
	public String reservaFactura(@PathVariable("reservaId") int reservaId,ModelMap modelMap) throws DataAccessException, EstadoReservaFacturaException {
		try {
			Optional<Reserva> reserva=reservaService.findFacturaReservaById(reservaId);
		if(reserva.isPresent()) {
			Map<String,Double> factura = reservaService.calcularFactura(reservaId);
      modelMap.addAttribute("factura",factura);
			modelMap.addAttribute("reserva",reserva.get());
			return "reservas/reservaFactura";
		}else {
			modelMap.addAttribute("message","No se ha encontrado la factura");
			return listadoReservas(modelMap);
		}
	
		}catch(EstadoReservaFacturaException e){
			modelMap.addAttribute("error","Estado de reserva no completado");
			return listadoReservas(modelMap);
		}
	
		
	}
}
