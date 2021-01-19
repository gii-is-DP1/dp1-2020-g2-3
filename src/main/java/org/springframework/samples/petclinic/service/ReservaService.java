package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.repository.TrabajadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository resRepo;
	
	
	@Transactional
	public long reservaCount() {
		
		return resRepo.count();
	}
	
	
	@Transactional
	public Iterable<Reserva> findAll(){
		 return resRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Reserva findById(int id) throws DataAccessException {
		return resRepo.findById(id);
	}
	
	@Transactional
	public Collection<Reserva> findReservasByClienteId(int id) throws DataAccessException {
		return resRepo.findReservasByClienteId(id);
	}


}
