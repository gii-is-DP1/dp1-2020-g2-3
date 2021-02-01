package org.springframework.samples.petclinic.repository;

import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Trayecto;

public interface TrayectoRepository extends CrudRepository<Trayecto,Integer> {
	
	@Query("SELECT tr FROM Trayecto tr where tr.origen = ?1 and tr.destino = ?2")
	Trayecto findByOrigenAndDestino(String origen,String destino) throws DataAccessException;
	
	@Query("SELECT DISTINCT tr.origen AS paradas FROM Trayecto tr")
	Set<String> findDistinctParadas() throws DataAccessException;

}
