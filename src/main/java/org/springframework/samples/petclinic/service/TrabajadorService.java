package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.repository.TrabajadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrabajadorService {

	@Autowired
	private TrabajadorRepository trabRepo;
	
	
	@Transactional
	public long trabajdorCount() {
		
		return trabRepo.count();
	}
	
	@Transactional
	public Iterable<Trabajador> findAll(){
		 return trabRepo.findAll();
	}
	
	
	@Transactional(readOnly = true)
	public Optional<Trabajador> findById(int id) throws DataAccessException {
		return trabRepo.findById(id);
	}
	
}
