package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Automovil;

public interface AutomovilRepository extends CrudRepository<Automovil,Integer> {

}
