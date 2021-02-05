package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.TipoTrabajador;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.TrabajadorRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.service.exceptions.FechaFinAnteriorInicioException;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
class TrabajadorServiceMockedTests {

	@Mock
	private TrabajadorRepository trabRepo;
	
	@Mock
	private UserService userService;
	
	@Mock
	private AuthoritiesService authoritiesService;
	
	@Mock
	private TipoTrabajadorService tipotrabService;
    
	protected TrabajadorService trabService;

	@BeforeEach
    void setup() {
    	
		trabService= new TrabajadorService(trabRepo, userService, authoritiesService);
    	
    }
	
    @Test
    @Transactional
    @DisplayName("Fecha de fin de contrato anterior a la fecha de inicio")
    public void fechaFinInicio() {
    	//ARRANGE
    	//ARRANGE
    			User  nuevoUser = new User();
    			nuevoUser.setUsername("nuevo1");
    			nuevoUser.setPassword("nuevo1");
    			TipoTrabajador   nuevoTipoTrabajador = new TipoTrabajador();
    			nuevoTipoTrabajador.setName("NuevoTipo");
    			tipotrabService.save(nuevoTipoTrabajador);
    			Contrato nuevoContrato = new Contrato();
    			nuevoContrato.setFechaInicio(Date.valueOf(LocalDate.of(2015, 2, 15)));
    			nuevoContrato.setFechaFin(Date.valueOf(LocalDate.of(2015, 1, 15)));
    			nuevoContrato.setSalarioMensual(1200.00);
    			Trabajador nuevoTrabajador= new Trabajador();
    			nuevoTrabajador.setNombre("Paco");
    			nuevoTrabajador.setApellidos("Torres");
    			nuevoTrabajador.setDni("23422432Q");
    			nuevoTrabajador.setTelefono("995778443");
    			nuevoTrabajador.setTipoTrabajador(nuevoTipoTrabajador);
    			nuevoTrabajador.setUser(nuevoUser);
    			nuevoTrabajador.setContrato(nuevoContrato);
    			
    	// ACT & ASSERT
       	assertThrows(FechaFinAnteriorInicioException.class,()->trabService.save(nuevoTrabajador));
    }
    

	
    

   
    
}
