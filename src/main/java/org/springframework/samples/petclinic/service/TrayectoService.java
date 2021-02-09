package org.springframework.samples.petclinic.service;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedParadaException;
import org.springframework.samples.petclinic.web.ReservaController;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class TrayectoService {

	
	private TrayectoRepository trayectoRepo;
	private RutaService rutaService;
	private UtilService utilService;
	@Autowired
	public TrayectoService(TrayectoRepository trayectoRepo,RutaService rutaService,UtilService utilService) {
		this.trayectoRepo=trayectoRepo;
		this.rutaService=rutaService;
		this.utilService=utilService;
	}
	
	/*la ruta que se le pasa como parámetro viene desde un formulario con la siguiente estructura:
	
	Tendrá como List<Trayecto> solo los trayectos intermedios,
    sin tener en cuenta origen-->parada1, tampoco tiene en cuenta que si origen!=Zahinos
    el primer trayecto debería ser Zahinos --> origen
    
    Además, cada trayecto intermedio será trayecto1= {origen=parada1, destino=null}
    
    El siguiente método calcula y encadena todos los trayectos necesarios a esa ruta, con sus kilómeotros y horas estimadas totales
    */
	
	@Transactional
	public Ruta calcularYAsignarTrayectos(Ruta rutaFormulario) throws DataAccessException,DuplicatedParadaException {
		
		if(rutaFormulario.getOrigenCliente().equals(rutaFormulario.getDestinoCliente())) {
			throw new DuplicatedParadaException();
		}else {
			Ruta rutaConstruida= rutaService.inicializarRuta(rutaFormulario);
			if(!rutaFormulario.getOrigenCliente().equals("Zahinos")) { //Se añade el trayecto Zahinos --> Origen Cliente en caso de que Origen Cliente no sea Zahinos
				log.info("Se añade el trayecto Zahinos --> Origen Cliente");
				rutaConstruida=this.recalcularRutaAddTrayecto(rutaConstruida,"Zahinos", rutaFormulario.getOrigenCliente(),false);

			}
			
			List<Trayecto> paradasIntermedias= rutaFormulario.getTrayectos();
			
			if(paradasIntermedias==null || paradasIntermedias.size()==0) { //No hay paradas intermedias
				rutaConstruida= this.recalcularRutaAddTrayecto(rutaConstruida, rutaFormulario.getOrigenCliente(), rutaFormulario.getDestinoCliente(),true);
				
			}else { //Hay paradas intermedias
				log.info("Hay paradas intermedias");
				if(rutaFormulario.getOrigenCliente().equals(paradasIntermedias.get(0).getOrigen())) {
					log.error("Origen del cliente: " + rutaFormulario.getOrigenCliente() + ", igual a la primera parada intermedia:" + paradasIntermedias.get(0).getOrigen());
					throw new DuplicatedParadaException();
				}else {
					//Trayecto Origen Cliente ---> Primera parada intermedia
					rutaConstruida=this.recalcularRutaAddTrayecto(rutaConstruida, rutaFormulario.getOrigenCliente(), paradasIntermedias.get(0).getOrigen(),true);
					//Trayectos de parada intermedia i ----> parada intermedia i+1
					int i=0;
					int listaSize=paradasIntermedias.size();
					while(i<listaSize-1) {  
						
						if(paradasIntermedias.get(i).getOrigen().equals(paradasIntermedias.get(i+1).getOrigen())) {
							log.error("Parada intermedia i igual que la parada intermedia i+1");
							throw new DuplicatedParadaException();
						}else {
							rutaConstruida=this.recalcularRutaAddTrayecto(rutaConstruida, paradasIntermedias.get(i).getOrigen(), paradasIntermedias.get(i+1).getOrigen(),true);
							i++;
						}
					}
				//Trayecto última parada intermedia ---> Destino cliente
					if(paradasIntermedias.get(listaSize-1).getOrigen().equals(rutaFormulario.getDestinoCliente())) {
						log.error("última parada intermedia igual al destino de la ruta");
						throw new DuplicatedParadaException();
					}else { 
						
						rutaConstruida=this.recalcularRutaAddTrayecto(rutaConstruida,paradasIntermedias.get(listaSize-1).getOrigen(), rutaFormulario.getDestinoCliente(),true);
					}
				}
			}
			//En cualquier caso, hay que incorporar el trayecto [Zahinos --> OrigenCliente]  y [DestinoCliente ---> Zahinos]
			//en estos trayectos no se sumarán las horas del cliente, porque son trayectos exclusivos del taxista	
			if(!rutaFormulario.getDestinoCliente().equals("Zahinos")) {
				rutaConstruida=this.recalcularRutaAddTrayecto(rutaConstruida,rutaFormulario.getDestinoCliente(), "Zahinos",false);				

			}
			//Se aproximan los resultados a 2 decimales
			rutaConstruida.setNumKmTotales(utilService.aproximarNumero(rutaConstruida.getNumKmTotales()));
			rutaConstruida.setHorasEstimadasTaxista(utilService.aproximarNumero(rutaConstruida.getHorasEstimadasTaxista()));
			rutaConstruida.setHorasEstimadasCliente(utilService.aproximarNumero(rutaConstruida.getHorasEstimadasCliente()));
			log.info("Se ha contruido y calculado la ruta");
			return rutaConstruida;
			
		}
	}
	
	//Dada una ruta y un origen/destino, se añade dicho trayecto y se actualizan los km totales, y horas estimadas de la ruta
	@Transactional
	public Ruta recalcularRutaAddTrayecto(Ruta ruta,String origenTrayecto, String destinoTrayecto,boolean sumarHorasCliente) {
		List<Trayecto> nuevaListaTrayectos= ruta.getTrayectos();
		Trayecto trayectoReal=trayectoRepo.findByOrigenAndDestino(origenTrayecto, destinoTrayecto);
		nuevaListaTrayectos.add(trayectoReal);
		ruta.setTrayectos(nuevaListaTrayectos);
		ruta.setNumKmTotales(ruta.getNumKmTotales()+trayectoReal.getNumKmTotales());
		if(sumarHorasCliente) {
			ruta.setHorasEstimadasCliente(ruta.getHorasEstimadasCliente()+trayectoReal.getHorasEstimadas());
		}
		ruta.setHorasEstimadasTaxista(ruta.getHorasEstimadasTaxista()+trayectoReal.getHorasEstimadas());
		return ruta;
	}
	

	
	
	@Transactional 
	public Trayecto findByOrigenAndDestino(String origen,String destino) { //CUSTOM QUERY
		return trayectoRepo.findByOrigenAndDestino(origen, destino);
	}
	
	@Transactional
	public Iterable<Trayecto> findAll(){ //MÉTODO DE CRUD REPOSITORY
		 return trayectoRepo.findAll();
	}
	
	@Transactional()
	public Iterable<String> findDistinctParadas(){ //CUSTOM QUERY
		return trayectoRepo.findDistinctParadas();
		
	}


	@Transactional(readOnly = true)
	public Optional<Trayecto> findTrayectoById(int id) throws DataAccessException { //Método de CRUD REPOSITORY
		return trayectoRepo.findById(id);
	}
	
	@Transactional()
	public void delete(Trayecto trayecto) throws DataAccessException  { //MÉTODO DE CRUD REPOSITORY
			trayectoRepo.delete(trayecto);
	}
	@Transactional()
	public void save(Trayecto trayecto)  { //MÉTODO DE CRUD REPOSITORY
		
		trayectoRepo.save(trayecto);
	}
	

}
