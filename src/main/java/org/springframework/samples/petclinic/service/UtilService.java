package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.time.ZoneId;
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
	
	
	@Transactional
	public Date unirFechaHora(Date fecha, Date hora) { //dado un Date con una fecha DD-MM-YYYY y una fecha con la hora HH:mm, obtiene un Date
													//con la fecha del primer parámetro y la hora del segundo parámetro
		
		Date fechaHora= new Date();
		fechaHora.setDate(fecha.getDate());
		fechaHora.setMonth(fecha.getMonth());
		fechaHora.setYear(fecha.getYear());
		fechaHora.setHours(hora.getHours());
		fechaHora.setMinutes(hora.getMinutes());
		return fechaHora;
	   }
	
	@Transactional
	public Date asDate(LocalDate localDate) { //Convierte LocalDate a Date
	    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	  }

}
