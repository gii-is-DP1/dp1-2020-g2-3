/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Servicio;
import org.springframework.samples.petclinic.repository.ServicioRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class ServicioService {
	
	@Autowired
	private ServicioRepository servicioRepository;


	@Transactional
	public long servicioCount() {
		
		return servicioRepository.count();
	}
	
	@Transactional
	public Iterable<Servicio> findAll(){
		 return servicioRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Servicio> findServicioById(int id) throws DataAccessException {
		return servicioRepository.findById(id);
	}
	
	@Transactional
	public Double calcularGastos(Date fecha1, Date fecha2) {
		Collection<Servicio> servicios = servicioRepository.findAllServicios();
		Double gastos = 0.0;
		for(Servicio s: servicios) {
			if(s.getFecha().after(fecha1) && s.getFecha().before(fecha2)) {
				gastos = gastos + s.getPrecio();
			}
		}
		return gastos;
	}
	
	@Transactional()
	public void delete(Servicio serv)  {
		
		servicioRepository.delete(serv);
		
	}
	@Transactional()
	public void save(Servicio serv)  {
		
		servicioRepository.save(serv);
	}
	

}
