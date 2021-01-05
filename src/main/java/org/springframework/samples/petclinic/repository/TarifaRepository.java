package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Tarifa;

public interface TarifaRepository extends CrudRepository<Tarifa,Integer>{

}
