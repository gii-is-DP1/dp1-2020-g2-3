package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Servicio;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.AutomovilService;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.AutomovilAsignadoServicioReservaException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=AutomovilController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class AutomovilControllerTests {
	

	@Autowired
	private AutomovilController automovilController;
	@MockBean
	private  AutomovilService autoService;
	@MockBean
	private  TrabajadorService trabService;
	@Autowired
	private MockMvc mockMvc;
	
	private List<Automovil> autos;

	@BeforeEach
	void setup() {
		Automovil auto1= Automovil.createAuto("Toyota", "Verso", 5, 123.0);
		Automovil auto2= Automovil.createAuto("Seat", "Ibiza", 5, 500.2);
		Automovil auto3= Automovil.createAuto("Audi", "A1", 7, 600.1);
		List<Automovil> autos= new ArrayList<Automovil>();
		autos.add(auto1);
		
		
		autos.add(auto2);
		autos.add(auto3);
		this.autos=autos;
		
		
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testListadoAutomoviles1() throws Exception {
		
		given(this.autoService.findAll()).willReturn(autos);
		
		mockMvc.perform(get("/automoviles/listado")).andExpect(status().isOk()).andExpect(model().attributeExists("automoviles"))
				.andExpect(model().attribute("automoviles", is(autos))).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("automoviles/listadoAutomoviles"));
	}
	
	

	@WithMockUser(value = "spring")
    @Test
    void borrarAutomovilTest1() throws Exception {
		
		given(this.autoService.findAutomovilById(1)).willReturn(Optional.ofNullable(autos.get(0)));

		
		mockMvc.perform(get("/automoviles/delete/{autoId}",1)).andExpect(status().isOk()).andExpect(model().attributeExists("message"))
				.andExpect(model().attribute("message", is("Automóvil borrado correctamente"))).andExpect(status().is2xxSuccessful()).andExpect(view().name("automoviles/listadoAutomoviles"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void borrarAutomovilQueNoExiste() throws Exception {
		
		
		given(this.autoService.findAutomovilById(1)).willReturn(Optional.ofNullable(null));

		
		mockMvc.perform(get("/automoviles/delete/{autoId}",1)).andExpect(status().isOk()).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", is("Automóvil no encontrado"))).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("automoviles/listadoAutomoviles"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void borrarAutomovilAsociadoAServicio() throws Exception {
		
		given(this.autoService.findAutomovilById(1)).willReturn(Optional.ofNullable(autos.get(0)));
		given(this.autoService.delete(autos.get(0))).willThrow(AutomovilAsignadoServicioReservaException.class);
		
	
		mockMvc.perform(get("/automoviles/delete/{autoId}",1)).andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", is("No se puede eliminar un automóvil que haya realizado un servicio o viaje")))
				.andExpect(status().isOk())
				.andExpect(view().name("automoviles/listadoAutomoviles"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void editAutomovilTest1() throws Exception {

		given(this.autoService.findAutomovilById(1)).willReturn(Optional.ofNullable(autos.get(0)));

	
		mockMvc.perform(get("/automoviles/edit/{autoId}",1))
				.andExpect(model().attribute("automovil", is(autos.get(0))))
				.andExpect(status().isOk())
				.andExpect(view().name("automoviles/updateAutomovilForm"));
	}
	
	
	@WithMockUser(value = "spring")
    @Test
    void editExceptionAutomovilTest() throws Exception {

		given(this.autoService.findAutomovilById(1)).willReturn(Optional.ofNullable(null));

	
		mockMvc.perform(get("/automoviles/edit/{autoId}",1))
				.andExpect(model().attributeDoesNotExist("automovil"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("error", "No se ha encontrado el automóvil a editar"))
				.andExpect(view().name("automoviles/listadoAutomoviles"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void postEditAutomovil() throws Exception {

		given(this.autoService.findAutomovilById(1)).willReturn(Optional.ofNullable(autos.get(0)));

		
		mockMvc.perform(post("/automoviles/edit/{autoId}",1)
				.with(csrf())
				.param("marca", "Toyota")
				.param("modelo", "Verso")
				.param("numPlazas","5")
				.param("kmRecorridos", "200.0")).andExpect(status().is2xxSuccessful())
				.andExpect(model().attribute("message", "Automóvil actualizado correctamente"))
				.andExpect(view().name("automoviles/listadoAutomoviles"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void postEditAutomovilErrorBinding() throws Exception {

		given(this.autoService.findAutomovilById(1)).willReturn(Optional.ofNullable(autos.get(0)));

		
		mockMvc.perform(post("/automoviles/edit/{autoId}",1)
				.with(csrf())
				.param("marca", "Toyota")
				.param("modelo", "Verso")
				.param("numPlazas","10")
				.param("kmRecorridos", "200.0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("automovil"))
				.andExpect(model().attributeHasFieldErrors("automovil", "numPlazas"))
				.andExpect(model().attributeExists("automovil"))
				.andExpect(view().name("automoviles/updateAutomovilForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void newAutomovil() throws Exception {
		
		mockMvc.perform(get("/automoviles//new"))
		.andExpect(model().attribute("automovil", new Automovil()))
		.andExpect(status().isOk())
		.andExpect(view().name("automoviles/updateAutomovilForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void newAutomovilPost() throws Exception {
		
		mockMvc.perform(post("/automoviles//new")
				.with(csrf())
				.param("marca", "Toyota")
				.param("modelo", "Verso")
				.param("numPlazas","4")
				.param("kmRecorridos", "200.0"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(model().attribute("message", "Automóvil creado correctamente"))
				.andExpect(view().name("automoviles/listadoAutomoviles"));
						
	}
	
	@WithMockUser(value = "spring")
    @Test
    void newAutomovilPostErrorBinding() throws Exception {
		
		mockMvc.perform(post("/automoviles//new")
				.with(csrf())
				.param("marca", "Toyota")
				.param("modelo", "Verso")
				.param("numPlazas","10")
				.param("kmRecorridos", "cadena"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(model().attributeHasErrors("automovil"))
				.andExpect(model().attributeHasFieldErrors("automovil", "numPlazas"))
				.andExpect(model().attributeHasFieldErrors("automovil", "kmRecorridos"))
				.andExpect(model().attributeExists("automovil"))
				.andExpect(view().name("automoviles/updateAutomovilForm"));
						
	}
	
	
}
