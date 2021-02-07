package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Servicio;
import org.springframework.samples.petclinic.model.Taller;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.service.AutomovilService;
import org.springframework.samples.petclinic.service.ServicioService;
import org.springframework.samples.petclinic.service.TallerService;
import org.springframework.samples.petclinic.service.TipoTrabajadorService;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ch.qos.logback.core.net.ObjectWriter;

	

@WebMvcTest(controllers=ServicioController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)

public class ServicioControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	private static final int TEST_SERVICIO_ID = 1;

	@MockBean
	private ServicioService servicioService;
	
	@MockBean
	private TrabajadorService trabajadorService;
	
	@MockBean
	private AutomovilService automovilService;
	
	@MockBean
	private TallerService tallerService;

	
	
	private Servicio s1;
	
	private Trabajador trab1;
	
	private Automovil a1;
	
	private Taller t1;

	@BeforeEach
	void setup() {

		s1 = new Servicio();
		trab1 = new Trabajador();
		a1 = new Automovil();
		t1 = new Taller();
		
		s1.setId(TEST_SERVICIO_ID);
		s1.setFecha(Date.valueOf(LocalDate.of(2015, 2, 15)));
		s1.setPrecio(53.98);
		s1.setTrabajador(trab1);
		s1.setAutomovil(a1);
		s1.setTaller(t1);
		s1.setDescripcion("Revisión de GPS");
		s1.setCompletado(true);
		s1.setFechaCompletado(Date.valueOf(LocalDate.of(2015, 2, 27)));
		given(this.servicioService.findServicioById(TEST_SERVICIO_ID)).willReturn(s1);

	}
	

	@WithMockUser(value = "spring")
        @Test
	void testFindListado() throws Exception {
		mockMvc.perform(get("/servicios/listado")).andExpect(status().isOk()).andExpect(model().attributeExists("servicios"))
				.andExpect(view().name("servicios/listadoServicios"));
	}
	
	
	
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessFindFormSuccess() throws Exception {
	given(this.servicioService.findServicioById(TEST_SERVICIO_ID)).willReturn(s1);

	mockMvc.perform(get("/servicios/listado")).andExpect(status().isOk()).andExpect(view().name("servicios/listadoServicios"));
}
	
	

	
	
	@WithMockUser(value = "spring")
    @Test
    void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/servicios/new")).andExpect(status().isOk()).andExpect(model().attributeExists("servicio"))
				.andExpect(view().name("servicios/updateServicioForm"));
	}

	
	@WithMockUser(value = "spring")
    @Test
    void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/servicios/new", TEST_SERVICIO_ID)
					.param("descripcion", "ou mama"))
			//.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("servicio"))
			.andExpect(model().attributeHasFieldErrors("servicio", "taller"))
			
			.andExpect(view().name("servicios/UpdateServicioForm"));
	}	
	
	

	
	/*	
	
	
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
	
	
	*/
	
	@Test
	public void testDelete() throws Exception{
		
		mockMvc.perform(get("servicios/delete/1"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("servicios/listadoServicios"));
		;
	}
	
	

}
