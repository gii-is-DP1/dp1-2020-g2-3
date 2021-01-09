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
	
	@Autowired
	public TrayectoService(TrayectoRepository trayectoRepo) {
		this.trayectoRepo=trayectoRepo;
	}
	/*la ruta que se le pasa como parámetro viene desde un formulario con la siguiente estructura:
	
	Tendrá como List<Trayecto> solo los trayectos intermedios,
    sin tener en cuenta origen-->parada1, tampoco tiene en cuenta que si origen!=Zahinos
    el primer trayecto debería ser Zahinos --> origen
    
    Además, cada trayecto intermedio será trayecto1= {origen=parada1, destino=null}
    
    El siguiente método calcula y encadena todos los trayectos necesarios a esa ruta
    */
	@Transactional
	public Ruta calcularYAsignarTrayectos(Ruta rutaFormulario) throws DataAccessException,DuplicatedParadaException {
			
		if(rutaFormulario.getOrigenCliente().equals(rutaFormulario.getDestinoCliente())) {
			
			throw new DuplicatedParadaException();
			
		}else {
			
			List<Trayecto> nuevaListaTrayectos= new ArrayList<Trayecto>();
			Double numKmTotal=0.0;
			Double horasEstimadasCliente=0.0;
			if(rutaFormulario.getTrayectos()==null || rutaFormulario.getTrayectos().size()==0) { //No hay paradas intermedias

					
					Trayecto trayecto= trayectoRepo.findByOrigenAndDestino(rutaFormulario.getOrigenCliente(), 
							rutaFormulario.getDestinoCliente());
					
					
					nuevaListaTrayectos.add(trayecto);
					numKmTotal+=trayecto.getNumKmTotales();
					horasEstimadasCliente+=trayecto.getHorasEstimadas();
				
				
			}else { //Hay paradas intermedias
				
				List<Trayecto> listaTrayectos= rutaFormulario.getTrayectos();
				
				if(rutaFormulario.getOrigenCliente().equals(listaTrayectos.get(0).getOrigen())) {
					System.out.println("origen igual a la primera parada intermedia");
					throw new DuplicatedParadaException();
				}else {
					Trayecto trayectoInicial= trayectoRepo.findByOrigenAndDestino(rutaFormulario.getOrigenCliente(), 
							listaTrayectos.get(0).getOrigen());

					nuevaListaTrayectos.add(trayectoInicial);
					numKmTotal+=trayectoInicial.getNumKmTotales();
					horasEstimadasCliente+=trayectoInicial.getHorasEstimadas();
					
					int i=0;
					int listaSize= listaTrayectos.size();
					while(i<listaSize-1) {
						if(listaTrayectos.get(i).getOrigen().equals(listaTrayectos.get(i+1).getOrigen())) {
							System.out.println("Parada intermedia i igual que la parada intermedia i+1");
							throw new DuplicatedParadaException();
						}else {
							Trayecto trayectoIntermedio= trayectoRepo.findByOrigenAndDestino(listaTrayectos.get(i).getOrigen(),
									listaTrayectos.get(i+1).getOrigen());
							nuevaListaTrayectos.add(trayectoIntermedio);
							numKmTotal+=trayectoIntermedio.getNumKmTotales();
							horasEstimadasCliente+=trayectoIntermedio.getHorasEstimadas();
							i++;
						}
						
					}
					if(listaTrayectos.get(listaSize-1).getOrigen().equals(rutaFormulario.getDestinoCliente())) {
						System.out.println("última parada intermedia igual al destino de la ruta");
						throw new DuplicatedParadaException();
					}else {
						Trayecto trayectoFinal=trayectoRepo.findByOrigenAndDestino(listaTrayectos.get(listaSize-1).getOrigen(), 
								rutaFormulario.getDestinoCliente());
						nuevaListaTrayectos.add(trayectoFinal);
						numKmTotal+=trayectoFinal.getNumKmTotales();
						horasEstimadasCliente+=trayectoFinal.getHorasEstimadas();
					}
					
					
				}
				
				
			}
			//En cualquier caso, hay que incorporar el trayecto [Zahinos --> OrigenCliente]  y [DestinoCliente ---> Zahinos]
			
			if(!rutaFormulario.getOrigenCliente().equals("Zahinos")) {
				Trayecto trayectoIdaTaxista= trayectoRepo.findByOrigenAndDestino("Zahinos", rutaFormulario.getOrigenCliente());
				nuevaListaTrayectos.add(0, trayectoIdaTaxista);
				numKmTotal+=trayectoIdaTaxista.getNumKmTotales();
				//en este caso no sumamos las horas estimadas porque el cliente no estará en el coche en este trayecto
			}
			if(!rutaFormulario.getDestinoCliente().equals("Zahinos")) {
				Trayecto trayectoVueltaTaxista= trayectoRepo.findByOrigenAndDestino(rutaFormulario.getDestinoCliente(),"Zahinos");
				nuevaListaTrayectos.add(trayectoVueltaTaxista);
				numKmTotal+=trayectoVueltaTaxista.getNumKmTotales();
				//en este caso no sumamos las horas estimadas porque el cliente no estará en el coche en este trayecto

			}
			Ruta nuevaRuta= new Ruta();
			double numKmTotalAproximado=Math.round(numKmTotal*100)/100;

			nuevaRuta.setTrayectos(nuevaListaTrayectos);
			nuevaRuta.setHorasEstimadasCliente(horasEstimadasCliente);
			nuevaRuta.setNumKmTotales(numKmTotalAproximado);
			nuevaRuta.setOrigenCliente(rutaFormulario.getOrigenCliente());
			nuevaRuta.setDestinoCliente(rutaFormulario.getDestinoCliente());
			return nuevaRuta;
			
		}
	
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
