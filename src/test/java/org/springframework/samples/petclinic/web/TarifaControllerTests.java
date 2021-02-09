package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.service.TarifaService;
import org.springframework.samples.petclinic.service.exceptions.DobleTarifaActivaException;


@WebMvcTest(controllers=TarifaController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class TarifaControllerTests {
	
	private static final int TEST_TARIFA_ID = 1;
	
	@Autowired
	private TarifaController tarifaController;
	
	@MockBean
	private TarifaService tarifaService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private List<Tarifa> taris;
	
	@BeforeEach
	void setup() {
		Tarifa tari1 = new Tarifa();
		tari1.setPrecioPorKm(0.3);
		tari1.setPrecioEsperaPorHora(3.0);
		tari1.setPorcentajeIvaRepercutido(12);
		tari1.setOriginal(true);
		tari1.setActivado(false);
		
		Tarifa tari2 = new Tarifa();
		tari2.setPrecioPorKm(0.4);
		tari2.setPrecioEsperaPorHora(4.0);
		tari2.setPorcentajeIvaRepercutido(15);
		tari2.setOriginal(true);
		tari2.setActivado(true);
		
		Tarifa tari3 = new Tarifa();
		tari3.setPrecioPorKm(0.6);
		tari3.setPrecioEsperaPorHora(5.0);
		tari3.setPorcentajeIvaRepercutido(20);
		tari3.setOriginal(true);
		tari3.setActivado(false);
		List<Tarifa> taris = new ArrayList<Tarifa>();
		
		taris.add(tari1);
		taris.add(tari2);
		taris.add(tari3);
		this.taris=taris;
		
//		given(this.tarifaService.findTarifaById(TEST_TARIFA_ID).get()).willReturn(estandar);
	}
	
	@WithMockUser(value = "spring")
    @Test
void testFindListado() throws Exception {
	
	given(this.tarifaService.findByOriginal()).willReturn(taris);
		
	mockMvc.perform(get("/tarifas/listado")).andExpect(status().isOk()).andExpect(model().attributeExists("tarifas"))
			.andExpect(model().attribute("tarifas", is(taris))).andExpect(status().is2xxSuccessful())
			.andExpect(view().name("tarifas/listadoTarifas"));
}
	
	@WithMockUser(value = "spring")
    @Test
void testActivarTarifa() throws Exception {
	
	given(this.tarifaService.findTarifaById(1)).willReturn(Optional.ofNullable(taris.get(0)));
		
	mockMvc.perform(get("/tarifas/activar/{tarifaId}",1)).andExpect(status().isOk()).andExpect(model().attributeExists("message"))
	.andExpect(model().attribute("message", is("Tarifa activada correctamente"))).andExpect(status().is2xxSuccessful()).andExpect(view().name("tarifas/listadoTarifas"));
}
	
	@WithMockUser(value = "spring")
    @Test
void testActivarTarifaError() throws Exception {
	
	given(this.tarifaService.findTarifaById(1)).willReturn(Optional.ofNullable(null));
		
	mockMvc.perform(get("/tarifas/activar/{tarifaId}",1)).andExpect(status().isOk()).andExpect(model().attributeExists("error"))
	.andExpect(model().attribute("error", is("No se ha encontrado la tarifa a activar"))).andExpect(status().is2xxSuccessful())
	.andExpect(view().name("tarifas/listadoTarifas"));
}

@WithMockUser(value = "spring")
@Test
void testProcessFindFormSuccess() throws Exception {
	
	given(this.tarifaService.findByOriginal()).willReturn(taris);

mockMvc.perform(get("/tarifas/listado")).andExpect(status().isOk()).andExpect(view().name("tarifas/listadoTarifas"));
}

@WithMockUser(value = "spring")
@Test
void testInitCreationForm() throws Exception {
	
	mockMvc.perform(get("/tarifas/new")).andExpect(status().isOk()).andExpect(model().attributeExists("tarifa"))
			.andExpect(view().name("tarifas/updateTarifaForm"));
}

@WithMockUser(value = "spring")
@Test
void testProcessCreationFormSuccess() throws Exception {
	
	given(this.tarifaService.findByOriginal()).willReturn(taris);
	mockMvc.perform(post("/tarifas/new")
						.with(csrf())
						.param("precioPorKm", "0.7")
						.param("porcentajeIvaRepercutido", "10")
						.param("precioEsperaPorHora", "2.0")
						.param("activado", "false")
						.param("original", "true"))
			.andExpect(status().isOk());
}

@WithMockUser(value = "spring")
@Test
void testProcessCreationFormHasErrors() throws Exception {
	given(this.tarifaService.findByOriginal()).willReturn(taris);
	mockMvc.perform(post("/tarifas/new")
					.with(csrf())
					.param("precioPorKm", "0.7")
					.param("porcentajeIvaRepercutido", "10")
					.param("activado", "false")
					.param("original", "true"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("tarifa"))
		.andExpect(model().attributeHasFieldErrors("tarifa", "precioEsperaPorHora"))
		.andExpect(view().name("tarifas/updateTarifaForm"));
}

//@WithMockUser(value = "spring")
//@Test
//void testProcessCreationFormHasErrorsTwoActiveTarifas() throws Exception {
//	given(this.tarifaService.findByOriginal()).willThrow(DobleTarifaActivaException.class);
//	mockMvc.perform(post("/tarifas/new")
//			.with(csrf())
//			.param("precioPorKm", "0.7")
//			.param("porcentajeIvaRepercutido", "10")
//			.param("precioEsperaPorHora", "2.0")
//			.param("activado", "true")
//			.param("original", "true"))
//			.andExpect(status().isOk())
//			.andExpect(model().attributeExists("error"))
//			.andExpect(model().attribute("error", is("Solo puede existir una tarifa activa a la vez")))
//			.andExpect(status().is2xxSuccessful())
//			.andExpect(view().name("tarifas/updateTarifaForm"));
//}

@WithMockUser(value = "spring")
@Test
void testDeleteTarifa() throws Exception {
	
	given(this.tarifaService.findTarifaById(1)).willReturn(Optional.ofNullable(taris.get(0)));

	
	mockMvc.perform(get("/tarifas/delete/{tarifaId}",1)).andExpect(status().isOk()).andExpect(model().attributeExists("message"))
			.andExpect(model().attribute("message", is("Tarifa borrada correctamente"))).andExpect(status().is2xxSuccessful()).andExpect(view().name("tarifas/listadoTarifas"));
}

@WithMockUser(value = "spring")
@Test
void testCantDeleteTarifaActivada() throws Exception {
	
	given(this.tarifaService.findTarifaById(1)).willReturn(Optional.ofNullable(taris.get(1)));

	
	mockMvc.perform(get("/tarifas/delete/{tarifaId}",1)).andExpect(status().isOk()).andExpect(model().attributeExists("error"))
	.andExpect(model().attribute("error", is("No puedes eliminar una tarifa que esté activa"))).andExpect(status().is2xxSuccessful())
	.andExpect(view().name("tarifas/listadoTarifas"));
}

@WithMockUser(value = "spring")
@Test
void testDeleteNonExistentTarifa() throws Exception {
	
	
	given(this.tarifaService.findTarifaById(1)).willReturn(Optional.ofNullable(null));

	
	mockMvc.perform(get("/tarifas/delete/{tarifaId}",1)).andExpect(status().isOk()).andExpect(model().attributeExists("message"))
			.andExpect(model().attribute("message", is("Tarifa no encontrada"))).andExpect(status().is2xxSuccessful())
			.andExpect(view().name("tarifas/listadoTarifas"));
}

@WithMockUser(value = "spring")
@Test
void testInitTarifaEditForm() throws Exception {

	given(this.tarifaService.findTarifaById(1)).willReturn(Optional.ofNullable(taris.get(0)));


	mockMvc.perform(get("/tarifas/edit/{tarifaId}",1))
			.andExpect(model().attribute("tarifa", is(taris.get(0))))
			.andExpect(status().isOk())
			.andExpect(view().name("tarifas/updateTarifaForm"));
}

@WithMockUser(value = "spring")
@Test
void testProcessTarifaEditFormHasErrors() throws Exception {

	given(this.tarifaService.findTarifaById(1)).willReturn(Optional.ofNullable(null));


	mockMvc.perform(get("/tarifas/edit/{tarifaId}",1))
			.andExpect(model().attributeDoesNotExist("tarifa"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("message", "No se ha encontrado la tarifa a editar"))
			.andExpect(view().name("tarifas/listadoTarifas"));
}

@WithMockUser(value = "spring")
@Test
void testProcessTarifaEditFormSuccess() throws Exception {

	given(this.tarifaService.findTarifaById(1)).willReturn(Optional.ofNullable(taris.get(0)));

	
	mockMvc.perform(post("/tarifas/edit/{tarifaId}",1)
			.with(csrf())
			.param("precioPorKm", "0.3")
			.param("porcentajeIvaRepercutido", "10")
			.param("precioEsperaPorHora", "3.0")
			.param("activado", "true")
			.param("original", "true")).andExpect(status().is2xxSuccessful())
			.andExpect(model().attribute("message", "Tarifa actualizada correctamente"))
			.andExpect(view().name("tarifas/listadoTarifas"));
}

@WithMockUser(value = "spring")
@Test
void testProcessTarifaEditErrorBinding() throws Exception {

	given(this.tarifaService.findTarifaById(1)).willReturn(Optional.ofNullable(taris.get(0)));

	
	mockMvc.perform(post("/tarifas/edit/{tarifaId}",1)
			.with(csrf())
			.param("precioPorKm", "0.3")
			.param("porcentajeIvaRepercutido", "diez")
			.param("precioEsperaPorHora", "3.0")
			.param("activado", "true")
			.param("original", "true"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("tarifa"))
			.andExpect(model().attributeHasFieldErrors("tarifa", "porcentajeIvaRepercutido"))
			.andExpect(model().attributeExists("tarifa"))
			.andExpect(view().name("tarifas/updateTarifaForm"));
}
}
