package org.springframework.samples.petclinic.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.EstadoReserva;
import org.springframework.samples.petclinic.repository.EstadoReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstadoReservaService {

	@Autowired
	private EstadoReservaRepository estadoRepo;
	
	

	
	@Transactional
	public Iterable<EstadoReserva> findAll(){ //CRUD REPOSITORY
		 return estadoRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<EstadoReserva> findEstadoById(int id) throws DataAccessException {  //CRUD REPOSITORY
		return estadoRepo.findById(id);
	}
	
	@Transactional()
	public void delete(EstadoReserva estado) throws DataAccessException  {  //CRUD REPOSITORY
		estadoRepo.delete(estado);
	}
	@Transactional()
	public void save(EstadoReserva estado)  {  //CRUD REPOSITORY
		
		estadoRepo.save(estado);
	}
	
}
