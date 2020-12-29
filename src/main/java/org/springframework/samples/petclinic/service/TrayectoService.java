package org.springframework.samples.petclinic.service;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrayectoService {

	@Autowired
	private TrayectoRepository trayectoRepo;
	
	@Transactional
	public Iterable<Trayecto> findAll(){
		 return trayectoRepo.findAll();
	}
	
	@Transactional()
	public Iterable<String> findDistinctParadas(){
		return trayectoRepo.findDistinctParadas();
		
	}


	@Transactional(readOnly = true)
	public Optional<Trayecto> findTrayectoById(int id) throws DataAccessException {
		return trayectoRepo.findById(id);
	}
	
	@Transactional()
	public void delete(Trayecto trayecto) throws DataAccessException  {
			trayectoRepo.delete(trayecto);
	}
	@Transactional()
	public void save(Trayecto trayecto)  {
		
		trayectoRepo.save(trayecto);
	}
	

}