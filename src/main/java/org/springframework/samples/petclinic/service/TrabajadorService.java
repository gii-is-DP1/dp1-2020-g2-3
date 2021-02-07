package org.springframework.samples.petclinic.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.repository.TrabajadorRepository;
import org.springframework.samples.petclinic.service.exceptions.FechaFinAnteriorInicioException;
import org.springframework.samples.petclinic.service.exceptions.FechaSalidaAnteriorActualException;
import org.springframework.samples.petclinic.web.ReservaController;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class TrabajadorService {
private TrabajadorRepository trabRepo;
	
	
	private UserService userService;
	
	
	private AuthoritiesService authoritiesService;
	
	@Autowired
	public TrabajadorService(TrabajadorRepository trabRepo, UserService userService, AuthoritiesService authoritiesService) {
		this.trabRepo = trabRepo;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
	}
	
	@Transactional
	public long trabajdorCount() {
		
		return trabRepo.count();
	}
	
	@Transactional
	public Iterable<Trabajador> findAll(){
		 return trabRepo.findAll();
	}
	public Trabajador findByUsername(String username) {
		return trabRepo.findByUsername(username);
	}
	
	
	@Transactional(readOnly = true)
	public Trabajador findById(int id) throws DataAccessException {
		return trabRepo.findById(id);
	}

	@Transactional
	public void save(Trabajador trabajador) throws FechaFinAnteriorInicioException  {
		
		trabRepo.save(trabajador);
		userService.saveUser(trabajador.getUser());
		authoritiesService.saveAuthorities(trabajador.getUser().getUsername(), trabajador.getTipoTrabajador().toString().toLowerCase());
		if(trabajador.getContrato().getFechaFin().compareTo(trabajador.getContrato().getFechaInicio())<0) { 
			log.error("fecha de fin es anterior a la de inicio, se lanza excepción");
			throw new FechaFinAnteriorInicioException();
		}else {
			log.info("la fecha de fin es posterior o igual a la de inicio, no se lanza excepción");
		}
	}
	


	
}

