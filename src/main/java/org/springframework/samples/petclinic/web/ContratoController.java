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
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.Servicio;
import org.springframework.samples.petclinic.model.Taller;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.AutomovilService;
import org.springframework.samples.petclinic.service.ContratoService;
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
@RequestMapping("/contratos")
public class ContratoController {

	private final  ContratoService contratoService;
	private final TrabajadorService trabService;
	private final TrabajadorController trabController;
	@Autowired
	public ContratoController(ContratoService contratoService, TrabajadorService trabService, TrabajadorController trabController) {
		this.contratoService = contratoService;
		this.trabService = trabService;
		this.trabController = trabController;

	}

	@GetMapping("/new")
	public String NewContrato(ModelMap modelMap) {
		modelMap.addAttribute("contrato",new Contrato());
		modelMap.addAttribute("trabajadores", trabService.findAll());
		return "contratos/updateContratoForm";
	} 
	
	@PostMapping("/new")
	public String saveNewContrato(@Valid Contrato contrato, BindingResult binding, ModelMap modelMap) {
		if(binding.hasErrors()) {			
			return "contratos/updateContratoForm";
		}else {
			contratoService.save(contrato);
			modelMap.addAttribute("message","Contrato creado correctamente");
			return trabController.listadoTrabajadores(modelMap);
		}
	}

	
}
