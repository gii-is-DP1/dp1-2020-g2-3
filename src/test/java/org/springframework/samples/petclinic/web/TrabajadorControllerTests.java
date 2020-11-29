package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=TrabajadorController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class TrabajadorControllerTests {
	
	private static final int TEST_TRABAJADOR_ID = 1;

	@Autowired
	private TrabajadorController trabajadorController;
	
	@MockBean
	private TrabajadorService trabajadorService;


	@Autowired
	private MockMvc mockMvc;
	
	private Trabajador charles;

	@BeforeEach
	void setup() {

		charles = new Trabajador();
		charles.setId(TEST_TRABAJADOR_ID);
		charles.setDNI("41234567L");
		charles.setNombre("Charles");
		charles.setApellidos("Pérez García");
		charles.setCorreoElectronico("charles@gmail.es");
		charles.setTelefono(608555102);
		given(this.trabajadorService.findById(TEST_TRABAJADOR_ID)).willReturn(charles);

	}
	
	
//
//	@WithMockUser(value = "spring")
//        @Test
//	void testFindListado() throws Exception {
//		mockMvc.perform(get("/trabajadores/trabajadoresList")).andExpect(status().isOk()).andExpect(model().attributeExists("trabajador"))
//				.andExpect(view().name("trabajadores/trabajadoresList"));
//	}
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessFindFormSuccess() throws Exception {
	given(this.trabajadorService.findById(TEST_TRABAJADOR_ID)).willReturn(charles);

	mockMvc.perform(get("/trabajadores/trabajadoresList")).andExpect(status().isOk()).andExpect(view().name("trabajadores/trabajadoresList"));
}
	

}
