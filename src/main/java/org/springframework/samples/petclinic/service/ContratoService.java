package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Contrato;
import org.springframework.samples.petclinic.repository.AutomovilRepository;
import org.springframework.samples.petclinic.repository.ContratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContratoService {

	@Autowired
	private ContratoRepository contratoRepo;
	
	
	@Transactional
	public long contratoCount() {
		
		return contratoRepo.count();
	}
	
	@Transactional
	public Iterable<Contrato> findAll(){
		 return contratoRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Contrato> findContratoById(int id) throws DataAccessException {
		return contratoRepo.findById(id);
	}
	
	
	@Transactional()
	public void save(Contrato contrato)  {
		
		contratoRepo.save(contrato);
	}
	
}
