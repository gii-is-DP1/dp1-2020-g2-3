package org.springframework.samples.petclinic.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.repository.RutaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RutaService {

	@Autowired
	private RutaRepository rutaRepo;
	
	@Transactional
	public Iterable<Ruta> findAll(){
		 return rutaRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Ruta> findRutaById(int id) throws DataAccessException {
		return rutaRepo.findById(id);
	}
	
	@Transactional()
	public void delete(Ruta ruta) throws DataAccessException  {
			rutaRepo.delete(ruta);
	}
	@Transactional()
	public void save(Ruta ruta)  {
		
		rutaRepo.save(ruta);
	}
	
}
