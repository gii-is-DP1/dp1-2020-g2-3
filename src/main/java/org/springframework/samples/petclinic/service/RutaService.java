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

	@Autowired
	private RutaRepository rutaRepo;
	
	@Autowired
	private TrayectoRepository trayectoRepo;
	
	
	@Transactional
	public Iterable<Ruta> findAll(){
		 return rutaRepo.findAll();
	}
	
	@Transactional
	public Ruta calcularYAsignarTrayectos(Ruta rutaFormulario) throws DataAccessException,DuplicatedParadaException {
			
		if(rutaFormulario.getOrigenCliente().equals(rutaFormulario.getDestinoCliente())) {
			
			throw new DuplicatedParadaException();
			
		}else {
			
			List<Trayecto> nuevaListaTrayectos= new ArrayList<Trayecto>();
			Double numKmTotal=0.0;
			Double horasEstimadasCliente=0.0;
			if(rutaFormulario.getTrayectos()==null) { //No hay paradas intermedias
			
					
					Trayecto trayecto= trayectoRepo.findByOrigenAndDestino(rutaFormulario.getOrigenCliente(), 
							rutaFormulario.getDestinoCliente());
					nuevaListaTrayectos.add(trayecto);
					numKmTotal+=trayecto.getNumKmTotales();
					horasEstimadasCliente+=trayecto.getHorasEstimadas();
				
				
			}else { //Hay paradas intermedias
				
				List<Trayecto> listaTrayectos= rutaFormulario.getTrayectos();
				
				if(rutaFormulario.getOrigenCliente().equals(listaTrayectos.get(0).getOrigen())) {
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
