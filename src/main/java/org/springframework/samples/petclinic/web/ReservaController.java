package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservas")

public class ReservaController {
	
private final ReservaService reservaService;
	
	
	@Autowired
	public ReservaController(ReservaService reservaService) {
		this.reservaService = reservaService;
	}
	
	
	@GetMapping(value = "/reservasList")
	public String listadoReservas(ModelMap modelMap) {
		String vista="reservas/reservasList";
		Iterable<Reserva> reservas= reservaService.findAll();
		modelMap.addAttribute("reserva", reservas);
		return vista;
	}

}
