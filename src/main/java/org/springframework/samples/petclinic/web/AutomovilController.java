package org.springframework.samples.petclinic.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.service.AutomovilService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/automoviles")
public class AutomovilController {

	private final  AutomovilService autoService;
	
	@Autowired
	public AutomovilController(AutomovilService autoService) {
		this.autoService = autoService;
	}
	
	@GetMapping("/listado")
	public String listadoAutomoviles(ModelMap modelMap) {
		String vista="automoviles/listadoAutomoviles";
		Iterable<Automovil> automoviles= autoService.findAll();
		modelMap.addAttribute("automoviles", automoviles);
		return vista;
	}
	
	@GetMapping(value="/delete/{autoId}")
	public String borrarAutomovil(@PathVariable("autoId") int autoId,ModelMap modelMap) {
		String vista= "automoviles/listadoAutomoviles";
		Optional<Automovil> automovil=autoService.findAutomovilById(autoId);
		if (automovil.isPresent()) {
			autoService.delete(automovil.get()); 
			modelMap.addAttribute("message", "Automóvil borrado correctamente");
		}else {
			
			modelMap.addAttribute("message", "Automóvil no encontrado");
		}
		return listadoAutomoviles(modelMap);
	}

}
