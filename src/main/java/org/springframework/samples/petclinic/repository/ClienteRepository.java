package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Integer>{

	
	@Query("SELECT cliente FROM Cliente cliente WHERE cliente.apellidos LIKE :lastName%")
	public Collection<Cliente> findByNombre(@Param("lastName") String nombre);
	
	@Query("SELECT cliente FROM Cliente cliente WHERE cliente.id = :id")
	public Cliente findById(@Param("id") int id);
	
	@Query("SELECT cliente.id FROM Cliente cliente WHERE cliente.user.username = ?1")
	public Integer findIdByUsername(String username);
	
	@Query("SELECT cliente FROM Cliente cliente WHERE cliente.user.username = ?1")
	public Cliente findClienteByUsername(String username);
	

}
