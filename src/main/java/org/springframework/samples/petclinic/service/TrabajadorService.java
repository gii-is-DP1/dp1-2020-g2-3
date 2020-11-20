package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	
}
