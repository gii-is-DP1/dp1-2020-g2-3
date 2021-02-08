package org.springframework.samples.petclinic.web;

import java.security.Principal;


import java.util.Collection;

import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MiPerfilController {

	

	private final ClienteService clienteService;
	
	private final AuthoritiesService authoService;
	private final TrabajadorService trabService;
	private final ClienteController clienteController;
	private final TrabajadorController trabajadorController;
	@Autowired
	public MiPerfilController(ClienteService clienteService, AuthoritiesService authoService,TrabajadorService trabService,ClienteController clienteController,TrabajadorController trabajadorController) {
		this.clienteService = clienteService;
		this.authoService=authoService;
		this.trabService=trabService;
		this.clienteController=clienteController;
		this.trabajadorController=trabajadorController;
	}
	
	@GetMapping("/miPerfil")
	public String miPerfil(ModelMap modelMap,Principal p) {
		String username = p.getName();
		
		Set<String> authorities= authoService.findAuthoritiesByUsername(username);
		if (authorities.contains("cliente")) { 
			Cliente cliente= clienteService.findClienteByUsername(username);
			modelMap.put("cliente", cliente);
			modelMap.put("tipoBean", "cliente");
			return "miPerfil/createOrUpdatePerfilForm";
		}else if(authorities.contains("taxista") || authorities.contains("admin")){
			Trabajador trabajador=trabService.findByUsername(username);
			modelMap.put("trabajador", trabajador);
			modelMap.put("tipoBean", "trabajador");
			return "miPerfil/createOrUpdatePerfilForm";
		}else {
			return "exception";
		}
		
	}
	
	@PostMapping("/miPerfil/cliente")
	public String miPerfilEdit1(@Valid Cliente clienteNuevo,ModelMap modelMap,Principal p,BindingResult result) {
		if(result.hasErrors()) {
			modelMap.put("cliente", clienteNuevo);
			modelMap.put("tipoBean", "cliente");
			return "miPerfil/createOrUpdatePerfilForm";
		}else {
			
			String username = p.getName(); //Todo se comprueba con la sesión en lugar del id
			Cliente clienteAntiguo= clienteService.findClienteByUsername(username);
			BeanUtils.copyProperties(clienteNuevo, clienteAntiguo, "id");
			clienteService.saveCliente(clienteAntiguo);
			modelMap.put("message", "Perfil actualizado correctamente");
			return this.miPerfil(modelMap, p);
			
		}

	}
	
	
	
	@PostMapping("/miPerfil/trabajador")
	public String miPerfilEdit2(@Valid Trabajador trabajadorNuevo,ModelMap modelMap,Principal p,BindingResult result) {
		if(result.hasErrors()) {
		
			modelMap.put("trabajador", trabajadorNuevo);
			modelMap.put("tipoBean", "trabajador");
			return "miPerfil/createOrUpdatePerfilForm";
		}else {
			
			String username = p.getName(); //Todo se comprueba con la sesión en lugar del id
			Trabajador trabajadorAntiguo= trabService.findByUsername(username);
			BeanUtils.copyProperties(trabajadorNuevo, trabajadorAntiguo, "id");
			trabService.savePerfil(trabajadorAntiguo);
			modelMap.put("message", "Perfil actualizado correctamente");
			return this.miPerfil(modelMap, p);
			
		}

	}
}
