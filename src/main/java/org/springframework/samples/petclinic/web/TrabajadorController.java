package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.service.ContratoService;
import org.springframework.samples.petclinic.service.TipoTrabajadorService;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trabajadores")
public class TrabajadorController {
	
	private final TrabajadorService trabajadorService;
	private final TipoTrabajadorService tipoTrabajadorService;

	
	
	@Autowired
	public TrabajadorController(TrabajadorService trabajadorService, TipoTrabajadorService tipoTrabajadorService) {
		this.trabajadorService = trabajadorService;
		this.tipoTrabajadorService = tipoTrabajadorService;

	}
	
	
	@GetMapping(value = "/trabajadoresList")
	public String listadoTrabajadores(ModelMap modelMap) {
		String vista="trabajadores/trabajadoresList";
		Iterable<Trabajador> trabajadores= trabajadorService.findAll();
		modelMap.addAttribute("trabajadores", trabajadores);
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
	public String saveNewTrabajador(@Valid Trabajador trabajador, BindingResult binding, ModelMap modelMap) {
		if(binding.hasErrors()) {
			modelMap.put("trabajador", trabajador);
			return "trabajadores/updateTrabajadorForm";
		}else {
			trabajadorService.save(trabajador);
			modelMap.addAttribute("message","Trabajador creado correctamente");
			return listadoTrabajadores(modelMap);
		}
}
}