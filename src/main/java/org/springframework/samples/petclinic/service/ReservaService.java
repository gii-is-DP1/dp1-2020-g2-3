package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.repository.AutomovilRepository;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.repository.RutaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

	@Autowired
	private ReservaRepository reservaRepo;
	private RutaRepository rutaRepo;
	private TrayectoRepository trayectoRepo;
	
	@Transactional
	public Iterable<Reserva> findAll(){
		 return reservaRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Reserva> findReservaById(int id) throws DataAccessException {
		return reservaRepo.findById(id);
	}
	
	@Transactional()
	public void delete(Reserva reserva) throws DataAccessException  {
			reservaRepo.delete(reserva);
	}
	@Transactional()
	public void save(Reserva reserva)  {
		
		reservaRepo.save(reserva);
	}
	
}
