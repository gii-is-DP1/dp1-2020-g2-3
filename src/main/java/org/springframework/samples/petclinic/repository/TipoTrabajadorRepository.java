package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.TipoTrabajador;


public interface TipoTrabajadorRepository extends CrudRepository<TipoTrabajador,Integer> {
	

}

