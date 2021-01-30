package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Reserva;

public interface ReservaRepository extends CrudRepository<Reserva,Integer> {
	

	@Query("SELECT  res FROM Reserva res where res.estadoReserva.name = 'Solicitada' ")
	Iterable<Reserva> findPeticionesReserva() throws DataAccessException;
	
}
