package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Tarifa;
import org.springframework.samples.petclinic.repository.TarifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TarifaService {
	
	private TarifaRepository tariRep;

	@Autowired
	public TarifaService(TarifaRepository tariRep) {
		this.tariRep=tariRep;
		
	}
	
	@Transactional
	public Tarifa findTarifaActiva() {
		
		return tariRep.findTarifaActiva();
		
	}
	
	@Transactional
	public long tarifaCount() {
		return tariRep.count();
	}
	
	@Transactional
	public Iterable<Tarifa> findAll(){
		 return tariRep.findAll();
	}
	
	@Transactional
	public Iterable<Tarifa> findByOriginal(){
		 return tariRep.findByOriginal();
	}
	
	@Transactional
	public void saveTarifa(Tarifa tarifa) throws DataAccessException {
		tarifa.setOriginal(true);
		tariRep.save(tarifa);
	}
	
	@Transactional(readOnly = true)
	public Optional<Tarifa> findTarifaById(int TarifaId) throws DataAccessException {
		return tariRep.findById(TarifaId);
	}
	
	@Transactional()
	public void delete(Tarifa tari) throws DataAccessException  {
			tariRep.delete(tari);
	}
	
	@Transactional()
	public void save(Tarifa tari)  {
		
		tariRep.save(tari);
	}
	
	@Transactional()
	public Optional<Tarifa> findCopyOfTarifa(Tarifa tarifa)  { //Busca si hay una tarifa "copia" igual a la dada como parámetro
		
		return tariRep.findCopyByTarifa(tarifa.getPrecioPorKm(), tarifa.getPorcentajeIvaRepercutido(), tarifa.getPrecioEsperaPorHora());
	}
	
	@Transactional
	public  Tarifa nuevaTarifa(boolean activado,boolean original,Integer porcentajeIvaRepercutido,Double precioEsperaPorHora,Double precioPorKm) {
		Tarifa nuevaTarifa= new Tarifa();
		nuevaTarifa.setActivado(activado);
		nuevaTarifa.setOriginal(original);
		nuevaTarifa.setPorcentajeIvaRepercutido(porcentajeIvaRepercutido);
		nuevaTarifa.setPrecioEsperaPorHora(precioEsperaPorHora);
		nuevaTarifa.setPrecioPorKm(precioPorKm);
		return nuevaTarifa;
	}

}
