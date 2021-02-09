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

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.AutomovilService;
import org.springframework.samples.petclinic.service.ClienteService;
import org.springframework.samples.petclinic.service.EstadoReservaService;
import org.springframework.samples.petclinic.service.ReservaService;
import org.springframework.samples.petclinic.service.RutaService;
import org.springframework.samples.petclinic.service.TarifaService;
import org.springframework.samples.petclinic.service.TrabajadorService;
import org.springframework.samples.petclinic.service.TrayectoService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.UtilService;
import org.springframework.samples.petclinic.service.exceptions.AutomovilAsignadoServicioReservaException;
import org.springframework.samples.petclinic.service.exceptions.AutomovilPlazasInsuficientesException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.EstadoReservaFacturaException;
import org.springframework.samples.petclinic.service.exceptions.ExisteViajeEnEsteHorarioException;
import org.springframework.samples.petclinic.service.exceptions.FechaLlegadaAnteriorSalidaException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
import org.springframework.samples.petclinic.service.exceptions.HoraSalidaSinAntelacionException;
import org.springframework.samples.petclinic.service.exceptions.ParadaYaAceptadaRechazadaException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;

@WebMvcTest(controllers=ReservaController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class ReservaControllerTests {
	
	@Autowired
	private ReservaController reservaController;
	@MockBean
	private   ReservaService reservaService;
	@MockBean
	private   TrayectoService trayectoService;
	@MockBean
	private  RutaService rutaService;
	@MockBean
	private  AuthoritiesService authoService;
	@MockBean
	private  EstadoReservaService estadoReservaService;
	@MockBean
	private  ClienteService clienteService;
	@MockBean
	private  TarifaService tarifaService;
	@MockBean
	private  AutomovilService autoService;
	@MockBean
	private  TrabajadorService trabajadorService;
	@MockBean
	private  UtilService utilService;
	@MockBean
	private  ClienteController clienteController;

	@Autowired
	private MockMvc mockMvc;

	private List<Reserva> listaReservas;
	private List<String> paradas;
			//Algunos tests son muy parecidos pero tienen ligeras diferencias que se necesitan probar para cubrir todo el 
			// controlador
	@BeforeEach
	void setup() {
		
		 Reserva reserva1= new Reserva();
		 reserva1.setDescripcionEquipaje("1 maleta mediana");
		 
		 
		 Reserva reserva2= new Reserva();
		 reserva2.setDescripcionEquipaje("2 maletas medianas");
		 
		 Reserva reserva3= new Reserva();
		 reserva3.setDescripcionEquipaje("3 maletas medianasS");
		 
		 List<Reserva> listaReservas= new ArrayList<Reserva>();
		 
		 listaReservas.add(reserva1);
		 listaReservas.add(reserva2);
		 listaReservas.add(reserva3);
		 this.listaReservas=listaReservas;
		 
			
		 	List<String> paradas= new ArrayList<String>();
			paradas.add("Zahinos");
			paradas.add("Badajoz");
			paradas.add("Oliva de la Frontera");
			this.paradas=paradas;
			
		 
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testListadoReservas() throws Exception {
		
		given(this.reservaService.findAll()).willReturn(listaReservas);
		mockMvc.perform(get("/reservas/reservasList")).andExpect(status().isOk())
		.andExpect(model().attributeExists("reservas"))
		.andExpect(model().attribute("reservas", is(listaReservas)))
		.andExpect(status().is2xxSuccessful())
		.andExpect(view().name("reservas/reservasList"));
		
	}
	
	@WithMockUser(value = "spring")
    @Test
    void nuevaReservaTest1() throws Exception {
		Date today= new Date();
		Reserva reserva= new Reserva();
		given(this.utilService.addFecha(today, Calendar.MINUTE, 45)).willReturn(today);
		given(this.trayectoService.findDistinctParadas()).willReturn(paradas);
		
		mockMvc.perform(get("/reservas/new")).andExpect(status().isOk())
		.andExpect(model().attributeExists("reserva"))
		.andExpect(model().attribute("paradas", is(paradas)))
		.andExpect(model().attribute("numCiudadesIntermedias", is(0)))
		.andExpect(model().attribute("finBucle", is(0)))
		.andExpect(status().isOk())
		.andExpect(view().name("reservas/newReservaForm"));
	}
	

	@WithMockUser(value = "spring")
    @Test
    void redirigirNewReservaFormTestBindingError() throws Exception {

		given(this.trayectoService.findDistinctParadas()).willReturn(paradas);
		
		mockMvc.perform(post("/reservas/redirigirNewReservaForm")
				.with(csrf())
				.param("numCiudadesIntermedias","0")
				.param("ruta.origenCliente", "Zahinos")
				.param("ruta.destinoCliente","Oliva de la Frontera")
				.param("fechaSalida", "1234")
				.param("horaSalida", "12354")
				.param("plazas_Ocupadas", "3")
				.param("descripcionEquipaje", "Maleta mediana")
				.param("action", "continuar"))
				.andExpect(model().attributeHasErrors("reserva"))
				.andExpect(model().attributeHasFieldErrors("reserva", "fechaSalida"))
				.andExpect(model().attributeHasFieldErrors("reserva", "horaSalida"))
				.andExpect(model().attributeExists("reserva"))
				.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
				.andExpect(model().attribute("paradas",paradas))
				.andExpect(model().attribute("numCiudadesIntermedias",0))
				.andExpect(model().attribute("finBucle",-1))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("reservas/newReservaForm"));
		
	}
	
	@WithMockUser(value = "spring")
    @Test
    void redirigirNewReservaFormTest() throws Exception {
		Reserva reserva= new Reserva();
		
		Ruta ruta= new Ruta();
		List<Trayecto> trayectosIntermedios= new ArrayList<Trayecto>();
		ruta.setTrayectos(trayectosIntermedios);
		reserva.setRuta(ruta);
		given(this.trayectoService.findDistinctParadas()).willReturn(paradas);
		
		given(this.reservaService.calcularReservaAPartirDeRuta(Mockito.any(Reserva.class), Mockito.anyBoolean(), Mockito.anyBoolean())).willReturn(reserva);
		given(this.rutaService.calcularHorasRutaCliente(Mockito.any(Ruta.class))).willReturn(1);
		given(this.rutaService.calcularMinutosRutaCliente(Mockito.any(Ruta.class))).willReturn(12);
		Set<String> authorities= new HashSet<String>();
		authorities.add("taxista");
		List<Cliente> clientes= new ArrayList<Cliente>();
		given(this.clienteService.findAll()).willReturn(new ArrayList<Cliente>());
		given(this.authoService.findAuthoritiesByUsername(Mockito.anyString())).willReturn(authorities);
			
		
		mockMvc.perform(post("/reservas/redirigirNewReservaForm")
				.with(csrf())
				.param("numCiudadesIntermedias","0")
				.param("ruta.origenCliente", "Zahinos")
				.param("ruta.destinoCliente","Oliva de la Frontera")
				.param("fechaSalida", "2021-02-08")
				.param("horaSalida", "17:40")
				.param("plazas_Ocupadas", "3")
				.param("descripcionEquipaje", "Maleta mediana")
				.param("action", "continuar"))
				.andExpect(model().attribute("clientes",clientes))
				.andExpect(model().attribute("reserva",reserva))
				.andExpect(model().attribute("trayectosIntermedios",trayectosIntermedios))
				.andExpect(model().attribute("horasRutaCliente",1))
				.andExpect(model().attribute("minutosRutaCliente",12))
				.andExpect(model().attribute("numCiudadesIntermedias",0))
				.andExpect(model().attribute("finBucle",-1))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("reservas/precioReserva"));
		
	}
	
	@WithMockUser(value = "spring")
    @Test
    void redirigirNewReservaFormTestExceptionParadaDuplicada() throws Exception {
	
		given(this.trayectoService.findDistinctParadas()).willReturn(paradas);
		given(this.reservaService.calcularReservaAPartirDeRuta(Mockito.any(Reserva.class), Mockito.anyBoolean(), 
				Mockito.anyBoolean())).willThrow(DuplicatedParadaException.class);
		
		mockMvc.perform(post("/reservas/redirigirNewReservaForm")
				.with(csrf())
				.param("numCiudadesIntermedias","0")
				.param("ruta.origenCliente", "Zahinos")
				.param("ruta.destinoCliente","Oliva de la Frontera")
				.param("fechaSalida", "2021-02-08")
				.param("horaSalida", "17:40")
				.param("plazas_Ocupadas", "3")
				.param("descripcionEquipaje", "Maleta mediana")
				.param("action", "continuar"))
				.andExpect(model().attributeExists("reserva"))
				.andExpect(model().attribute("paradas",paradas))
				.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
				.andExpect(model().attribute("numCiudadesIntermedias",0))
				.andExpect(model().attribute("finBucle",-1))
				.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
				.andExpect(model().attribute("error","El origen y destino deben ser diferentes."
						+ " (Dos paradas consecutivas tampoco pueden ser iguales)"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("reservas/newReservaForm"));
		
	}
	
	@WithMockUser(value = "spring")
    @Test
    void redirigirNewReservaFormTestFechaSalidaAnteriorActualException() throws Exception {
	
		given(this.trayectoService.findDistinctParadas()).willReturn(paradas);
		given(this.reservaService.calcularReservaAPartirDeRuta(Mockito.any(Reserva.class), Mockito.anyBoolean(), 
				Mockito.anyBoolean())).willThrow(FechaSalidaAnteriorActualException.class);
		
		mockMvc.perform(post("/reservas/redirigirNewReservaForm")
				.with(csrf())
				.param("numCiudadesIntermedias","0")
				.param("ruta.origenCliente", "Zahinos")
				.param("ruta.destinoCliente","Oliva de la Frontera")
				.param("fechaSalida", "2021-02-08")
				.param("horaSalida", "17:40")
				.param("plazas_Ocupadas", "3")
				.param("descripcionEquipaje", "Maleta mediana")
				.param("action", "continuar"))
				.andExpect(model().attributeExists("reserva"))
				.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
				.andExpect(model().attribute("paradas",paradas))
				.andExpect(model().attribute("numCiudadesIntermedias",0))
				.andExpect(model().attribute("finBucle",-1))
				.andExpect(model().attribute("error","La fecha y hora de salida no puede ser anterior al instante actual"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("reservas/newReservaForm"));
		
	}
	
	@WithMockUser(value = "spring")
    @Test
    void redirigirNewReservaFormTestHoraSalidaSinAntelacionException() throws Exception {
	
		given(this.trayectoService.findDistinctParadas()).willReturn(paradas);
		given(this.reservaService.calcularReservaAPartirDeRuta(Mockito.any(Reserva.class), Mockito.anyBoolean(), 
				Mockito.anyBoolean())).willThrow(HoraSalidaSinAntelacionException.class);
		
		mockMvc.perform(post("/reservas/redirigirNewReservaForm")
				.with(csrf())
				.param("numCiudadesIntermedias","0")
				.param("ruta.origenCliente", "Zahinos")
				.param("ruta.destinoCliente","Oliva de la Frontera")
				.param("fechaSalida", "2021-02-08")
				.param("horaSalida", "17:40")
				.param("plazas_Ocupadas", "3")
				.param("descripcionEquipaje", "Maleta mediana")
				.param("action", "continuar"))
				.andExpect(model().attributeExists("reserva"))
				.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
				.andExpect(model().attribute("paradas",paradas))
				.andExpect(model().attribute("numCiudadesIntermedias",0))
				.andExpect(model().attribute("finBucle",-1))
				.andExpect(model().attribute("error","La fecha de salida debe realizarse con al menos 40 minutos de antelación"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("reservas/newReservaForm"));
		
	}
	
	@WithMockUser(value = "spring")
    @Test
    void redirigirNewReservaFormAddParadaTest() throws Exception {
		given(this.trayectoService.findDistinctParadas()).willReturn(paradas);
		
		mockMvc.perform(post("/reservas/redirigirNewReservaForm")
				.with(csrf())
				.param("numCiudadesIntermedias","0")
				.param("ruta.origenCliente", "Zahinos")
				.param("ruta.destinoCliente","Oliva de la Frontera")
				.param("fechaSalida", "2021-02-08")
				.param("horaSalida", "17:40")
				.param("plazas_Ocupadas", "3")
				.param("descripcionEquipaje", "Maleta mediana")
				.param("action", "addParada"))
				.andExpect(model().attributeExists("reserva"))
				.andExpect(model().attribute("paradas", paradas))
				.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
				.andExpect(model().attribute("numCiudadesIntermedias", 1))
				.andExpect(model().attribute("finBucle", 0)) //Se elimina el binding result para esta vista, los datos se comprobarán después...
				.andExpect(model().attributeDoesNotExist("org.springframework.validation.BindingResult.reserva"))
				.andExpect(status().is2xxSuccessful())
				.andExpect(view().name("reservas/newReservaForm"));
		
		
	}
	
	
	
		@WithMockUser(value = "spring")
		@Test
	    void redirigirNewReservaFormErrorImposible() throws Exception {
			given(this.trayectoService.findDistinctParadas()).willReturn(paradas);
			
			mockMvc.perform(post("/reservas/redirigirNewReservaForm")
					.with(csrf())
					.param("numCiudadesIntermedias","0")
					.param("ruta.origenCliente", "Zahinos")
					.param("ruta.destinoCliente","Oliva de la Frontera")
					.param("fechaSalida", "2021-02-08")
					.param("horaSalida", "17:40")
					.param("plazas_Ocupadas", "3")
					.param("descripcionEquipaje", "Maleta mediana")
					.param("action", "valorInventado"))
					.andExpect(status().isOk())
					.andExpect(view().name("exception"));
		
		}
		
		@WithMockUser(value = "spring")
	    @Test
	    void redirigirPrecioReservaTestBindingError() throws Exception {

			given(this.trayectoService.findDistinctParadas()).willReturn(this.paradas);
			
			mockMvc.perform(post("/reservas/redirigirPrecioReserva")
					.with(csrf())
					.param("numCiudadesIntermedias","0")
					.param("ruta.origenCliente", "Zahinos")
					.param("ruta.destinoCliente","Oliva de la Frontera")
					.param("fechaSalida", "1234")
					.param("horaSalida", "cadena")
					.param("plazas_Ocupadas", "10")
					.param("descripcionEquipaje", "Maleta mediana")
					.param("action", "confirmarReserva"))
					.andExpect(model().attributeHasErrors("reserva"))
					.andExpect(model().attributeHasFieldErrors("reserva", "fechaSalida"))
					.andExpect(model().attributeHasFieldErrors("reserva", "horaSalida"))
					.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
					.andExpect(model().attributeHasFieldErrors("reserva", "plazas_Ocupadas"))
					.andExpect(model().attributeExists("reserva"))
					.andExpect(model().attribute("paradas",this.paradas))
					.andExpect(model().attribute("numCiudadesIntermedias",0))
					.andExpect(model().attribute("finBucle",-1))
					.andExpect(status().is2xxSuccessful())
					.andExpect(view().name("reservas/newReservaForm"));
			
		}
		
		@WithMockUser(value = "spring")
	    @Test
	    void redirigirPrecioReservaConfirmarReservaSuccess() throws Exception {
			Reserva reserva= new Reserva();
			given(this.trayectoService.findDistinctParadas()).willReturn(this.paradas);
			given(this.reservaService.calcularYConfirmarNuevaReserva(Mockito.any(Reserva.class),Mockito.anyString())).willReturn(reserva);
			given(this.utilService.addFecha(Mockito.any(Date.class), Mockito.anyInt(), Mockito.anyInt())).willReturn(new Date());
			mockMvc.perform(post("/reservas/redirigirPrecioReserva")
					.with(csrf())
					.param("numCiudadesIntermedias","0")
					.param("ruta.origenCliente", "Zahinos")
					.param("ruta.destinoCliente","Oliva de la Frontera")
					.param("fechaSalida", "2021-02-08")
					.param("horaSalida", "14:30")
					.param("plazas_Ocupadas", "2")
					.param("descripcionEquipaje", "Maleta mediana")
					.param("action", "confirmarReserva"))
					.andExpect(status().isOk())
					.andExpect(model().attribute("message","¡Reserva solicitada con éxito!"))
					.andExpect(model().attributeExists("reserva"))
					.andExpect(model().attribute("paradas",paradas))
					.andExpect(model().attribute("numCiudadesIntermedias",0))
					.andExpect(model().attribute("finBucle",0))
					.andExpect(view().name("reservas/newReservaForm"));
		}
		
		@WithMockUser(value = "spring")
	    @Test
	    void redirigirPrecioReservaDuplicatedParadaException() throws Exception {
			Reserva reserva= new Reserva();
			given(this.trayectoService.findDistinctParadas()).willReturn(this.paradas);
			given(this.reservaService.calcularYConfirmarNuevaReserva(Mockito.any(Reserva.class),Mockito.anyString()))
			.willThrow(DuplicatedParadaException.class);
			
			mockMvc.perform(post("/reservas/redirigirPrecioReserva")
					.with(csrf())
					.param("numCiudadesIntermedias","0")
					.param("ruta.origenCliente", "Zahinos")
					.param("ruta.destinoCliente","Oliva de la Frontera")
					.param("fechaSalida", "2021-02-08")
					.param("horaSalida", "14:30")
					.param("plazas_Ocupadas", "2")
					.param("descripcionEquipaje", "Maleta mediana")
					.param("action", "confirmarReserva"))
			.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
					.andExpect(status().isOk())
					.andExpect(model().attribute("error","El origen y destino deben ser diferentes."
							+ " (Dos paradas consecutivas tampoco pueden ser iguales)"))
					.andExpect(model().attributeExists("reserva"))
					.andExpect(model().attribute("paradas",paradas))
					.andExpect(model().attribute("numCiudadesIntermedias",0))
					.andExpect(model().attribute("finBucle",-1))
					.andExpect(view().name("reservas/newReservaForm"));
					
		}
		
		@WithMockUser(value = "spring")
	    @Test
	    void redirigirPrecioReservaFechaSalidaAnteriorActualException() throws Exception {
			Reserva reserva= new Reserva();
			given(this.trayectoService.findDistinctParadas()).willReturn(this.paradas);
			given(this.reservaService.calcularYConfirmarNuevaReserva(Mockito.any(Reserva.class),Mockito.anyString()))
			.willThrow(FechaSalidaAnteriorActualException.class);
			
			mockMvc.perform(post("/reservas/redirigirPrecioReserva")
					.with(csrf())
					.param("numCiudadesIntermedias","0")
					.param("ruta.origenCliente", "Zahinos")
					.param("ruta.destinoCliente","Oliva de la Frontera")
					.param("fechaSalida", "2021-02-08")
					.param("horaSalida", "14:30")
					.param("plazas_Ocupadas", "2")
					.param("descripcionEquipaje", "Maleta mediana")
					.param("action", "confirmarReserva"))
					.andExpect(status().isOk())
					.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
					.andExpect(model().attribute("error","La fecha y hora de salida no puede ser anterior al instante actual"))
					.andExpect(model().attributeExists("reserva"))
					.andExpect(model().attribute("paradas",paradas))
					.andExpect(model().attribute("numCiudadesIntermedias",0))
					.andExpect(model().attribute("finBucle",-1))
					.andExpect(view().name("reservas/newReservaForm"));
					
		}
		

		@WithMockUser(value = "spring")
	    @Test
	    void redirigirPrecioReservaHoraSalidaSinAntelacionException() throws Exception {
			Reserva reserva= new Reserva();
			given(this.trayectoService.findDistinctParadas()).willReturn(this.paradas);
			given(this.reservaService.calcularYConfirmarNuevaReserva(Mockito.any(Reserva.class),Mockito.anyString()))
			.willThrow(HoraSalidaSinAntelacionException.class);
			
			mockMvc.perform(post("/reservas/redirigirPrecioReserva")
					.with(csrf())
					.param("numCiudadesIntermedias","0")
					.param("ruta.origenCliente", "Zahinos")
					.param("ruta.destinoCliente","Oliva de la Frontera")
					.param("fechaSalida", "2021-02-08")
					.param("horaSalida", "14:30")
					.param("plazas_Ocupadas", "2")
					.param("descripcionEquipaje", "Maleta mediana")
					.param("action", "confirmarReserva"))
					.andExpect(model().attribute("error","La reserva debe realizarse con un mínimo de 1 hora de antelación"))
					.andExpect(model().attributeExists("reserva"))
					.andExpect(model().attribute("paradas",paradas))
					.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
					.andExpect(model().attribute("numCiudadesIntermedias",0))
					.andExpect(model().attribute("finBucle",-1))
					.andExpect(status().isOk())
					.andExpect(view().name("reservas/newReservaForm"));
		}
		
		@WithMockUser(value = "spring")
	    @Test
	    void redirigirPrecioAtras() throws Exception {
			Reserva reserva= new Reserva();
			given(this.trayectoService.findDistinctParadas()).willReturn(this.paradas);
			
			mockMvc.perform(post("/reservas/redirigirPrecioReserva")
					.with(csrf())
					.param("numCiudadesIntermedias","0")
					.param("ruta.origenCliente", "Zahinos")
					.param("ruta.destinoCliente","Oliva de la Frontera")
					.param("fechaSalida", "2021-02-08")
					.param("horaSalida", "14:30")
					.param("plazas_Ocupadas", "2")
					.param("descripcionEquipaje", "Maleta mediana")
					.param("action", "atras"))
					.andExpect(model().attributeExists("reserva"))
					.andExpect(model().attribute("paradas",paradas))
					.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
					.andExpect(model().attribute("numCiudadesIntermedias",0))
					.andExpect(model().attribute("finBucle",-1))
					.andExpect(status().isOk())
					.andExpect(view().name("reservas/newReservaForm"));
		}
		
		@WithMockUser(value = "spring")
	    @Test
	    void redirigirPrecioCambiarValorBotonSubmit() throws Exception {
			Reserva reserva= new Reserva();
			given(this.trayectoService.findDistinctParadas()).willReturn(this.paradas);
			
			mockMvc.perform(post("/reservas/redirigirPrecioReserva")
					.with(csrf())
					.param("numCiudadesIntermedias","0")
					.param("ruta.origenCliente", "Zahinos")
					.param("ruta.destinoCliente","Oliva de la Frontera")
					.param("fechaSalida", "2021-02-08")
					.param("horaSalida", "14:30")
					.param("plazas_Ocupadas", "2")
					.param("descripcionEquipaje", "Maleta mediana")
					.param("action", "valorInventado"))
					.andExpect(status().isOk())
					.andExpect(view().name("exception"));
		}
		
		@WithMockUser(value = "spring")
	    @Test
	    void borrarReservaTest1() throws Exception {
			Reserva reserva= new Reserva();
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));
			given(this.reservaService.findAll()).willReturn(listaReservas);

			mockMvc.perform(get("/reservas/delete/{reservaId}",1))
					.andExpect(model().attribute("message","Reserva eliminada correctamente"))
					.andExpect(model().attribute("reservas", this.listaReservas))
					.andExpect(status().isOk())
					.andExpect(view().name("reservas/reservasList"));
			
		}
		
		@WithMockUser(value = "spring")
	    @Test
	    void borrarReservaTestNoEncontrada() throws Exception {
			Reserva reserva= new Reserva();
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(null));
			given(this.reservaService.findAll()).willReturn(listaReservas);

			mockMvc.perform(get("/reservas/delete/{reservaId}",1))
					.andExpect(model().attribute("error","Reserva no encontrada!"))
					.andExpect(model().attribute("reservas", this.listaReservas))
					.andExpect(status().isOk())
					.andExpect(view().name("reservas/reservasList"));
			
		}
		
		@Nested
		class testsEditReservas{
			
			List<Trayecto> trayectosIntermedios;
			Reserva reserva;
			List<EstadoReserva> estadosReserva;
			List<Cliente> clientes;
			List<Trabajador> trabajadores;
			List<Automovil> automoviles;
			Set<String> authorities;
			@BeforeEach
			void setup() throws Exception{
				given(trayectoService.findDistinctParadas()).willReturn(paradas);

				//Largo pero necesario
				Reserva reserva= new Reserva();
				Ruta ruta= new Ruta();
				List<Trayecto> trayectosIntermedios= new ArrayList<Trayecto>();
				this.trayectosIntermedios=trayectosIntermedios;
				ruta.setTrayectos(trayectosIntermedios);
				reserva.setRuta(ruta);
				given(reservaService.calcularReservaAPartirDeRuta(Mockito.any(Reserva.class), Mockito.anyBoolean(), Mockito.anyBoolean())).willReturn(this.reserva);

				this.reserva=reserva;
				given(reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));
				given(reservaService.findAll()).willReturn(listaReservas);
				given(rutaService.obtenerTrayectosIntermedios(Mockito.any(Ruta.class))).willReturn(trayectosIntermedios);
				given(rutaService.calcularHorasRutaCliente(Mockito.any(Ruta.class))).willReturn(1);
				given(rutaService.calcularMinutosRutaCliente(Mockito.any(Ruta.class))).willReturn(12);	
				List<EstadoReserva> estadosReserva= new ArrayList<EstadoReserva>();
				this.estadosReserva=estadosReserva;
				given(estadoReservaService.findAll()).willReturn(this.estadosReserva);
				
				List<Cliente> clientes= new ArrayList<Cliente>();
				this.clientes=clientes;
				given(clienteService.findAll()).willReturn(this.clientes);
				
			
				
				List<Trabajador> trabajadores= new ArrayList<Trabajador>();
				this.trabajadores=trabajadores;
				given(trabajadorService.findAll()).willReturn(this.trabajadores);
				
				List<Automovil> automoviles=new ArrayList<Automovil>();
				this.automoviles=automoviles;
				given(autoService.findAll()).willReturn(this.automoviles);
				
				Set<String> authorities= new HashSet<String>();
				authorities.add("taxista");
				this.authorities=authorities;
				given(authoService.findAuthoritiesByUsername(Mockito.anyString())).willReturn(this.authorities);
			}
			
			@WithMockUser(value = "spring")
		    @Test
		    void editReservaTestSuccess() throws Exception {
				


				mockMvc.perform(get("/reservas/edit/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("estadosReserva",estadosReserva))
						.andExpect(model().attribute("clientes",clientes))
						.andExpect(model().attribute("trabajadores",trabajadores))
						.andExpect(model().attribute("automoviles",automoviles))
						.andExpect(model().attribute("reserva",reserva))
						.andExpect(model().attribute("trayectosIntermedios",trayectosIntermedios))
						.andExpect(model().attribute("horasRutaCliente",1))
						.andExpect(model().attribute("minutosRutaCliente",12))
						.andExpect(model().attribute("numCiudadesIntermedias",0))
						.andExpect(model().attribute("finBucle",-1))
						.andExpect(view().name("reservas/editReservaForm"));
				
			}
			
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditReservaFormErroresBinding() throws Exception {
				
				mockMvc.perform(post("/reservas/redirigirEditReservaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("fechaSalida", "cadena")
						.param("id", "1")
						.param("horaSalida", "cadena")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("plazas_Ocupadas", "10")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "editarRuta"))
						.andExpect(status().isOk())
						.andExpect(model().attributeHasFieldErrors("reserva", "fechaSalida"))
						.andExpect(model().attributeHasFieldErrors("reserva", "horaSalida"))
						.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
						.andExpect(model().attributeHasFieldErrors("reserva", "plazas_Ocupadas"))
						.andExpect(model().attribute("estadosReserva",estadosReserva))
						.andExpect(model().attribute("clientes",clientes))
						.andExpect(model().attribute("trabajadores",trabajadores))
						.andExpect(model().attribute("automoviles",automoviles))
						.andExpect(model().attributeExists("reserva"))
						.andExpect(model().attribute("trayectosIntermedios",trayectosIntermedios))
						.andExpect(model().attribute("horasRutaCliente",1))
						.andExpect(model().attribute("minutosRutaCliente",12))
						.andExpect(model().attribute("numCiudadesIntermedias",0))
						.andExpect(model().attribute("finBucle",-1))
						.andExpect(view().name("reservas/editReservaForm"));
						
			}
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditReservaEditRUTA() throws Exception { //Editar la RUTA de una reserva
				
				mockMvc.perform(post("/reservas/redirigirEditReservaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("id", "1")
						.param("fechaSalida", "2021-02-08")
						.param("horaSalida", "14:30")
						.param("plazas_Ocupadas", "2")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "editarRuta"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("paradas",paradas))
						.andExpect(model().attributeExists("reserva"))
						.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
						.andExpect(model().attribute("numCiudadesIntermedias",0))
						.andExpect(model().attribute("finBucle",-1))
						.andExpect(view().name("reservas/editRutaForm"));
						
			}
			
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditReservaGUARDARReserva() throws Exception { 
				
				mockMvc.perform(post("/reservas/redirigirEditReservaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("id", "1")
						.param("fechaSalida", "2021-02-08")
						.param("horaSalida", "14:30")
						.param("plazas_Ocupadas", "2")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "guardarReserva"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/reservasList"));
						
			}
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditReservaGUARDARReservaDuplicatedParadaException() throws Exception { 
				
				given(reservaService.guardarReservaEditada(Mockito.any(Reserva.class), Mockito.any(Reserva.class)))
				.willThrow(DuplicatedParadaException.class);
				

				mockMvc.perform(post("/reservas/redirigirEditReservaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("id", "1")
						.param("fechaSalida", "2021-02-08")
						.param("horaSalida", "14:30")
						.param("plazas_Ocupadas", "2")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "guardarReserva"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("paradas",paradas))
						.andExpect(model().attribute("numCiudadesIntermedias",0))
						.andExpect(model().attribute("finBucle",-1))
						.andExpect(model().attributeExists("reserva"))
						.andExpect(model().attribute("error","El origen y destino deben ser diferentes."
								+ " (Dos paradas consecutivas tampoco pueden ser iguales)"))
						.andExpect(view().name("reservas/editRutaForm"));
						
			}
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditReservaGUARDARReservaFechaLlegadaAnteriorSalidaException() throws Exception { 
				
				given(reservaService.guardarReservaEditada(Mockito.any(Reserva.class), Mockito.any(Reserva.class)))
				.willThrow(FechaLlegadaAnteriorSalidaException.class);
				

				mockMvc.perform(post("/reservas/redirigirEditReservaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("id", "1")
						.param("fechaSalida", "2021-02-08")
						.param("horaSalida", "14:30")
						.param("plazas_Ocupadas", "2")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "guardarReserva"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","La fecha de llegada no puede ser anterior a la de salida"))
						.andExpect(model().attribute("estadosReserva",estadosReserva))
						.andExpect(model().attribute("clientes",clientes))
						.andExpect(model().attribute("trabajadores",trabajadores))
						.andExpect(model().attribute("automoviles",automoviles))
						.andExpect(model().attributeExists("reserva"))
						.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
						.andExpect(model().attribute("trayectosIntermedios",trayectosIntermedios))
						.andExpect(model().attribute("horasRutaCliente",1))
						.andExpect(model().attribute("minutosRutaCliente",12))
						.andExpect(model().attribute("numCiudadesIntermedias",0))
						.andExpect(model().attribute("finBucle",-1))
						.andExpect(view().name("reservas/editReservaForm"));
				
						
			}
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditReservaGUARDARReservaNoPresente() throws Exception { 
				
				given(reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(null));

				

				mockMvc.perform(post("/reservas/redirigirEditReservaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("id", "1")
						.param("fechaSalida", "2021-02-08")
						.param("horaSalida", "14:30")
						.param("plazas_Ocupadas", "2")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "guardarReserva"))
						.andExpect(status().isOk())
						.andExpect(view().name("exception"));
						
			}
			
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditReservaGUARDARReservaValorDiferenteBotonSubmit() throws Exception {
				

				

				mockMvc.perform(post("/reservas/redirigirEditReservaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("id", "1")
						.param("fechaSalida", "2021-02-08")
						.param("horaSalida", "14:30")
						.param("plazas_Ocupadas", "2")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "valorInventado"))
						.andExpect(status().isOk())
						.andExpect(view().name("exception"));
						
			}
			
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditRutaFormRecalcularReservaBindingErrors() throws Exception { //Recalcular la ruta de una reserva

				mockMvc.perform(post("/reservas//redirigirEditRutaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("id", "1")
						.param("fechaSalida", "cadena")
						.param("horaSalida", "14:30")
						.param("plazas_Ocupadas", "2")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "recalcularReserva"))
						.andExpect(status().isOk())
						.andExpect(model().attributeHasFieldErrors("reserva", "fechaSalida"))
						.andExpect(model().attribute("paradas", paradas))
						.andExpect(model().attribute("numCiudadesIntermedias", 0))
						.andExpect(model().attribute("finBucle", -1))
						.andExpect(model().attributeExists("reserva"))
						.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
						.andExpect(view().name("reservas/editRutaForm"));
						
			}
			
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditRutaFormRecalcularReservaSuccess() throws Exception {
		
				mockMvc.perform(post("/reservas/redirigirEditRutaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("id", "1")
						.param("fechaSalida", "2021-02-08")
						.param("horaSalida", "14:30")
						.param("plazas_Ocupadas", "2")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "recalcularReserva"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("estadosReserva",estadosReserva))
						.andExpect(model().attribute("clientes",clientes))
						.andExpect(model().attribute("trabajadores",trabajadores))
						.andExpect(model().attribute("automoviles",automoviles))
						.andExpect(model().attribute("trayectosIntermedios",trayectosIntermedios))
						.andExpect(model().attribute("horasRutaCliente",1))
						.andExpect(model().attribute("minutosRutaCliente",12))
						.andExpect(model().attribute("numCiudadesIntermedias",0))
						.andExpect(model().attribute("finBucle",-1))
						.andExpect(view().name("reservas/editReservaForm"));
						
			}
			
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditRutaFormAddParada() throws Exception {
		
				mockMvc.perform(post("/reservas/redirigirEditRutaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("id", "1")
						.param("fechaSalida", "2021-02-08")
						.param("horaSalida", "14:30")
						.param("plazas_Ocupadas", "2")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "addParada"))
						.andExpect(status().isOk())
						.andExpect(model().attributeDoesNotExist("org.springframework.validation.BindingResult.reserva"))
						.andExpect(model().attribute("numCiudadesIntermedias",1))
						.andExpect(model().attribute("finBucle",0))
						.andExpect(model().attribute("paradas",paradas))
						.andExpect(model().attributeExists("reserva"))
						.andExpect(model().attribute("reserva", hasProperty("descripcionEquipaje",is("Maleta mediana"))))
						.andExpect(view().name("reservas/editRutaForm"));
						
			}
			
			@WithMockUser(value = "spring")
		    @Test
		    void redirigirEditRutaFormCambiarValorDelBoton() throws Exception {
		
				mockMvc.perform(post("/reservas/redirigirEditRutaForm")
						.with(csrf())
						.param("numCiudadesIntermedias","0")
						.param("ruta.origenCliente", "Zahinos")
						.param("ruta.destinoCliente","Oliva de la Frontera")
						.param("id", "1")
						.param("fechaSalida", "2021-02-08")
						.param("horaSalida", "14:30")
						.param("plazas_Ocupadas", "2")
						.param("horasRutaCliente", "1")
						.param("minutosRutaCliente", "12")
						.param("descripcionEquipaje", "Maleta mediana")
						.param("action", "Valor inventado"))
						.andExpect(status().isOk())
						.andExpect(view().name("exception"));
						
			}
		}
		
		
		
		@WithMockUser(value = "spring")
	    @Test
	    void editReservaTestReservaNoEncontrada() throws Exception {
			
		
			given(this.reservaService.findAll()).willReturn(listaReservas);
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(null));
		
			mockMvc.perform(get("/reservas/edit/{reservaId}",1))
					.andExpect(status().isOk())
					.andExpect(model().attribute("error","No se ha encontrado la reserva a editar"))
					.andExpect(model().attribute("reservas",listaReservas))
					.andExpect(view().name("reservas/reservasList"));
		}
		@WithMockUser(value = "spring")
		@Test
		void listadoPeticionesReserva() throws Exception {
				
			
				given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);
			
				mockMvc.perform(get("/reservas//peticionesReservas"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
				
			}
		
		@WithMockUser(value = "spring")
		@Test
		void aceptarReservaTest1() throws Exception {
			Reserva reserva= new Reserva();
			List<Automovil> automoviles=new ArrayList<Automovil>();
			given(autoService.findAll()).willReturn(automoviles);
			
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));
			
				mockMvc.perform(get("/reservas/aceptar/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("automoviles",automoviles))
						.andExpect(view().name("reservas/selectAutomovil"));
				
			}
		
		@WithMockUser(value = "spring")
		@Test
		void aceptarReservaTestReservaNoEncontrada() throws Exception {
		
			
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(null));
			given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);

				mockMvc.perform(get("/reservas/aceptar/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","Reserva no encontrada"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
				
			}
		
		@WithMockUser(value = "spring")
		@Test
		void aceptarReservaPostTest() throws Exception {
		
			Reserva reserva= new Reserva();
			Automovil auto= new Automovil();
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));
			given(this.autoService.findAutomovilById(Mockito.anyInt())).willReturn(Optional.ofNullable(auto));

			given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);

				mockMvc.perform(post("/reservas/aceptar/{reservaId}",1)
						.with(csrf())
						.param("reservaId", "1")
						.param("autoId","1"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("message","Reserva aceptada correctamente"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
				
			}

		@WithMockUser(value = "spring")
		@Test
		void aceptarReservaPostReservaNoEncontrada() throws Exception {
		
			
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(null));
			given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);

				mockMvc.perform(post("/reservas/aceptar/{reservaId}",1)
						.with(csrf())
						.param("reservaId", "1")
						.param("autoId","1"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","Reserva no encontrada"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
				
			}
		

		@WithMockUser(value = "spring")
		@Test
		void aceptarReservaPostAutomovilNoEncontrado() throws Exception {
		
			Reserva reserva= new Reserva();
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));
			given(this.autoService.findAutomovilById(Mockito.anyInt())).willReturn(Optional.ofNullable(null));

			given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);

				mockMvc.perform(post("/reservas/aceptar/{reservaId}",1)
						.with(csrf())
						.param("reservaId", "1")
						.param("autoId","1"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","El automóvil que se ha intentado asignar no existe"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
			}
		
		@WithMockUser(value = "spring")
		@Test
		void aceptarReservaPostParadaYaAceptadaRechazadaException() throws Exception {
			Reserva reserva= new Reserva();
			Automovil auto= new Automovil();
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));
			given(this.autoService.findAutomovilById(Mockito.anyInt())).willReturn(Optional.ofNullable(auto));

			given(this.reservaService.aceptarReserva(Mockito.any(Reserva.class), Mockito.any(Automovil.class), Mockito.anyString()))
			.willThrow(ParadaYaAceptadaRechazadaException.class);
			

			
			given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);

				mockMvc.perform(post("/reservas/aceptar/{reservaId}",1)
						.with(csrf())
						.param("reservaId", "1")
						.param("autoId","1"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","La reserva que se intenta aceptar ya ha sido aceptada/rechazada anteriormente"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
				
			
			}
		@WithMockUser(value = "spring")
		@Test
		void aceptarReservaPostAutomovilPlazasInsuficientesException() throws Exception {
			Reserva reserva= new Reserva();
			Automovil auto= new Automovil();
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));
			given(this.autoService.findAutomovilById(Mockito.anyInt())).willReturn(Optional.ofNullable(auto));

			given(this.reservaService.aceptarReserva(Mockito.any(Reserva.class), Mockito.any(Automovil.class), Mockito.anyString()))
			.willThrow(AutomovilPlazasInsuficientesException.class);
			

			
			given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);

				mockMvc.perform(post("/reservas/aceptar/{reservaId}",1)
						.with(csrf())
						.param("reservaId", "1")
						.param("autoId","1"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","El automóvil que ha seleccionado no "
								+ "tiene suficientes plazas para realizar la reserva"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
				
			
			}
		
		@WithMockUser(value = "spring")
		@Test
		void aceptarReservaPostExisteViajeEnEsteHorarioException() throws Exception {
			Reserva reserva= new Reserva();
			Automovil auto= new Automovil();
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));
			given(this.autoService.findAutomovilById(Mockito.anyInt())).willReturn(Optional.ofNullable(auto));

			given(this.reservaService.aceptarReserva(Mockito.any(Reserva.class), Mockito.any(Automovil.class), Mockito.anyString()))
			.willThrow(ExisteViajeEnEsteHorarioException.class);
			

			
			given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);

				mockMvc.perform(post("/reservas/aceptar/{reservaId}",1)
						.with(csrf())
						.param("reservaId", "1")
						.param("autoId","1"))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","El taxista ya tiene una reserva aceptada en este periodo de tiempo."))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
				
			
			}
		
		@WithMockUser(value = "spring")
		@Test
		void rechazarReserva() throws Exception {
			Reserva reserva= new Reserva();
		
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));
	
			given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);

				mockMvc.perform(get("/reservas/rechazar/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("message","Reserva rechazada correctamente"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
				
			}
		
		@WithMockUser(value = "spring")
		@Test
		void rechazarReservaNoEncontrada() throws Exception {
		
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(null));
	
			given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);

				mockMvc.perform(get("/reservas/rechazar/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","Reserva no encontrada"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
				
			}

		@WithMockUser(value = "spring")
		@Test
		void rechazarReservaParadaYaAceptadaRechazadaException() throws Exception {
			Reserva reserva= new Reserva();
		
			given(this.reservaService.rechazarReserva(Mockito.any(Reserva.class))).willThrow(ParadaYaAceptadaRechazadaException.class);
			given(this.reservaService.findReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));
			given(this.reservaService.findPeticionesReserva()).willReturn(listaReservas);

				mockMvc.perform(get("/reservas/rechazar/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","La reserva que se intenta rechazar ya "
								+ "ha sido aceptada/rechazada anteriormente"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/peticionesReservas"));
				
			}
		
		@WithMockUser(value = "spring")
		@Test
		void reservaFacturaTest() throws Exception {
			Reserva reserva= new Reserva();
			
			Map<String,Double> facturaMap= new HashMap<String,Double>();
			given(this.reservaService.calcularFactura(Mockito.anyInt())).willReturn(facturaMap);
			given(this.reservaService.findFacturaReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));

				mockMvc.perform(get("/reservas/reservaFactura/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("factura",facturaMap))
						.andExpect(model().attribute("reserva",reserva))
						.andExpect(view().name("reservas/reservaFactura"));
				
			}
		@WithMockUser(value = "spring")
		@Test
		void reservaFacturaTestReservaNoEncontrada() throws Exception {
			given(this.reservaService.findAll()).willReturn(listaReservas);
			given(this.reservaService.findFacturaReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(null));

				mockMvc.perform(get("/reservas/reservaFactura/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","No se ha encontrado la factura"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/reservasList"));
				
			}
		
		@WithMockUser(value = "spring")
		@Test
		void reservaFacturaTestEstadoReservaFacturaException() throws Exception {
			given(this.reservaService.findAll()).willReturn(listaReservas);
			given(this.reservaService.findFacturaReservaById(Mockito.anyInt())).willThrow(EstadoReservaFacturaException.class);
				mockMvc.perform(get("/reservas/reservaFactura/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","Estado de reserva no completado"))
						.andExpect(model().attribute("reservas",listaReservas))
						.andExpect(view().name("reservas/reservasList"));
				
			}
		
		
		@WithMockUser(value = "spring")
		@Test
		void reservaFacturaTestCliente() throws Exception {
			Reserva reserva= new Reserva();
			
			Map<String,Double> facturaMap= new HashMap<String,Double>();
			given(this.reservaService.calcularFactura(Mockito.anyInt())).willReturn(facturaMap);
			given(this.reservaService.findFacturaReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(reserva));

				mockMvc.perform(get("/reservas/reservaMiFactura/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("factura",facturaMap))
						.andExpect(model().attribute("reserva",reserva))
						.andExpect(view().name("reservas/reservaFactura"));
				
			}
		@WithMockUser(value = "spring")
		@Test
		void reservaFacturaTestReservaNoEncontradaCliente() throws Exception {
			
			given(this.clienteController.showReservas(Mockito.any(ModelMap.class), Mockito.any(Principal.class)))
			.willReturn("reservas/misReservas");
			given(this.reservaService.findFacturaReservaById(Mockito.anyInt())).willReturn(Optional.ofNullable(null));

				mockMvc.perform(get("/reservas/reservaMiFactura/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","No se ha encontrado la factura"))
						.andExpect(view().name("reservas/misReservas"));
				
			}
		
		@WithMockUser(value = "spring")
		@Test
		void reservaFacturaTestEstadoReservaFacturaExceptionCliente() throws Exception {
			given(this.clienteController.showReservas(Mockito.any(ModelMap.class), Mockito.any(Principal.class)))
			.willReturn("reservas/misReservas");
			given(this.reservaService.findFacturaReservaById(Mockito.anyInt())).willThrow(EstadoReservaFacturaException.class);
				mockMvc.perform(get("/reservas/reservaMiFactura/{reservaId}",1))
						.andExpect(status().isOk())
						.andExpect(model().attribute("error","Estado de reserva no completado"))
						.andExpect(view().name("reservas/misReservas"));
				
			}
		
		
	
		
		
		
		
		
	
}
