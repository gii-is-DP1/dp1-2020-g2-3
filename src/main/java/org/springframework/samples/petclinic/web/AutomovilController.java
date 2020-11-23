package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.service.AutomovilService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/automoviles")
public class AutomovilController {

	private final  AutomovilService autoService;
	private final TrabajadorService trabService;
	@Autowired
	public AutomovilController(AutomovilService autoService,TrabajadorService trabService) {
		this.autoService = autoService;
		this.trabService = trabService;

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
		Optional<Automovil> automovil=autoService.findAutomovilById(autoId);
		if (automovil.isPresent()) {
			autoService.delete(automovil.get()); 
			modelMap.addAttribute("message", "Automóvil borrado correctamente");
		}else {
			
			modelMap.addAttribute("message", "Automóvil no encontrado");
		}
		return listadoAutomoviles(modelMap);
	}
	
	@GetMapping("/edit/{autoId}")
	public String editAutomovil(@PathVariable("autoId") int autoId,ModelMap modelMap) {
		Optional<Automovil> automovil=autoService.findAutomovilById(autoId);
		if(automovil.isPresent()) {
			modelMap.addAttribute("automovil",automovil.get());
			Iterable<Trabajador> trabajadores=trabService.findAll();
			modelMap.addAttribute("trabajadores", trabajadores);
			return "automoviles/updateAutomovilForm";
		}else {
			modelMap.addAttribute("message","No se ha encontrado el automóvil a editar");
			return listadoAutomoviles(modelMap);
		}
	} 
	
	@PostMapping("/edit/{autoId}")
	public String editAutomovil(@PathVariable("autoId") int id, @Valid Automovil modifiedAutomovil, BindingResult binding, ModelMap modelMap) {
		Optional<Automovil> automovil=autoService.findAutomovilById(id);
		
		if(binding.hasErrors()) {			
			return "automoviles/updateAutomovilForm";
		}else {
			
			BeanUtils.copyProperties(modifiedAutomovil, automovil.get(), "id");
			autoService.save(automovil.get());
			modelMap.addAttribute("message","Automóvil actualizado correctamente");
			return listadoAutomoviles(modelMap);
		}
	}
	
	@GetMapping("/new")
	public String editNewAutomovil(ModelMap modelMap) {
		modelMap.addAttribute("automovil",new Automovil());
		modelMap.addAttribute("trabajadores", trabService.findAll());
		return "automoviles/updateAutomovilForm";
	} 
	
	@PostMapping("/new")
	public String saveNewAutomovil(@Valid Automovil automovil, BindingResult binding, ModelMap modelMap) {
		if(binding.hasErrors()) {			
			return "automoviles/updateAutomovilForm";
		}else {
			autoService.save(automovil);
			modelMap.addAttribute("message","Automóvil creado correctamente");
			return listadoAutomoviles(modelMap);
		}
	}

}
