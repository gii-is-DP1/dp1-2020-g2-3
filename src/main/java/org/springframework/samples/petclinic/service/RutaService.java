package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.RutaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RutaService {


	private RutaRepository rutaRepo;
	
	
	@Autowired
	public RutaService(RutaRepository rutaRepo) {
		this.rutaRepo=rutaRepo;
	}
	
	@Transactional
	public Iterable<Ruta> findAll(){
		 return rutaRepo.findAll();
	}
	
	
	@Transactional()
	public Optional<Ruta> findRutaByRuta(Ruta ruta){//Nos intentará devolver una ruta existente en la BD igual que la dada como parámetro 
													// comparando también sus TRAYECTOS asociados (Relación ManyToMany)
		List<Trayecto> trayectosRutaParametro= ruta.getTrayectos();
		Collection<Ruta> rutasPosibles=findRutasByAttributes(ruta.getOrigenCliente(),ruta.getDestinoCliente(),ruta.getNumKmTotales(),ruta.getHorasEstimadasCliente());
		Optional<Ruta> resultado= Optional.ofNullable(null);
		
	if(rutasPosibles.size()!=0 && rutasPosibles!=null) {
			//Comprobamos si los trayectos de alguna de esas rutas candidatas coinciden con los trayectos de nuestra ruta
			boolean rutaEncontrada=false;
		for(Ruta r:rutasPosibles) {
				List<Trayecto> trayectosPosibleRuta= r.getTrayectos();
				if(trayectosPosibleRuta.size()==trayectosRutaParametro.size()) {
					boolean coincidenTodosLosTrayectos=true;
					int i=0;
					while(i<trayectosPosibleRuta.size() && coincidenTodosLosTrayectos) { //Si hay algún trayecto que NO coincide la siguiente iteración
						Trayecto trayectoParametro=trayectosRutaParametro.get(i);			// del bucle no se ejecuta  y se pasa a la siguiente Ruta
						Trayecto trayectoPosibleRuta= trayectosPosibleRuta.get(i);
						
							if(!trayectoParametro.equals(trayectoPosibleRuta)) {
								coincidenTodosLosTrayectos=false;
								break;
							}
							i++;
						}
					if(coincidenTodosLosTrayectos) { //Cuando encontremos una ruta paramos el bucle for
						
						rutaEncontrada=true;
						resultado=Optional.ofNullable(r);
						break;
						}
					}
			}	
		}
		return resultado;	
	}
	
	
	@Transactional
	public int calcularHorasRutaCliente(Ruta ruta) {
		double totalHoras= ruta.getHorasEstimadasCliente();
		double parteDecimal= totalHoras%1;
		int horasCliente= (int) (totalHoras-parteDecimal);
		return horasCliente;
	}
	
	@Transactional
	public int calcularMinutosRutaCliente(Ruta ruta) {
		double totalHoras= ruta.getHorasEstimadasCliente();
		int minutosRealesAproximados= (int)Math.round((totalHoras%1)*60);
		return minutosRealesAproximados;
	}
	@Transactional(readOnly = true)
	public Optional<Ruta> findRutaById(int id) throws DataAccessException {
		return rutaRepo.findById(id);
	}
	
	@Transactional()
	public void delete(Ruta ruta) throws DataAccessException  {
			rutaRepo.delete(ruta);
	}
	@Transactional()
	public void save(Ruta ruta)  {
		
		rutaRepo.save(ruta);
	}
	
	@Transactional()
	public Collection<Ruta> findRutasByAttributes(String origenCliente, String destinoCliente,
			Double numKmTotales, Double horasEstimadasCliente)  {
		
		return rutaRepo.findRutasByAttributes(origenCliente, destinoCliente, numKmTotales, horasEstimadasCliente);
	}
	
	
	
	
}
