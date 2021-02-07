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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.TipoTrabajadorService;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.samples.petclinic.service.UserService;
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
	
	@MockBean
	private TipoTrabajadorService tipoTrabajadorService;
	
	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;
	
	private Trabajador charles;
	private User usuario;
	

	@BeforeEach
	void setup() {
		

		usuario = new User();
		usuario.setUsername("usuario");
		usuario.setPassword("usuario");
		usuario.setEnabled(true);
		charles = new Trabajador();
		charles.setId(TEST_TRABAJADOR_ID);
		charles.setDni("41234567L");
		charles.setNombre("Charles");
		charles.setApellidos("Pérez García");
		charles.setEmail("charles@gmail.es");
		charles.setTelefono("608555102");
		charles.setUser(usuario);
		given(this.trabajadorService.findById(TEST_TRABAJADOR_ID)).willReturn(charles);

	}
	
	

	@WithMockUser(value = "spring")
        @Test
	void testFindListado() throws Exception {
		mockMvc.perform(get("/trabajadores/trabajadoresList")).andExpect(status().isOk()).andExpect(model().attributeExists("trabajadores"))
				.andExpect(view().name("trabajadores/trabajadoresList"));
	}
	
	
	
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessFindFormSuccess() throws Exception {
	given(this.trabajadorService.findById(TEST_TRABAJADOR_ID)).willReturn(charles);

	mockMvc.perform(get("/trabajadores/trabajadoresList")).andExpect(status().isOk()).andExpect(view().name("trabajadores/trabajadoresList"));
}
	
	
	
	
	
	@WithMockUser(value = "spring")
    @Test
    void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/trabajadores/new")).andExpect(status().isOk()).andExpect(model().attributeExists("trabajadores"))
				.andExpect(view().name("trabajadores/updateTrabajadorForm"));
	}
	
	
	
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/trabajadores/new").param("nombre", "Mike").param("apellidos", "Bloggs")
							.with(csrf())
							.param("dni", "42424242F")
							.param("email", "mike@gmail.com")
							.param("telefono", "672823123"))
				.andExpect(status().isOk());
	}
	
	
	
	
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/trabajadores/new")
						.with(csrf())
						.param("nombre", "Mike")
						.param("apellidos", "Bloggs")
						.param("dni", "42424211L")
						.param("email", "mike@gmail.com"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("trabajador"))
			.andExpect(model().attributeHasFieldErrors("trabajador", "telefono"))
			.andExpect(view().name("trabajadores/updateTrabajadorForm"));
	}	

	@WithMockUser(value = "spring")
    @Test
    void testProcessDespedirTrabajadorSuccess() throws Exception {
		mockMvc.perform(get("/trabajadores/despedir/{trabajadorId}",TEST_TRABAJADOR_ID))
						.andExpect(status().isOk())
						.andExpect(model().attribute("message", is("Trabajador despedido")));
	}
	



}
