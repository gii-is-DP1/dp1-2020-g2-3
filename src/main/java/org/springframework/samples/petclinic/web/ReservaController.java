package org.springframework.samples.petclinic.web;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.RutaService;
import org.springframework.samples.petclinic.service.TrayectoService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
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
	
	@Autowired
	public ReservaController(ReservaService reservaService,TrayectoService trayectoService,RutaService rutaService,ClienteService clienteService) {
		this.reservaService=reservaService;
		this.trayectoService=trayectoService;
		this.rutaService=rutaService;
		this.clienteService=clienteService;
	}
	
	@GetMapping("/new")
	public String newReserva(ModelMap modelMap) {
		Reserva nuevaReserva= new Reserva();
		Date today= new Date();
		nuevaReserva.setFechaSalida(today);
		nuevaReserva.setHoraSalida(today);
		modelMap.addAttribute("reserva",nuevaReserva);
		Iterable<String> paradas= trayectoService.findDistinctParadas();
		modelMap.addAttribute("paradas", paradas);
		modelMap.addAttribute("numCiudadesIntermedias", 0);
		modelMap.addAttribute("finBucle", 0);
		return "reservas/newReservaForm";
	}
	
	
	@PostMapping("/redirigir")
	public String redirigir(@Valid Reserva reserva,BindingResult binding,ModelMap modelMap,@RequestParam("action") String action,@RequestParam("numCiudadesIntermedias") Integer numCiudadesIntermedias) {
	
		//Se decidirá entre añadir una nueva parada al jsp de la reserva,calcular su precio, confirmar la reserva o volver al formulario
	
		Iterable<String> paradas= trayectoService.findDistinctParadas();	
		if(action.equals("continuar")) {
			//El usuario ha completado el formulario, ahora sí validamos los datos y mostramos los errores
			if(binding.hasErrors()) {
				modelMap.put("reserva",reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				return "reservas/newReservaForm";
			}else {
				
				return mostrarPrecioReserva(reserva,modelMap,paradas,numCiudadesIntermedias);
			}
			
		}else if(action.equals("addParada")) {
			//En esta parte no queremos mostrar los erroes del binding, porque el cliente todavía está editando el formulario
			//Por ello quitamos el binding
			modelMap.put("org.springframework.validation.BindingResult.reserva", null);
			return addParada(reserva,modelMap,numCiudadesIntermedias,paradas);
		}else if (action.equals("confirmarReserva")) {
			return "exception";
			
		}else if(action.equals("atras")) {
			return "exception";
			
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
	
	//No tiene url, viene desde /redirigir porque el mismo formulario tiene 2 botones, y "redirigir" llama a un sitio u otro 
	// dependiendo del botón pulsado
	
	public String mostrarPrecioReserva(Reserva reserva, ModelMap modelMap,Iterable<String> paradas,Integer numCiudadesIntermedias) {
		
		
			try {
				
				Ruta nuevaRuta= rutaService.calcularYAsignarTrayectos(reserva.getRuta());
				reserva.setRuta(nuevaRuta);
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
				return "reservas/precioReserva";
			}catch(DuplicatedParadaException e){
				modelMap.put("reserva", reserva);
				modelMap.put("paradas", paradas);
				modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias);
				//Mostrar el mensaje en rojo mejor porque es una excepción
				modelMap.addAttribute("error", "El origen y destino deben ser diferentes."
						+ " (Dos paradas consecutivas tampoco pueden ser iguales)");

				return "reservas/newReservaForm";	

			}			
		
	}
	
	@PostMapping("/new")
	public String newReserva(Reserva reserva, ModelMap modelMap,BindingResult binding) {
		
		System.out.println(reserva.getNumKmTotales());
		
		return "reservas/newReservaForm";
	}
	
	

}
