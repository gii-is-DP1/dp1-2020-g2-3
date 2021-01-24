package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Reserva;

public interface ReservaRepository extends CrudRepository<Reserva, Integer> {
	
	@Query("SELECT reserva FROM Reserva reserva WHERE reserva.id =:id")
	public Reserva findById(@Param("id") int id);
	
	@Query("SELECT reserva FROM Reserva reserva WHERE reserva.cliente.id = :id")
	public Collection<Reserva> findReservasByClienteId(@Param("id") int id);
	
	

}

