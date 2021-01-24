package org.springframework.samples.petclinic.repository;

import java.util.Set;


import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Authorities;



public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{
	
	@Query("SELECT DISTINCT autho.authority FROM Authorities autho JOIN autho.user  WHERE autho.user.username = ?1 ")
	Set<String> findAuthoritiesByUsername(String username) throws DataAccessException;

}
