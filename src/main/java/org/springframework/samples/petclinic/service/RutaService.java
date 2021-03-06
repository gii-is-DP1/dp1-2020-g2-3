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
import org.springframework.samples.petclinic.web.ReservaController;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RutaService {


	private RutaRepository rutaRepo;
	
	
	@Autowired
	public RutaService(RutaRepository rutaRepo) {
		this.rutaRepo=rutaRepo;
	}
	
	@Transactional
	public Iterable<Ruta> findAll(){ //Método de CRUD Repository 
		 return rutaRepo.findAll();
	}
	
	
	@Transactional()
	public Optional<Ruta> findRutaByRuta(Ruta ruta){//Retorna una ruta  de la BD (si existe) igual que la dada como parámetro
													// comparando también sus TRAYECTOS asociados (Relación ManyToMany), la ruta dada como parámetro no tiene id
		List<Trayecto> trayectosRutaParametro= ruta.getTrayectos();
		Collection<Ruta> rutasPosibles=this.findRutasByAttributes(ruta.getOrigenCliente(),ruta.getDestinoCliente(),ruta.getNumKmTotales(),ruta.getHorasEstimadasCliente(),ruta.getHorasEstimadasTaxista());
		Optional<Ruta> resultado= Optional.ofNullable(null);
	if(rutasPosibles!=null && rutasPosibles.size()!=0) {
		
			//Comprobamos si los trayectos de alguna de esas rutas candidatas coinciden con los trayectos de nuestra ruta
			boolean rutaEncontrada=false;
		for(Ruta r:rutasPosibles) {
				List<Trayecto> trayectosPosibleRuta= r.getTrayectos();
				if(trayectosPosibleRuta.size()==trayectosRutaParametro.size()) {
					boolean coincidenTodosLosTrayectos=true;
					int i=0;
					while(i<trayectosPosibleRuta.size()) { //Si hay algún trayecto que NO coincide la siguiente iteración
						Trayecto trayectoParametro=trayectosRutaParametro.get(i);			// del bucle no se ejecuta  y se pasa a la siguiente Ruta
						Trayecto trayectoPosibleRuta= trayectosPosibleRuta.get(i);
						

							if(!trayectoParametro.equals(trayectoPosibleRuta)) { //Necesario porque puede ocurrir que ambas rutas tengan el mismo número de trayectos pero no sean iguales...
								coincidenTodosLosTrayectos=false;
								break;
							}
							i++;
						}
					if(coincidenTodosLosTrayectos) { //Cuando encontremos una ruta paramos el bucle for
						log.info("Se ha encontrado una ruta igual en la BD, con los mismos trayectos intermedios");
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
		log.info("Se han calculado las horas de la ruta del cliente");
		return horasCliente;
	}
	
	@Transactional
	public int calcularMinutosRutaCliente(Ruta ruta) {
		double totalHoras= ruta.getHorasEstimadasCliente();
		int minutosRealesAproximados= (int)Math.round((totalHoras%1)*60);
		log.info("se han calculado los minutos de la ruta del cliente");
		return minutosRealesAproximados;
	}
	@Transactional 
	public List<Trayecto> obtenerTrayectosIntermedios(Ruta ruta){ //obtenemos los trayectos intermedios empezando por el que tenga como origen la primera parada intermedia
		List<Trayecto> trayectosIntermedios= new ArrayList<Trayecto>();
		List<Trayecto> listaTrayectos= ruta.getTrayectos();
		
		boolean origenEncontrado= false;
		int i=0;
			while(i<listaTrayectos.size()) {
				
				Trayecto tr= listaTrayectos.get(i);
				
				if(!origenEncontrado && tr.getOrigen().equals(ruta.getOrigenCliente()) && tr.getDestino().equals(ruta.getDestinoCliente())) { //No hay paradas intermedias
					break;
				}else if(!origenEncontrado && tr.getOrigen().equals(ruta.getOrigenCliente())) {
					origenEncontrado=true;
					
				}else if(origenEncontrado) {
					trayectosIntermedios.add(tr);
					
					if(tr.getDestino().equals(ruta.getDestinoCliente())) {
						
						break;
					}
					
					
				}
				i++;
			}
			log.info("Se han obtenido los trayectos intermedios de la ruta");
		
		return trayectosIntermedios;
	}
	@Transactional(readOnly = true)
	public Optional<Ruta> findRutaById(int id) throws DataAccessException { //Método de CRUD Repository 
		return rutaRepo.findById(id);
	}
	
	@Transactional()
	public void delete(Ruta ruta) throws DataAccessException  { //Método de CRUD Repository 
			rutaRepo.delete(ruta);
	}
	@Transactional()
	public void save(Ruta ruta)  { //Método de CRUD Repository 
		
		rutaRepo.save(ruta);
	}
	
	@Transactional()
	public Collection<Ruta> findRutasByAttributes(String origenCliente, String destinoCliente,
			Double numKmTotales, Double horasEstimadasCliente, Double horasEstimadasTaxista)  {
		
		return rutaRepo.findRutasByAttributes(origenCliente, destinoCliente, numKmTotales, horasEstimadasCliente,horasEstimadasTaxista);
	}
	
	@Transactional
	public Ruta inicializarRuta(Ruta ruta)  {
		Ruta nuevaRuta= new Ruta();
		List<Trayecto> nuevaListaTrayectos= new ArrayList<Trayecto>();
		nuevaRuta.setNumKmTotales(0.0);
		nuevaRuta.setHorasEstimadasCliente(0.0);
		nuevaRuta.setHorasEstimadasTaxista(0.0);
		nuevaRuta.setTrayectos(nuevaListaTrayectos);
		nuevaRuta.setOrigenCliente(ruta.getOrigenCliente());
		nuevaRuta.setDestinoCliente(ruta.getDestinoCliente());
		return nuevaRuta;
		
	}
	
	
	
	
}
