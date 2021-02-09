package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.TipoTrabajadorService;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.FechaFinAnteriorInicioException;
import org.springframework.samples.petclinic.service.exceptions.TrabajadorNoActivo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/trabajadores")
public class TrabajadorController {
	
	private final TrabajadorService trabajadorService;
	private final TipoTrabajadorService tipoTrabajadorService;
	private final UserService userService;

	
	
	@Autowired
	public TrabajadorController(TrabajadorService trabajadorService, TipoTrabajadorService tipoTrabajadorService, UserService userService) {
		this.trabajadorService = trabajadorService;
		this.tipoTrabajadorService = tipoTrabajadorService;
		this.userService = userService;
	}
	
	
	@GetMapping(value = "/trabajadoresList")
	public String listadoTrabajadores(ModelMap modelMap) {
		String vista="trabajadores/trabajadoresList";
		Iterable<Trabajador> trabajadores= trabajadorService.findAll();
		modelMap.addAttribute("trabajadores", trabajadores);
		log.info("Mostrando el listado de trabajadores");
		return vista;
	}
	
	@GetMapping("/new")
	public String editNewTrabajador(ModelMap modelMap) {
		Iterable<Trabajador> trabajadores= trabajadorService.findAll();
		modelMap.addAttribute("trabajadores", trabajadores);
		modelMap.addAttribute("trabajador",new Trabajador());
		modelMap.addAttribute("tipostrabajador", tipoTrabajadorService.findAll());
		return "trabajadores/updateTrabajadorForm";
	} 
	
	@PostMapping("/new")
	public String saveNewTrabajador(@Valid Trabajador trabajador, BindingResult binding, ModelMap modelMap) throws FechaFinAnteriorInicioException {
		if(binding.hasErrors()) {
			log.error("error de binding" + binding.getAllErrors());
			modelMap.put("trabajador", trabajador);
			return "trabajadores/updateTrabajadorForm";
		}else {
				
		try {
			trabajadorService.save(trabajador);
		}
		catch(FechaFinAnteriorInicioException e){
			modelMap.put("trabajador", trabajador);
			modelMap.addAttribute("error", "La fecha de fin no puede ser anterior a la de incio");
			return "trabajadores/updateTrabajadorForm";
		}
			modelMap.addAttribute("message","Trabajador creado correctamente");
			return listadoTrabajadores(modelMap);
		}
}
	
	
	@GetMapping("/despedir/{trabajadorId}")
	public String despedirTrabajador(@PathVariable("trabajadorId") int trabajadorId,ModelMap modelMap) throws DataAccessException, TrabajadorNoActivo {
		
		try {	
			Trabajador trabajador=trabajadorService.findById(trabajadorId);
			User user = trabajador.getUser();
			userService.despedirTrabajador(user);
		} catch(TrabajadorNoActivo e) {
			modelMap.addAttribute("error", "El trabajador seleccionado no está activo");
			return listadoTrabajadores(modelMap);
		}
		
			modelMap.addAttribute("message", "Trabajador despedido");
		return listadoTrabajadores(modelMap);
	}
	
	

}