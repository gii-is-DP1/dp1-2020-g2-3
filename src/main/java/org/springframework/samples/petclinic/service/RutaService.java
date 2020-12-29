package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Ruta;
import org.springframework.samples.petclinic.model.Trayecto;
import org.springframework.samples.petclinic.repository.RutaRepository;
import org.springframework.samples.petclinic.repository.TrayectoRepository;
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
	
	@Transactional()
	public Ruta calcularYAsignarTrayectos(Ruta rutaFormulario) {
			
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
			
			Trayecto trayectoInicial= trayectoRepo.findByOrigenAndDestino(rutaFormulario.getOrigenCliente(), 
					listaTrayectos.get(0).getOrigen());

			nuevaListaTrayectos.add(trayectoInicial);
			numKmTotal+=trayectoInicial.getNumKmTotales();
			horasEstimadasCliente+=trayectoInicial.getHorasEstimadas();
			
			int i=0;
			int listaSize= listaTrayectos.size();
			while(i<listaSize-1) {
				Trayecto trayectoIntermedio= trayectoRepo.findByOrigenAndDestino(listaTrayectos.get(i).getOrigen(),
						listaTrayectos.get(i+1).getOrigen());
				nuevaListaTrayectos.add(trayectoIntermedio);
				numKmTotal+=trayectoIntermedio.getNumKmTotales();
				horasEstimadasCliente+=trayectoIntermedio.getHorasEstimadas();
				i++;
			}
			Trayecto trayectoFinal=trayectoRepo.findByOrigenAndDestino(listaTrayectos.get(listaSize-1).getOrigen(), 
					rutaFormulario.getDestinoCliente());
			nuevaListaTrayectos.add(trayectoFinal);
			numKmTotal+=trayectoFinal.getNumKmTotales();
			horasEstimadasCliente+=trayectoFinal.getHorasEstimadas();
			
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
		rutaFormulario.setTrayectos(nuevaListaTrayectos);
		rutaFormulario.setHorasEstimadasCliente(horasEstimadasCliente);
		rutaFormulario.setNumKmTotales(numKmTotal);
		return rutaFormulario;
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
	
}
