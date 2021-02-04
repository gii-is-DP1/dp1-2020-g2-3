package org.springframework.samples.petclinic.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.TipoTrabajador;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.repository.TipoTrabajadorRepository;
import org.springframework.samples.petclinic.repository.TrabajadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoTrabajadorService {

	@Autowired
	private TipoTrabajadorRepository tipotrabRepo;
	
	
	@Transactional
	public long trabajdorCount() {
		
		return tipotrabRepo.count();
	}
	
	@Transactional
	public Iterable<TipoTrabajador> findAll(){
		 return tipotrabRepo.findAll();
	}
	
	
	@Transactional(readOnly = true)
	public Optional<TipoTrabajador> findById(int id) throws DataAccessException {
		return tipotrabRepo.findById(id);
	}

	@Transactional
	public void save(TipoTrabajador trabajador)  {
		
		tipotrabRepo.save(trabajador);
	}
	
	
}

