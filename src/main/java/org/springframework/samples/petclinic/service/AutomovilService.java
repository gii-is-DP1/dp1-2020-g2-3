package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.repository.AutomovilRepository;
import org.springframework.samples.petclinic.service.exceptions.AutomovilAsignadoServicioReservaException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.web.ReservaController;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
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
		log.info("Obteniendo listado de automóviles");
		 return autoRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Automovil> findAutomovilById(int id) throws DataAccessException {
		
		return autoRepo.findById(id);
	}
	
	@Transactional()
	public Automovil delete(Automovil auto) throws AutomovilAsignadoServicioReservaException  {
			
			try {
				autoRepo.delete(auto);
				log.info("Automóvil borrado");
				return auto;
			}catch(DataAccessException e) {
				log.error("Se ha intentado borrar un automóvil que tenía asignada una reserva o servicio");
				//necesario generar una excepción PERSONALIZADA para luego testearla en el controller test
				throw new AutomovilAsignadoServicioReservaException();
			}
			
			
	
	}
	@Transactional()
	public void save(Automovil auto)  {
		log.info("Automóvil guardado");
		autoRepo.save(auto);
	}
	
}
