package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Contrato;

public interface ContratoRepository extends CrudRepository<Contrato,Integer> {
	
}
