package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.repository.AutomovilRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutomovilService {

	@Autowired
	private AutomovilRepository autoRepo;
	
	
	@Transactional
	public long automovilCount() {
		
		return autoRepo.count();
	}
	
	@Transactional
	public Iterable<Automovil> findAll(){
		 return autoRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Automovil> findAutomovilById(int id) throws DataAccessException {
		return autoRepo.findById(id);
	}
	
	@Transactional()
	public void delete(Automovil auto) throws DataAccessException  {
		
		autoRepo.delete(auto);
		
	}
	@Transactional()
	public void save(Automovil auto)  {
		
		autoRepo.save(auto);
	}
	
}
