/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.util.Collection;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Servicio;
import org.springframework.samples.petclinic.model.Taller;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.AutomovilService;
import org.springframework.samples.petclinic.service.ServicioService;
import org.springframework.samples.petclinic.service.TallerService;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/servicios")
public class ServicioController {

	private final  ServicioService servicioService;
	private final TrabajadorService trabService;
	private final AutomovilService autoService;
	private final TallerService tallerService;
	@Autowired
	public ServicioController(ServicioService servicioService,TrabajadorService trabService, AutomovilService autoService, TallerService tallerService) {
		this.servicioService = servicioService;
		this.trabService = trabService;
		this.autoService = autoService;
		this.tallerService = tallerService;

	}

	@GetMapping("/listado")
	public String listadoServicios(ModelMap modelMap) {
		String vista="servicios/listadoServicios";
		Iterable<Servicio> servicios= servicioService.findAll();
		modelMap.addAttribute("servicios", servicios);
		return vista;
	}
	
	@GetMapping(value="/delete/{servicioId}")
	public String borrarServicio(@PathVariable("servicioId") int servicioId,ModelMap modelMap) {
		Optional<Servicio> servicio=servicioService.findServicioById(servicioId);
		if (servicio.isPresent()) {
			servicioService.delete(servicio.get()); 
			modelMap.addAttribute("message", "Servicio borrado correctamente");
		}else {
			
			modelMap.addAttribute("message", "Servicio no encontrado");
		}
		return listadoServicios(modelMap);
	}
	
	
	@GetMapping("/edit/{servicioId}")
	public String editServicio(@PathVariable("servicioId") int servicioId,ModelMap modelMap) {
		Optional<Servicio> servicio=servicioService.findServicioById(servicioId);
		if(servicio.isPresent()) {
			modelMap.addAttribute("servicio",servicio.get());
			Iterable<Trabajador> trabajadores=trabService.findAll();
			modelMap.addAttribute("trabajadores", trabajadores);
			Iterable<Automovil> automoviles=autoService.findAll();
			modelMap.addAttribute("automoviles", automoviles);
			Iterable<Taller> talleres=tallerService.findAll();
			modelMap.addAttribute("talleres", talleres);
			return "servicios/updateServicioForm";
		}else {
			modelMap.addAttribute("message","No se ha encontrado el servicio a editar");
			return listadoServicios(modelMap);
		}
	} 
	
	@PostMapping("/edit/{servicioId}")
	public String editServicio(@PathVariable("servicioId") int id, @Valid Servicio modifiedServicio, BindingResult binding, ModelMap modelMap) {
		Optional<Servicio> servicio=servicioService.findServicioById(id);
		
		if(binding.hasErrors()) {	
			modelMap.addAttribute("message", binding.getAllErrors());
			modelMap.put("servicio", modifiedServicio);
			Iterable<Trabajador> trabajadores=trabService.findAll();
			modelMap.addAttribute("trabajadores", trabajadores);
			Iterable<Automovil> automoviles=autoService.findAll();
			modelMap.addAttribute("automoviles", automoviles);
			Iterable<Taller> talleres=tallerService.findAll();
			modelMap.addAttribute("talleres", talleres);
			return "servicios/updateServicioForm";
		}else {
			
			BeanUtils.copyProperties(modifiedServicio, servicio.get(), "id");
			servicioService.save(servicio.get());
			modelMap.addAttribute("message","Servicio actualizado correctamente");
			return listadoServicios(modelMap);
		}
	}
	

	@GetMapping("/new")
	public String NewServicio(ModelMap modelMap) {
		modelMap.addAttribute("servicio",new Servicio());
		modelMap.addAttribute("trabajadores", trabService.findAll());
		modelMap.addAttribute("automoviles", autoService.findAll());
		modelMap.addAttribute("talleres", tallerService.findAll());
		return "servicios/updateServicioForm";
	} 
	
	@PostMapping("/new")
	public String saveNewServicio(@Valid Servicio servicio, BindingResult binding, ModelMap modelMap) {
		if(binding.hasErrors()) {			
			return "servicios/updateServicioForm";
		}else {
			servicioService.save(servicio);
			modelMap.addAttribute("message","Servicio creado correctamente");
			return listadoServicios(modelMap);
		}
	}

	
}
