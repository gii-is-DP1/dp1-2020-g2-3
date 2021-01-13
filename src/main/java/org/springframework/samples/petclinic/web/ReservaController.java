package org.springframework.samples.petclinic.web;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.EstadoReservaService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.RutaService;
import org.springframework.samples.petclinic.service.TrayectoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
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
	
	
	@Autowired
	public ReservaController(ReservaService reservaService,TrayectoService trayectoService,RutaService rutaService,AuthoritiesService authoService,EstadoReservaService estadoReservaService) {
		this.reservaService=reservaService;
		this.trayectoService=trayectoService;
		this.rutaService=rutaService;
		this.authoService=authoService;
		this.estadoReservaService=estadoReservaService;
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
	
	
	@PostMapping("/redirigir")
	public String redirigir(@Valid Reserva reserva,BindingResult binding,ModelMap modelMap,@RequestParam("action") String action,@RequestParam("numCiudadesIntermedias") Integer numCiudadesIntermedias,Principal p) {
		//necesitamos este método porque tenemos varios botones para un mismo formulario
		//Se decidirá entre añadir una nueva parada al jsp de la reserva,calcular su precio, confirmar la reserva o volver al formulario inicial
		System.out.println("Trayectos en redirigir:" +  reserva.getRuta().getTrayectos());
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
				
				return mostrarPrecioReserva(reserva,modelMap,paradas,numCiudadesIntermedias,p);
			}
			
		}else if(action.equals("addParada")) {
			//En esta parte no queremos mostrar los erroes del binding, porque el cliente todavía está editando el formulario
			//Por ello lo ponemos null
			modelMap.put("org.springframework.validation.BindingResult.reserva", null);
			return addParada(reserva,modelMap,numCiudadesIntermedias,paradas);
		}else if (action.equals("confirmarReserva")) {
			Set<String> authorities= authoService.findAuthoritiesByUsername(p.getName());
		//Comprobamos otra vez el binding por si se ha intentado modificar manualmente algún atributo desde el jsp
			if(binding.hasErrors()) {
				modelMap.put("reserva",reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle", numCiudadesIntermedias-1);
				return "reservas/newReservaForm";
			}else if (authorities.contains("admin") || authorities.contains("taxista")){
				//Un taxista de la empresa no puede solicitar un viaje
					modelMap.put("reserva",reserva);
					modelMap.put("paradas", paradas);
					modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
					modelMap.put("finBucle", numCiudadesIntermedias-1);
					modelMap.put("error", "No puedes solicitar una reserva siendo un trabajador de la empresa");
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
	
	
	public String addParada(Reserva reserva,ModelMap modelMap,@PathVariable("numCiudadesIntermedias") Integer numCiudadesIntermedias,Iterable<String> paradas) {
		
		
		modelMap.put("reserva",reserva);
		modelMap.put("paradas", paradas);
		modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias+1);
		modelMap.put("finBucle", numCiudadesIntermedias);
		return "reservas/newReservaForm";
	}
	
	//No tiene url, viene desde /redirigir porque el mismo formulario tiene varios botones, y "redirigir" llama a un sitio u otro 
	// dependiendo del botón pulsado
	
	public String mostrarPrecioReserva(Reserva reserva, ModelMap modelMap,Iterable<String> paradas,Integer numCiudadesIntermedias,Principal p) {
		
		
			try {
				Reserva reservaCalculada= reservaService.calcularNuevaReserva(reserva, false); //Reserva con precio,horaEstimada de llegada, km totales...
				if(!reservaCalculada.getRuta().getOrigenCliente().equals("Zahinos")) {
					modelMap.put("trayectoIdaTaxista", reservaCalculada.getRuta().getTrayectos().get(0));
				}
				if(!reservaCalculada.getRuta().getDestinoCliente().equals("Zahinos")) {
					modelMap.put("trayectoVueltaTaxista", reservaCalculada.getRuta().getTrayectos().get(reservaCalculada.getRuta().getTrayectos().size()-1));
				}
				
				List<Trayecto> trayectosIntermedios= reserva.getRuta().getTrayectos(); //Trayectos intermedios, que serán los que tenga la ruta antigua que vino desde el formulario
				modelMap.put("reserva", reservaCalculada);
				modelMap.put("trayectosIntermedios", trayectosIntermedios);
				modelMap.put("horasRutaCliente", rutaService.calcularHorasRutaCliente(reservaCalculada.getRuta()));
				modelMap.put("minutosRutaCliente", rutaService.calcularMinutosRutaCliente(reservaCalculada.getRuta()));
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle",numCiudadesIntermedias-1);				
				return "reservas/precioReserva";
			}catch(DuplicatedParadaException e){
			
				modelMap.put("reserva", reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle", numCiudadesIntermedias-1);
				modelMap.addAttribute("error", "El origen y destino deben ser diferentes."
						+ " (Dos paradas consecutivas tampoco pueden ser iguales)");

				return "reservas/newReservaForm";	

			} catch(FechaSalidaAnteriorActualException e2) {
			
				modelMap.put("reserva", reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle", numCiudadesIntermedias-1);
				modelMap.addAttribute("error", "La fecha y hora de salida no puede ser anterior al instante actual");

				return "reservas/newReservaForm";	
			}
	}
	
	
	public String confirmarNuevaReserva(Reserva reserva,ModelMap modelMap,Iterable<String> paradas,Integer numCiudadesIntermedias,Principal p) {
		try {
			Set<String> authorities= authoService.findAuthoritiesByUsername(p.getName());
			if (authorities.contains("admin") || authorities.contains("taxista")) { //Un taxista de la empresa no puede solicitar un viaje
				modelMap.put("reserva",reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle", numCiudadesIntermedias-1);
				modelMap.put("error", "No puedes solicitar una reserva siendo un trabajador de la empresa");
				return "reservas/newReservaForm";	
				
			}else { //Es un cliente el que realiza la reserva y su entidad se obtiene  desde la sesión iniciada
				reservaService.calcularYConfirmarReserva(reserva,p.getName());
				modelMap.addAttribute("message", "¡Reserva solicitada con éxito!");
				return newReserva(modelMap);
				
			}

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
	public String editReserva(@PathVariable("reservaId") int reservaId,ModelMap modelMap) {
		
		Optional<Reserva> reservaOptional=reservaService.findReservaById(reservaId);
		if(reservaOptional.isPresent()) {
			Reserva reserva=reservaOptional.get();
			modelMap.addAttribute("reserva",reserva);
			if(!reserva.getRuta().getOrigenCliente().equals("Zahinos")) {
				System.out.println("El origen no es zahinos");
				modelMap.put("trayectoIdaTaxista", reserva.getRuta().getTrayectos().get(0));
			}
			if(!reserva.getRuta().getDestinoCliente().equals("Zahinos")) {
				modelMap.put("trayectoVueltaTaxista", reserva.getRuta().getTrayectos().get(reserva.getRuta().getTrayectos().size()-1));
			}
			
		
			//Obtenemos los trayectos intermedios empezando por el que tenga la primera parada como origen
			List<Trayecto> trayectosIntermedios= rutaService.obtenerTrayectosIntermedios(reserva.getRuta());
			Iterable<EstadoReserva> estadosReserva= estadoReservaService.findAll();
			modelMap.put("estadosReserva", estadosReserva);
			Integer numCiudadesIntermedias= trayectosIntermedios.size();
			modelMap.put("trayectosIntermedios", trayectosIntermedios);
			modelMap.put("horasRutaCliente", rutaService.calcularHorasRutaCliente(reserva.getRuta()));
			modelMap.put("minutosRutaCliente", rutaService.calcularMinutosRutaCliente(reserva.getRuta()));
			modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
			modelMap.put("finBucle",numCiudadesIntermedias-1);
			
			return "reservas/editReservaForm";
		}else {
			modelMap.addAttribute("message","No se ha encontrado la reserva a editar");
			return listadoReservas(modelMap);
		}
	} 
	
}
