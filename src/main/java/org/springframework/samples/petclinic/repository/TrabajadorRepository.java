package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Trabajador;

public interface TrabajadorRepository extends Repository<Trabajador, Integer>{
	
	void save(Trabajador trabajador) throws DataAccessException;
	
	Collection<Trabajador> findAll() throws DataAccessException;

}
