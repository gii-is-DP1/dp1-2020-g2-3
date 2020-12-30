package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Automovil;
import org.springframework.samples.petclinic.model.Reserva;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.AutomovilRepository;
import org.springframework.samples.petclinic.repository.ClienteRepository;
import org.springframework.samples.petclinic.repository.ReservaRepository;
import org.springframework.samples.petclinic.repository.RutaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaService {

	@Autowired
	private ReservaRepository reservaRepo;
	@Autowired
	private RutaRepository rutaRepo;
	@Autowired
	private TrayectoRepository trayectoRepo;
	
	@Transactional
	public Iterable<Reserva> findAll(){
		 return reservaRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Optional<Reserva> findReservaById(int id) throws DataAccessException {
		return reservaRepo.findById(id);
	}
	
	@Transactional
	public Double calcularPrecio(Double kmTotal, Double precioPorKm) {
		//precioPorKm realmente habría que buscarlo de la tarifa que se encuentre activa en este momento.
		
		return kmTotal*precioPorKm;								
		
	}
	@Transactional
	public Date calcularFechaYHoraLlegada(Date fechaSalida, Date horaSalida, Double horasEstimadasCliente) {
		int minutosSumar= (int)Math.round(horasEstimadasCliente*60); //Devolvemos un Date con la fecha y hora de llegada
		fechaSalida.setHours(horaSalida.getHours());
		fechaSalida.setMinutes(horaSalida.getMinutes());
		
		if(minutosSumar!=0) {
			 Calendar calendar = Calendar.getInstance();
		      calendar.setTime(fechaSalida); 
		      calendar.add(Calendar.MINUTE, minutosSumar); 
		      fechaSalida= calendar.getTime(); 
		     
		}
		return fechaSalida;
	}
	
	@Transactional
	public void delete(Reserva reserva) throws DataAccessException  {
			reservaRepo.delete(reserva);
	}
	@Transactional
	public void save(Reserva reserva)  {
		
		reservaRepo.save(reserva);
	}
	
}
