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

import java.util.Collection;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Servicio;
import org.springframework.samples.petclinic.model.Trabajador;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.repository.ServicioRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ServicioService {
		
	private ServicioRepository servicioRepository;

	@Autowired
	public ServicioService(ServicioRepository servicioRepository) {
		this.servicioRepository=servicioRepository;
		
	}

	
	
	
	@Transactional
	public long servicioCount() {
		
		return servicioRepository.count();
	}
	
	@Transactional
	public Iterable<Servicio> findAll(){
		 return servicioRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Servicio findServicioById(int id) throws DataAccessException {
		return servicioRepository.findById(id);
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
