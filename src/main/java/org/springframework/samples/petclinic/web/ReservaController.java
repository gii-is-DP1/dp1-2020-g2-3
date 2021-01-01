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
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
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
	private final ClienteService clienteService;
	private final EstadoReservaService estadoService;
	private final AuthoritiesService authoService;
	
	
	@Autowired
	public ReservaController(ReservaService reservaService,TrayectoService trayectoService,RutaService rutaService,ClienteService clienteService,EstadoReservaService estadoService,AuthoritiesService authoService) {
		this.reservaService=reservaService;
		this.trayectoService=trayectoService;
		this.rutaService=rutaService;
		this.clienteService=clienteService;
		this.estadoService=estadoService;
		this.authoService=authoService;
	}
	
	@GetMapping("/new")
	public String newReserva(ModelMap modelMap) {
		Reserva nuevaReserva= new Reserva();
		Date today= new Date();
		nuevaReserva.setFechaSalida(today);
		//Mostramos una fecha de salida predeterminada del día de hoy, con 5 minutos más a los actuales en la hora de salida
		// ya que no se pueden realizar reservas con una fecha anterior al instante actuall
		 Calendar calendar = Calendar.getInstance();
	      calendar.setTime(today); 
	      calendar.add(Calendar.MINUTE, 5); 
	      today= calendar.getTime(); 
	      /////////////
		nuevaReserva.setHoraSalida(today);
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
				reservaService.fechaSalidaAnteriorActual(reserva.getFechaSalida(), reserva.getHoraSalida()); //Comprobar fecha 
				Ruta nuevaRuta= rutaService.calcularYAsignarTrayectos(reserva.getRuta());
				if(!reserva.getRuta().getOrigenCliente().equals("Zahinos")) {
					modelMap.put("trayectoIdaTaxista", nuevaRuta.getTrayectos().get(0));
				}
				if(!reserva.getRuta().getDestinoCliente().equals("Zahinos")) {
					modelMap.put("trayectoVueltaTaxista", nuevaRuta.getTrayectos().get(nuevaRuta.getTrayectos().size()-1));
				}
				System.out.println("horas estimadas cliente: " + nuevaRuta.getHorasEstimadasCliente());
				//Esta ruta es solo para calcular el precio, los km totales, fecha estimada de llegada en función
				// de los trayectos que se hayan calculado... 
				
				//La nueva ruta se asignará a la reserva UNA VEZ SE CONFIRME para meterla en la BD
				
				
				reserva.setNumKmTotales(nuevaRuta.getNumKmTotales());
				Date fechaHoraLlegada=reservaService.calcularFechaYHoraLlegada(reserva.getFechaSalida(), reserva.getHoraSalida(),nuevaRuta.getHorasEstimadasCliente());
				reserva.setFechaLlegada(fechaHoraLlegada);
				reserva.setHoraLlegada(fechaHoraLlegada);
				Double precioPorKm=0.41; //Esto habría que cambiarlo cuadno estén implementadas las tarifas
				reserva.setPrecioTotal(reservaService.calcularPrecio(nuevaRuta.getNumKmTotales(), precioPorKm));
				modelMap.put("reserva", reserva);
				modelMap.put("horasRutaCliente", rutaService.calcularHorasRutaCliente(nuevaRuta));
				modelMap.put("minutosRutaCliente", rutaService.calcularMinutosRutaCliente(nuevaRuta));
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
			if (authorities.contains("admin") || authorities.contains("taxista")) { //Se obtiene el cliente como parámetro, es el trabajador el que realiza la reserva
				//Un taxista de la empresa no puede solicitar un viaje
				modelMap.put("reserva",reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				modelMap.put("finBucle", numCiudadesIntermedias-1);
				modelMap.put("error", "No puedes solicitar una reserva siendo un trabajador de la empresa");
				return "reservas/newReservaForm";	
				
			}else { //Es un cliente el que realiza la reserva y su entidad se obtiene  desde la sesión iniciada
				
				Cliente cliente= clienteService.findClienteByUsername(p.getName());
				reserva.setCliente(cliente);
				Ruta nuevaRuta= rutaService.calcularYAsignarTrayectos(reserva.getRuta());
				Optional<Ruta> rutaExistente= rutaService.findRutaByRuta(nuevaRuta);
				if(rutaExistente.isPresent()) {
					System.out.println("Ya existe una ruta similar! Se asignará desde la BD");
					nuevaRuta= rutaExistente.get();
					System.out.println("Trayectos de la ruta existente: " + rutaExistente.get().getTrayectos());
					
				}else {
					
					System.out.println("No existe la ruta, se creará una nueva");
				}
			
				reserva.setRuta(nuevaRuta);
				
				reserva.setNumKmTotales(nuevaRuta.getNumKmTotales());
				Date fechaHoraLlegada=reservaService.calcularFechaYHoraLlegada(reserva.getFechaSalida(), reserva.getHoraSalida(),nuevaRuta.getHorasEstimadasCliente());
				reserva.setFechaLlegada(fechaHoraLlegada);
				reserva.setHoraLlegada(fechaHoraLlegada);
				Double precioPorKm=0.41; //Esto habría que cambiarlo cuadno estén implementadas las tarifas
				Double precioTotal=reservaService.calcularPrecio(nuevaRuta.getNumKmTotales(), precioPorKm);
				reserva.setPrecioTotal(precioTotal);
				reserva.setHorasEspera(0.0);
				reserva.setEstadoReserva(estadoService.findEstadoById(1).get());
				reserva.setPrecioDistancia(precioTotal); //Cuando se crea el viaje las horas de espera son 0 y el precio total es el de los km recorridos
				reserva.setPrecioEspera(0.0);
				
				Double precioIvaRedondeado= (double)Math.round((0.1*precioTotal)*100)/100; //Esto se cambiará cuando estén implementadas las tarifas
				reserva.setPrecioIVA(precioIvaRedondeado);
				Double baseImponibleRedondeada= (double)Math.round((0.9*precioTotal)*100)/100;
				reserva.setBaseImponible(baseImponibleRedondeada); //Esto se cambiará cunado estén implementadas las tarifas
				rutaService.save(reserva.getRuta());
				reservaService.save(reserva);
				
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

		}
	}
}
