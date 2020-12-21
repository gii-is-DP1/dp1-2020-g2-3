package org.springframework.samples.petclinic.web;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.TrayectoService;
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
	
	@Autowired
	public ReservaController(ReservaService reservaService,TrayectoService trayectoService) {
		this.reservaService=reservaService;
		this.trayectoService=trayectoService;
	}
	
	@GetMapping("/new")
	public String newReserva(ModelMap modelMap) {
		modelMap.addAttribute("reserva",new Reserva());
		modelMap.addAttribute("ruta",new Ruta());
		Iterable<String> ciudadesOrigen= trayectoService.findCiudadesOrigen();
		Iterable<String> ciudadesDestino= trayectoService.findCiudadesDestino();
		modelMap.addAttribute("ciudadesOrigen", ciudadesOrigen);
		modelMap.addAttribute("ciudadesDestino", ciudadesDestino);
		modelMap.addAttribute("numCiudadesIntermedias", 0);
		modelMap.addAttribute("finBucle", 0);
		return "reservas/newReservaForm";
	}
	
	
	@PostMapping("/redirigir")
	public String redirigir(Reserva reserva,Ruta ruta,ModelMap modelMap,@RequestParam("action") String action,@RequestParam("numCiudadesIntermedias") Integer numCiudadesIntermedias) {
	
		
		//COMPROBAR BINDING AQUÍ
		
		//Se decidirá entre añadir una nueva parada al jsp de la reserva, o seguir con la solicitud
		if(action.equals("continuar")) {
			
			return mostrarPrecioReserva(reserva,ruta,modelMap);
		}else if(action.equals("addParada")) {
			
			return addParada(reserva,ruta,modelMap,numCiudadesIntermedias);
		}else {
			
			return "exception";
		}
	
	}
	
	
	public String addParada(@Valid Reserva reserva,@Valid Ruta ruta,ModelMap modelMap,@PathVariable("numCiudadesIntermedias") Integer numCiudadesIntermedias) {
		
		
		modelMap.put("reserva",reserva);
		Iterable<String> ciudadesOrigen= trayectoService.findCiudadesOrigen();
		Iterable<String> ciudadesDestino= trayectoService.findCiudadesDestino();
		modelMap.put("ciudadesOrigen", ciudadesOrigen);
		modelMap.put("ciudadesDestino", ciudadesDestino);
		modelMap.put("numCiudadesIntermedias", numCiudadesIntermedias+1);
		modelMap.put("finBucle", numCiudadesIntermedias);
		modelMap.put("ruta", ruta);
		
	
		return "reservas/newReservaForm";
	}
	
	//No tiene url, viene desde /redirigir porque el mismo formulario tiene 2 botones, y "redirigir" llama a un sitio u otro 
	// dependiendo del botón pulsado
	public String mostrarPrecioReserva(Reserva reserva,Ruta ruta, ModelMap modelMap) {
		//EN DESARROLLO
		
		return "reservas/precioReserva";
	}
	
	

}
