package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Taller;
import org.springframework.samples.petclinic.repository.AutomovilRepository;
import org.springframework.samples.petclinic.repository.TallerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TallerService {

	@Autowired
	private TallerRepository tallerRepo;
	
	
	@Transactional
	public long tallerCount() {
		
		return tallerRepo.count();
	}
	
	@Transactional
	public Iterable<Taller> findAll(){
		 return tallerRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Taller> findTallerById(int id) throws DataAccessException {
		return tallerRepo.findById(id);
	}
	
	@Transactional()
	public void delete(Taller taller) throws DataAccessException  {
		tallerRepo.delete(taller);
	}
	@Transactional()
	public void save(Taller taller)  {
		
		tallerRepo.save(taller);
	}
	
}
