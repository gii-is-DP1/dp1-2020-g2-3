package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Tarifa;

public interface TarifaRepository extends CrudRepository<Tarifa,Integer>{
	
	@Query("FROM Tarifa WHERE original=true")
	public Iterable<Tarifa> findByOriginal();
	
	
	@Query("SELECT t FROM Tarifa t WHERE t.activado=true")
	public Tarifa findTarifaActiva();
	
	
	@Query("SELECT t FROM Tarifa t WHERE t.precioPorKm =?1 AND t.porcentajeIvaRepercutido = ?2 and  t.precioEsperaPorHora =?3 and t.original = false")
	public Optional<Tarifa> findCopyByTarifa(Double precioPorKm,Integer porcentajeIvaRepercutido,Double precioEsperaPorHora);
	
	
}
