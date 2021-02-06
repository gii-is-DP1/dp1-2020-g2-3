package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.ServicioService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers=EconomiaController.class, 
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class EconomiaControllerTests {

	@Autowired
	private EconomiaController economiaController;
	
	@MockBean
	private ReservaService reservaService;
	
	@MockBean
	private ServicioService servicioService;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@WithMockUser(value = "spring")
	@Test
	void testInitFindForm() throws Exception{
		mockMvc.perform(get("/economias/find"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("fecha1"))
		.andExpect(model().attributeExists("fecha2"))
		.andExpect(view().name("economias/findEconomias"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormSuccess() throws Exception {
		mockMvc.perform(get("/economias/calcular")
				.param("fecha1", "1900-01-01")
				.param("fecha2", "1900-12-31"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("ingresos"))
		.andExpect(model().attributeExists("gastos"))
		.andExpect(view().name("economias/listEconomias"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormConFechasNull() throws Exception {
		mockMvc.perform(get("/economias/calcular")
				.param("fecha1", "null")
				.param("fecha2", "null"))
		.andExpect(status().isOk())
		.andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormConFecha2AnteriorAFecha1() throws Exception {
		mockMvc.perform(get("/economias/calcular")
				.param("fecha1", "1900-01-01")
				.param("fecha2", "1800-01-01"))
		.andExpect(status().isOk())
		.andExpect(view().name("economias/findEconomias"));
	}
}
