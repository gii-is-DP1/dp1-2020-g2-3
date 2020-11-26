package org.springframework.samples.petclinic.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trabajadores")
public class TrabajadorController {
	
	private final TrabajadorService trabajadorService;
	
	
	@Autowired
	public TrabajadorController(TrabajadorService trabajadorService) {
		this.trabajadorService = trabajadorService;
	}
	
	
	@GetMapping(value = "/trabajadoresList")
	public String listadoTrabajadores(ModelMap modelMap) {
		String vista="trabajadores/trabajadoresList";
		Iterable<Trabajador> trabajadores= trabajadorService.findAll();
		modelMap.addAttribute("trabajadores", trabajadores);
		return vista;
	}

}
