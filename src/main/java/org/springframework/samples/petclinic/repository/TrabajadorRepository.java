package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Trabajador;

public interface TrabajadorRepository extends CrudRepository<Trabajador,Integer> {
	
	@Query("SELECT trabajador FROM Trabajador trabajador WHERE trabajador.id =:id")
	public Trabajador findById(@Param("id") int id);
	
	//@Query("SELECT trabajador FROM Trabajador trabajador WHERE trabajador.id =:id")
	//public Collection<Trabajador> findAllActivos();

	@Query("SELECT trabajador FROM Trabajador trabajador WHERE trabajador.user.username =:username")
	public Trabajador findByUsername(@Param("username") String username);


}

