package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;

public interface RutaRepository extends CrudRepository<Ruta,Integer> {
	
	@Query("SELECT DISTINCT ruta FROM Ruta ruta JOIN ruta.trayectos WHERE ruta.origenCliente = ?1 and ruta.destinoCliente = ?2 "
			+ " and ruta.numKmTotales = ?3  and ruta.horasEstimadasCliente = ?4 and ruta.horasEstimadasTaxista = ?5")
	public Collection<Ruta> findRutasByAttributes(String origenCliente, String destinoCliente,
			Double numKmTotales, Double horasEstimadasCliente,Double horasEstimadasTaxista);
	
	
}
