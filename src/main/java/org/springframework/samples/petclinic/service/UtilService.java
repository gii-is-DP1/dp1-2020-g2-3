package org.springframework.samples.petclinic.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.samples.petclinic.repository.TarifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtilService {
	@Transactional
	public double aproximarNumero(Double numero) { //Aproxima un  Double a 2 decimales
		double numeroAproximado=(double)Math.round(numero * 100d) / 100d;
		return numeroAproximado;
	}
	
	@Transactional
	public Date addFecha(Date fechaBase, int tipoFecha, int cantidadSumar ) { //Permite sumarle dias, horas o cualquier unidad de tiempo a otra fecha y te devuelve un Date del resultado
		   Calendar calendar = Calendar.getInstance();
		      calendar.setTime(fechaBase); 
		      calendar.add(tipoFecha, cantidadSumar);
		      return calendar.getTime();
	   }
	

}
