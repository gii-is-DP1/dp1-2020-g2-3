package org.springframework.samples.petclinic.web;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.ServicioService;
import org.springframework.samples.petclinic.service.UtilService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/economias")
public class EconomiaController {

	private final ReservaService reservaService;
	private final ServicioService servicioService;
	private final UtilService utilService;
	
	@Autowired
	public EconomiaController(ReservaService reservaService, ServicioService servicioService, UtilService utilService) {
		this.reservaService = reservaService;
		this.servicioService = servicioService;
		this.utilService = utilService;
	}
	
	@GetMapping(value = "/find")
	public String initFindForm(final ModelMap model) {
		LocalDate fecha1 = LocalDate.now();
		LocalDate fecha2 = LocalDate.now();	
		model.put("fecha1", fecha1);
		model.put("fecha2", fecha2);
		log.info("Creando formulario de economia.");
		return "economias/findEconomias";
	}
	
	@GetMapping(value = "/calcular")
	public String processFindForm(@RequestParam("fecha1") String fecha1, @RequestParam("fecha2") String fecha2, final ModelMap model) {
		
		if (fecha1!="" && fecha2!="") {
			LocalDate fechaInicial = LocalDate.parse(fecha1);
			LocalDate fechaFin = LocalDate.parse(fecha2);
			if((fechaInicial.isAfter(fechaFin))) {
				model.put("error", "La fecha final no puede ser anterior a la fecha inicial.");
				log.error("La fecha final no puede ser anterior a la fecha inicial. Fecha inicial: " + fechaInicial + "; Fecha final: " + fechaFin);
				return "economias/findEconomias";
			}else {
				Double ingresos = reservaService.calcularIngresos(utilService.asDate(fechaInicial), utilService.asDate(fechaFin));
				Double gastos = servicioService.calcularGastos(utilService.asDate(fechaInicial), utilService.asDate(fechaFin));
				model.put("ingresos", ingresos);
				model.put("gastos", gastos);
				log.info("Mostrando los ingresos (" + ingresos + ") y gastos (" + gastos + ")");
				return "economias/listEconomias";
			}
		}else {
			model.put("error", "Debes introducir una fecha.");
			log.error("Se debe introducir una fecha en el formulario de economia.");
			return "economias/findEconomias";
		}		
	}
	
}
