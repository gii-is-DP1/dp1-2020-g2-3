package org.springframework.samples.petclinic.model;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Getter 
@Setter
@Entity
@Table(name = "Ruta")
public class Ruta extends BaseEntity {

	
	@Column(name = "origen_cliente")
	@NotEmpty
	private String origenCliente;
	
	@Column(name = "destino_cliente")
	@NotEmpty
	private String destinoCliente;
	
	@Min(0)
	@Digits(fraction=2,integer=6)
	@Column(name = "num_Km_Totales")
	@NotNull
	private  Double numKmTotales;
	
	@Min(0)
	@Digits(fraction=2,integer=3)
	@Column(name = "horas_Estimadas_Cliente")
	@NotNull
	private  Double horasEstimadasCliente;
	
	@Min(0)
	@Digits(fraction=2,integer=3)
	@Column(name = "horas_Estimadas_Taxista")
	@NotNull
	private  Double horasEstimadasTaxista;
	
	
	@ManyToMany
	@OrderColumn
	List<Trayecto> trayectos;
	
	@Override
	public String toString() {
		return "Ruta [origenCliente=" + origenCliente + ", destinoCliente=" + destinoCliente + ", numKmTotales="
				+ numKmTotales + ", horasEstimadasCliente=" + horasEstimadasCliente + "]";
	}
	
	
	
}
