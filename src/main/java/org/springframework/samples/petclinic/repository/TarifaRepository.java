package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Tarifa;

public interface TarifaRepository extends CrudRepository<Tarifa,Integer>{
	
	@Query("FROM Tarifa WHERE original=true")
	public Iterable<Tarifa> findByOriginal();
	
	
	@Query("SELECT t FROM Tarifa t WHERE t.activado=true")
	public Tarifa findTarifaActiva();
	
}
