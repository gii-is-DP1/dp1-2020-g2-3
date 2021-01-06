package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.samples.petclinic.repository.TarifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TarifaService {
	@Autowired
	private TarifaRepository tariRep;
	
	@Transactional
	public long tarifaCount() {
		return tariRep.count();
	}
	
	@Transactional
	public Iterable<Tarifa> findAll(){
		 return tariRep.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Tarifa> findTarifaById(int TarifaId) throws DataAccessException {
		return tariRep.findById(TarifaId);
	}
	
	@Transactional()
	public void delete(Tarifa tari) throws DataAccessException  {
			tariRep.delete(tari);
	}
	
	@Transactional()
	public void save(Tarifa tari)  {
		
		tariRep.save(tari);
	}

}