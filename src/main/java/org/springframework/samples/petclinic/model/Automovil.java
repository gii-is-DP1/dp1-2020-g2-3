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
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@Entity
@Table(name = "Automovil")
public class Automovil extends BaseEntity {
	
	// Falta añadir restricciones simples
	
	@Column(name = "marca")
	@Size(min = 1, max =25)
	@NotEmpty
	private String marca;
	
	@Size(min = 1, max =40)
	@Column(name = "modelo")
	@NotEmpty
	private  String modelo;
	
	
	
	@Column(name = "num_Plazas")
	@Digits(fraction = 0, integer = 1)
	@Max(7)
	@Min(1)
	@NotNull
	private  Integer numPlazas;
	
	@Min(0)
	@Digits(fraction=2,integer=6)
	@Column(name = "km_Recorridos")
	@NotNull
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
