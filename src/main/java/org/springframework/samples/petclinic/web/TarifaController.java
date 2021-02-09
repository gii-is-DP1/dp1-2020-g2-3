package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.samples.petclinic.service.TarifaService;
import org.springframework.samples.petclinic.service.exceptions.DobleTarifaActivaException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/tarifas")
public class TarifaController {

		private final  TarifaService tariService;
		@Autowired
		public TarifaController(TarifaService tariService) {
			this.tariService = tariService;

		}

		@GetMapping("/listado")
		public String listadoTarifas(ModelMap modelMap) {
			String vista="tarifas/listadoTarifas";
			Iterable<Tarifa> tarifas= tariService.findByOriginal();
			modelMap.addAttribute("tarifas", tarifas);
			log.info("Mostrando listado de tarifas");
			return vista;
		}
		
		@GetMapping("/activar/{tarifaId}")
		public String activarTarifa(@PathVariable("tarifaId") int tarifaId,ModelMap modelMap) {
			Optional<Tarifa> tarifa=tariService.findTarifaById(tarifaId);
			if(tarifa.isPresent()) {
				tariService.activarTarifa(tarifa.get());
				modelMap.addAttribute("message", "Tarifa activada correctamente");
				return listadoTarifas(modelMap);
			}else {
				modelMap.addAttribute("error","No se ha encontrado la tarifa a editar");
				return listadoTarifas(modelMap);
			}
		} 
		
		
		@GetMapping(value="/delete/{tarifaId}")
		public String borrarTarifa(@PathVariable("tarifaId") int tarifaId,ModelMap modelMap) {
			Optional<Tarifa> tarifa=tariService.findTarifaById(tarifaId);
			if (tarifa.isPresent()) {
				if(tarifa.get().getActivado()) {
					
					modelMap.addAttribute("error", "No puedes eliminar una tarifa que esté activa");

				}else {
					
					tariService.delete(tarifa.get()); 
					modelMap.addAttribute("message", "Tarifa borrada correctamente");
				}
				
			}else {
				
				modelMap.addAttribute("message", "Tarifa no encontrada");
			}
			return listadoTarifas(modelMap);
		}
		
		
		@GetMapping("/edit/{tarifaId}")
		public String editTarifa(@PathVariable("tarifaId") int tarifaId,ModelMap modelMap) {
			Optional<Tarifa> tarifa=tariService.findTarifaById(tarifaId);
			if(tarifa.isPresent()) {
				modelMap.addAttribute("tarifa",tarifa.get());
				return "tarifas/updateTarifaForm";
			}else {
				modelMap.addAttribute("message","No se ha encontrado la tarifa a editar");
				return listadoTarifas(modelMap);
			}
		} 
		
		@PostMapping("/edit/{tarifaId}")
		public String editTarifa(@PathVariable("tarifaId") int tarifaId, @Valid Tarifa modifiedTarifa, BindingResult binding, ModelMap modelMap) throws DobleTarifaActivaException{
			Optional<Tarifa> tarifa=tariService.findTarifaById(tarifaId);
			
			if(binding.hasErrors()) {			
				modelMap.put("tarifa", modifiedTarifa);
				return "tarifas/updateTarifaForm";
			}else {
				
				
				try {
					modifiedTarifa.setId(tarifaId);
					tariService.saveTarifaOriginal(modifiedTarifa);
					modelMap.addAttribute("message","Tarifa actualizada correctamente");
					
				}catch(DobleTarifaActivaException e) {
					
					modelMap.addAttribute("error","No puede haber más de una tarifa activa a la vez");
				}
				
				return listadoTarifas(modelMap);
			}
		}
		
		
		

		@GetMapping("/new")
		public String NewTarifa(ModelMap modelMap) {
			modelMap.addAttribute("tarifa",new Tarifa());
			return "tarifas/updateTarifaForm";
		} 
		
		@PostMapping("/new")
		public String saveNewTarifa(@Valid Tarifa tarifa, BindingResult binding, ModelMap modelMap) throws DobleTarifaActivaException {
			if(binding.hasErrors()) {			
				return "tarifas/updateTarifaForm";
			}else {
				
				try {
				
				tariService.saveTarifaOriginal(tarifa);
				modelMap.addAttribute("message","Tarifa creada correctamente");
					
				}catch(DobleTarifaActivaException e) {
					
					modelMap.addAttribute("error","Solo puede existir una tarifa activa a la vez");

				}
			
				return listadoTarifas(modelMap);
			}
		}

		
	}

