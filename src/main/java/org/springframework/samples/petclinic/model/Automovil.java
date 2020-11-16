package org.springframework.samples.petclinic.model;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
@Data
@Entity
@Table(name = "Automovil")
public class Automovil extends BaseEntity {
	
	// Falta añadir restricciones simples
	
	@Column(name = "marca")    
	@NotEmpty
	private String marca;

	@Column(name = "modelo")
	@NotEmpty
	private  String modelo;
	
	
	
	@Column(name = "num_Plazas")
	@Digits(fraction = 0, integer = 7)
	@NotEmpty
	private  Integer numPlazas;
	
	@Column(name = "km_Recorridos")
	@NotEmpty
	private  Double kmRecorridos;
	
	@ManyToOne
	@JoinColumn(name = "trabajador_id",referencedColumnName="id") //Cuando la clave primaria de Trabajador sea DNI habrá que referenciarlo aquí en lugar del id como está ahora.
	private Trabajador trabajador;
	
	@Override
	public String toString() {
		return "Automovil [marca=" + marca + ", modelo=" + modelo + ", numPlazas=" + numPlazas + ", kmRecorridos="
				+ kmRecorridos + ", trabajador=" + trabajador + "]";
	}
}
