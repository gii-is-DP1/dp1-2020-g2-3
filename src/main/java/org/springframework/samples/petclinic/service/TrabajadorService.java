package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.repository.TrabajadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrabajadorService {
	
	private TrabajadorRepository trabajadorRepository;
	
//	@Autowired
//	private AuthoritiesService authoritiesService;

	@Autowired
	public TrabajadorService(TrabajadorRepository trabajadorRepository) {
		this.trabajadorRepository = trabajadorRepository;
	}	
	
	@Transactional(readOnly = true)
	public Iterable<Trabajador> findAll(){
		return trabajadorRepository.findAll();
	}
}
