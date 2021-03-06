package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Tarifa")
public class Tarifa extends BaseEntity{
	
	@Column(name = "precio_Por_Km")
	@NotNull
	private Double precioPorKm;
	
	@Column(name = "porcentaje_Iva_Repercutido")
	@NotNull
	private Integer porcentajeIvaRepercutido;
	
	@Column(name = "precio_Espera_Por_Hora")
	@NotNull
	private Double precioEsperaPorHora;
	
	@Column(name = "activado")
	private Boolean activado;
	
	@Column(name = "original")
	private Boolean original;
	
	public Double getPrecioPorKm() {
		return precioPorKm;
	}

	public void setPrecioPorKm(Double precioPorKm) {
		this.precioPorKm = precioPorKm;
	}

	public Integer getPorcentajeIvaRepercutido() {
		return porcentajeIvaRepercutido;
	}

	public void setPorcentajeIvaRepercutido(Integer porcentajeIvaRepercutido) {
		this.porcentajeIvaRepercutido = porcentajeIvaRepercutido;
	}
	
	public Double getPrecioEsperaPorHora() {
		return precioEsperaPorHora;
	}

	public void setPrecioEsperaPorHora(Double precioEsperaPorHora) {
		this.precioEsperaPorHora = precioEsperaPorHora;
	}
	
	public Boolean getActivado() {
		return activado;
	}

	public void setActivado(Boolean activado) {
		this.activado = activado;
	}
	
	public Boolean getOriginal() {
		return original;
	}

	public void setOriginal(Boolean original) {
		this.original = original;
	}
	
	
	
	

}
