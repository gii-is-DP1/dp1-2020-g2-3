package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	
}
