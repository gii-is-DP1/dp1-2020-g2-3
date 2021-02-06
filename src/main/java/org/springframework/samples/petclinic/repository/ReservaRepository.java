package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Cliente;
import org.springframework.samples.petclinic.model.Reserva;

public interface ReservaRepository extends CrudRepository<Reserva, Integer> {
	
	

	@Query("SELECT reserva FROM Reserva reserva WHERE reserva.cliente.id = :id")
	public Collection<Reserva> findReservasByClienteId(@Param("id") int id);
	
	@Query("SELECT reserva FROM Reserva reserva WHERE reserva.estadoReserva = '2' and reserva.trabajador.id = :id")
	public Collection<Reserva> findReservasAceptadasByTrabajadorId(@Param("id") int id);
	
	@Query("SELECT  res FROM Reserva res where res.estadoReserva.name = 'Solicitada' ")
	Iterable<Reserva> findPeticionesReserva() throws DataAccessException;
	
	@Query("SELECT reserva FROM Reserva reserva WHERE reserva.estadoReserva = '2' or reserva.estadoReserva = '4'")
	public Collection<Reserva> findByEstadoReservaCompletadaOAceptada();

	@Query("SELECT  res FROM Reserva res where res.estadoReserva.name = 'Solicitada' ")
	Iterable<Reserva> findPeticionesReserva() throws DataAccessException;
	
	@Query("SELECT  res FROM Reserva res where res.cliente.user.username = ?1")

	@Query("SELECT reserva FROM Reserva reserva WHERE reserva.id = :id")
	public Reserva findResById(@Param("id") int id);
}
