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

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers=ClienteController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class ClienteControllerTests {
	
	private static final int TEST_CLIENTE_ID = 1;

	@Autowired
	private ClienteController clienteController;
	
	@MockBean
	private ClienteService clienteService;
        
    @MockBean
	private UserService userService;
        
    @MockBean
    private AuthoritiesService authoritiesService; 

	@Autowired
	private MockMvc mockMvc;

	private Cliente lucas;
	
	@BeforeEach
	void setup() {

		lucas = new Cliente();
		lucas.setId(TEST_CLIENTE_ID);
		lucas.setFirstName("Lucas");
		lucas.setLastName("Perez");
		lucas.setDni("42562345H");
		lucas.setEmail("lucas@gmail.com");
		lucas.setTelefono("608555103");
		given(this.clienteService.findClienteById(TEST_CLIENTE_ID)).willReturn(lucas);

	}
	
	@WithMockUser(value = "spring")
    @Test
    void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/clientes/new")).andExpect(status().isOk()).andExpect(model().attributeExists("cliente"))
				.andExpect(view().name("clientes/createOrUpdateClienteForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/clientes/new").param("firstName", "Joe").param("lastName", "Bloggs")
							.with(csrf())
							.param("dni", "42424242F")
							.param("email", "joe@gmail.com")
							.param("telefono", "01316761638"))
				.andExpect(status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/clientes/new")
						.with(csrf())
						.param("firstName", "Joe")
						.param("lastName", "Bloggs")
						.param("dni", "42424211L")
						.param("email", "joe@gmail.com"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("cliente"))
			.andExpect(model().attributeHasFieldErrors("cliente", "telefono"))
			.andExpect(view().name("clientes/createOrUpdateClienteForm"));
	}	

	@WithMockUser(value = "spring")
    @Test
    void testInitFindForm() throws Exception {
		mockMvc.perform(get("/clientes/find")).andExpect(status().isOk()).andExpect(model().attributeExists("cliente"))
			.andExpect(view().name("clientes/findClientes"));
	}

	@WithMockUser(value = "spring")
	@Test
    void testProcessFindFormSuccess() throws Exception {
	given(this.clienteService.findClienteByNombre("")).willReturn(Lists.newArrayList(lucas, new Cliente()));
	mockMvc.perform(get("/clientes")).andExpect(status().isOk()).andExpect(view().name("clientes/findClientes"));
	}

	@WithMockUser(value = "spring")
    @Test
    void testProcessFindFormByLastName() throws Exception {
		given(this.clienteService.findClienteByNombre(lucas.getLastName())).willReturn(Lists.newArrayList(lucas));

		mockMvc.perform(get("/clientes").param("lastName", "Perez")).andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/clientes/" + TEST_CLIENTE_ID));
	}

    @WithMockUser(value = "spring")
    @Test
    void testProcessFindFormNoClientesFound() throws Exception {
    	mockMvc.perform(get("/clientes").param("lastName", "Unknown Surname")).andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("cliente", "lastName"))
				.andExpect(model().attributeHasFieldErrorCode("cliente", "lastName", "notFound"))
				.andExpect(view().name("clientes/findClientes"));
    }

    @WithMockUser(value = "spring")
@Test
void testInitUpdateClienteForm() throws Exception {
	mockMvc.perform(get("/clientes/{clienteId}/edit", TEST_CLIENTE_ID)).andExpect(status().isOk())
			.andExpect(model().attributeExists("cliente"))
			.andExpect(model().attribute("cliente", hasProperty("lastName", is("Perez"))))
			.andExpect(model().attribute("cliente", hasProperty("firstName", is("Lucas"))))
			.andExpect(model().attribute("cliente", hasProperty("dni", is("42562345H"))))
			.andExpect(model().attribute("cliente", hasProperty("email", is("lucas@gmail.com"))))
			.andExpect(model().attribute("cliente", hasProperty("telefono", is("608555103"))))
			.andExpect(view().name("clientes/createOrUpdateClienteForm"));
}

    @WithMockUser(value = "spring")
    @Test
    void testProcessUpdateClienteFormSuccess() throws Exception {
    	mockMvc.perform(post("/clientes/{clienteId}/edit", TEST_CLIENTE_ID)
						.with(csrf())
						.param("firstName", "Joe")
						.param("lastName", "Bloggs")
						.param("dni", "11111111L")
						.param("email", "joe@gmail.com")
						.param("telefono", "01616291589"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/clientes/{clienteId}"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testProcessUpdateClienteFormHasErrors() throws Exception {
    	mockMvc.perform(post("/clientes/{clienteId}/edit", TEST_CLIENTE_ID)
						.with(csrf())
						.param("firstName", "Joe")
						.param("lastName", "Bloggs")
						.param("dni", "11111111L")
						.param("email", "joe@gmail.com"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("cliente"))
			.andExpect(model().attributeHasFieldErrors("cliente", "telefono"))
			.andExpect(view().name("clientes/createOrUpdateClienteForm"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testShowCliente() throws Exception {
    	mockMvc.perform(get("/clientes/{clienteId}", TEST_CLIENTE_ID)).andExpect(status().isOk())
			.andExpect(model().attribute("cliente", hasProperty("lastName", is("Perez"))))
			.andExpect(model().attribute("cliente", hasProperty("firstName", is("Lucas"))))
			.andExpect(model().attribute("cliente", hasProperty("dni", is("42562345H"))))
			.andExpect(model().attribute("cliente", hasProperty("email", is("lucas@gmail.com"))))
			.andExpect(model().attribute("cliente", hasProperty("telefono", is("608555103"))))
			.andExpect(view().name("clientes/clienteDetails"));
    }

}