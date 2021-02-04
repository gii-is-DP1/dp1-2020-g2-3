package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.CancelacionViajeAntelacionException;
import org.springframework.samples.petclinic.service.exceptions.ParadaYaAceptadaRechazadaException;
import org.springframework.samples.petclinic.service.exceptions.ReservaYaRechazada;
import org.springframework.samples.petclinic.service.exceptions.ReservasSoliAceptException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClienteController {

	private static final String VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM = "clientes/createOrUpdateClienteForm";

	private final ClienteService clienteService;
	
	private final ReservaService reservaService;
	
	@Autowired
	public ClienteController(ClienteService clienteService, ReservaService reservaService, UserService userService, AuthoritiesService authoritiesService) {
		this.clienteService = clienteService;
		this.reservaService = reservaService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/clientes/new")
	public String initCreationForm(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		return VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/clientes/new")
	public String processCreationForm(@Valid Cliente cliente, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
		}
		else {
			//creating owner, user and authorities
			this.clienteService.saveCliente(cliente);
			
			return "redirect:/clientes/" + cliente.getId();
		}
	}

	@GetMapping(value = "/clientes/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("cliente", new Cliente());
		return "clientes/findClientes";
	}

	@GetMapping(value = "/clientes")
	public String processFindForm(Cliente cliente, BindingResult result, Map<String, Object> model) {

		// allow parameterless GET request for /clientes to return all records
		if (cliente.getNombre() == null) {
			cliente.setApellidos(""); // empty string signifies broadest possible search
		}

		// find clientes by nombre
		Collection<Cliente> results = this.clienteService.findClienteByNombre(cliente.getApellidos());
		if (results.isEmpty()) {
			// no clients found
			result.rejectValue("apellidos", "notFound", "not found");
			return "clientes/findClientes";
		}
		else if (results.size() == 1) {
			// 1 owner found
			cliente = results.iterator().next();
			return "redirect:/clientes/" + cliente.getId();
		}
		else {
			// multiple owners found
			model.put("selections", results);
			return "clientes/clientesList";
		}
	}
	
	@GetMapping("/clientes/listado")
	public String listadoClientes(ModelMap model) {
		String vista="clientes/clientesList";
		Iterable<Cliente> clientes = clienteService.findAll();
		model.addAttribute("selections",clientes);
		return vista;
	}

	@GetMapping(value = "/clientes/{clienteId}/edit")
	public String initUpdateClienteForm(@PathVariable("clienteId") int clienteId, Model model) {
		Cliente cliente = this.clienteService.findClienteById(clienteId);
		model.addAttribute(cliente);
		return VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/clientes/{clienteId}/edit")
	public String processUpdateClienteForm(@Valid Cliente cliente, BindingResult result,
			@PathVariable("clienteId") int clienteId) {
		Cliente clienteAntiguo= clienteService.findClienteById(clienteId);
		if (result.hasErrors()) {
			return VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
		}
		else {
			
			BeanUtils.copyProperties(cliente, clienteAntiguo, "id");
			clienteService.saveCliente(clienteAntiguo);
			return "redirect:/clientes/{clienteId}";
		}
	}

	
	@GetMapping("/clientes/{clienteId}")
	public ModelAndView showCliente(@PathVariable("clienteId") int clienteId) {
		ModelAndView mav = new ModelAndView("clientes/clienteDetails");
		mav.addObject(this.clienteService.findClienteById(clienteId));
		return mav;
	}
	
	@GetMapping("/clientes/myReservas")
	public String showReservas(ModelMap modelMap, Principal p) {
		String username = p.getName();
		String vista="reservas/misReservas";
		Iterable<Reserva> reservas= reservaService.findReservasByUsername(username);
		modelMap.addAttribute("reservas", reservas);
		return vista;
	}
	
	@GetMapping(value= "/clientes/myReservas/cancelar/{reservaId}")
	public String cancelarReserva(@PathVariable("reservaId") int reservaId,ModelMap modelMap, Principal p) throws DataAccessException, ReservaYaRechazada{
		Optional<Reserva> reservaOptional= reservaService.findReservaById(reservaId);
		if(!reservaOptional.isPresent()) {
			modelMap.addAttribute("error", "Reserva no encontrada");
			return showReservas(modelMap,p);
		}else {
			try {
				reservaService.cancelarReserva(reservaOptional.get());
				modelMap.addAttribute("message", "Reserva cancelada correctamente");
			}catch(CancelacionViajeAntelacionException e) {
				modelMap.addAttribute("error", "No puedes cancelar una reserva con una antelación menor a 24 horas");
			}catch(ReservaYaRechazada e) {
				modelMap.addAttribute("error", "No puedes cancelar una reserva que no tenga un estado Solicitada o Aceptada");
			}
		}
			return showReservas(modelMap, p);
		}
	}
	

