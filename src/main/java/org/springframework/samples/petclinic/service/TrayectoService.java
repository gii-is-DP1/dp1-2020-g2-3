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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrayectoService {

	
	private TrayectoRepository trayectoRepo;
	private RutaService rutaService;
	
	@Autowired
	public TrayectoService(TrayectoRepository trayectoRepo,RutaService rutaService) {
		this.trayectoRepo=trayectoRepo;
		this.rutaService=rutaService;
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
			
			Ruta rutaConstruida= this.rutaService.inicializarRuta(rutaFormulario);
			List<Trayecto> paradasIntermedias= rutaFormulario.getTrayectos();
			if(paradasIntermedias==null || paradasIntermedias.size()==0) { //No hay paradas intermedias
				rutaConstruida= this.recalcularRutaAddTrayecto(rutaConstruida, rutaFormulario.getOrigenCliente(), rutaFormulario.getDestinoCliente(),true);
				
			}else { //Hay paradas intermedias
				
				if(rutaFormulario.getOrigenCliente().equals(paradasIntermedias.get(0).getOrigen())) {
					System.out.println("origen igual a la primera parada intermedia");
					throw new DuplicatedParadaException();
				}else {
					//Trayecto Origen Cliente ---> Primera parada intermedia
					rutaConstruida=this.recalcularRutaAddTrayecto(rutaConstruida, rutaFormulario.getOrigenCliente(), paradasIntermedias.get(0).getOrigen(),true);
					//Trayectos de parada intermedia i ----> parada intermedia i+1
					int i=0;
					int listaSize=paradasIntermedias.size();
					while(i<listaSize-1) {  
						
						if(paradasIntermedias.get(i).getOrigen().equals(paradasIntermedias.get(i+1).getOrigen())) {
							System.out.println("Parada intermedia i igual que la parada intermedia i+1");
							throw new DuplicatedParadaException();
						}else {
							rutaConstruida=this.recalcularRutaAddTrayecto(rutaConstruida, paradasIntermedias.get(i).getOrigen(), paradasIntermedias.get(i+1).getOrigen(),true);
							i++;
						}
					}
				//Trayecto última parada intermedia ---> Destino cliente
					if(paradasIntermedias.get(listaSize-1).getOrigen().equals(rutaFormulario.getDestinoCliente())) {
						System.out.println("última parada intermedia igual al destino de la ruta");
						throw new DuplicatedParadaException();
					}else { 
						
						rutaConstruida=this.recalcularRutaAddTrayecto(rutaConstruida,paradasIntermedias.get(listaSize-1).getOrigen(), rutaFormulario.getDestinoCliente(),true);
					}
				}
			}
			//En cualquier caso, hay que incorporar el trayecto [Zahinos --> OrigenCliente]  y [DestinoCliente ---> Zahinos]
			//en estos trayectos no se sumarán las horas del cliente, porque son trayectos exclusivos del taxista	
			if(!rutaFormulario.getOrigenCliente().equals("Zahinos")) {
				rutaConstruida=this.recalcularRutaAddTrayecto(rutaConstruida,"Zahinos", rutaFormulario.getOrigenCliente(),false);

			}if(!rutaFormulario.getDestinoCliente().equals("Zahinos")) {
				rutaConstruida=this.recalcularRutaAddTrayecto(rutaConstruida,rutaFormulario.getDestinoCliente(), "Zahinos",false);				

			}
			//Se aproximan los resultados a 2 decimales
			rutaConstruida.setNumKmTotales(this.aproximarNumero(rutaConstruida.getNumKmTotales()));
			rutaConstruida.setHorasEstimadasTaxista(this.aproximarNumero(rutaConstruida.getHorasEstimadasTaxista()));
			rutaConstruida.setHorasEstimadasCliente(this.aproximarNumero(rutaConstruida.getHorasEstimadasCliente()));
			System.out.println("Horas estimadas Cliente: " + rutaConstruida.getHorasEstimadasCliente());
			return rutaConstruida;
			
		}
	}
	
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
	public double aproximarNumero(Double numero) {
		double numeroAproximado=(double)Math.round(numero * 100d) / 100d;
		return numeroAproximado;
	}
	
	
	@Transactional 
	public Trayecto findByOrigenAndDestino(String origen,String destino) {
		return trayectoRepo.findByOrigenAndDestino(origen, destino);
	}
	
	@Transactional
	public Iterable<Trayecto> findAll(){
		 return trayectoRepo.findAll();
	}
	
	@Transactional()
	public Iterable<String> findDistinctParadas(){
		return trayectoRepo.findDistinctParadas();
		
	}


	@Transactional(readOnly = true)
	public Optional<Trayecto> findTrayectoById(int id) throws DataAccessException {
		return trayectoRepo.findById(id);
	}
	
	@Transactional()
	public void delete(Trayecto trayecto) throws DataAccessException  {
			trayectoRepo.delete(trayecto);
	}
	@Transactional()
	public void save(Trayecto trayecto)  {
		
		trayectoRepo.save(trayecto);
	}
	

}
